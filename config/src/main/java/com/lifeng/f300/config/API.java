package com.lifeng.f300.config;

/**
 * Created by happen on 2017/8/7.
 */

public class API {
    public static class WRAPPER {
        public static final String STATUS = "status";
        public static final String DATA = "data";
        public static final String MESSAGE = "MESSAGE";
    }

    public static class CODE {

        public static final String  SUCCEED ="00";

        public static final String ANALYSIS_ERROR = "-1";
        public static final String NET_INVALID = "-2";
        public static final String TIMEOUT="TimeoutException";
        public static final String CONN_EXP = "ConnectException";
    }
    public static class CODE_MEG {
        public static final String TIMEOUT_MEG="服务器连接超时";
        public static final String OTHER_MSG = "网络错误4";
        public static final String CONN_EXP_MSG = "服务器连接失败";
    }
}
