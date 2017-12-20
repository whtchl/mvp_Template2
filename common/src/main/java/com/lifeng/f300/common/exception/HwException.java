package com.lifeng.f300.common.exception;

/**
 * Created by happen on 2017/8/17.
 */

public class HwException extends Exception {
    public static class CODE{
        public static final int SUCCESS=1;
        public static final int ERR_WIRTE_FILE = 2;
        //public static final int ERR_LOADMASTERKEY =2;


    }
    public static class MSG_CODE{
         //public static  final String ERR_MSG_LOADMASTERKEY="主秘钥写入失败";
         public static final String ERR_MSG_WIRTE_FILE = "写文件失败";
    }


    private int status;
    private String message;

    public int getStatus() {
        return status;
    }

    public String getInfo() {
        return message;
    }
    public HwException(){}
    public HwException(int status, String message) {
        super("HwException status:" + status + "  message:" + message);
        this.status = status;
        this.message = message;
    }
}
