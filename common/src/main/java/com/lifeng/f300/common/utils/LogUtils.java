package com.lifeng.f300.common.utils;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

/**
 * Created by happen on 2017/8/7.
 */

public class LogUtils {
    private static final boolean modeEnable = false;// 打正式包要改成false
    private static final boolean DEBUG = true;

    public static void d(String tag, String msg) {
        d(tag, msg, null);
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (DEBUG)
            Log.d(tag, msg, tr);
    }

    public static void i(String tag, String msg) {
        i(tag, msg, null);
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (DEBUG)
            Log.i(tag, msg, tr);
    }

    public static void w(String tag, String msg) {
        w(tag, msg, null);
    }

    public static void w(String tag, Throwable tr) {
        w(tag, "", tr);
    }

    public static void w(String tag, String msg, Throwable tr) {
        Log.w(tag, msg, tr);
    }

    public static void e(String tag, String msg) {
        e(tag, msg, null);
    }

    public static void e(String tag, String msg, Throwable tr) {
        Log.e(tag, msg, tr);
    }

    @SuppressLint("NewApi")
    public static void openStrictMode() {
        if (modeEnable) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog() // 打印logcat，当然也可以定位到dropbox，通过文件保存相应的log
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }
    }

}
