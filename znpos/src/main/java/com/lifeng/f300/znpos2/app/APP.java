package com.lifeng.f300.znpos2.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.lifeng.beam.Beam;

import com.jude.utils.JUtils;
import com.lifeng.f300.common.utils.LogUtils;
import com.lifeng.f300.hardware.common.Hw;
import com.lifeng.f300.sqlite.db.DbManager;

/**
 * Created by Mr.Jude on 2015/7/28.
 */
public class APP extends Application {
    private static APP instance = null;
    public static APP getInstance(){
        return instance;
    }
   // public RefWatcher RefWatcher;

    // 荔峰F300
    public final static String PULL_DOWN_BROARD = "android.intent.witsi.action.safetytrigge";
    // 推送的app唯一id
    public static String softSn = "";
    public static String pushAppid = "";
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.d("wang","onCreate1");

        if ("com.lifeng.f300.znpos".equals(getCurProcessName(getApplicationContext()))) {
            LogUtils.d("wang","onCreate2");
            instance = this;
            //RefWatcher = LeakCanary.install(this);
            //Fresco.initialize(this);
            JUtils.initialize(this);
            JUtils.setDebug(true, "wang");

            DbManager.initialize(this);
            Hw.initialize(this);
            //JFileManager.getInstance().init(this, Dir.values());
            //DataCleaner.Update(this, 18);
            Beam.init(this);
            Beam.setActivityLifeCycleDelegateProvider(ActivityDelegate::new);
            Beam.setViewExpansionDelegateProvider(PaddingTopViewExpansion::new);
            /*ListConfig.setDefaultListConfig(new ListConfig().
                    setRefreshAble(true).
                    setContainerLayoutRes(R.layout.activity_recyclerview).
                    setContainerProgressRes(R.layout.include_loading).
                    setPaddingNavigationBarAble(true));
            ViewConfig.setDefaultViewConfig(new ViewConfig().
                    setProgressRes(R.layout.activity_progress).
                    setErrorRes(R.layout.activity_error)
            );*/
        }

    }

    /* 一个获得当前进程的名字的方法 */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }



}
