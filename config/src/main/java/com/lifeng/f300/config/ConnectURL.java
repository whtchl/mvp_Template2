package com.lifeng.f300.config;

import android.os.Environment;

/**
 * Created by happen on 2017/8/11.
 */

public class ConnectURL {
    public static final String questionsInfoWebUrl = "http://www.lifengpay.com/pos_doc/help.html?requestTime="; // 常见问题的html5地址
    public static final String companyLifengInfoWebUrl = "http://m.lifengpay.com/about.html?requestTime="; // 公司介绍的html5地址
    public static final String serviceAgreementWebUrl = "http://www.lifengpay.com/pos_doc/lifeng-agreement.html?requestTime="; // 服务协议的html5地址

    public static final String PATH_SPECIAL= "/special/";
    // 激活保存的地址：
    public static String FILE_REGISTER = "register.txt";
    // 签到保存的地址：
    public static String FIlE_SIGN = "sign.txt";
    // 激活的校验：第一次就是加载的
    public static String REGISTER_VERIFYCODE = "60641374722960456737953136489547";
    public static int TIMEOUT = 60000; // 60s超时,银行卡请求的超时
    // 本地数据加密
    public static String DESUTILS_KEY = "lifengposkeylifengposkey";
    public static String ENVIRONMENT = Environment.getExternalStorageDirectory().getPath();
    public static final String KEYTYPE = "001"; // 签到密钥管理

    //APP Path
    public static String APP_PATH="/data/data/com.lifeng.f300.znpos/";

    /** SDCard路径 */
    public static final String SD_CARD_PATH = Environment.getExternalStorageDirectory().getPath();

    //类型
    public static final String ACTIVATE = "activate";              // 激活
    public static final String SIGIN_IN = "logon";                 // 签到
    public static final String CONSUME = "consume";                // 消费
    public static final String QUERY_TRADE = "queryTrade";         // 单笔交易查询
    public static final String SETTLEMNT = "logout";               // 结算（签退）
}
