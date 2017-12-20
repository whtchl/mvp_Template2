package com.lifeng.f300.sqlite.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.lifeng.f300.common.entites.OperatorInfoBrief;
import com.lifeng.f300.common.entites.TransDataBrief;

/**
 * Created by happen on 2017/9/5.
 */

public class TransDataDBTable extends AbsDBTable<TransDataBrief> {
    private static TransDataDBTable instance = new TransDataDBTable();

    public static TransDataDBTable getInstance() {
        return instance;
    }

    public static final String TABLE_NAME_TRANS_DATA = "transData";

/*
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
    *//*<!-- 其他参数  -->*//*
    private String batch_no;
    private String trace_no;
    private String cash_amount;
    private String bank_amount;
    private String bank_type;
    private String bank_trade_type;
    private String pay_type;

    *//*<!-- biz_code根据业务需要取不同的值,如会员卡充值取值:mb_card_dpt_add -->*//*
    private String biz_type;
    private String biz_value;


    *//*<!-- 银行卡交易需要的参数 -->*//*
    private String track2;
    private String track3;
    private String icc_data;
    private String pin_data;
    private String card_seq;
    *//*<!-- 扫码支付参数 -->*//*
    private String scan_auth_code;*/


    public static class Column {
        public static String SERIAL_NUMBER = "serial_number";
        public static String  REQ_TYPE = "req_type";
        public static String REQ_TIME = "req_time";
        public static String HARDWARE_SN = "hardware_sn";
        public static String POS_LOCATION = "pos_location";
        public static String MCH_CODE = "mch_code";
        public static String POS_CODE = "pos_code";
        public static String POS_OPERATOR = "pos_operator";
        public static String API_VERSION = "api_version";
        public static String APP_VERSION = "app_version";
        public static String SIGN = "sign";
        public static String BATCH_NO = "batch_no";
        public static String TRACE_NO = "trace_no";
        public static String CASH_AMOUNT = "cash_amount";

        public static String BANK_AMOUNT = "bank_amount";
        public static String BANK_TYPE = "bank_type";
        public static String BANK_TRADE_TYPE = "bank_trade_type";
        public static String PAY_TYPE = "pay_type";

        public static String BIZ_TYPE = "biz_type";
        public static String BIZ_VALUE = "biz_value";

        public static String TRACK2 = "track2";
        public static String TRACK3 = "track3";
        public static String ICC_DATA = "icc_data";
        public static String PIN_DATA = "pin_data";
        public static String CARD_SEQ="card_seq";

        public static String SCAN_AUTH_CODE =  "scan_auth_code";
    }



    @Override
    public String create() {
        StringBuffer buffer = new StringBuffer("CREATE TABLE IF NOT EXISTS ");
        buffer.append(TABLE_NAME_TRANS_DATA+ "(");
        buffer.append(TransDataDBTable.Column.SERIAL_NUMBER + " VARCHAR(6) NOT NULL,");

        buffer.append(TransDataDBTable.Column.REQ_TYPE + " VARCHAR(20) NOT NULL,");
        buffer.append(TransDataDBTable.Column.REQ_TIME + " VARCHAR(19) NOT NULL,");
        buffer.append(TransDataDBTable.Column.HARDWARE_SN + " VARCHAR(32) NOT NULL,");
        buffer.append(TransDataDBTable.Column.POS_LOCATION + " VARCHAR(32),");
        buffer.append(TransDataDBTable.Column.MCH_CODE + " VARCHAR(15) NOT NULL,");
        buffer.append(TransDataDBTable.Column.POS_CODE + " VARCHAR(8) NOT NULL,");
        buffer.append(TransDataDBTable.Column.POS_OPERATOR + " VARCHAR(20) NOT NULL,");
        buffer.append(TransDataDBTable.Column.API_VERSION + " VARCHAR(20) NOT NULL,");
        buffer.append(TransDataDBTable.Column.APP_VERSION + " VARCHAR(32) NOT NULL,");
        buffer.append(TransDataDBTable.Column.SIGN + " VARCHAR(32) NOT NULL,");
        buffer.append(TransDataDBTable.Column.BATCH_NO + " VARCHAR(10) NOT NULL,");
        buffer.append(TransDataDBTable.Column.TRACE_NO + " VARCHAR(10) NOT NULL,");
        buffer.append(TransDataDBTable.Column.CASH_AMOUNT + " VARCHAR(10),");
        buffer.append(TransDataDBTable.Column.BANK_AMOUNT + " VARCHAR(10) ,");
        buffer.append(TransDataDBTable.Column.BANK_TYPE + " VARCHAR(10) DEFAULT '0' ,");
        buffer.append(TransDataDBTable.Column.BANK_TRADE_TYPE + " VARCHAR(10),");
        buffer.append(TransDataDBTable.Column.PAY_TYPE + " VARCHAR(10) DEFAULT '0' ,");

        buffer.append(TransDataDBTable.Column.BIZ_TYPE + " VARCHAR(32),");
        buffer.append(TransDataDBTable.Column.BIZ_VALUE + " VARCHAR(100),");

        buffer.append(TransDataDBTable.Column.TRACK2 + " VARCHAR(200),");
        buffer.append(TransDataDBTable.Column.TRACK3 + " VARCHAR(200),");
        buffer.append(TransDataDBTable.Column.ICC_DATA + " VARCHAR(1000),");
        buffer.append(TransDataDBTable.Column.PIN_DATA + " VARCHAR(32),");
        buffer.append(TransDataDBTable.Column.CARD_SEQ + " VARCHAR(32),");

        buffer.append(TransDataDBTable.Column.SCAN_AUTH_CODE + " VARCHAR(32)");

        buffer.append(" );");

        return buffer.toString();
    }

