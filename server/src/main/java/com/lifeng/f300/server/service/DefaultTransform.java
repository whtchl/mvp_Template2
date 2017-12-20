package com.lifeng.f300.server.service;

import com.lifeng.f300.common.utils.LogUtils;

import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by happen on 2017/8/7.
 */

public class DefaultTransform<T> implements Observable.Transformer<T, T> {
    @Override
    public Observable<T> call(Observable<T> tObservable) {

        return tObservable.doOnError(new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                LogUtils.d("wang"," doOnError call " + Thread.currentThread().getName()+ throwable.getLocalizedMessage());
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }
}
