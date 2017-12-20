package com.lifeng.f300.common.entites;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by happen on 2017/12/6.
 */

public class LpResponse  implements Serializable {
    private static final long serialVersionUID = 5940447851375677787L;


    //列表信息
    public String id;            // 报案编号
    //String happen_time;   //  报案时间
    //String report_money;  // 报案金额*//*


    //详情

    public String  address           ;// 报案地址
    public String  report_time       ;// 报案时间
    public String  informant         ;//报案人
    public String  informant_phone   ;//报案人手机
    public String  informant_open_id ;//报案人微信
    public String  cause             ;//    案情介绍
    public String report_imgs       ;//报案图片
    public String  report_money      ;//索赔金额
    public String  happen_time       ;//出险时间
    public String  policy_code       ;//确认理赔后的理赔单号
    public String  paid_money        ;//确认理赔金额
    public String  status            ;//状态：1 待初审确认 3 上级审核中 5 待财务打款 7 待核销 9 已核销 11 零赔付结案
    public String  close_time        ;// 结案时间
    public String  close_money       ;// 结案金额
    public String  remark            ;// 理赔进度备注
    public List<Object> logs;

    public String name;
    public String path;
}
