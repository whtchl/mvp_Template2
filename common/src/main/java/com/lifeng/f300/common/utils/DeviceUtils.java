package com.lifeng.f300.common.utils;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.os.*;
import com.jude.utils.JUtils;


/**
 * Describe : 设备工具类<br/>
 * Date : 2015年11月1日下午4:22:28 <br/>
 * Version : 1.0 <br/>
 *
 * @author wangkeze
 */
public class DeviceUtils {
    /** 机身序列号为16位 */
    public static final int DEVICE_SN_LENGTH = 16;

    /** Intel芯片设备型号 */
    public static final String DEVICE_MODE_NAME_INTEL = "S600B";


    /**
     * Intel方案设备
     *
     * @return
     */
    public static boolean isIntelDevice() {
        return Build.MODEL.contains(DEVICE_MODE_NAME_INTEL);
    }

    /**
     * 获取机身编号
     *
     * @return
     */
    public static String getDeviceSn(Context context) {
        JUtils.Log("Build mode:"+Build.MODEL);
        if (BuildModle.isHI98()) {
            return Build.SERIAL;
        } else if (BuildModle.isF300()) {
            /*String sn = SystemProperties.get("ro.dev.barcode");
            if (!TextUtils.isEmpty(sn)) {
                if (sn.length() >= DEVICE_SN_LENGTH) {
                    return sn.substring(0, DEVICE_SN_LENGTH).trim();
                }else{
                    return sn.trim();
                }
            }else{
                return "";
            }*/
            return "21D2015113000050";//getSn(context);
        } else if(BuildModle.isG100()){
            return getSn(context);
        }else{
            return Build.SERIAL;
        }
    }


    public static String getSn(Context context){
        StringBuilder deviceId = new StringBuilder();

        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String sn = tm.getSimSerialNumber();
        JUtils.Log("sn"+sn);
        if(StringUtil.isNotEmpty(sn)) {
            //deviceId.append("sn");
            deviceId.append(sn);
            JUtils.Log("getDeviceId : ", deviceId.toString());
            return deviceId.toString();
        }
        return "";
    }
}
