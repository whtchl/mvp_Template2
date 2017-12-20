package com.lifeng.f300.hardware.g100;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.lifeng.f300.common.entites.TransResponse;
import com.lifeng.f300.common.utils.Constants;
import com.lifeng.f300.common.utils.LogUtils;
import com.lifeng.f300.config.IntentActions;
import com.lifeng.f300.config.Permission;
import com.lifeng.f300.hardware.R;
import com.witsi.sdk.mpos.WtDevContrl;

/**
 * Created by happen on 2017/10/25.
 */

public class LoadWorkKey {
    private static final int OK = 1;
    protected static final int SUCCESS = 0;
    protected static final int FAILS = -1;
    protected static final int MSG_IMPORT_WORK_KEY_DELAY = 0x0A;
    private DeviceManager deviceManager;
    private TransResponse selectEntity;
    private WtDevContrl wtDevContrl;
    private SecretKeyManager manager;
    private Context context;
    private boolean isPullDown=false;//F300是否是拆机自毁状态,false:就是没有拆过机子

    /**
     * 导入工作密钥
     * @param context
     * @param selectEntity 签到的数据
     */
	public LoadWorkKey(Context context,TransResponse selectEntity) {
        this.context = context;
        this.selectEntity = selectEntity;
    }

    /**
     * 导入荔峰的工作密钥
     */
    public void loadWorkSecretInSystem() {
        if(Permission.getInstance().controlPowOnOff()){
            DeviceManager.posPowerOn(); //上电
        }

        context.registerReceiver(pullDownBroadcastReceiver, new IntentFilter(IntentActions.PULL_DOWN_BROARD));//监听拆机广播
        handler.sendEmptyMessageDelayed(MSG_IMPORT_WORK_KEY_DELAY, Constants.DELAY_TIME);
    }