    @Override
    public ContentValues to(TransDataBrief object) {

        ContentValues vals = new ContentValues();
        vals.put(TransDataDBTable.Column.SERIAL_NUMBER, object.getSerial_number());
        vals.put(TransDataDBTable.Column.REQ_TYPE, object.getReq_type());
        vals.put(TransDataDBTable.Column.REQ_TIME, object.getReq_type());
        vals.put(TransDataDBTable.Column.HARDWARE_SN, object.getHardware_sn());
        vals.put(TransDataDBTable.Column.POS_LOCATION, object.getPos_location());
        vals.put(TransDataDBTable.Column.MCH_CODE, object.getMch_code());
        vals.put(TransDataDBTable.Column.POS_CODE, object.getPos_code());
        vals.put(TransDataDBTable.Column.POS_OPERATOR, object.getPos_operator());
        vals.put(TransDataDBTable.Column.API_VERSION, object.getApi_version());
        vals.put(TransDataDBTable.Column.APP_VERSION, object.getApp_version());
        vals.put(TransDataDBTable.Column.SIGN, object.getSign());
        vals.put(TransDataDBTable.Column.BATCH_NO, object.getBatch_no());
        vals.put(TransDataDBTable.Column.TRACE_NO, object.getTrace_no());
        vals.put(TransDataDBTable.Column.CASH_AMOUNT, object.getCash_amount());
        vals.put(TransDataDBTable.Column.BANK_AMOUNT, object.getBank_amount());
        vals.put(TransDataDBTable.Column.BANK_TYPE, object.getBank_type());
        vals.put(TransDataDBTable.Column.BANK_TRADE_TYPE, object.getBank_trade_type());
        vals.put(TransDataDBTable.Column.PAY_TYPE, object.getPay_type());

        vals.put(TransDataDBTable.Column.BIZ_TYPE, object.getBiz_type());
        vals.put(TransDataDBTable.Column.BIZ_VALUE, object.getBiz_value());

        vals.put(TransDataDBTable.Column.TRACK2, object.getTrack2());
        vals.put(TransDataDBTable.Column.TRACK3, object.getTrack3());
        vals.put(TransDataDBTable.Column.ICC_DATA, object.getIcc_data());
        vals.put(TransDataDBTable.Column.PIN_DATA, object.getPin_data());
        vals.put(TransDataDBTable.Column.CARD_SEQ, object.getCard_seq());

        vals.put(TransDataDBTable.Column.SCAN_AUTH_CODE, object.getScan_auth_code());

        return vals;
    }

    @Override
    public TransDataBrief from(Cursor cursor) {

        TransDataBrief transDataBrief = new TransDataBrief(
                cursor.getString(cursor.getColumnIndexOrThrow(TransDataDBTable.Column.SERIAL_NUMBER)),
                cursor.getString(cursor.getColumnIndexOrThrow(TransDataDBTable.Column.REQ_TYPE)),
                cursor.getString(cursor.getColumnIndexOrThrow(TransDataDBTable.Column.REQ_TIME)),
                cursor.getString(cursor.getColumnIndexOrThrow(TransDataDBTable.Column.HARDWARE_SN)),
                cursor.getString(cursor.getColumnIndexOrThrow(TransDataDBTable.Column.POS_LOCATION)),
                cursor.getString(cursor.getColumnIndexOrThrow(TransDataDBTable.Column.MCH_CODE)),
                cursor.getString(cursor.getColumnIndexOrThrow(TransDataDBTable.Column.POS_CODE)),
                cursor.getString(cursor.getColumnIndexOrThrow(TransDataDBTable.Column.POS_OPERATOR)),
                cursor.getString(cursor.getColumnIndexOrThrow(TransDataDBTable.Column.API_VERSION)),
                cursor.getString(cursor.getColumnIndexOrThrow(TransDataDBTable.Column.APP_VERSION)),
                cursor.getString(cursor.getColumnIndexOrThrow(TransDataDBTable.Column.SIGN)),
                cursor.getString(cursor.getColumnIndexOrThrow(TransDataDBTable.Column.BATCH_NO)),
                cursor.getString(cursor.getColumnIndexOrThrow(TransDataDBTable.Column.TRACE_NO)),
                cursor.getString(cursor.getColumnIndexOrThrow(TransDataDBTable.Column.CASH_AMOUNT)),
                cursor.getString(cursor.getColumnIndexOrThrow(TransDataDBTable.Column.BANK_AMOUNT)),
                cursor.getString(cursor.getColumnIndexOrThrow(TransDataDBTable.Column.BANK_TYPE)),
                cursor.getString(cursor.getColumnIndexOrThrow(TransDataDBTable.Column.BANK_TRADE_TYPE)),
                cursor.getString(cursor.getColumnIndexOrThrow(TransDataDBTable.Column.PAY_TYPE)),

                cursor.getString(cursor.getColumnIndexOrThrow(TransDataDBTable.Column.BIZ_TYPE)),
                cursor.getString(cursor.getColumnIndexOrThrow(TransDataDBTable.Column.BIZ_VALUE)),

                cursor.getString(cursor.getColumnIndexOrThrow(TransDataDBTable.Column.TRACK2)),
                cursor.getString(cursor.getColumnIndexOrThrow(TransDataDBTable.Column.TRACK3)),
                cursor.getString(cursor.getColumnIndexOrThrow(TransDataDBTable.Column.ICC_DATA)),
                cursor.getString(cursor.getColumnIndexOrThrow(TransDataDBTable.Column.PIN_DATA)),
                cursor.getString(cursor.getColumnIndexOrThrow(TransDataDBTable.Column.CARD_SEQ)),

                cursor.getString(cursor.getColumnIndexOrThrow(TransDataDBTable.Column.SCAN_AUTH_CODE))
        );

        return transDataBrief;

    }
}
