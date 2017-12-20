package com.lifeng.f300.znpos2.module.user;

import com.jude.utils.JUtils;
import com.lifeng.beam.bijection.Presenter;

import com.lifeng.f300.common.entites.SettlementRequest;
import com.lifeng.f300.common.entites.SettlementResponse;
import com.lifeng.f300.common.entites.SingleFlowEntity;
import com.lifeng.f300.common.entites.TransResponse;
import com.lifeng.f300.common.utils.CheckNetIsOk;
import com.lifeng.f300.common.utils.LogUtils;
import com.lifeng.f300.config.API;
import com.lifeng.f300.model.LocalTransRecordModel;
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
 * Created by happen on 2017/9/7.
 */

public class LocalTransRecordPresenter extends Presenter<LocalTransRecordActivity> {


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
    public void pQueryFlowInfoByBatchNo2(List<SingleFlowEntity> mSingleFlowEntity) {
        if (!CheckNetIsOk.isNetOk(getView())) {
            return;
        }
        subscribe2(createUrlObservable2(mSingleFlowEntity));
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
     * 区别于createUrlObservable3，这个是给recycleView用的
     * @param mSingleFlowEntity
     * @return
     */
    public Observable<TransResponse> createUrlObservable2(List<SingleFlowEntity> mSingleFlowEntity) {
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
                        getView().RefreshRecycleView();
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

