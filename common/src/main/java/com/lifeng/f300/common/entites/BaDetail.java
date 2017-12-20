package com.lifeng.f300.common.entites;

import java.util.List;

/**
 * Created by happen on 2017/12/7.
 */

public class BaDetail {

    String third;
    String mchcode;
    String report_money;
    String happen_time;
    String address;
    String cause;
    String informant;
    String informant_phone;
    String sign;

    /* List<String> picName        ;
     List<String>  picBase64      ;*/
    public BaDetail(String third, String mchcode, String report_money, String happen_time
            , String address, String cause, String informant, String informant_phone,String sign) {
        third = third;
        mchcode = mchcode;
        report_money = report_money;
        happen_time = happen_time;
        address = address;
        cause = cause;
        informant = informant;
        informant_phone = informant_phone;
        sign = sign;
    }
}
