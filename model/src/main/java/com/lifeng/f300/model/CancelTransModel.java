package com.lifeng.f300.model;

import com.google.gson.Gson;
import com.jude.utils.JUtils;
import com.lifeng.beam.model.AbsModel;
import com.lifeng.f300.common.entites.BankEntity;
import com.lifeng.f300.common.entites.CancelTransBean;
import com.lifeng.f300.common.entites.CancelTransResponBean;
import com.lifeng.f300.common.utils.LogUtils;
import com.lifeng.f300.hardware.common.Hw;
import com.lifeng.f300.server.service.DefaultTransform;
import com.lifeng.f300.server.service.ServiceClient;

import java.util.HashMap;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by happen on 2017/9/7.
 */

public class CancelTransModel extends AbsModel {
    public static CancelTransModel getInstance() {
        return getInstance(CancelTransModel.class);
    }


    public Observable<BankEntity> mLoadCrashbankCard(final String transAmt){
        JUtils.Log("CancelTransModel mLoadCrashBankCard");
        return Observable.create(new Observable.OnSubscribe<BankEntity>(){

            @Override
            public void call(Subscriber<? super BankEntity> subscriber) {
                BankEntity bankEntity = null;
                try{
                    Thread.sleep(10000);
                    bankEntity =  Hw.getInstance().hwLoadCrashBankCard(transAmt);
                }catch(Exception e){
                    subscriber.onError(e);
                }
                if(bankEntity != null){
                    subscriber.onNext(bankEntity);
                }

                JUtils.Log(" subscriber.onNext(\"\");"+Thread.currentThread().getName().toString());
                subscriber.onCompleted();
            }
        }).compose(new DefaultTransform<BankEntity>());
    }


    public Observable<CancelTransResponBean> mSendCancelRequest(CancelTransBean cancelTransBean){

        HashMap<String, String> hashMap = new HashMap<String, String>();
        /*hashMap.put("TYPE", ConnectURL.SIGIN_IN);
        hashMap.put("REQUEST_TIME", System.currentTimeMillis() + "");
        String deviceType = (TextUtils.isEmpty(Build.MODEL) ? "" : Build.MODEL);  //设备类型
        hashMap.put("DEVICETYPE", deviceType);
        hashMap.put("MERCHANTCODE", merchant_codes);
        hashMap.put("POSCODE", poscode);
        hashMap.put("DEVIDEID", DeviceUtils.getDeviceSn());  //设备号
        hashMap.put("VISION", packInfo.versionName);  //版本号
        hashMap.put("KEYTYPE", ConnectURL.KEYTYPE);
        Tools.getInstance().calcMD5(hashMap, master_key);;*/
        Gson gson = new Gson();
        String strEntity = gson.toJson(hashMap);
        return ServiceClient.getService().cancel(strEntity)
                .compose(new DefaultTransform<CancelTransResponBean>())
                .doOnNext(new Action1<CancelTransResponBean>() {
                    @Override
                    public void call(CancelTransResponBean cancelTransResponBean) {
                        LogUtils.d("wang", " mSendCancelRequest doOnNext call " + Thread.currentThread().getName() +
                                " \n Thread id:" + Thread.currentThread().getId());
                    }
                });
    }
}
