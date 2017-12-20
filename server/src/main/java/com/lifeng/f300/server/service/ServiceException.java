package com.lifeng.f300.server.service;

import retrofit.converter.ConversionException;

/**
 * Created by happen on 2017/8/7.
 */

public class ServiceException extends ConversionException {
    private String status;
    private String message;

    public  String getStatus() {
        return status;
    }

    public String getInfo() {
        return message;
    }

    public ServiceException(String status, String message) {
        super("ServiceException status:"+status+"  message:"+message);
        this.status = status;
        this.message = message;
    }

}