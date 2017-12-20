package com.lifeng.f300.server.service;

//import com.jude.utils.JUtils;
//import com.lifeng.f300.common.utils.LogUtils;


import com.lifeng.f300.common.utils.LogUtils;

import rx.Observer;


/**
 * Created by happen on 2017/8/7.
 */
public abstract class ServiceResponse<T> implements Observer<T> {

    @Override
    public void onNext(T t) {
        _onNext(t);
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
       LogUtils.d("wang","Server Error:"+e.getLocalizedMessage()+"  ---  "+e.getCause().toString());
       if(e.getCause() instanceof  ServiceException){
           LogUtils.d("wang","instance of ServiceException");
           _onError(((ServiceException) e.getCause()).getInfo());
       }else{
           LogUtils.d("wang","not ServiceException");
           _onError(e.getCause().toString());
       }
    }

    protected abstract void _onError(String message);
    protected abstract void _onNext(T t);
}
