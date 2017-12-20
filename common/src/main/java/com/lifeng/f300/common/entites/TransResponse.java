package com.lifeng.f300.common.entites;

import java.io.Serializable;
import java.util.List;

/**
 * Created by happen on 2017/8/10.
 * 所有交易之后返回的javaBean
 */

/*  银行卡交易后返回数据
responseInfo:{
   AUTH_CODE               : 636432 ,
   MEMBERID               :0,
   TRACE_NO               : 000003 ,
   CARD_EXPIRE               : 2108 ,
   BANK_TYPE               :1,
   BANK_AMOUNT               :2.0,
   MESSAGE               :  ,
   STATUS               : 00 ,
   BANK_CARD_TYPE               : 0 ,
   INVOICE_ID               : 000001013187 ,
   BANK_STARASN               : 622689******8173 ,
   DEVICETYPE               : HI98 ,
   IS_BD               :0,
   TRADE_SN               :100000063573,
   REMARK               : 银行卡消费：2.0; ,
   BATCHNO               : 000159 ,
   POINT_BALANCE               :0.0,
   BM_NAME               : 现代金控正式环境 ,
   TOTAL               :2.0,
   LOCAL_TIME               : 20170918141653 ,
   ORDERINFOLIST               :[],
   FEE_AMOUNT               : 0.01 ,
   TRADE_UNIQUEID               : 100003491505715413361 ,
   BM_MERID               : 834332373720001 ,
   SUB_POINT               :0.0,
   COUPON_AMOUNT               :0.0,
   BANKDATA15               : 0918 ,
   PAID               :2.0,
   BM_POSID               : 74542074 ,
   INVOICE_CODE               : http://weixin.leefengpay.com/index.php?s=/addon/Member/Member/invoiceInfo/mcode/100000000000003/uniqueid/10000349150571541336100000311 ,
   SUB_DEPOSIT               :0.0,
   ADD_POINT               :0.0,
   CARD_BANK_NAME               : 中信银行信用卡中心 ,
   REQUST_TYPE               : consume ,
   REFER_NUM               : 170918302038 ,
   GOODSINFOLIST               :[],
   DEPOSIT_BALANCE               :0,
   RESPONSE_TIME               : 2017-09-18 14:16:55 ,
   MD5               : e40bc5140d894c755dd6333705b7657a 
}

*/
/*{
        "CARD_NO": "",
        "MEMBER_NAME": "",
        "ADD_DEPOSIT": 0.00,
        "ORG_INVOICE_ID": 0,
        "BANK_TYPE": 1,
        "BANK_AMOUNT": 2.0,
        "STATUS": "00",
        "INVOICE_TYPE": 3,
        "POINT_BALANCE": 0.00,
        "LOCAL_TIME": "20170918141653",
        "BANK": 2.00,
        "TRADE_UNIQUEID": "100003491505715413361",
        "BM_MERID": "834332373720001",
        "CASH": 0.00,
        "BM_POSID": "74542074",
        "DEPOSIT_ADD": 0.00,
        "DEPOSIT_SUB": 0.00,
        "ADD_POINT": 0.00,
        "TOTAL_BONUS": 0.00,
        "REQUST_TYPE": "queryTrade",
        "GOODSINFOLIST": [],
        "POINT_SUB": 0.00,
        "AUTH_CODE": "636432",
        "MEMBERID": 0,
        "TRACE_NO": "000003",
        "MESSAGE": "",
        "INVOICE_ID": "000001013187",
        "TRANS_STATUS": 2,
        "BANK_STARASN": "622689******8173",
        "INSTALMENT": 0,
        "IS_BD": 0,
        "TRADE_SN": "100000063573",
        "REMARK": "银行卡消费：2.0;",
        "BATCHNO": "000159",
        "POINT_ADD": 0.00,
        "TOTAL": 2.00,
        "ORDERINFOLIST": [],
        "E_SIGNATURE": "1",
        "DAY_OF_MOUNTH": 0,
        "SUB_POINT": 0.00,
        "PAID": 2.00,
        "INVOICE_CODE": "http://weixin.leefengpay.com/index.php?s=/addon/Member/Member/invoiceInfo/mcode/100000000000003/uniqueid/10000349150571541336100000311",
        "SUB_DEPOSIT": 0.00,
        "REVERSE_ID": "1",
        "COUPON": 0.00,
        "REFER_NUM": "170918302038",
        "MEMBER_ID": 0,
        "DEPOSIT_BALANCE": 0.00,
        "RESPONSE_TIME": "2017-09-25 14:06:35",
        "COUPON_CODE": "",
        "TRANS_TIME": "2017-09-18 14:16:55"
        }*/

