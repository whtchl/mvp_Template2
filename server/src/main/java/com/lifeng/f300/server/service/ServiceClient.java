package com.lifeng.f300.server.service;

import com.lifeng.f300.config.IPPORT;
import com.lifeng.f300.server.config.Service;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import szx.config.config;

/**
 * Created by happen on 2017/8/7.
 */

public class ServiceClient {
    //读超时长，单位：毫秒
    public static final int DEFAULT_TIME_OUT = 12;
    //连接时长，单位：毫秒
    public static final int CONNECT_TIME_OUT = 12;
    private static Service mService;
    private static OkHttpClient okHttpClient;

    public static OkHttpClient getOkHttpClient(){
        if (okHttpClient==null){
            okHttpClient = new OkHttpClient();
            okHttpClient.setReadTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);
            okHttpClient.setConnectTimeout(DEFAULT_TIME_OUT,TimeUnit.SECONDS);
            okHttpClient.setWriteTimeout(DEFAULT_TIME_OUT,TimeUnit.SECONDS);

        }
        return okHttpClient;
    }
    public static Service getService(){
        if (mService == null){
            createService();
        }

        return mService;
    }

    public static void setServiceNull(){
        mService = null;
    }
    private static void createService(){
        mService = createAdapter().create(Service.class);
    }

    private static RestAdapter createAdapter(){
       // boolean isInDebugServer = JUtils.getSharedPreference().getBoolean("DebugServer",false);

        IPPORT.getInstance().getIpPort();
        return new RestAdapter.Builder()
                .setEndpoint(IPPORT.getInstance().getIpPort())
               /* .setRequestInterceptor(new HeaderInterceptors())*/
                .setLogLevel(RestAdapter.LogLevel.FULL )
                .setConverter(new WrapperConverter())
                .setClient(new OkClient(getOkHttpClient()))
                /*.setRequestInterceptor(new HeaderInterceptors())*/
                .build();
    }


    /**
     * 食责险 返回码不同
     */
    public static Service getServiceSzx(){
        if (mService == null){
            createServiceSzx();
        }

        return mService;
    }
    private static void createServiceSzx(){
        mService = createAdapterSzx().create(Service.class);
    }


    private static RestAdapter createAdapterSzx(){
        // boolean isInDebugServer = JUtils.getSharedPreference().getBoolean("DebugServer",false);

        IPPORT.getInstance().getIpPort();
        return new RestAdapter.Builder()
                .setEndpoint(config.URI)
               /* .setRequestInterceptor(new HeaderInterceptors())*/
                .setLogLevel(RestAdapter.LogLevel.FULL )
                .setConverter(new WrapperConverterSzx())
                .setClient(new OkClient(getOkHttpClient()))
                /*.setRequestInterceptor(new HeaderInterceptors())*/
                .build();
    }
}
