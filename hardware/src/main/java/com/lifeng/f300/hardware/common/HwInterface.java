package com.lifeng.f300.hardware.common;

import com.lifeng.f300.common.entites.BankEntity;
import com.lifeng.f300.common.entites.TransResponse;
import com.lifeng.f300.common.exception.HwException;

/**
 * Created by happen on 2017/8/16.
 */

public interface HwInterface {
    //初始化设备
    public void initHwObject();

    //加载主秘钥
    public void loadMasterKey(final String responseResult,final TransResponse transResponse) throws HwException;

    //读取银行卡卡号
    public void readBankCardNum();

    //读取银行卡信息
    public BankEntity readBankCardInfo();

    //获取机身编号
    public String getDeviceSn();

    //加载工作秘钥
    public void loadWorkKey(final TransResponse t);

    //读取刷银行卡的信息
    public BankEntity hwLoadCrashBankCard(final String transAmt);

    //关闭读取银行卡信息
    public void closeReadBankCard();


}
