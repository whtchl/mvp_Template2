package com.lifeng.szx.ui;

import android.util.Log;

import com.jude.utils.JUtils;
import com.lifeng.beam.bijection.Presenter;
import com.lifeng.f300.common.utils.CheckNetIsOk;
import com.lifeng.f300.common.utils.LogUtils;
import com.lifeng.f300.config.API;
import com.lifeng.f300.server.service.ServiceResponseSzx;
import com.lifeng.szx.MainActivity;
import com.lifeng.szx.R;

import rx.functions.Action0;
import szx.entites.SzxTransResponse;
import szx.model.SzxModel;

/**
 * Created by happen on 2017/12/5.
 */

public class BaPresenter   extends Presenter<BaActivity> {

    public void pUploadSingleFile(String file,String thirdMark,String thirdMCode ,final int i){
        if (!CheckNetIsOk.isNetOk( getView())) {
            return;
        }
        getView().getExpansion().showProgressDialog(getView().getString(R.string.waiting));
        SzxModel.getInstance().UploadSingleFile(file,thirdMark,thirdMCode) .finallyDo(new Action0() {
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
                        //getView().gotoXdjk2();
                         if(i==2) {
                             getView().gotoSendSecondPIC(szxTransResponse);
                         }else if(i==3){
                             getView().gotoSendThirdPIC(szxTransResponse);
                         }else if(i==0){
                             getView().gotoUploadBaInfo(szxTransResponse);
                         }
                    }

                });
    }


    public void pSubmitBaInfo(String third,String mchcode,String report_money,String happen_time,
                              String address,String cause,String informant,String informant_phone,
                             String files){
        if (!CheckNetIsOk.isNetOk( getView())) {
            return;
        }
        getView().getExpansion().showProgressDialog(getView().getString(R.string.waiting));

        SzxModel.getInstance().SubmitBaInfo5( third, mchcode,report_money, happen_time, address, cause, informant, informant_phone,
                files)
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
                        //getView().gotoXdjk2();
                        JUtils.ToastLong("报案成功");
                        getView().gotoMainActivity();
                    }

                });
    }
}
