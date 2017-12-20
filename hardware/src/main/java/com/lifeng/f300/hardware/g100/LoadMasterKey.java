package com.lifeng.f300.hardware.g100;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;

import com.jude.utils.JUtils;
import com.lifeng.f300.common.entites.TransResponse;
import com.lifeng.f300.common.utils.Constants;
import com.lifeng.f300.common.utils.LogUtils;
import com.lifeng.f300.config.Permission;
import com.lifeng.f300.hardware.R;
import com.witsi.sdk.mpos.WtDevContrl;

/**
 * Decripe : 导入设备主密钥F300
 * Created by happen on 2017/10/17.
 */

public class LoadMasterKey {
    private static final int OK = 1;
    protected static final int SUCCESS = 0;
    protected static final int FAILS = -1;
    protected static final int MSG_IMPORT_MASTER_KEY_DELAY = 0x0B;
    private String masterKeyVerbuf = "0000000000000000";
    private DeviceManager deviceManager;
    private TransResponse selectEntity;
    private WtDevContrl wtDevContrl;
    private SecretKeyManager manager;
    private Context context;
    private String masterKey = "";
    private String masterKeyValbuf = "";
    private boolean isPullDown=false;//F300是否是拆机自毁状态,false:就是没有拆过机子

    /**
     * 导入主密钥
     * @param context
     * @param selectEntity 签到的数据
     */
    public LoadMasterKey(Context context,TransResponse selectEntity) {
        this.context = context;
        this.selectEntity = selectEntity;
    }

    public void     loadMasterSecretInSystem() {
        JUtils.Log("loadMasterSecretInSystem");
        if(Permission.getInstance().controlPowOnOff()){
            DeviceManager.posPowerOn(); //上电
        }

        //context.registerReceiver(pullDownBroadcastReceiver, new IntentFilter(Constants.PULL_DOWN_BROARD));//监听拆机广播
        handler.sendEmptyMessageDelayed(MSG_IMPORT_MASTER_KEY_DELAY, Constants.DELAY_TIME);
    }

    private BroadcastReceiver pullDownBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isPullDown=true;//已经是拆机自毁状态了，不能进行操作了F300,这个广播实在上电的时候由底层发送上来的
        }
    };

    /**
     * 导入主密钥
     * @param selectEntity
     */
    private void saveMasterKey(TransResponse selectEntity) {
        JUtils.Log("saveMasterKey");
        data = new Bundle();
        message = Message.obtain();
        if (!TextUtils.isEmpty(selectEntity.MASTERKEY) && selectEntity.MASTERKEY.length() >= 32) {
            // 主密钥：下发下来是明文的：先通过32个F对明文的主密钥加密
            masterKey= selectEntity.MASTERKEY.substring(0, 32);

            // 校验码: 用明文的主密钥对16个0加密，取前8位为校验码
            masterKeyValbuf= selectEntity.MASTERKEY.substring(32);
            JUtils.Log( "主密钥密文:" + masterKey +  "主密钥的校验码：" + masterKeyValbuf);

            // 导入主密钥

            wtDevContrl = WtDevContrl.getInstance();
            if(wtDevContrl == null){   JUtils.Log( " wtDevContrl is null ");}
            manager = new SecretKeyManager(wtDevContrl);
            deviceManager = new DeviceManager(context, wtDevContrl);
            deviceManager.initDevice();
            handler.sendEmptyMessageDelayed(OK, Constants.DELAY_TIME);
        }else{
            message.what=FAILS;
            data.putString(Constants.TOAST, "主密钥为空或者位数异常");
            message.setData(data);
            handlerToast.sendMessage(message);
            JUtils.Log( "主密钥为空或者位数异常");
        }
    }

    private int deviceConnectionedLoopCount = 0;

    // 延迟操作
    private Handler handler = new Handler() {
        private Runnable runnable;
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case OK:
                    JUtils.Log("message ok");
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
                    JUtils.Log("wtDevContrl.isConnected()");
                    if (wtDevContrl.isConnected()) {

                        // 子线程中的回调
                        if(Permission.getInstance().isLoadSameKey()){
                            //主密钥密文:485a201ecac8a229a5f6d8da0472e205主密钥的校验码：9f841ae4
                            String sameMasterKey = "485a201ecac8a229a5f6d8da0472e205";
                            String samemasterKeyValbuf = "9f841ae4";
                            manager.setMasterKey(sameMasterKey, masterKeyVerbuf, samemasterKeyValbuf);
                        }else{
                            manager.setMasterKey(masterKey, masterKeyVerbuf, masterKeyValbuf);
                        }


                        SecretKeyManager.OnLoadSecretKeyCallback workKeyCallback = new SecretKeyManager.OnLoadSecretKeyCallback(){
                            @Override
                            public void onLoadState(boolean state) {
                                JUtils.Log( state+" master is OK");
                                if (state) {
                                    message.what=SUCCESS;
                                    data.putString(Constants.TOAST, "初始化成功");
                                    message.setData(data);
                                        handlerToast.sendMessage(message);
                                } else {
                                    message.what=FAILS;
                                    data.putString(Constants.TOAST, "主密钥错误");
                                    message.setData(data);
                                    handlerToast.sendMessage(message);
                                }
                                if(Permission.getInstance().controlPowOnOff()){
                                    DeviceManager.posPowerOff();

                                }
                            }
                        };
                        manager.setOnLoadMasterKeyCallback(workKeyCallback);
                        manager.loadMasterKey();
                    } else {
                        JUtils.Log("device connection error.");
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

                case MSG_IMPORT_MASTER_KEY_DELAY:
                    saveMasterKey(selectEntity);
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
                    loadMasterListenear.getMasterState(true,data.getString(Constants.TOAST));   //导入密钥成功
                    break;
                case FAILS:
                    wtDevContrl.destroy();
                    loadMasterListenear.getMasterState(false,data.getString(Constants.TOAST));  //导入密钥失败
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    //接口的回调
    public interface LoadMasterListenear{
        public void getMasterState(boolean flag,String toastInfo);//提示信息
    }
    public LoadMasterListenear loadMasterListenear;
    private Bundle data;
    private Message message;
    public void setMasterCallBack(LoadMasterListenear loadMasterListenear){
        this.loadMasterListenear=loadMasterListenear;
    }
}