public class TransResponse implements Serializable {
    private static final long serialVersionUID = 5940447851375677789L;
    //激活
    public String MERCHANT_CODE;
    public String POSCODE;
    public String MASTERKEY;
    public String IP;
    public String PORT;
    public String LOGO;
    public String TRANSFER_KEY;
    public String MERCHANTNAME;
    public String AREA_DESC;
    public String VERSION_DESC;
    public String STATUS;
    public String MESSAGE;

    //签到
    public String MERCHANTID;
    public String MERCHANTCODE;
    public String BATCHNUM;
    public String SECONDARYKEY;
    public String MACKEY;
    public String PINKEY;
    public String MIFAREKEY;
    public String AUTH;
    public String TELNO;
    public String WEIADDRESS;
    public String CARDPREFIX;
    public String POINTRATE;
    public String CHECKPASSPORD;
    public String UPDATESTATUS;
    public String UPDATEADDR;
    public String UPDATEPORT;
    public String UPDATEPATH;
    public String UPDATEFILE;
    public String ISSUECARD;
    public String ISINS;
    public Boolean FORCE_TO_UP;
    public String ORDER_SERVER_URL ;
    public String POSPUSH_SERVER_URL ;
    public String INSURANCE_SERVER_URL ;
    public String POSNAME;



    //pay response
    public String AUTH_CODE;
    public String MEMBERID;
    public String TRACE_NO;
    public String CARD_EXPIRE;
    public String BANK_TYPE;                   //第三方支付类型 0没有、1银行卡、2微信、3支付宝、4其它(第三方银行卡) 5.yhj微信  6.yhj支付宝
    public String BANK_AMOUNT;
    public String BANK_CARD_TYPE;
    public String INVOICE_ID;
    public String BANK_STARASN;
    public String DEVICETYPE;
    public String IS_BD;
    public String TRADE_SN;
    public String REMARK;
    public String BATCHNO;
    public String POINT_BALANCE;
    public String BM_NAME;
    public String TOTAL;
    public String LOCAL_TIME;
    public String FEE_AMOUNT;
    public String TRADE_UNIQUEID;
    public String BM_MERID;
    public String SUB_POINT;
    public String COUPON_AMOUNT;
    public String BANKDATA15;
    public String PAID;
    public String BM_POSID;
    public String INVOICE_CODE;
    public String SUB_DEPOSIT;
    public String ADD_POINT;
    public String CARD_BANK_NAME;
    public String REQUST_TYPE;
    public String REFER_NUM;
    public String DEPOSIT_BALANCE;
    public String RESPONSE_TIME;
    public String MD5;

    //单笔流水查询
    public String TRANS_TIME;
    //	1.充值(支付方式：现金、银行卡、其他如支票；送积分或储值)
    //	2.计次购买（支付方式：储值余额、现金、银行卡、其他如支票)
    //	3.消费(支付方式：储值、现金、银行卡、券、积分；消费送积分)
    //	4.计次消费（减少计次数）
    //	5.积分兑换
    //	6.激活卡(送积分、送储值额)、分期返还
    //	7.生日赠积分
    //	8. 推荐人积分
    //	9. 手工调整积分
    //	10. 清除卡积分（积分清0）
    //	11. 其它送积分或储值
    //	12.退卡（卡余额清零，包括积分余额和储值余额）
    //	13.卡卡转账
    //	14.补卡
    //	15.作废
    //	16.售卡
    //	17.清除赠送储值
    //	18.充值挂账补款
    //	19.计次充值挂账补款
    //	20.撤销交易
    //	21.油卡充值
    public String INVOICE_TYPE;
    public String TRANS_STATUS;               //1、正常 2、被撤销
}
