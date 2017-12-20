package com.lifeng.f300.server.bean;

/**
 * Created by happen on 2017/8/9.
 */
//请求
/*{"DEVICETYPE":"HI98"
"LICENSE":"9852846966339432"
"SOFT_SN":"3982066066217156061"
"PUSH_APPID":"8933075"
"REQUEST_TIME":"1502076431905"
"TYPE":"activate"
"MD5":"404483a196730adcd7c91891a575e6c0"
"DEVICEID":"1S3D0638611C41HFW000043"}*/
//回复
/*{
"AGENT_ID": "888888000888888" ,
"MERCHANT_CODE": "100000000000003" ,
"TRANSFER_KEY": "" ,
"PORT": "8080" ,
"VERSION_DESC": "" ,
"IP": "111.1.41.104" ,
"LOGO": "http://test.lifengpay.com/upload/100000000000003/logo.png" ,
"MASTERKEY": "ae0820f025d3fbbde67c4bb18773446bdd5ed9c9" ,
"MESSAGE": "" ,
"STATUS": "00" ,
"POSCODE": "10000349" ,
"AREA_DESC": "北京市市辖区东城区" ,
"REQUST_TYPE": "activate" ,
"MERCHANTNAME": "荔峰测试3" ,
"RESPONSE_TIME": "2017-08-07 11:37:22"
}*/


public class ActiviteBean {
    public String DEVICETYPE;
    public String LICENSE;
    public String SOFT_SN;
    public String PUSH_APPID;
    public String REQUEST_TIME;
    public String TYPE;
    public String MD5;
    public String DEVICEID;

    public String getDEVICETYPE() {
        return DEVICETYPE;
    }

    public void setDEVICETYPE(String DEVICETYPE) {
        this.DEVICETYPE = DEVICETYPE;
    }

    public String getLICENSE() {
        return LICENSE;
    }

    public void setLICENSE(String LICENSE) {
        this.LICENSE = LICENSE;
    }

    public String getSOFT_SN() {
        return SOFT_SN;
    }

    public void setSOFT_SN(String SOFT_SN) {
        this.SOFT_SN = SOFT_SN;
    }

    public String getPUSH_APPID() {
        return PUSH_APPID;
    }

    public void setPUSH_APPID(String PUSH_APPID) {
        this.PUSH_APPID = PUSH_APPID;
    }

    public String getREQUEST_TIME() {
        return REQUEST_TIME;
    }

    public void setREQUEST_TIME(String REQUEST_TIME) {
        this.REQUEST_TIME = REQUEST_TIME;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getMD5() {
        return MD5;
    }

    public void setMD5(String MD5) {
        this.MD5 = MD5;
    }

    public String getDEVICEID() {
        return DEVICEID;
    }

    public void setDEVICEID(String DEVICEID) {
        this.DEVICEID = DEVICEID;
    }

/*    public String DEVICETYPE;
    public String LICENSE;
    public String SOFT_SN;
    public String PUSH_APPID;
    public String REQUEST_TIME;
    public String TYPE;
    public String MD5;
    public String DEVICEID;*/


    @Override
    public String toString() {
        return "ActiviteBean{" +
                "DEVICETYPE='" + DEVICETYPE + '\'' +
                ", LICENSE='" + LICENSE + '\'' +
                ", SOFT_SN='" + SOFT_SN + '\'' +
                ", PUSH_APPID=" + PUSH_APPID +
                ", REQUEST_TIME='" + REQUEST_TIME + '\'' +
                ", TYPE=" + TYPE +
                ", MD5='" + MD5 + '\'' +
                ", DEVICEID=" + DEVICEID +
                '}';
    }
}
