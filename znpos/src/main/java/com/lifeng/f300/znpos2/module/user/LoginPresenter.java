package com.lifeng.f300.znpos2.module.user;


import com.jude.utils.JUtils;
import com.lifeng.beam.bijection.Presenter;
import com.lifeng.f300.common.entites.TransResponse;
import com.lifeng.f300.common.utils.CheckNetIsOk;
import com.lifeng.f300.common.utils.LogUtils;
import com.lifeng.f300.config.API;
import com.lifeng.f300.hardware.common.HwResponse;
import com.lifeng.f300.model.AccountModel;
import com.lifeng.f300.server.service.ServiceResponse;
import com.lifeng.f300.znpos2.R;

import rx.functions.Action0;


/**
 * Created by happen on 2017/8/4.
 */

public class LoginPresenter extends Presenter<LoginActivity> {
/*
    Subscriber<String> mStartActivitySubscriber = new Subscriber<String>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            *//*inited = true;
            getView().stopRefresh();
            getView().showError(e);*//*
        }

        @Override
        public void onNext(String ms) {
            *//*inited = true;
            getAdapter().clear();
            getAdapter().addAll(ms);
            page = 1;*//*
            LogUtils.d("wang","ms"+ms);
            getView().startSignInActivity();

        }

        @Override
        public void onStart() {
        }
    };
    public Subscriber<String> getStartAcitivtySubscriber(){
        return mStartActivitySubscriber;
    }*/

    public void activate(String LICENSE){
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

                        /*if (error.getMessage().contains("TimeoutException")) {
                            Message obtain = Message.obtain();
                            Bundle data = new Bundle();
                            data.putSerializable("transBean", transBean);
                            data.putBoolean("isRecharge", false);
                            obtain.setData(data);
                            obtain.what = DelayedWhat;
                            handlerDelayedSelect.sendMessageDelayed(obtain, 10000);// 10s之后再去查询
                        }*/

                        getView().sendMsgLoadMasterKey(transResponse);
                    }
                });
    }

    public void ploadMasterkey(TransResponse transResponse){
        getView().getExpansion().showProgressDialog(getView().getString(R.string.master_key));
        //AccountModel.getInstance().mloadMasterKey(transResponse);
        AccountModel.getInstance().mloadMasterKey(transResponse).finallyDo(new Action0() {
            @Override
            public void call() {
                LogUtils.d("wang","call dismissDialog");
                getView().getExpansion().dismissProgressDialog();
            }
        }).subscribe(new HwResponse<String>() {
            @Override
            protected void _onError(String message) {
                LogUtils.d("wang","LoginPresenter _OnError:"+message);
                JUtils.Toast(message);
            }

            @Override
            protected void _onNext(String message) {
                LogUtils.d("wang","LoginPresenter _onNext:"+message);
                //JUtils.Toast("_onNext");
                // getView().startSignInActivity();
            }
        });
    }


/*    public void ploadMasterkey(TransResponse transResponse){
        getView().getExpansion().showProgressDialog(getView().getString(R.string.master_key));
        //AccountModel.getInstance().mloadMasterKey(transResponse);
           AccountModel.getInstance().mloadMasterKey(transResponse).finallyDo(new Action0() {
            @Override
            public void call() {
                LogUtils.d("wang","call dismissDialog");
                getView().getExpansion().dismissProgressDialog();
            }
        }).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                LogUtils.d("wang","ploadMasterkey onCompleted ");
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.d("wang","ploadMasterkey onError ");
            }

            @Override
            public void onNext(String s) {
                LogUtils.d("wang","ploadMasterkey "+ Thread.currentThread().getName()+
                        " \n Thread id:"+Thread.currentThread().getId());
                getView().startSignInActivity();
            }
        });
    }*/


}
