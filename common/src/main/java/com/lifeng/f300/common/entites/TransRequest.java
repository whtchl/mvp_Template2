package com.lifeng.f300.common.entites;

import java.io.Serializable;

/**
 * Created by happen on 2017/9/1.
 */

public class TransRequest implements Serializable {

    private static final long serialVersionUID = 8093268038505860369L;

    public String  SECONDARYKEY;	                     //  二级秘钥

    public String track2 = "";                           //2磁道信息
    public String track3 = "";                           //3磁道信息
    public String iccData="";                            //iccdata(Date55)
    public String cash = "";                             //现金额
    public String bankAmount ="";                        //银行卡金额
    public String bankType ="";                          //第三方支付类型  银行卡1、支付宝2、微信3、其它4虚拟银行卡,5微信支付（被扫）、6支付宝（被扫）
    public String traceNo="";                            //流水号
    public String poseCode= "";                          //终端号
    public String merchantcode = "";                     //商户号
    public String operatorId = "";                       //操作员号
    public String batchno = "";                          //批次号
    public String pan = "";                              //银行卡号
    public String pin = "";                              //银行卡密码
    public String billNo = "";                           //订单号
    public int transType;                                //交易的类型
    public int type;                                     //为撤单做准备的交易类型：1现金 2、银行卡支付 3、扫码 4、储值 5积分  6优惠券 7 计次
    public String bankCardReadType="";                   //银行卡读取方式  0刷磁条卡；1插IC卡；2非接
    public String data23="";                             //银行卡序列号
    public String cardDate;                              //银行卡有效期
    public String deviceSn;                              //设备SN号
/*
    public String bankCardReadType="";              //银行卡读取方式  0刷磁条卡；1插IC卡；2非接
    public String data23="";            //银行卡序列号
    public String bankCard = "";            //银行卡号
    public String bankPin = "";            //银行卡密码
    public String merchantcode = "";            //商户号
    public String userName = "";            //操作员号
    public String batchno = "";            //批次号*/



    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getBankAmount() {
        return bankAmount;
    }

    public void setBankAmount(String bankAmount) {
        this.bankAmount = bankAmount;
    }
}
