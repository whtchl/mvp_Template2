package com.lifeng.f300.znpos2.demo;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.lifeng.f300.common.utils.LogUtils;
import com.lifeng.f300.common.widget.WaitingDialog;
import com.lifeng.f300.config.Permission;
import com.lifeng.f300.hardware.g100.DeviceManager;
import com.lifeng.f300.hardware.g100.SecretKeyManager;
import com.lifeng.f300.znpos2.R;
import com.witsi.sdk.mpos.WtDevContrl;

/**
 * Created by happen on 2017/10/26.
 */

public class LoadAidKey {
    private static final int OK = 1;
    protected static final int SUCCESS = 0;
    protected static final int FAILS = -1;
    protected static final int MSG_IMPORT_AID_KEY_DELAY = 0x0C;
    private DeviceManager deviceManager;
    private WtDevContrl wtDevContrl;
    private Context context;
    private String[] paramList = new String[] {
            "9F0607A0000000041010DF0101009F08020002DF1105FC5080A000DF1205F85080F800DF130504000000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF180100",
            "9F0607A0000000043060DF0101009F08020002DF1105FC5058A000DF1205F85058F800DF130504000000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF180101",
            "9F0607A0000000031010DF0101009F08020140DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF180100",
            "9F0607A0000000032010DF0101009F08020140DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF180100",
            "9F0607A0000000033010DF0101009F08020140DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF180100",
            "9F0607A0000000651010DF0101009F08020200DF1105FC6024A800DF1205FC60ACF800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF180100",
            "9F0608A000000333010101DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06000000100000DF1906000000100000DF2006000000100000DF2106000000100000",
            "9F0608A000000333010102DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06000000100000DF1906000000100000DF2006000000100000DF2106000000100000",
            "9F0608A000000333010103DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06000000100000DF1906000000100000DF2006000000100000DF2106000000100000",
            "9F0608A000000333010106DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06000000100000DF1906000000100000DF2006000000100000DF2106000000100000",
    };

    /**
     * 导入Aid密钥
     * @param wtDevContrl 芯片对象
     */
    public LoadAidKey(Context context) {
        this.context = context;
    }

    public void loadAidSecretInSystem() {
        if(Permission.getInstance().controlPowOnOff()){
            DeviceManager.posPowerOn(); //上电
        }

        handler.sendEmptyMessageDelayed(MSG_IMPORT_AID_KEY_DELAY, 700);
    }

    /**
     * 导入Aid密钥
     */
    private void saveAidKey() {
        LogUtils.d("wang", "enter aid Key");
        // 导入工作密钥
        wtDevContrl = WtDevContrl.getInstance();
        manager = new SecretKeyManager(wtDevContrl);
        deviceManager = new DeviceManager(context, wtDevContrl);
        deviceManager.initDevice();
        handler.sendEmptyMessageDelayed(OK, 700);
    }

    private int deviceConnectionedLoopCount = 0;
    private WaitingDialog waitingDialog;

    // 延迟操作
    private Handler handler = new Handler() {
        private Runnable runnable;
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case OK:
                    if (wtDevContrl.isConnected()) {
                        // 子线程中的回调
                        SecretKeyManager.OnLoadSecretKeyCallback workAidCallback = new SecretKeyManager.OnLoadSecretKeyCallback(){
                            @Override
                            public void onLoadState(boolean state) {
                                LogUtils.d("wang", state+" aid is OK");
                                if(waitingDialog != null){
                                    waitingDialog.dismiss();
                                    waitingDialog = null;
                                }
                                if (state) {
                                    handlerToast.sendEmptyMessage(SUCCESS);
                                } else {
                                    handlerToast.sendEmptyMessage(FAILS);
                                }
                                DeviceManager.posPowerOff();
                            }
                        };
                        manager.setOnLoadAidKeyCallback(workAidCallback);
                        manager.loadAidKey(paramList);
                    } else {
                        LogUtils.d("wang", "device connection error.");
                        runnable = new Runnable(){
                            public void run(){
                                if (deviceConnectionedLoopCount > 3) {
                                    deviceConnectionedLoopCount = 0;
                                    LogUtils.d("wang", "device connection error.");
                                    LogUtils.d("wang","设备未连接，导入Aid失败");
                                } else {
                                    ++deviceConnectionedLoopCount;
                                    handler.sendEmptyMessageDelayed(OK, 700);
                                }
                            }
                        };
                        handler.post(runnable);
                    }
                    break;

                case MSG_IMPORT_AID_KEY_DELAY:
                    waitingDialog = new WaitingDialog(context, R.style.CustomProgressDialog,"正在更新Aid密钥");
                    waitingDialog.show();
                    saveAidKey();
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private Handler handlerToast = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    wtDevContrl.destroy();
                    loadAidListenear.getAidSate(true);   //导入Aid密钥成功
                    break;
                case FAILS:
                    wtDevContrl.destroy();
                    loadAidListenear.getAidSate(false);  //导入Aid密钥失败
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    //接口的回调
    public interface LoadAidListenear{
        public void getAidSate(boolean flag);
    }
    public LoadAidListenear loadAidListenear;
    private SecretKeyManager manager;
    public void setAidCallBack(LoadAidListenear loadAidListenear){
        this.loadAidListenear=loadAidListenear;
    }
}
