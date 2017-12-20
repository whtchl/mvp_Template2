package com.lifeng.f300.hardware.g100;

import android.os.Environment;
import android.serialport.SerialDev;
import android.util.Log;

import com.jude.utils.JUtils;
import com.lifeng.f300.common.entites.BankEntity;
import com.lifeng.f300.common.entites.TransResponse;
import com.lifeng.f300.common.exception.HwException;
import com.lifeng.f300.config.ConnectURL;
import com.lifeng.f300.config.Permission;
import com.lifeng.f300.hardware.common.Hw;
import com.lifeng.f300.hardware.common.HwInterface;
import com.witsi.sdk.mpos.WtDevContrl;

/**
 * Created by happen on 2017/8/15.
 */

public class G100  extends Hw implements HwInterface {
    public WtDevContrl wtDevContrl;
    public boolean initEmvOK=false;
    /** 默认初始化状态 */
    private final int STEP_DEFAULT = 0x02;

    private int step = STEP_DEFAULT;


    public static final String PATH_SPECIAL_BODAO = Environment
            .getExternalStorageDirectory().getPath();


    public static final String REGISTER_DATA_BODAO=PATH_SPECIAL_BODAO+ ConnectURL.PATH_SPECIAL+ConnectURL.FILE_REGISTER;

    private static G100 mInstance;

    private G100() {
    }

    public static G100 getInstance() {
        if (mInstance == null) {
            synchronized (G100.class) {
                if (mInstance == null) {
                    mInstance = new G100();
                }
            }
        }
        return mInstance;
    }

    @Override
    public void initHwObject(){
        if(Permission.getInstance().controlPowOnOff()){
            DeviceManager.posPowerOn();
        }

        wtDevContrl = WtDevContrl.getInstance();
        if(!initEmvOK){
            wtDevContrl.initDev(mApplicationContent, SerialDev.getInstance().initSerialDev(mApplicationContent,"ttyMT1",115200));
            step = STEP_DEFAULT;
            // execStep();
        }
    }

    @Override
    public void loadMasterKey(final String responseResult, TransResponse selectEntity) throws HwException {
        JUtils.Log("G100 loadMasterKey");
        //导G100的主密钥
        LoadMasterKey loadMasterKey = new LoadMasterKey(mApplicationContent,selectEntity);
        loadMasterKey.setMasterCallBack(new LoadMasterKey.LoadMasterListenear() {
            @Override
            public void getMasterState(boolean flag,String toastInfo) {
                if (flag) {
                    JUtils.Log("G100 loadMasterKey3");
                    try {
                        writeRegisterInSystem(responseResult);
                    } catch (HwException e) {
                        JUtils.Log(e.getInfo());
                    }
                }else{
                    JUtils.Log("toastInfo:"+toastInfo);
                    JUtils.ToastLong(toastInfo);
                }
            }
        });
        JUtils.Log("G100 loadMasterKey2");
        loadMasterKey.loadMasterSecretInSystem();
    }

    @Override
    public void readBankCardNum() {

    }

    @Override
    public BankEntity readBankCardInfo() {
        BankEntity bankEntity = new BankEntity();

        return bankEntity;
    }

    @Override
    public String getDeviceSn() {
        return null;
    }

    @Override
    public void loadWorkKey(final TransResponse transResponse) {
        LoadWorkKey loadWorkKey = new LoadWorkKey(mApplicationContent, transResponse);
        loadWorkKey.setWorksCallBack(new LoadWorkKey.LoadWorkListenear() {
            @Override
            public void getWorkState(boolean flag,String toastInfo) {
                if(flag){
                    JUtils.Log("comeintoLifengSystem");
                }
                /*if(waitingDialog != null){
                    waitingDialog.dismiss();
                    waitingDialog = null;
                }
                if (flag) {
                    comeIntoLifengSystem();
                }else{
                    showErrorInfoDialog(toastInfo,false);
                }*/
            }
        });

        loadWorkKey.loadWorkSecretInSystem();

    }

    @Override
    public BankEntity hwLoadCrashBankCard(String transAmt) {
        return null;
    }

    @Override
    public void closeReadBankCard() {

    }


}