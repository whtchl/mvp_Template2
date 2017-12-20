package com.lifeng.f300.common.entites;

/**
 * Created by happen on 2017/9/7.
 */

public class CancelTransBean {

    public String operatorId="";  //操作员
    public String cardNo="";  //会员卡
    public String passWord="";  //会员卡密码
    public String track2="";
    public String track3="";
    public String data55="";
    public String bankPin="";
    public String bankType=""; //0没有、1银行卡、2微信、3支付宝、4其它
    public String invoiceId=""; //会员系统流水号
    public String oriBatchno=""; //原交易批次号
    public String oriTraceNo=""; //原交易流水号
    public String data23="";//序列号

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getTrack2() {
        return track2;
    }

    public void setTrack2(String track2) {
        this.track2 = track2;
    }

    public String getTrack3() {
        return track3;
    }

    public void setTrack3(String track3) {
        this.track3 = track3;
    }

    public String getData55() {
        return data55;
    }

    public void setData55(String data55) {
        this.data55 = data55;
    }

    public String getBankPin() {
        return bankPin;
    }

    public void setBankPin(String bankPin) {
        this.bankPin = bankPin;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getOriBatchno() {
        return oriBatchno;
    }

    public void setOriBatchno(String oriBatchno) {
        this.oriBatchno = oriBatchno;
    }

    public String getOriTraceNo() {
        return oriTraceNo;
    }

    public void setOriTraceNo(String oriTraceNo) {
        this.oriTraceNo = oriTraceNo;
    }

    public String getData23() {
        return data23;
    }

    public void setData23(String data23) {
        this.data23 = data23;
    }

    public String getBankCardReadType() {
        return bankCardReadType;
    }

    public void setBankCardReadType(String bankCardReadType) {
        this.bankCardReadType = bankCardReadType;
    }

    public String bankCardReadType="";//0刷磁条卡；1插IC卡；2非接
}
