package com.lifeng.f300.model;

import android.os.Build;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.lifeng.beam.model.AbsModel;
import com.lifeng.f300.common.entites.TransResponse;
import com.lifeng.f300.common.utils.LogUtils;
import com.lifeng.f300.common.utils.Tools;
import com.lifeng.f300.config.ConnectURL;
import com.lifeng.f300.hardware.common.Hw;
import com.lifeng.f300.server.service.DefaultTransform;
import com.lifeng.f300.server.service.ServiceClient;

import java.util.HashMap;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by happen on 2017/8/25.
 */

public class AboutUsModel  extends AbsModel {
    public static AboutUsModel getInstance() {
        return getInstance(AboutUsModel.class);
    }


    public Observable<TransResponse> mloadCheckUpdateSystem() {
        //重新输入ip地址时，需要将service重置为空。
        ServiceClient.setServiceNull();

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("TYPE", ConnectURL.ACTIVATE);
        hashMap.put("REQUEST_TIME", System.currentTimeMillis() + "");
        String deviceType = (TextUtils.isEmpty(Build.MODEL) ? "" : Build.MODEL);  //设备类型
        hashMap.put("DEVICETYPE", deviceType);
        hashMap.put("DEVICEID", Hw.getInstance().getDeviceSn());
        //hashMap.put("LICENSE", LICENSE);
        Tools.getInstance().calcMD5(hashMap, ConnectURL.REGISTER_VERIFYCODE);
        Gson gson = new Gson();
        String strEntity = gson.toJson(hashMap);
        return ServiceClient.getService().checkUpdate(strEntity)
                .compose(new DefaultTransform<TransResponse>())
                .doOnNext(new Action1<TransResponse>() {
                    @Override
                    public void call(TransResponse transResponse) {
                        LogUtils.d("wang", " mloadCheckUpdateSystem doOnNext call " + Thread.currentThread().getName() +
                                " \n Thread id:" + Thread.currentThread().getId());
                    }
                });
    }

}
