package com.lifeng.f300.hardware.hi98;

import android.os.Build;
import android.os.Environment;

import com.lifeng.f300.common.entites.BankEntity;
import com.lifeng.f300.common.entites.TransResponse;
import com.lifeng.f300.config.ConnectURL;
import com.lifeng.f300.hardware.common.HwInterface;

/**
 * Created by happen on 2017/8/15.
 */


public class Hi98 implements HwInterface{
    public static final String PATH_SPECIAL_HI98 = Environment
            .getExternalStorageDirectory().getPath();

    public static final String HI98_SN = "HI98";  //HI98 POS
    public static final String REGISTER_DATA_HI98 = PATH_SPECIAL_HI98 + ConnectURL.PATH_SPECIAL + ConnectURL.FILE_REGISTER;



    private static Hi98 mInstance;

    private Hi98() {
    }

    public static Hi98 getInstance() {
        if (mInstance == null) {
            synchronized (Hi98.class) {
                if (mInstance == null) {
                    mInstance = new Hi98();
                }
            }
        }
        return mInstance;
    }

    @Override
    public void initHwObject() {

    }

    @Override
    public void loadMasterKey(final String responseResult,final TransResponse transResponse) {

    }

    @Override
    public void readBankCardNum() {

    }

    @Override
    public BankEntity readBankCardInfo() {
         return  null;
    }

    @Override
    public String getDeviceSn() {
        return Build.SERIAL;
    }

    @Override
    public void loadWorkKey(final TransResponse transResponse) {

    }

    @Override
    public BankEntity hwLoadCrashBankCard(String transAmt) {
        return null;
    }

    @Override
    public void closeReadBankCard() {

    }
}

