package com.lifeng.f300.model;

import android.os.Build;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.jude.utils.JUtils;
import com.lifeng.beam.model.AbsModel;
import com.lifeng.f300.common.entites.BankEntity;
import com.lifeng.f300.common.entites.TransRequest;
import com.lifeng.f300.common.entites.TransResponse;
import com.lifeng.f300.common.utils.LogUtils;
import com.lifeng.f300.common.utils.Tools;
import com.lifeng.f300.config.ConnectURL;
import com.lifeng.f300.hardware.common.Hw;
import com.lifeng.f300.server.service.DefaultTransform;
import com.lifeng.f300.server.service.ServiceClient;

import java.util.HashMap;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by happen on 2017/9/4.
 */

public class GoCashierModel extends AbsModel {

    /*public Observable<String> mloadMasterKey(final TransResponse transResponse) {
        Gson gson1 = new Gson();
        final String reslutInfo = gson1.toJson(transResponse);
        LogUtils.d("wang", "ResultInfo:" + reslutInfo);

        return Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                LogUtils.d("wang", "mloadMasterKey " + Thread.currentThread().getName() +
                        " Thread id:" + Thread.currentThread().getId());

                try {
                    hw.getInstance().loadMasterKey(reslutInfo, transResponse);
                } catch (Exception e) {
                    subscriber.onError(e);
                }
                subscriber.onNext("");
                LogUtils.d("wang", " subscriber.onNext(\"\");");
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
    }*/
    public static GoCashierModel getInstance() {
        return getInstance(GoCashierModel.class);
    }

    public Observable<BankEntity> mLoadCrashbankCard(final String transAmt){
        JUtils.Log("GoCashierModel mLoadCrashBankCard");
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
        });//.compose(new DefaultTransform<BankEntity>());
    }


    public Observable<BankEntity> mReadBankInfo(){
        JUtils.Log("GoCashierModel mLoadCrashBankCard");
        return Observable.create(new Observable.OnSubscribe<BankEntity>(){

            @Override
            public void call(Subscriber<? super BankEntity> subscriber) {
                BankEntity bankEntity = null;
                try{
                    Thread.sleep(10000);
                    bankEntity = Hw.getInstance().readBankCardInfo();
                }catch(Exception e){
                    subscriber.onError(e);
                }
                if(bankEntity != null){
                    subscriber.onNext(bankEntity);
                }

                JUtils.Log(" subscriber.onNext(\"\");"+Thread.currentThread().getName().toString());
                subscriber.onCompleted();
            }
        });//.compose(new DefaultTransform<BankEntity>());
    }

    /**
     * 消费
     * @param transDataBrief
     * @return
     */
    public Observable<TransResponse> mPay(TransRequest transDataBrief){

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("TYPE",ConnectURL.CONSUME);
        hashMap.put("REQUEST_TIME",System.currentTimeMillis()+"");
        hashMap.put("DEVICETYPE",TextUtils.isEmpty(Build.MODEL) ? "" : Build.MODEL);
        hashMap.put("MERCHANTCODE",transDataBrief.merchantcode);
        hashMap.put("POSCODE",transDataBrief.poseCode);
        hashMap.put("EMPNO",transDataBrief.operatorId);
        hashMap.put("BATCHNO",transDataBrief.batchno);
        hashMap.put("TRACE_NO",transDataBrief.traceNo);
        hashMap.put("TRACK2",transDataBrief.track2);
        hashMap.put("TRACK3",transDataBrief.track3);
        hashMap.put("DATA55",transDataBrief.iccData);
        hashMap.put("BANK_PIN",transDataBrief.pin);
        hashMap.put("BANK_TYPE",transDataBrief.bankType);
        hashMap.put("BILLNO",transDataBrief.billNo);
        hashMap.put("BANK_AMOUNT",transDataBrief.bankAmount);
        hashMap.put("DATA23",transDataBrief.data23);

        Tools.getInstance().calcMD5(hashMap,transDataBrief.SECONDARYKEY);
        Gson gson = new Gson();
        String strEntity = gson.toJson(hashMap);
        return ServiceClient.getService().consume(strEntity)
                .compose(new DefaultTransform<TransResponse>())
                .doOnNext(new Action1<TransResponse>() {
                    @Override
                    public void call(TransResponse transResponse) {
                        LogUtils.d("wang", " mPay doOnNext call " + Thread.currentThread().getName() +
                                " \n Thread id:" + Thread.currentThread().getId());
                    }
                });
    }


}
