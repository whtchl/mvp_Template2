package com.lifeng.f300.hardware.common;

import com.jude.utils.JUtils;
import com.lifeng.f300.common.exception.HwException;
import com.lifeng.f300.common.utils.LogUtils;

import rx.Observer;


/**
 * Created by happen on 2017/8/18.
 */
public abstract class HwResponse<T> implements Observer<T> {

    @Override
    public void onNext(T t) {
        _onNext(t);
    }

    @Override
    public void onCompleted() {
        JUtils.Log("hwResponse onCompleted");
    }

    @Override
    public void onError(Throwable e) {

        LogUtils.d("wang","Hw Error:"+e.getLocalizedMessage()+"   "+e.getMessage()+"   "+e.getClass().getName());
        if(e instanceof HwException){
            LogUtils.d("wang","instance of hwException");
            _onError(((HwException) e).getInfo());
        }
        else{
            LogUtils.d("wang","not hwException");
            _onError("写文件错误");
        }
    }

    protected abstract void _onError(String message);
    protected abstract void _onNext(T t);
}