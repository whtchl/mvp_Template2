package com.lifeng.f300.model;


import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.lifeng.beam.model.AbsModel;
import com.lifeng.f300.common.entites.TransResponse;
import com.lifeng.f300.common.exception.HwException;
import com.lifeng.f300.common.utils.LogUtils;
import com.lifeng.f300.common.utils.Tools;
import com.lifeng.f300.config.ConnectURL;
import com.lifeng.f300.config.Permission;
import com.lifeng.f300.hardware.common.Hw;
import com.lifeng.f300.server.service.DefaultTransform;
import com.lifeng.f300.server.service.ServiceClient;

import java.util.HashMap;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;


/**
 * Created by happen on 2017/8/7.
 */

public class AccountModel extends AbsModel {
    public static AccountModel getInstance() {
        return getInstance(AccountModel.class);
    }

    @Override
    protected void onAppCreateOnBackThread(Context ctx) {
        super.onAppCreateOnBackThread(ctx);
    }

    public Observable<String> mloadMasterKeytest(final TransResponse transResponse) {
        Gson gson1 = new Gson();
        final String reslutInfo = gson1.toJson(transResponse);
        LogUtils.d("wang", "ResultInfo:" + reslutInfo);

        // hw.getInstance().loadMasterKey(reslutInfo,transResponse);
        return Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                LogUtils.d("wang", "mloadMasterKey " + Thread.currentThread().getName() +
                        " Thread id:" + Thread.currentThread().getId());

                String str = Hw.getInstance().testErr(3);
                if (str.contains("0")) {
                    subscriber.onError(new HwException(1, "221"));
                }
                LogUtils.d("wang", "call onNext after err");
                subscriber.onNext("");

                subscriber.onCompleted();
            }
        }).compose(new DefaultTransform<String>())

                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        LogUtils.d("wang", "mloadMasterKey doOnNext " + Thread.currentThread().getName() +
                                " Thread id:" + Thread.currentThread().getId());
                    }
                });
    }

    public Observable<String> mloadMasterKey(final TransResponse transResponse) {
        Gson gson1 = new Gson();
        final String reslutInfo = gson1.toJson(transResponse);
        LogUtils.d("wang", "ResultInfo:" + reslutInfo);

        return Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                LogUtils.d("wang", "mloadMasterKey " + Thread.currentThread().getName() +
                        " Thread id:" + Thread.currentThread().getId());

                try {
                    Hw.getInstance().loadMasterKey(reslutInfo, transResponse);
                } catch (Exception e) {
                    subscriber.onError(e);
                }
                subscriber.onNext("");
                LogUtils.d("wang", " subscriber.onNext(\"\");");
                subscriber.onCompleted();
            }
        });/*.compose(new DefaultTransform<String>())
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        LogUtils.d("wang", "mloadMasterKey doOnNext " + Thread.currentThread().getName() +
                                " Thread id:" + Thread.currentThread().getId());
                    }
                });*/
    }

    public Observable<TransResponse> activate(String LICENSE) {
        //重新输入ip地址时，需要将service重置为空。
        ServiceClient.setServiceNull();

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("TYPE", ConnectURL.ACTIVATE);
        hashMap.put("REQUEST_TIME", System.currentTimeMillis() + "");
        String deviceType = (TextUtils.isEmpty(Build.MODEL) ? "" : Build.MODEL);  //设备类型
        if(Permission.getInstance().testParam()){
            hashMap.put("DEVICETYPE", "F300");
            hashMap.put("DEVICEID", "21D2015113000050");
        }else {
            hashMap.put("DEVICETYPE", deviceType);
            hashMap.put("DEVICEID", Hw.getInstance().getDeviceSn());
        }

        hashMap.put("LICENSE", LICENSE);
        Tools.getInstance().calcMD5(hashMap, ConnectURL.REGISTER_VERIFYCODE);

        Gson gson = new Gson();
        String strEntity = gson.toJson(hashMap);
        LogUtils.d("wang","activate:"+strEntity);

        return ServiceClient.getService().activate(strEntity)
                .compose(new DefaultTransform<TransResponse>())
                .doOnNext(new Action1<TransResponse>() {
                    @Override
                    public void call(TransResponse transResponse) {
                        LogUtils.d("wang", " activite doOnNext call " + Thread.currentThread().getName() +
                                " \n Thread id:" + Thread.currentThread().getId());
                    }
                });
    }
}
