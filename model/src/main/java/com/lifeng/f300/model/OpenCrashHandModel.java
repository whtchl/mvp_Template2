package com.lifeng.f300.model;

import android.os.Build;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.lifeng.beam.model.AbsModel;
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

/**
 * Created by happen on 2017/9/6.
 */

public class OpenCrashHandModel extends AbsModel {

    public static OpenCrashHandModel getInstance() {
        return getInstance(OpenCrashHandModel.class);
    }

    /**
     * 上传签名图片
     * @param signatureStr
     * @param transResponse
     * @return
     */
    public Observable<TransResponse> mUploadSignPic(final String signatureStr, final TransResponse transResponse) {
        return ServiceClient.getService().logon(signatureStr)
                .compose(new DefaultTransform<TransResponse>())
                .doOnNext(new Action1<TransResponse>() {
                    @Override
                    public void call(TransResponse transResponse) {
                        LogUtils.d("wang", " signatureStr doOnNext call " + Thread.currentThread().getName() +
                                " \n Thread id:" + Thread.currentThread().getId());
                    }
                });
    }
}
