package com.lifeng.f300.znpos2.module.user;

import com.jude.utils.JUtils;
import com.lifeng.beam.bijection.Presenter;
import com.lifeng.f300.common.entites.TransResponse;
import com.lifeng.f300.common.utils.CheckNetIsOk;
import com.lifeng.f300.config.API;
import com.lifeng.f300.model.OpenCrashHandModel;
import com.lifeng.f300.server.service.ServiceResponse;
import com.lifeng.f300.znpos2.R;

import rx.functions.Action0;

/**
 * Created by happen on 2017/9/5.
 */

public class OpenCrashHandPresenter extends Presenter<OpenCrashHandActivity> {

    public void pUploadSignPic(final String signatureStr, final TransResponse transResponse){
        if(!CheckNetIsOk.isNetOk(getView())){
            return;
        }
        getView().getExpansion().showProgressDialog(getView().getString(R.string.waiting));
        OpenCrashHandModel.getInstance().mUploadSignPic(signatureStr,transResponse)
                .finallyDo(new Action0() {
                    @Override
                    public void call() {
                        getView().getExpansion().dismissProgressDialog();
                    }
                }).subscribe(new ServiceResponse<TransResponse>() {
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

                getView().sendMsgloadSubmit();
                //loadSubmit(bitmapByte);
            }
        });
    }

    /*public void activate(String LICENSE){
        if (!CheckNetIsOk.isNetOk( getView())) {
            return;
        }
        getView().getExpansion().showProgressDialog(getView().getString(R.string.waiting));
        AccountModel.getInstance().activate(LICENSE)
                .finallyDo(new Action0() {
                    @Override
                    public void call() {
                        LogUtils.d("wang","call dismissProgressDialog");
                        getView().getExpansion().dismissProgressDialog();
                    }
                })
                .subscribe(new ServiceResponse<TransResponse>() {
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
                        //getView().finish();
                        //getView().vloadMasterKey(transResponse);

                        *//*if (error.getMessage().contains("TimeoutException")) {
                            Message obtain = Message.obtain();
                            Bundle data = new Bundle();
                            data.putSerializable("transBean", transBean);
                            data.putBoolean("isRecharge", false);
                            obtain.setData(data);
                            obtain.what = DelayedWhat;
                            handlerDelayedSelect.sendMessageDelayed(obtain, 10000);// 10s之后再去查询
                        }*//*



                        getView().sendMessage(transResponse);
                    }
                });
    }*/
}
