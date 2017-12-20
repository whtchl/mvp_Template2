package com.lifeng.f300.model;

import android.os.Build;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.jude.utils.JUtils;
import com.lifeng.beam.model.AbsModel;
import com.lifeng.f300.common.entites.SettlementRequest;
import com.lifeng.f300.common.entites.SettlementResponse;
import com.lifeng.f300.common.entites.SingleFlowEntity;
import com.lifeng.f300.common.entites.TransResponse;
import com.lifeng.f300.common.utils.DeviceUtils;
import com.lifeng.f300.common.utils.LogUtils;
import com.lifeng.f300.common.utils.Tools;
import com.lifeng.f300.config.ConnectURL;
import com.lifeng.f300.server.service.DefaultTransform;
import com.lifeng.f300.server.service.ServiceClient;

import java.util.HashMap;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by happen on 2017/9/7.
 */

public class LocalTransRecordModel extends AbsModel {
    public static LocalTransRecordModel getInstance() {
        return getInstance(LocalTransRecordModel.class);
    }

    /*public Observable<TransResponse> mQueryFlowInfoByBatchno(final String batchNum,final String trace_no,final String operator_Id,
                                                             final String merchantCode,final String posCode,final String secondaryKey ) {
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("TYPE",ConnectURL.QUERY_TRADE);
        hashMap.put("REQUEST_TIME", System.currentTimeMillis()+"");
        String deviceType=(TextUtils.isEmpty(Build.MODEL) ? "" : Build.MODEL);
        hashMap.put("DEVICETYPE",deviceType);
        hashMap.put("MERCHANTCODE",merchantCode);
        hashMap.put("POSCODE", posCode);
        hashMap.put("EMPNO", operator_Id);
        hashMap.put("BATCHNO", batchNum);
        hashMap.put("TRACE_NO", trace_no);
        Tools.getInstance().calcMD5(hashMap, secondaryKey);
        Gson gson = new Gson();
        String strEntity = gson.toJson(hashMap);
        JUtils.Log("mQueryFlowInfoByBatchno:"+strEntity);
        return ServiceClient.getService().queryTrade(strEntity)
                .compose(new DefaultTransform<TransResponse>())
                .doOnNext(new Action1<TransResponse>() {
                    @Override
                    public void call(TransResponse transResponse) {
                        LogUtils.d("wang", " mQueryFlowInfoByBatchno doOnNext call " + Thread.currentThread().getName() +
                                " \n Thread id:" + Thread.currentThread().getId());
                    }
                });
    }*/
    public Observable<TransResponse> mQueryFlowInfoByBatchno(final String batchNum,final String trace_no,final String operator_Id,
                                                             final String merchantCode,final String posCode,final String secondaryKey ) {
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("TYPE",ConnectURL.QUERY_TRADE);
        hashMap.put("REQUEST_TIME", System.currentTimeMillis()+"");
        String deviceType=(TextUtils.isEmpty(Build.MODEL) ? "" : Build.MODEL);
        hashMap.put("DEVICETYPE",deviceType);
        hashMap.put("MERCHANTCODE",merchantCode);
        hashMap.put("POSCODE", posCode);
        hashMap.put("EMPNO", operator_Id);
        hashMap.put("BATCHNO", batchNum);
        hashMap.put("TRACE_NO", trace_no);
        Tools.getInstance().calcMD5(hashMap, secondaryKey);
        Gson gson = new Gson();
        String strEntity = gson.toJson(hashMap);
        //JUtils.Log("mQueryFlowInfoByBatchno:"+strEntity);
        return ServiceClient.getService().queryTrade(strEntity).compose(new DefaultTransform<TransResponse>())
                .doOnNext(new Action1<TransResponse>() {
                    @Override
                    public void call(TransResponse transResponse) {
                        LogUtils.d("wang", " mQueryFlowInfoByBatchno doOnNext call " +transResponse.TRACE_NO);
                    }
                });
    }

