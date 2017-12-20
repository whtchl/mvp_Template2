package com.lifeng.f300.server.service;

import retrofit.converter.ConversionException;

/**
 * Created by happen on 2017/8/7.
 */

public class ServiceExceptionSzx extends ConversionException {
    private Boolean status;
    private String message;

    public  Boolean getStatus() {
        return status;
    }

    public String getInfo() {
        return message;
    }

    public ServiceExceptionSzx(Boolean status, String message) {
        super("ServiceException status:"+status+"  message:"+message);
        this.status = status;
        this.message = message;
    }

}