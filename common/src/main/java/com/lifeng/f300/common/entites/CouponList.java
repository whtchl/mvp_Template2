package com.lifeng.f300.common.entites;

import java.io.Serializable;

/**
 * Created by happen on 2017/9/4.
 */

public class CouponList implements Serializable {

    private static final long serialVersionUID = 1245742674218768902L;

    public String CONTENT;//优惠券内容 [MONEY][EXPIREDATE][COUPON]
    public String COUPONCODE;// 编号优惠券794762902815
    public String COUPONID;//优惠券id 1
    public String COUPONSTATUS;// 优惠券状态2
    public String EXPIREDATE;// 有效期 2016-06-07 00:00:00
    public String PRICE;// 优惠券标价值 10.00
    public String SENDMERCHANTID;//发行商户id 227
    public String SENDMERCHANTNAME;//发行商户名称 海林市昆仑社区服务有限公司
    public String SENDMOBILE;//接收手机号 15247170266
    public String SENDTIME;//发行时间 2015-03-07 11:34:25
    public boolean isCheck;
    public String CARD;

    //查询优惠券批次
    public String  BATCHID;
    public String CREATEDCOUNT;
    public String BATCHNAME;
    public int TOTAL;
    public int EXPIREDAYS;
    public String CANCELEDCOUNT;
    public String ACTIVATEDCOUNT;
    public String EXCHANGEDCOUNT;

}
