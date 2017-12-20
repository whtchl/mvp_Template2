package com.lifeng.f300.common.utils;

import android.os.Build;
import android.text.TextUtils;


/**
 * Created by happen on 2017/8/15.
 */

public class BuildModle {


    /**
     * 是否是海信HI98的设备
     * @return
     */
    public static boolean isHI98(){
        if (!TextUtils.isEmpty(Build.MODEL) && Build.MODEL.startsWith("HI98")) {
            return true;
        }
        return false;
    }

    /**
     * 是否是F300的设备
     * @return
     */
    public static boolean isF300(){
        if (!TextUtils.isEmpty(Build.MODEL) && Build.MODEL.startsWith("F300")) {
            return true;
        }
        return false;
    }


    /**
     * 是否是G100 波导的设备
     * @return
     */
    public static boolean isG100(){
        if (!TextUtils.isEmpty(Build.MODEL) && Build.MODEL.startsWith("G100") ) {
            return true;
        }
        return false;
    }
}
