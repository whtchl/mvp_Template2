package com.lifeng.f300.common.entites;

/**
 * Created by happen on 2017/9/5.
 */

public class TransDataBrief {


    private String serial_number;
    //request
    /*<!-- 公共参数 -->*/
    private String req_type;
    private String req_time;
    private String hardware_sn;
    private String pos_location;
    private String mch_code;
    private String pos_code;
    private String pos_operator;
    private String api_version;
    private String app_version;
    private String sign;
    /*<!-- 其他参数  -->*/
    private String batch_no;
    private String trace_no;
    private String cash_amount;
    private String bank_amount;
    private String bank_type;
    private String bank_trade_type;
    private String pay_type;

    /*<!-- biz_code根据业务需要取不同的值,如会员卡充值取值:mb_card_dpt_add -->*/
    private String biz_type;
    private String biz_value;


    /*<!-- 银行卡交易需要的参数 -->*/
    private String track2;
    private String track3;
    private String icc_data;
    private String pin_data;
    private String card_seq;
    /*<!-- 扫码支付参数 -->*/
    private String scan_auth_code;

    public TransDataBrief(String serial_number,

                          String req_type,
                          String req_time,
                          String hardware_sn,
                          String pos_location,
                          String mch_code,
                          String pos_code,
                          String pos_operator,
                          String api_version,
                          String app_version,
                          String sign,

                          String batch_no,
                          String trace_no,
                          String cash_amount,
                          String bank_amount,
                          String bank_type,
                          String bank_trade_type,
                          String pay_type,

                          String biz_type,
                          String biz_value,

                          String track2,
                          String track3,
                          String icc_data,
                          String pin_data,
                          String card_seq,

                          String scan_auth_code) {

        this.serial_number = serial_number;

        this.req_type = req_type;
        this.req_time = req_time;
        this.hardware_sn = hardware_sn;
        this.pos_location = pos_location;
        this.mch_code = mch_code;
        this.pos_code = pos_code;
        this.pos_operator = pos_operator;
        this.api_version = api_version;
        this.app_version = app_version;
        this.sign = sign;

        this.batch_no = batch_no;
        this.trace_no = trace_no;
        this.cash_amount = cash_amount;
        this.bank_amount = bank_amount;
        this.bank_type = bank_type;
        this.bank_trade_type = bank_trade_type;
        this.pay_type = pay_type;


        this.biz_type = biz_type;
        this.biz_value = biz_value;


        this.track2 = track2;
        this.track3 = track3;
        this.icc_data = icc_data;
        this.pin_data = pin_data;
        this.card_seq = card_seq;

        this.scan_auth_code = scan_auth_code;
    }


    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getReq_type() {
        return req_type;
    }

    public void setReq_type(String req_type) {
        this.req_type = req_type;
    }

    public String getReq_time() {
        return req_time;
    }

    public void setReq_time(String req_time) {
        this.req_time = req_time;
    }

    public String getHardware_sn() {
        return hardware_sn;
    }

    public void setHardware_sn(String hardware_sn) {
        this.hardware_sn = hardware_sn;
    }

    public String getPos_location() {
        return pos_location;
    }

    public void setPos_location(String pos_location) {
        this.pos_location = pos_location;
    }

    public String getMch_code() {
        return mch_code;
    }

    public void setMch_code(String mch_code) {
        this.mch_code = mch_code;
    }

    public String getPos_code() {
        return pos_code;
    }

    public void setPos_code(String pos_code) {
        this.pos_code = pos_code;
    }

    public String getPos_operator() {
        return pos_operator;
    }

    public void setPos_operator(String pos_operator) {
        this.pos_operator = pos_operator;
    }

    public String getApi_version() {
        return api_version;
    }

    public void setApi_version(String api_version) {
        this.api_version = api_version;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getBatch_no() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

    public String getTrace_no() {
        return trace_no;
    }

    public void setTrace_no(String trace_no) {
        this.trace_no = trace_no;
    }

    public String getCash_amount() {
        return cash_amount;
    }

    public void setCash_amount(String cash_amount) {
        this.cash_amount = cash_amount;
    }

    public String getBank_amount() {
        return bank_amount;
    }

    public void setBank_amount(String bank_amount) {
        this.bank_amount = bank_amount;
    }

    public String getBank_type() {
        return bank_type;
    }

    public void setBank_type(String bank_type) {
        this.bank_type = bank_type;
    }

    public String getBank_trade_type() {
        return bank_trade_type;
    }

    public void setBank_trade_type(String bank_trade_type) {
        this.bank_trade_type = bank_trade_type;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getBiz_type() {
        return biz_type;
    }

    public void setBiz_type(String biz_type) {
        this.biz_type = biz_type;
    }

    public String getBiz_value() {
        return biz_value;
    }

    public void setBiz_value(String biz_value) {
        this.biz_value = biz_value;
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

    public String getIcc_data() {
        return icc_data;
    }

    public void setIcc_data(String icc_data) {
        this.icc_data = icc_data;
    }

    public String getPin_data() {
        return pin_data;
    }

    public void setPin_data(String pin_data) {
        this.pin_data = pin_data;
    }

    public String getCard_seq() {
        return card_seq;
    }

    public void setCard_seq(String card_seq) {
        this.card_seq = card_seq;
    }

    public String getScan_auth_code() {
        return scan_auth_code;
    }

    public void setScan_auth_code(String scan_auth_code) {
        this.scan_auth_code = scan_auth_code;
    }

     /*<!-- 会员卡参数 -->*/
    /*private String deposit_amount;
    private String point_amount;
    private String coupon_amount;
    private String mb_mobile;
    private String mb_card_no;
    private String mb_card_pwd;
    private String mb_coupon_code;
    private String mb_goods_order_no;*/
    /*<!-- 第三方参数-->*/

   /* private String third_trade;
    private String third_biz_type;
    private String third_biz_value;
    private String third_uid;
    private String third_org_code;
    private String third_mch_code;*/

    //response
    /*<!-- 公共参数 -->*/
/*    private String resp_code;
    private String resp_msg;
    private String sign;*/

  /*  *//*<!-- 其他参数(resp_code="00"时有效) -->*//*
    private String bank_type;
    private String bank_trade_type;
    private String pay_type;
    private String biz_type;
    private String biz_value;
    private String bank_amt;
    private String trade_status;
    private String batch_no;
    private String trace_no;
    private String trade_uid;
    private String trade_date;
    private String trade_time;
    private String fee_amt;
    private String bank_card;
    private String dc_type;
    private String req_callback_url;

    *//*<!-- 第三方参数 -->*//*
    private String third_trade;
    private String third_biz_type;
    private String third_biz_value;
    private String third_uid;
    private String third_org_code;
    private String third_mch_code;
    private String third_pos_code;
    *//*<!-- 会员卡参数 -->*//*
    private String mb_card_no;
    private String mb_mobile;
    private String mb_ori_dpt_csm_amt;
    private String mb_deposit_csm_amt;
    private String mb_largess_csm_amt;
    private String mb_point_csm_value;
    private String mb_deposit_add_amt;
    private String mb_largess_add_amt;
    private String mb_point_add_value;
    private String mb_coupon_amt;
    private String mb_coupon_code;
    private String mb_goods_order_no;
    private String mb_user_name;
    private String mb_user_id;*/
}
