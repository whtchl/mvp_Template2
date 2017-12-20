package com.lifeng.testlib;

import com.jude.utils.JUtils;
import com.tchl.server3.http.widget.SolidApplication;

/**
 * Created by happen on 2017/12/18.
 */

public class App  extends SolidApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        JUtils.setDebug(true,"wang");
       /* Bmob.initialize(this, "caed915330178bff62bfd281b627c77f");

        // 使用推送服务时的初始化操作
        BmobInstallation.getCurrentInstallation(this).save();
        // 启动推送服务
        BmobPush.startWork(this);

        LeakCanary.install(this);
        MultiTypeInstaller.install();*/
    }
}