    private BroadcastReceiver pullDownBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isPullDown=true;//已经是拆机自毁状态了，不能进行操作了F300,这个广播是在上电的时候由底层发送上来的
        }
    };

    private String workKeyVerbuf[] = { "0000000000000000", "0000000000000000", "0000000000000000" };
    private String[] workKey = new String[3];
    private String[] workKeyValbuf = new String[3];

    /**
     * 导入工作密钥
     * @param selectEntity
     */
    private void saveSecretKey(TransResponse selectEntity) {
        data = new Bundle();
        message = Message.obtain();
        if (!TextUtils.isEmpty(selectEntity.PINKEY) && !TextUtils.isEmpty(selectEntity.SECONDARYKEY)
                && selectEntity.PINKEY.length() >= 32 && selectEntity.SECONDARYKEY.length() >= 32) {
            // 工作密钥
            workKey[0] = selectEntity.PINKEY.substring(0, 32);
            workKey[2] = selectEntity.SECONDARYKEY.substring(0, 32);
            workKey[1] = null; //默认

            // 效验码
            workKeyValbuf[0] = selectEntity.PINKEY.substring(32);
            workKeyValbuf[2] = selectEntity.SECONDARYKEY.substring(32);
            workKeyValbuf[1] = null; //默认

            LogUtils.d("wang", "pin密钥:" + workKey[0] +  "pin校验码：" + workKeyValbuf[0]);
            LogUtils.d("wang", "磁道密钥:" + workKey[2] +  "磁道校验码: " + workKeyValbuf[2]);

            // 导入工作密钥
            wtDevContrl = WtDevContrl.getInstance();
            manager = new SecretKeyManager(wtDevContrl);
            deviceManager = new DeviceManager(context, wtDevContrl);
            deviceManager.initDevice();
            handler.sendEmptyMessageDelayed(OK, Constants.DELAY_TIME);
        }else{
            message.what=FAILS;
            data.putString(Constants.TOAST, "工作密钥为空或密钥位数错误");
            message.setData(data);
            handlerToast.sendMessage(message);
            LogUtils.d("wang", "工作密钥为空或者位数异常");
        }
    }

    private int deviceConnectionedLoopCount = 0;

    // 延迟操作
    private Handler handler = new Handler() {
        private Runnable runnable;
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case OK:
                    if (null == wtDevContrl) {
                        message.what=FAILS;
                        data.putString(Constants.TOAST, "芯片对象初始化为空");
                        message.setData(data);
                        handlerToast.sendMessage(message);
                        return ;
                    }

                    if (isPullDown) {
                        message.what=FAILS;
                        data.putString(Constants.TOAST, context.getString(R.string.f300_error));
                        message.setData(data);
                        handlerToast.sendMessage(message);
                        return ;
                    }

                    if (wtDevContrl.isConnected()) {
                        // 子线程中的回调
                        if(Permission.getInstance().isLoadSameKey()){
                            /*pin密钥:4bf318c2da3be594de82daf3345ed1dfpin校验码：e39d5253
                            磁道密钥:96978c9fe6a35bd6d2371170f7a65d4b磁道校验码: de658c8e*/
                            // 工作密钥
                            String PINKEY = "4bf318c2da3be594de82daf3345ed1df";
                            String[] sameworkKey = new String[3];
                            String[] sameworkKeyValbuf = new String[3];

                            sameworkKey[0] = "4bf318c2da3be594de82daf3345ed1df";
                            sameworkKey[2] = "96978c9fe6a35bd6d2371170f7a65d4b";
                            sameworkKey[1] = null; //默认

                            // 效验码
                            workKeyValbuf[0] = "e39d5253";
                            workKeyValbuf[2] = "de658c8e";
                            workKeyValbuf[1] = null; //默认
                            manager.setWorkKey(workKey, workKeyVerbuf, workKeyValbuf);

                        }else{
                            manager.setWorkKey(workKey, workKeyVerbuf, workKeyValbuf);

                        }
                        SecretKeyManager.OnLoadSecretKeyCallback workKeyCallback = new SecretKeyManager.OnLoadSecretKeyCallback(){
                            @Override
                            public void onLoadState(boolean state) {
                                LogUtils.d("wang", state+" work is OK");
                                if (state) {
                                    message.what=SUCCESS;
                                    data.putString(Constants.TOAST, "签到成功");
                                    message.setData(data);
                                    handlerToast.sendMessage(message);
                                } else {
                                    message.what=FAILS;
                                    data.putString(Constants.TOAST, "工作密钥失效");
                                    message.setData(data);
                                    handlerToast.sendMessage(message);
                                }
                                DeviceManager.posPowerOff();
                            }
                        };
                        manager.setOnLoadWorkKeyCallback(workKeyCallback);
                        manager.loadWorkKey();
                    } else {
                        LogUtils.d("wang", "device connection error.");
                        runnable = new Runnable(){
                            public void run(){
                                if (deviceConnectionedLoopCount > 3) {
                                    deviceConnectionedLoopCount = 0;
                                    message.what=FAILS;
                                    data.putString(Constants.TOAST, "芯片连接失败");
                                    message.setData(data);
                                    handlerToast.sendMessage(message);
                                } else {
                                    ++deviceConnectionedLoopCount;
                                    handler.sendEmptyMessageDelayed(OK, Constants.DELAY_TIME);
                                }
                            }
                        };
                        handler.post(runnable);
                    }
                    break;

                case MSG_IMPORT_WORK_KEY_DELAY:
                    saveSecretKey(selectEntity);
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
            data = msg.getData();
            switch (msg.what) {
                case SUCCESS:
                    wtDevContrl.destroy();
                    loadWorkListenear.getWorkState(true,data.getString(Constants.TOAST));   //导入工作密钥成功
                    break;
                case FAILS:
                    wtDevContrl.destroy();
                    loadWorkListenear.getWorkState(false,data.getString(Constants.TOAST));  //导入工作密钥失败
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    //接口的回调
    public interface LoadWorkListenear{
        public void getWorkState(boolean flag,String toastInfo);
    }
    public LoadWorkListenear loadWorkListenear;
    private Bundle data;
    private Message message;
    public void setWorksCallBack(LoadWorkListenear loadWorkListenear){
        this.loadWorkListenear=loadWorkListenear;
    }
}

