package com.lifeng.f300.znpos2.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.serialport.SerialDev;
import android.support.v4.app.FragmentActivity;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.BaseException;
import com.lifeng.f300.common.utils.BuildModle;
import com.lifeng.f300.common.utils.LogUtils;
import com.lifeng.f300.common.utils.ToastMakeUtils;
import com.lifeng.f300.common.widget.AllDialog;
import com.lifeng.f300.common.widget.BankCarshDialog;
import com.lifeng.f300.config.ConnectURL;
import com.lifeng.f300.config.Permission;
import com.lifeng.f300.hardware.g100.DeviceManager;
import com.lifeng.f300.znpos2.utils.BdPermission;
import com.witsi.sdk.mpos.WtCallback;
import com.witsi.sdk.mpos.WtDevContrl;

/**
 * Created by happen on 2017/10/26.
 */

public class BaseReadCardActivity extends FragmentActivity {
    public WtDevContrl wtDevContrl;
    public boolean initEmvOK=false;
 /*   public CardBinder vipCb;  //盛本操作会员卡的对象
    protected PBOCBinder pboc;
    protected PinpadBinder pinpad;
    protected BankOperateSb bankOperateSb;   //盛本操作银行卡的对象
    protected BankOperateHI98 bankOperateHI98Cancel;*/

    protected HttpUtils http = new HttpUtils(ConnectURL.TIMEOUT);
    public static final String BROARD_FILTER1 ="com.lifeng.f300.znpos.ACTION_LOGOUT1";
    protected Gson gson = new Gson();
    /** 默认初始化状态 */
    private final int STEP_DEFAULT = 0x02;
    /** 刷卡完成 */
    private final int STEP_READ_BANK_CARD_COMPLETE = 0x03;
    /** 初始化设备准备 */
    private static final int MSG_INIT_DEVICE_SETUP_TIME = 0x12;
    /** 初始化设备准备时间 */
    private static final int INIT_DEVICE_SETUP_TIME_DELAY = 200;
    private int step = STEP_DEFAULT;

    /**
     * 继承FragmentActivity：是为了兼容低版本（3.0以下）getSupportFragmentManager() 获取对象FragmentManager
     * 继承Activity：高版本：3.0以上 getFragmentManager() 获取对象FragmentManager
     * */

    /**
     * 上电
     * @param savedInstanceState
     * @throws BaseException
     */
    protected void onCreateEqually(Bundle savedInstanceState)
            throws BaseException {
        LogUtils.d("wang", "onCreateEqually!!!!!!!!!!");
        if (BuildModle.isG100()) {
            LogUtils.d("wang", "onCreateEqually!!!!!!!!!2!");
            if(Permission.getInstance().controlPowOnOff()){
                LogUtils.d("wang", "onCreateEqually!!!!!!!!!!3");
                DeviceManager.posPowerOn();
            }
            LogUtils.d("wang", "onCreateEqually!!!!!!!!!4!");
        }
        registerReceiver(finishBaseReadCardActivity, new IntentFilter(BROARD_FILTER1));
    }

    private BroadcastReceiver finishBaseReadCardActivity = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
            System.exit(0);
        }
    };

    //
    /**
     * 打开芯片
     * @throws BaseException
     */
    protected void onStartEqually() throws BaseException {
        LogUtils.d("wang", "onStartEqually");
        if (BuildModle.isG100()) {
            if(BdPermission.getInstance().demoG100() && false){
                LogUtils.d("wang", "init WtDevContrl ");
                wtDevContrl = WtDevContrl.getInstance();
                wtDevContrl.initDev(this, SerialDev.getInstance().initSerialDev(this, "ttyMT1", 115200));
            }else{
                wtDevContrl = WtDevContrl.getInstance();
                if (!initEmvOK) {
                    LogUtils.d("wang", "initEmvOK:"+initEmvOK);
                    wtDevContrl.initDev(this, SerialDev.getInstance().initSerialDev(this, "ttyMT1", 115200));
                    step = STEP_DEFAULT;
                    execStep();
                }else{
                    LogUtils.d("wang", "不需要重新初始化设备");
                }
            }
//			BluetoothService.deviceName = BluetoothesUtils.getMac();
//			Intent service = new Intent(this, BluetoothService.class);
//			startService(service);
        }/*else if(BdPermission.getInstance().demoG100()){
			LogUtils.d("wang", "init WtDevContrl ");
			wtDevContrl = WtDevContrl.getInstance();
			wtDevContrl.initDev(this, SerialDev.getInstance().initSerialDev(this, "ttyMT1", 115200));
		}*/
       else{
            LogUtils.d("wang", "其他的设备");
        }
    }

    /**
     * 关闭设备和土司
     *
     * @throws BaseException
     */
    protected void onStopEqually() throws BaseException {
        LogUtils.d("wang", "onStopEqually");
        if (BuildModle.isG100()) {
            wtDevContrl.Led(0x01, false);
        }
        ToastMakeUtils.cancel();   //关闭土司
        AllDialog.closeDialog();   //关闭弹框

        BankCarshDialog.closeDialog();//关闭银行卡的弹框

       /* if (!BuildModle.isG100()) {
            this.sendBroadcast(new Intent(CancelTransActivity.BROARD_FILTER_CANCEL));//关闭撤销界面
        }*/
    }

    private void execStep() {
        switch (step) {
            case STEP_DEFAULT:
                handler.sendEmptyMessageDelayed(MSG_INIT_DEVICE_SETUP_TIME,INIT_DEVICE_SETUP_TIME_DELAY);
                break;

            default:
                break;
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 读卡完成
                case STEP_READ_BANK_CARD_COMPLETE:
                    execStep();
                    break;

                // 初始化设备准备
                case MSG_INIT_DEVICE_SETUP_TIME:
                    wtDevContrl.initEmv(1, new WtCallback.WtEmvInitCallback() {
                        @Override
                        public void onGetEmvInitState(WtCallback.EmvInitState state) {
                            switch (state) {
                                case EMVINIT_SUCC://初始化成功
                                    initEmvOK=true;
                                    LogUtils.d("wang", "初始化成功");
                                    break;
                                case EMVINIT_IO_ERR://IO 错误
                                    initEmvOK=false;
                                    onStop();
                                    LogUtils.d("wang", "IO 错误");
                                    break;
                                case EMVLIB_LOAD_ERR://EMV 库加载失败
                                    initEmvOK=false;
                                    onStop();
                                    LogUtils.d("wang", "EMV 库加载失败");
                                    break;
                                case EMVFILE_LOAD_ERR://EMV 公钥证书加载失败
                                    initEmvOK=false;
                                    onStop();
                                    LogUtils.d("wang", "EMV 公钥证书加载失败");
                                    break;
                                case EMVINIT_OTHER_ERR://其他错误
                                    initEmvOK=false;
                                    onStop();
                                    LogUtils.d("wang", "其他错误");
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            onCreateEqually(savedInstanceState);

        } catch (BaseException e) {

            handleException(e);

        } catch (Exception e) {

            handleException(e);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {

            onStartEqually();

        } catch (BaseException e) {

            handleException(e);

        } catch (Exception e) {

            handleException(e);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {

            onStopEqually();

        } catch (BaseException e) {

            handleException(e);

        } catch (Exception e) {

            handleException(e);
        }
    }

    public void handleException(Exception exEXCP) {
        exEXCP.printStackTrace();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {

            onStopEqually();

        } catch (BaseException e) {

            handleException(e);

        } catch (Exception e) {

            handleException(e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            onStartEqually();
        } catch (BaseException e) {
            handleException(e);
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(finishBaseReadCardActivity);

    }
}
