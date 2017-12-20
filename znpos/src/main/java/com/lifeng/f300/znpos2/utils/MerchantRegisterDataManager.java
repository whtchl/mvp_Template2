package com.lifeng.f300.znpos2.utils;

import android.text.TextUtils;

import com.lifeng.f300.common.utils.DesUtils;
import com.lifeng.f300.common.utils.FileUtils;
import com.lifeng.f300.common.utils.LogUtils;
import com.lifeng.f300.common.utils.PosDataUtils;
import com.lifeng.f300.common.utils.StringUtil;
import com.lifeng.f300.config.ConnectURL;

import org.json.JSONObject;

/**
 * Created by happen on 2017/8/15.
 */

public class MerchantRegisterDataManager {

    private static MerchantRegisterDataManager instance;
    private RegisterData registerData;
    private String content = "";

    private String ip_port="";

    /**
     * 获取实例
     *
     * @return
     */
    public static MerchantRegisterDataManager getInstance() {
        instance = new MerchantRegisterDataManager();
        return instance;
    }

    /**
     * 获取注册的基本信息
     */
    public RegisterData getRegisterData() {
        return registerData;
    }

    public static class RegisterData {
        //激活参数
        public String IP;                //地址：192.168.1.14
        public String LOGO;                //图像：100000000000064535.jpg
        public String MASTERKEY;        //主密钥：564CA6C1CE5FA3BF
        public String MERCHANT_CODE;    //商户编号:100000000000064
        public String MESSAGE;            //消息
        public String PORT;                //服务器端口:8088
        public String POSCODE;            //终端号:00000011
        public String STATUS;            //00
        public String RESPONSE_TIME;    //响应的时间
        public String MERCHANTNAME;     //商家名称
        public String AREA_DESC;       //地区
        public String VERSION_DESC;//会员系统的版本
        public String AGENT_ID;//应用市场的唯一标识(代理号)
        public boolean merchantRegisterIsOk;//app是否激活成功
    }


    private MerchantRegisterDataManager() {
        loadData();
    }

/*    private void toStrings(){
        LogUtils.d("wang","ip:"+registerData.IP+
                 "\n MASTERKEY:"+registerData.MASTERKEY+
                 "\n MERCHANT_CODE:"+registerData.MERCHANT_CODE+
                 "\n MESSAGE:"+registerData.MESSAGE+
                 "\n PORT:"+registerData.PORT+
                 "\n POSCODE:"+registerData.POSCODE+
                "\n STATUS:"+registerData.STATUS+
                "\n RESPONSE_TIME:"+registerData.RESPONSE_TIME+
                "\n MERCHANTNAME:"+registerData.MERCHANTNAME+
                "\n AREA_DESC:"+registerData.AREA_DESC+
                "\n VERSION_DESC:"+registerData.VERSION_DESC+
                "\n AGENT_ID:"+registerData.AGENT_ID
        );
    }*/

    /**
     * 加载数据
     */
    public void loadData() {
        LogUtils.d("wang","loadData");
        // 读取商户数据存储文件
       /* if (BuildModle.isHI98()) {
            content = FileUtils.readFile(Hi98.REGISTER_DATA_HI98, "UTF-8");
        }*/

       content = FileUtils.readFile(ConnectURL.APP_PATH+ConnectURL.FILE_REGISTER, PosDataUtils.CHARSET_UTF_8);
        if (TextUtils.isEmpty(content)) {
            LogUtils.d("wang",ConnectURL.FILE_REGISTER+"的内容是空");
            RegisterData data = new RegisterData();
            data.merchantRegisterIsOk = false;
            registerData = data;

        } else {
            try {
                LogUtils.d("wang","getDesString");
                content = DesUtils.getDesString(content, ConnectURL.DESUTILS_KEY.getBytes());//解密
                JSONObject json = new JSONObject(content);
                RegisterData data = new RegisterData();

                data.IP = StringUtil.getJsonInfo(json, "IP");
                data.LOGO = StringUtil.getJsonInfo(json, "LOGO");
                data.MASTERKEY = StringUtil.getJsonInfo(json, "MASTERKEY");
                data.MERCHANT_CODE = StringUtil.getJsonInfo(json, "MERCHANT_CODE");
                data.MESSAGE = StringUtil.getJsonInfo(json, "MESSAGE");
                data.PORT = StringUtil.getJsonInfo(json, "PORT");
                data.POSCODE = StringUtil.getJsonInfo(json, "POSCODE");
                data.STATUS = StringUtil.getJsonInfo(json, "STATUS");
                data.RESPONSE_TIME = StringUtil.getJsonInfo(json, "RESPONSE_TIME");
                data.MERCHANTNAME = StringUtil.getJsonInfo(json, "MERCHANTNAME");
                data.AREA_DESC = StringUtil.getJsonInfo(json, "AREA_DESC");
                data.VERSION_DESC = StringUtil.getJsonInfo(json, "VERSION_DESC");
                data.AGENT_ID = StringUtil.getJsonInfo(json, "AGENT_ID");

                LogUtils.d("wang","ip:"+data.IP);
                data.merchantRegisterIsOk = true;
                registerData = data;
            } catch (Exception e) {
                RegisterData data = new RegisterData();
                data.merchantRegisterIsOk = false;
                registerData = data;
            }
        }
    }
}
