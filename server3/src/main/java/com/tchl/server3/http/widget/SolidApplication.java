package com.tchl.server3.http.widget;

import android.app.Application;
import android.os.Environment;

import com.jude.utils.JUtils;

import java.io.File;

/**
 * Created by happen on 2017/12/18.
 */

public class SolidApplication extends Application {
    private static SolidApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        //ToastUtils.init(this);
        JUtils.initialize(this);
        JUtils.setDebug(true, "wang");
    }

    public static SolidApplication getInstance() {
        return mInstance;
    }

    @Override
    public File getCacheDir() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File cacheDir = getExternalCacheDir();
            if (cacheDir != null && (cacheDir.exists() || cacheDir.mkdirs())) {
                return cacheDir;
            }
        }
        return super.getCacheDir();
    }
}
