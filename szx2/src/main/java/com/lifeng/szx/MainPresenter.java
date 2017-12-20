package com.lifeng.szx;

import android.util.Log;

import com.jude.utils.JUtils;
import com.lifeng.beam.bijection.Presenter;
import com.lifeng.f300.common.utils.CheckNetIsOk;
import com.lifeng.f300.common.utils.LogUtils;
import com.lifeng.f300.config.API;
import com.lifeng.f300.server.service.ServiceResponseSzx;

import rx.functions.Action0;
import szx.entites.SzxTransResponse;
import szx.model.SzxModel;
/**
 * Created by happen on 2017/11/20.
 */

public class MainPresenter  extends Presenter<MainActivity> {
    public void pSubmitFoodInfo(String phone,String name,String money){
        if (!CheckNetIsOk.isNetOk( getView())) {
            return;
        }
        getView().getExpansion().showProgressDialog(getView().getString(R.string.waiting));
        SzxModel.getInstance().sendSms(phone,name,money)
                .finallyDo(new Action0() {
                    @Override
                    public void call() {
                        LogUtils.d("wang","call dismissProgressDialog");
                        getView().getExpansion().dismissProgressDialog();
                    }
                })
                .subscribe(new ServiceResponseSzx<SzxTransResponse>() {
                    @Override
                    protected void _onError(String cause) {

                        Log.d("wang",""+cause+Thread.currentThread().getName());
                        if(cause.contains(API.CODE.TIMEOUT)){
                            JUtils.ToastLong(API.CODE_MEG.TIMEOUT_MEG);
                        }else if (cause.contains(API.CODE.CONN_EXP)){
                            JUtils.ToastLong(API.CODE_MEG.CONN_EXP_MSG);
                        }else{
                            JUtils.ToastLong(cause);
                        }
                    }

                    @Override
                    protected void _onNext(SzxTransResponse szxTransResponse) {
                        //JUtils.ToastLong("打开现代金控收银APP");
                        getView().gotoXdjk2();
                    }

                });
    }




}
