package com.lifeng.f300.common.entites;

import java.io.Serializable;

/**
 * 银行卡交易的必备要素
 * Created by happen on 2017/9/4.
 */
/*
09-13 10:32:43.558: D/wang(26700):
        HI98密码: 40D5B86B0DBAF5D9
        交易卡号：6226890091868173
        二磁道：6226890091868173D21082011924490100000
        三磁道：
        icc Data(55)：9F2608032798D791D79D559F2701809F101307020103A00000040A010000000000FA24FDE69F3704631958649F360201C6950500000000009A031709139C01009F02060000000000015F2A02015682027C009F1A0201569F03060000000000009F3303E0F1C89F3501229F1E085465726D696E616C8408A0000003330101029F090200209F410400000004
        卡序列号：001
        third code:
        BankCardReadType:2
        expdate:2108
*/
public class BankEntity implements Serializable {
    public String PAN="";  //银行卡号
    public String TRANK2="";
    public String TRANK3="";
    public String ICCDATA="";//55域
    public String CARDDATE="";//卡有效期
    public String PIN="";//卡密码

    public String getPIN() {
        return PIN;
    }

    public void setPIN(String PIN) {
        this.PIN = PIN;
    }

    public String getPINLEN() {
        return PINLEN;
    }

    public void setPINLEN(String PINLEN) {
        this.PINLEN = PINLEN;
    }

    public String PINLEN="";//密码长度
    public String DATA23="";//银行卡序列号

    public String getBANKCARDREADTYPE() {
        return BANKCARDREADTYPE;
    }

    public void setBANKCARDREADTYPE(String BANKCARDREADTYPE) {
        this.BANKCARDREADTYPE = BANKCARDREADTYPE;
    }

    public String BANKCARDREADTYPE="";//银行卡类型 0刷磁条卡；1插IC卡；2非接


    public String getDATA23() {
        return DATA23;
    }
    public void setDATA23(String dATA23) {
        DATA23 = dATA23;
    }
    public String getPAN() {
        return PAN;
    }
    public void setPAN(String pAN) {
        PAN = pAN;
    }
    public String getTRANK2() {
        return TRANK2;
    }
    public void setTRANK2(String tRANK2) {
        TRANK2 = tRANK2;
    }
    public String getTRANK3() {
        return TRANK3;
    }
    public void setTRANK3(String tRANK3) {
        TRANK3 = tRANK3;
    }
    public String getICCDATA() {
        return ICCDATA;
    }
    public void setICCDATA(String iCCDATA) {
        ICCDATA = iCCDATA;
    }
    public String getCARDDATE() {
        return CARDDATE;
    }
    public void setCARDDATE(String cARDDATE) {
        CARDDATE = cARDDATE;
    }


}
