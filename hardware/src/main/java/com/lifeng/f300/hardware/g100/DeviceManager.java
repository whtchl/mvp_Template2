package com.lifeng.f300.hardware.g100;

import android.content.Context;
import android.os.Build;
import android.serialport.SerialDev;
import android.witsi.arqII.api.DataTransmit;

import com.idean.s600.pay.manage.FinanicalPowerManager;
import com.idean.s600.pay.manage.IntelPosPowerManager;
import com.jude.utils.JUtils;
import com.lifeng.f300.common.utils.BuildModle;
import com.lifeng.f300.common.utils.Constants;
import com.lifeng.f300.common.utils.DeviceUtils;
import com.lifeng.f300.config.Permission;
import com.witsi.sdk.mpos.WtCallback;
import com.witsi.sdk.mpos.WtDevContrl;
/**
 * Created by happen on 2017/10/17.
 */

public class DeviceManager {

    private static final String TAG = DeviceManager.class.getSimpleName();

    /** 设备名称(MTK) */
    private static final String DEV_NAME_MTK = "ttyMT1";
    /** 设置名称(Intel) */
    private static final String DEV_NAME_INTEL = "ttyS1";

    /** 波特率,指与底层的通讯速度 */
    private static final int BAND_RATE = 115200;

    private Context context;
    private WtDevContrl wtDevContrl;

    public DeviceManager(Context context, WtDevContrl wtDevContrl) {
        this.context = context;
        this.wtDevContrl = wtDevContrl;
    }

    /**
     * 初始化串口通讯
     *
     * @return
     */
    private DataTransmit initSerial() {
        String devName = null;
        if (DeviceUtils.isIntelDevice()) {
            devName = DEV_NAME_INTEL;
        } else {
            devName = DEV_NAME_MTK;
        }
        return SerialDev.getInstance().initSerialDev(context, devName,
                BAND_RATE);
    }

    /**
     * 初始化设备
     */
    public void initDevice() {
        JUtils.Log("start init device ###" +  Build.MODEL);
        if (Build.MODEL.contains(Constants.F300_DEVICE)
                || Build.MODEL.contains(Constants.S600_DEVICE)
                || Build.MODEL.contains(Constants.G100_DEVICE)){
            DataTransmit transmit = initSerial();
            JUtils.Log("CALL  wtDevContrl.initDev ");
            wtDevContrl.initDev(context, transmit);
        }
        JUtils.Log("end init device ###");
    }

    /**
     * 芯片是否已经通讯正常(Witsi芯片通讯),阻塞式接口
     *
     * @return
     */
    public boolean isConnected() {
        return wtDevContrl.isConnected();
    }

    /**
     * Witsi上电
     */
    public static void posPowerOn() {
        JUtils.Log("start witsi power on ###");
        new Thread() {
            public void run() {
                if (Build.MODEL.contains(Constants.F300_DEVICE)
                        || Build.MODEL.contains(Constants.S600_DEVICE)
                        || Build.MODEL.contains(Constants.G100_DEVICE)) {
                    if (DeviceUtils.isIntelDevice()) {
                        IntelPosPowerManager.posPowerOn();
                    } else {
                        FinanicalPowerManager.posPowerOn();
                    }
                    try {
                        sleep(200);
                    } catch (InterruptedException e) {
                        JUtils.Log("Thread.sleep() error."+" /n "+e);
                    }
                    JUtils.Log("end witsi power on ###");
                } else {
                    JUtils.Log("其他设备加载1 "+ Build.MODEL);
                }
            }
        }.start();
    }

    /**
     * Witsi下电
     */
    public static void posPowerOff() {
        if (Build.MODEL.contains(Constants.F300_DEVICE)
                || Build.MODEL.contains(Constants.S600_DEVICE)
                || Build.MODEL.contains(Constants.G100_DEVICE)) {
            if(Permission.getInstance().controlPowOnOff()){
                if (DeviceUtils.isIntelDevice()) {
                    IntelPosPowerManager.posPowerOff();
                } else {
                    FinanicalPowerManager.posPowerOff();
                }
            }

        } else {
            JUtils.Log("其他设备加载1 "+ Build.MODEL);
        }
    }

    /**
     * 进入密令模式(写主密钥需要先进入命令模式,演示程序时使用,正式版本不建议使用)
     */
    public void enterWitsiCmdMode() {
        JUtils.Log("enter witsi cmd mode");
        wtDevContrl.gotoCmdMode(new WtCallback.WtGotoCmdModeCallback() {
            @Override
            public void onExit() {
                JUtils.Log("Exit witsi cmd mode");
            }
        });
    }

    /**
     * 退出命令模式(设置完主密钥之后需要退出命令模式)
     */
    public void exitWitsiCmdMode() {
        new Thread() {
            public void run() {
                try {
                    if (Build.MODEL.contains(Constants.F300_DEVICE)
                            || Build.MODEL.contains(Constants.S600_DEVICE)) {
                        if(Permission.getInstance().controlPowOnOff()){
                            FinanicalPowerManager.posPowerOff();
                            Thread.sleep(200);
                        }


                        if(Permission.getInstance().controlPowOnOff()){
                            FinanicalPowerManager.posPowerOn();
                            Thread.sleep(200);
                        }


                        initDevice();
                    } else {
                        JUtils.Log("退出命令模式emv");
                    }
                } catch (InterruptedException e) {
                    JUtils.Log(e.getMessage()+"  /n  "+ e);
                }
            }
        }.start();
    }
}

