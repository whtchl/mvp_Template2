package com.lifeng.f300.znpos2.module.user;

import com.jude.utils.JUtils;
import com.lifeng.beam.bijection.Presenter;
import com.lifeng.f300.common.entites.TransResponse;
import com.lifeng.f300.common.utils.CheckNetIsOk;
import com.lifeng.f300.common.utils.LogUtils;
import com.lifeng.f300.config.API;
import com.lifeng.f300.model.AboutUsModel;
import com.lifeng.f300.server.service.ServiceResponse;
import com.lifeng.f300.znpos2.R;

import rx.functions.Action0;

/**
 * Created by happen on 2017/8/25.
 */

public class AboutUsPresenter extends Presenter<AboutUsActivity> {

    public void ploadCheckUpdateSystem(){
        if (!CheckNetIsOk.isNetOk( getView())) {
            return;
        }
        getView().getExpansion().showProgressDialog(getView().getString(R.string.waiting));

        AboutUsModel.getInstance().mloadCheckUpdateSystem()
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
                        if (null != transResponse && "00".equals(transResponse.STATUS)) {
                            getView().sendMsgLoadDownShowDialog(transResponse);
                        }else{
                            JUtils.Toast(transResponse!=null ? transResponse.MESSAGE : getView().getString(R.string.parse_error));
                        }
                    }
                });

    }
}
