package com.lifeng.f300.znpos2.module.user;

import com.jude.utils.JUtils;
import com.lifeng.beam.bijection.Presenter;
import com.lifeng.f300.common.entites.OperatorInfoBrief;
import com.lifeng.f300.common.entites.SettlementRequest;
import com.lifeng.f300.common.entites.SettlementResponse;
import com.lifeng.f300.common.entites.SingleFlowEntity;
import com.lifeng.f300.common.entites.TransResponse;
import com.lifeng.f300.common.utils.CheckNetIsOk;
import com.lifeng.f300.common.utils.LogUtils;
import com.lifeng.f300.config.API;
import com.lifeng.f300.config.Permission;
import com.lifeng.f300.hardware.common.HwResponse;
import com.lifeng.f300.model.LocalTransRecordModel;
import com.lifeng.f300.model.SignInModel;
import com.lifeng.f300.server.service.ServiceResponse;
import com.lifeng.f300.znpos2.R;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by happen on 2017/8/21.
 */
/**
 * pQueryDB 查询数据库，确认用户名存在--> pSignIn 访问服务器  --> pWriteSignFile 写sign.txt 文件
 * -->提示下载app或写工作秘钥
 */

public class SignInPresenter  extends Presenter<SignInActivity> {

    //判断用户名和密码是否正确
    public void pCheckOperatorAndPwd(final String name,final String pwd){
        JUtils.Log("call pCheckOperatorAndPwd ");
        SignInModel.getInstance().mCheckOperatorAndPwd(name,pwd).subscribe(new Subscriber<List<OperatorInfoBrief>>() {
            @Override
            public void onCompleted() {
                LogUtils.d("wang","pQueryDB onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.d("wang","pQueryDB onError");
                JUtils.Log("Throwable:"+e.getMessage()+" || /n "+ e.getCause().getMessage());
                JUtils.ToastLong(getView().getString(R.string.operator_without)+getView().getString(R.string.or)+getView().getString(R.string.password_wrong));
            }

            @Override
            public void onNext(List<OperatorInfoBrief> operatorInfoBriefs) {
                LogUtils.d("wang", "pQueryDB call " + Thread.currentThread().getName() +
                        " Thread id:" + Thread.currentThread().getId());
                if(operatorInfoBriefs.size()==1){
                    for(int i=0; i<operatorInfoBriefs.size();i++){
                        LogUtils.d("wang",operatorInfoBriefs.get(i).getOperatorId()+ "  "+operatorInfoBriefs.get(i).getPassword());
                    }
                    getView().setOpeatorAndPwd();
                   // getView().sendMsgSigninServer(operatorInfoBriefs.get(0).getOperatorId());
                    getView().vSignInServer(operatorInfoBriefs.get(0).getOperatorId());
                    //getView().startMainActivity();
                }else{
                    getView().toastWrongOperatorOrPwd();
                }
            }
        });
    }

   /* public void pQueryDB(final String name, final String pwd){
        SignInModel.getInstance().mQueryDB(name,pwd).subscribe(new Subscriber<List<OperatorInfoBrief>>() {
            @Override
            public void onCompleted() {
                LogUtils.d("wang","pQueryDB onCompleted");
            }

            @Override
            public void onError(Throwable e) {
               LogUtils.d("wang","pQueryDB onError");
            }

            @Override
            public void onNext(List<OperatorInfoBrief> operatorInfoBriefs) {
                LogUtils.d("wang", "pQueryDB call " + Thread.currentThread().getName() +
                        " Thread id:" + Thread.currentThread().getId());
                if(operatorInfoBriefs.size()==1){
                    for(int i=0; i<operatorInfoBriefs.size();i++){
                        LogUtils.d("wang",operatorInfoBriefs.get(i).getOperatorId()+ "  "+operatorInfoBriefs.get(i).getPassword());
                    }
                    getView().setOpeatorAndPwd();
                    getView().sendMsgSigninServer(operatorInfoBriefs.get(0).getOperatorId());
                    //getView().startMainActivity();
                }else{
                    getView().toastWrongOperatorOrPwd();
                }
            }
        });
    }*/

    public void pSignIn(final String username,final String  merchant_codes,final String master_key,final String poscode){
        if (!CheckNetIsOk.isNetOk( getView())) {
            return;
        }
        getView().getExpansion().showProgressDialog(getView().getString(R.string.waiting));

        SignInModel.getInstance().mSignIn(username,merchant_codes,master_key,poscode)
                .finallyDo(new Action0() {
                    @Override
                    public void call() {
                        LogUtils.d("wang","call dismissProgressDialog");
                        getView().getExpansion().dismissProgressDialog();
                    }
                })
                .subscribe(new ServiceResponse<TransResponse>() {
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    protected void _onError(String cause) {
                        if(cause.contains(API.CODE.TIMEOUT)){
                            JUtils.Toast(API.CODE_MEG.TIMEOUT_MEG);
                        }else if (cause.contains(API.CODE.CONN_EXP)){
                            JUtils.Toast(API.CODE_MEG.CONN_EXP_MSG);
                        }else{
                            JUtils.Toast(cause);
                        }
                    }

                    @Override
                    protected void _onNext(TransResponse transResponse) {
                        JUtils.Log(" subscribe onNext " + Thread.currentThread().getName());
                        //JUtils.Log("------"+transResponse.PO);
                        getView().sendMsgWriteSignFile(transResponse);
                        //getView().vSignFile(transResponse);
                    }
                });
    }

    public void pWriteSignFile(final TransResponse transResponse){

        getView().getExpansion().showProgressDialog(getView().getString(R.string.write_sign_file));
        SignInModel.getInstance().mWriteSignFile(transResponse).finallyDo(new Action0() {
            @Override
            public void call() {
                LogUtils.d("wang","call dismissDialog");
                getView().getExpansion().dismissProgressDialog();
            }
        }).subscribe(new HwResponse<String>() {
            @Override
            protected void _onError(String message) {
                LogUtils.d("wang","SignInPresenter _OnError:"+message);
                JUtils.Toast(getView().getString(R.string.wrong_save_sign_file));
            }

            @Override
            protected void _onNext(String message) {
                LogUtils.d("wang","SignInPresenter _onNext:"+message);
                getView().sendMsgShowUpdateAppDialog();
            }
        });
    }

    public void pLoadWorkKey(final  TransResponse t){
        getView().getExpansion().showProgressDialog(getView().getString(R.string.load_work_key));
        SignInModel.getInstance().mLoadWorkKey(t).finallyDo(new Action0() {
            @Override
            public void call() {
                LogUtils.d("wang","call dismissDialog");
                getView().getExpansion().dismissProgressDialog();
            }
        }).subscribe(new HwResponse<String>() {
            @Override
            protected void _onError(String message) {
                LogUtils.d("wang","pLoadWorkKey _OnError:"+message);
                JUtils.Toast(getView().getString(R.string.wrong_load_work_key));
            }

            @Override
            protected void _onNext(String message) {
                LogUtils.d("wang","pLoadWorkKey _onNext:"+message);

                if(Permission.getInstance().isPopupReadCardActivity()){

                    getView().sendMsgGotoReadCardActivity();
                  /*  Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    //SharedPreferenceUtil.putStringData(SignInActivity.this,"operatorId", signName);
                    JUtils.getSharedPreference().edit().putString("operatorId", signName).apply();
                    getView().startActivity(intent);
                    finish();
                    break;*/
                }else{
                    //提示用户进行结算
                    getView().isSettlement();
                    //getView().sendMsgGotoLifengSystem();
                }

            }
        });
    }

    /**
     * 结算
     */
    public void pSettlement(SettlementRequest request) {
        if (!CheckNetIsOk.isNetOk(getView())) {
            return;
        }
        getView().getExpansion().showProgressDialog(getView().getString(R.string.jsz));
        LocalTransRecordModel.getInstance().mSettlement(request)
                .finallyDo(new Action0() {
                    @Override
                    public void call() {
                        LogUtils.d("wang", "call dismissProgressDialog");
                        getView().getExpansion().dismissProgressDialog();
                    }
                })
                .subscribe(new ServiceResponse<SettlementResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    protected void _onError(String cause) {
                        if (cause.contains(API.CODE.TIMEOUT)) {
                            JUtils.Toast(API.CODE_MEG.TIMEOUT_MEG);
                        } else if (cause.contains(API.CODE.CONN_EXP)) {
                            JUtils.Toast(API.CODE_MEG.CONN_EXP_MSG);
                        } else {
                            JUtils.Toast(cause);
                        }
                    }

                    @Override
                    protected void _onNext(SettlementResponse settlementResponse) {
                        JUtils.Log(" subscribe onNext " + Thread.currentThread().getName());
                        getView().clearViewData();
                        //getView().sendMsgWriteSignFile(transResponse);
                    }
                });
        //getView().getExpansion().showProgressDialog(getView().getString(R.string.waiting));
    }

