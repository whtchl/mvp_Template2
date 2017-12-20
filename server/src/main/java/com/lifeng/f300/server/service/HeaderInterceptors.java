package com.lifeng.f300.server.service;

import retrofit.RequestInterceptor;

/**
 * Created by happen on 2017/8/7.
 */

public class HeaderInterceptors implements RequestInterceptor {
    public static String TOKEN = "";
    public static String UID = "";

    @Override
    public void intercept(RequestFacade request) {
        request.addHeader("uid", UID);
        request.addHeader("token", TOKEN);
    }
}
