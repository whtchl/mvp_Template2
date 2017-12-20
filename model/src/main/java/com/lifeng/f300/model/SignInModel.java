package com.lifeng.f300.model;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.jude.utils.JUtils;
import com.lifeng.beam.model.AbsModel;
import com.lifeng.f300.common.entites.OperatorInfoBrief;
import com.lifeng.f300.common.entites.TransResponse;
import com.lifeng.f300.common.utils.DeviceUtils;
import com.lifeng.f300.common.utils.LogUtils;
import com.lifeng.f300.common.utils.Tools;
import com.lifeng.f300.config.ConnectURL;
import com.lifeng.f300.hardware.common.Hw;
import com.lifeng.f300.server.service.DefaultTransform;
import com.lifeng.f300.server.service.ServiceClient;
import com.lifeng.f300.sqlite.db.DbManager;
import com.lifeng.f300.sqlite.db.OperatorInfoDBTable;

import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by happen on 2017/8/22.
 */

public class SignInModel extends AbsModel {


    //private BriteDatabase mDbBrite;

    public static SignInModel getInstance() {
        return getInstance(SignInModel.class);
    }

    PackageInfo packInfo = null;
    Context mContext;
    @Override
    protected void onAppCreate(Context ctx) {
        try {
            packInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            mContext  = ctx;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        /*SqlBrite mSqlBrite = SqlBrite.create();
        mDbBrite = mSqlBrite.wrapDatabaseHelper(new DBHelper(ctx), Schedulers.io());
        mDbBrite.setLoggingEnabled(true);*/
    }

    /**
     * 判断用户名和密码是否正确
     *
     * @param name
     * @param pwd
     * @return
     */
    public Observable<List<OperatorInfoBrief>> mCheckOperatorAndPwd(final String name, final String pwd) {
        return DbManager.checkOperatorAndPwdOTB(name, pwd).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 设置中，查询操作人员
     *
     * @param name 查询操作员
     * @return
     */
    public Observable<List<OperatorInfoBrief>> mQueryOperators(final String name) {
        if ("99".equals(name)) {
            return DbManager.queryOperatorsOTB()
                    .mapToList(new Func1<Cursor, OperatorInfoBrief>() {
                                   @Override
                                   public OperatorInfoBrief call(Cursor cursor) {
                                       LogUtils.d("wang", "mQueryOperators Func1 " + Thread.currentThread().getName() +
                                               " Thread id:" + Thread.currentThread().getId());
                                       return OperatorInfoDBTable.getInstance().from(cursor);
                                   }
                               }
                    ).subscribeOn(Schedulers.io())
                    .compose(new DefaultTransform<List<OperatorInfoBrief>>());
        } else {
            return DbManager.queryOperatorByOperatorIdOTB(name)
                    .mapToList(new Func1<Cursor, OperatorInfoBrief>() {
                                   @Override
                                   public OperatorInfoBrief call(Cursor cursor) {
                                       JUtils.Log("number :" + cursor.getCount());
                                       LogUtils.d("wang", "mQueryOperators Func1 " + Thread.currentThread().getName() +
                                               " Thread id:" + Thread.currentThread().getId());
                                       return OperatorInfoDBTable.getInstance().from(cursor);
                                   }
                               }
                    ).subscribeOn(Schedulers.io())
                    .compose(new DefaultTransform<List<OperatorInfoBrief>>());
        }
    }

    /**
     * 签到
     *
     * @param username 操作员
     * @return
     */
    public Observable<TransResponse> mSignIn(final String username, final String merchant_codes, final String master_key, final String poscode) {

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("TYPE", ConnectURL.SIGIN_IN);
        hashMap.put("REQUEST_TIME", System.currentTimeMillis() + "");
        String deviceType = (TextUtils.isEmpty(Build.MODEL) ? "" : Build.MODEL);  //设备类型
        hashMap.put("DEVICETYPE", deviceType);
        hashMap.put("MERCHANTCODE", merchant_codes);
        hashMap.put("POSCODE", poscode);
        hashMap.put("DEVIDEID", DeviceUtils.getDeviceSn(mContext));  //设备号
        hashMap.put("VISION", packInfo.versionName);  //版本号
        hashMap.put("USERNAME",username);
        hashMap.put("KEYTYPE", ConnectURL.KEYTYPE);
        Tools.getInstance().calcMD5(hashMap, master_key);
        Gson gson = new Gson();
        String strEntity = gson.toJson(hashMap);
        LogUtils.d("wang","getTransRequestParams:"+strEntity);
        return ServiceClient.getService().logon(strEntity)
                .compose(new DefaultTransform<TransResponse>())
                .doOnNext(new Action1<TransResponse>() {
                    @Override
                    public void call(TransResponse transResponse) {
                        LogUtils.d("wang", " mSignIn doOnNext call " + Thread.currentThread().getName() +
                                " \n Thread id:" + Thread.currentThread().getId());
                    }
                });
    }

    /**
     * 写sign.txt 文件
     */
    public Observable<String> mWriteSignFile(final TransResponse transResponse) {
        Gson gson1 = new Gson();
        final String reslutInfo = gson1.toJson(transResponse);
        LogUtils.d("wang", "ResultInfo:" + reslutInfo);
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                LogUtils.d("wang", "mWriteSignFile " + Thread.currentThread().getName() +
                        " Thread id:" + Thread.currentThread().getId());

                try {
                    Hw.getInstance().writeSignFile(reslutInfo, transResponse);
                } catch (Exception e) {
                    subscriber.onError(e);
                }
                subscriber.onNext("");
                LogUtils.d("wang", " subscriber.onNext(\"\");");
                subscriber.onCompleted();
            }
        }).compose(new DefaultTransform<String>())
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        LogUtils.d("wang", "mloadMasterKey doOnNext " + Thread.currentThread().getName() +
                                " Thread id:" + Thread.currentThread().getId());
                    }
                });
    }

    public Observable<String> mLoadWorkKey(final  TransResponse t) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                LogUtils.d("wang", "mLoadWorkKey " + Thread.currentThread().getName() +
                        " Thread id:" + Thread.currentThread().getId());

                try {
                    Hw.getInstance().loadWorkKey(t);
                } catch (Exception e) {
                    subscriber.onError(e);
                }
                subscriber.onNext("");
                LogUtils.d("wang", " subscriber.onNext(\"\");");
                subscriber.onCompleted();
            }
        });/*.compose(new DefaultTransform<String>())
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        LogUtils.d("wang", "mloadMasterKey doOnNext " + Thread.currentThread().getName() +
                                " Thread id:" + Thread.currentThread().getId());
                    }
                });*/
    }


    /*public long addOperator(final OperatorInfoBrief operator) {
        return mDbBrite.insert(OperatorInfoDBTable.TABLE_NAME_OPERATOR_INFO, OperatorInfoDBTable.getInstance().to(operator));
    }

    public Observable<List<OperatorInfoBrief>> mQueryPersonByOperatorId(final String operatorId) {
        JUtils.Log("call mQueryPersonByOperatorId ");

        return mDbBrite.createQuery(OperatorInfoDBTable.TABLE_NAME_OPERATOR_INFO, "SELECT * FROM "
                        + OperatorInfoDBTable.TABLE_NAME_OPERATOR_INFO
                        + " WHERE "
                        + OperatorInfoDBTable.COLUMN_OPERATORID
                        + " = ?"
                , operatorId)
                .mapToList(new Func1<Cursor, OperatorInfoBrief>() {
                    @Override
                    public OperatorInfoBrief call(Cursor cursor) {
                        LogUtils.d("wang", "queryPersonByOperatorId Func1 " + Thread.currentThread().getName() +
                                " Thread id:" + Thread.currentThread().getId());
                        return OperatorInfoDBTable.getInstance().from(cursor);
                    }
                });
    }*/
}