    /**
     * 查询一个批次号下多个流水信息
     * @param mSingleFlowEntity   流水列表
     */
    public void  pQueryFlowInfoByBatchNoSignInActivity(List<SingleFlowEntity> mSingleFlowEntity){
        if (!CheckNetIsOk.isNetOk(getView())) {
            return;
        }
        subscribe2(createUrlObservable3(mSingleFlowEntity));
    }



    private void subscribe2(Observable<TransResponse> observable) {
        observable.subscribe(new Action1<TransResponse>() {
            @Override
            public void call(TransResponse s) {
                if(s!=null) {
                    //serialNumList.add(s);
                    JUtils.Log(" ------------------ " + s.MESSAGE + " " + s.STATUS+ s.REMARK+ Thread.currentThread().getName());
                    getView().addFlowTransResponseList(s);
                }
            }
        });
    }

    /**
     *
     * @param mSingleFlowEntity
     * @return
     */
    public Observable<TransResponse> createUrlObservable3(List<SingleFlowEntity> mSingleFlowEntity) {
        getView().getExpansion().showProgressDialog(getView().getString(R.string.cxlsxx));
        return Observable.from(mSingleFlowEntity)
                .concatMap(new Func1<SingleFlowEntity, Observable<TransResponse>>() {
                    @Override
                    public Observable<TransResponse> call(SingleFlowEntity s) {
                        return createFlowObservableMultiThread(s);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .finallyDo(new Action0() {
                    @Override
                    public void call() {
                        getView().getExpansion().dismissProgressDialog();
                        getView().sendMsgSettlement();

                    }
                });
    }

    private synchronized Observable<TransResponse> createFlowObservableMultiThread(SingleFlowEntity s) {
        return Observable.create(new Observable.OnSubscribe<TransResponse>() {
            @Override
            public void call(Subscriber<? super TransResponse> subscriber) {
                TransResponse mTransResponse;
                try{
                    //Thread.sleep(3000);
                    mTransResponse = LocalTransRecordModel.getInstance().mQueryFlowInfoByBatchno2(s);
                    JUtils.Log("createFlowObservableMultiThread:" + mTransResponse.STATUS + "  " + mTransResponse.MESSAGE+"  "+mTransResponse.REMARK+  Thread.currentThread().getName());
                    subscriber.onNext(mTransResponse);
                    subscriber.onCompleted();
                }catch (Exception e){
                    JUtils.Log("createFlowObservableMultiThread:"+e.getMessage());

                    subscriber.onCompleted();
                }
            }
        }).subscribeOn(Schedulers.io());

    }

}