    public TransResponse mQueryFlowInfoByBatchno2(final SingleFlowEntity mSingleFlowEntity ) {
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("TYPE",ConnectURL.QUERY_TRADE);
        hashMap.put("REQUEST_TIME", System.currentTimeMillis()+"");
        String deviceType=(TextUtils.isEmpty(Build.MODEL) ? "" : Build.MODEL);
        hashMap.put("DEVICETYPE",deviceType);
        hashMap.put("MERCHANTCODE",mSingleFlowEntity.getMerchantCode());
        hashMap.put("POSCODE", mSingleFlowEntity.getPosCode());
        hashMap.put("EMPNO", mSingleFlowEntity.getOperatorId());
        hashMap.put("BATCHNO", mSingleFlowEntity.getBatchno());
        hashMap.put("TRACE_NO", mSingleFlowEntity.getSerialNumber());
        Tools.getInstance().calcMD5(hashMap, mSingleFlowEntity.getSecondaryKey());
        Gson gson = new Gson();
        String strEntity = gson.toJson(hashMap);
        //JUtils.Log("mQueryFlowInfoByBatchno:"+strEntity);
        return ServiceClient.getService().queryTrade2(strEntity);
    }

    /**
     * 结算
     */
    public Observable<SettlementResponse> mSettlement(final SettlementRequest request){
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("TYPE", ConnectURL.SETTLEMNT);
        hashMap.put("REQUEST_TIME", System.currentTimeMillis()+"");
        String deviceType=(TextUtils.isEmpty(Build.MODEL) ? "" : Build.MODEL);    //设备类型
        hashMap.put("DEVICETYPE",deviceType);
        hashMap.put("MERCHANTCODE", request.MERCHANTCODE);
        hashMap.put("POSCODE",request.POSCODE);
        hashMap.put("BATCHNO",request.BATCHNUM);    //结算的当前批次号


        hashMap.put("DEVICEID",request.DEVICEID);//POS设备序列号
        hashMap.put("VISION",request.VISION); //版本號
        hashMap.put("USERNAME", request.USERNAME);    //操作員號
        hashMap.put("KEYTYPE",request.KEYTYPE);    //秘钥类型

        hashMap.put("CONSUME_COUNT_TRADE",request.CONSUME_COUNT_TRADE);
        hashMap.put("CONSUME_DEPOSIT_ADD",request.CONSUME_DEPOSIT_ADD);
        hashMap.put("CONSUME_DEPOSIT_SUB",request.CONSUME_DEPOSIT_SUB);
        hashMap.put("CONSUME_POINT_SUB",request.CONSUME_POINT_SUB);
        hashMap.put("CONSUME_CASH_AMOUNT", request.CONSUME_CASH_AMOUNT);
        hashMap.put("CONSUME_BANK_AMOUNT", request.CONSUME_BANK_AMOUNT);
        hashMap.put("CONSUME_COUPON_AMOUNT", request.CONSUME_COUPON_AMOUNT);

        //撤销
        hashMap.put("CANCEL_COUNT_TRADE",request.CANCEL_COUNT_TRADE);
        hashMap.put("CANCEL_DEPOSIT_ADD",request.CANCEL_DEPOSIT_ADD);
        hashMap.put("CANCEL_DEPOSIT_SUB",request.CANCEL_DEPOSIT_SUB);
        hashMap.put("CANCEL_POINT_SUB",request.CANCEL_POINT_SUB);
        hashMap.put("CANCEL_CASH_AMOUNT", request.CANCEL_CASH_AMOUNT);
        hashMap.put("CANCEL_BANK_AMOUNT",request.CANCEL_BANK_AMOUNT);
        hashMap.put("CANCEL_COUPON_AMOUNT", request.CANCEL_COUPON_AMOUNT);
        hashMap.put("INVOICE_ID_LIST", request.INVOICE_ID_LIST);

        Tools.getInstance().calcMD5(hashMap, request.SECONDARYKEY);
        Gson gson = new Gson();
        String strEntity = gson.toJson(hashMap);
        return ServiceClient.getService().logout(strEntity);

    }
}
