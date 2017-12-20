package com.lifeng.f300.common.entites;

import java.io.Serializable;

/**
 * Created by happen on 2017/10/16.
 */

public class SettlementRequest implements Serializable {

    private static final long serialVersionUID = -7993512288618509447L;

    public String 	MERCHANTCODE="";                           //商户编号
    public String 	POSCODE="";                                //POS终端号
    public String   BATCHNUM="";                               //批次号
    public String 	DEVICEID="";                               //POS设备序列号
    public String 	VISION="";                                 //终端版本号
    public String 	USERNAME="";                               //操作员号
    public String 	KEYTYPE="";                                //秘钥类型
    public String 	CONSUME_COUNT_TRADE="";                    //消费总笔数   消费笔数不包括撤销的笔数
    public String 	CONSUME_DEPOSIT_ADD="";                    //充值金额
    public String 	CONSUME_DEPOSIT_SUB="";                    //储值消费
    public String 	CONSUME_POINT_SUB="";                      //积分消费
    public String 	CONSUME_CASH_AMOUNT="";                    //现金消费
    public String 	CONSUME_BANK_AMOUNT="";                    //银行卡消费--
    public String 	CONSUME_COUPON_AMOUNT="";                  //优惠券消费
    public String 	CANCEL_COUNT_TRADE="";                     //被撤销的总笔数--
    public String 	CANCEL_DEPOSIT_ADD="";                     //被撤销的充值
    public String 	CANCEL_DEPOSIT_SUB="";                     //被撤销的储值消费
    public String 	CANCEL_POINT_SUB="";                       //被撤销的积分消费
    public String 	CANCEL_CASH_AMOUNT="";                     //被撤销的现金消费
    public String 	CANCEL_BANK_AMOUNT="";                     //被撤销的银行卡消费  被撤销的总额
    public String 	CANCEL_COUPON_AMOUNT="";                   //被消费的优惠券消费
    public String 	INVOICE_ID_LIST="";                        // INVOICE_ID流水id--凭证号
    public String   SECONDARYKEY;	                           //  二级秘钥

}
