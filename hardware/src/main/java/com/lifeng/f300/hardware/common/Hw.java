package com.lifeng.f300.hardware.common;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;

import com.lifeng.f300.common.entites.BankEntity;
import com.lifeng.f300.common.entites.TransResponse;
import com.lifeng.f300.common.exception.HwException;
import com.lifeng.f300.common.utils.BuildModle;
import com.lifeng.f300.common.utils.LogUtils;
import com.lifeng.f300.common.utils.PosDataUtils;
import com.lifeng.f300.common.utils.WriteInfoToFile;
import com.lifeng.f300.config.ConnectURL;
import com.lifeng.f300.config.IntentActions;
import com.lifeng.f300.hardware.g100.G100;
import com.lifeng.f300.hardware.hi98.Hi98;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by happen on 2017/8/15.
 */

public class Hw extends HwException implements HwInterface{
    // HI98设备
    /*public static IcCard icHI98 = null;
    public static MagCard magHI98 = null;
    public static PiccCard piccHI98 = null;
    public static Ped pedHI98 = null;
    public static SpiPrinter printHI98 = null;
    public static Sys sys = null;
    public static EmvL2 emvsdk = null;*/
    protected static Context mApplicationContent;
    private static Hw mInstance;

    protected Hw() {
    }

    public static Hw getInstance() {
        if (mInstance == null) {
            synchronized (Hw.class) {
                if (mInstance == null) {
                    mInstance = new Hw();
                }
            }
        }

        return mInstance;
    }

    public static void initialize(Application app){
        mApplicationContent = app.getApplicationContext();
    }

    @Override
    public void initHwObject(){
        // HI98设备
        /*if (BuildModle.isHI98()) {
           *//* icHI98 = new IcCard();
            magHI98 = new MagCard();
            piccHI98 = new PiccCard();
            pedHI98 = new Ped();
            printHI98 = new SpiPrinter();
            sys = new Sys(this);
            emvProcHI98 = EmvProc.getInstance();
            cardReaderHI98 =  CardReader.getInstance();
            iccardResultHI98 = new  ICCardResult();
            emvsdk = EmvL2.getInstance();*//*
        }*/
        if(BuildModle.isG100()){
            G100.getInstance().initHwObject();
        }
    }
    public String testErr(int i){
        if(i==0){
            return "0 err";
        }else{
            return "other err";
        }
    }

    @Override
    public void loadMasterKey(final String responseResult,final TransResponse transResponse) throws HwException {
        /*if(BuildModle.isHI98()){
            Hi98.getInstance().loadMasterKey(responseResult,transResponse);
        }else{*/


        //}
        if(BuildModle.isG100()){
            G100.getInstance().loadMasterKey(responseResult,transResponse);
        }else{
            writeRegisterInSystem(responseResult);

        }
        return;
    }

    public void writeSignFile(String resultInfo,TransResponse transResponse)throws HwException {
        writeSignFileInSystem(resultInfo);

    }

    @Override
    public void readBankCardNum() {

    }

    @Override
    public BankEntity readBankCardInfo() {

        if(BuildModle.isG100()){
            return G100.getInstance().readBankCardInfo();
        }

        return new BankEntity();

    }

    @Override
    public String getDeviceSn() {

        if(BuildModle.isHI98()){
            return Hi98.getInstance().getDeviceSn();
        }else{
            return Build.SERIAL;
        }
        //return "";
    }

    @Override
    public void loadWorkKey(final TransResponse transResponse) {
        if(BuildModle.isG100()){
            G100.getInstance().loadWorkKey(transResponse);
        }else{
            ;
        }
    }

    @Override
    public BankEntity hwLoadCrashBankCard(final String transAmt){
        return new BankEntity();
    }

    @Override
    public void closeReadBankCard(){};
    /**
     * 写入初始化成功的文件
     */
    protected void  writeRegisterInSystem(String responseResult) throws HwException {
        /*try {
            throw  new HwException("初始化信息保存失败21","初始化信息保存失败21");
        } catch (HwException e) {
            e.printStackTrace();
        }*/
        WriteInfoToFile writeInfoToFile = new WriteInfoToFile();
        writeInfoToFile.setWriteOverCallBack(new WriteInfoToFile.WriteOverListener() {
            @Override
            public void  writeOverCallBack(boolean writeOK) {
                if (writeOK) {
                    LogUtils.d("wang","delete file:"+ Environment.getExternalStorageDirectory().getPath()+"/lifeng/"+PosDataUtils.POS_DATA_NAME);
                    PosDataUtils.delFile(Environment.getExternalStorageDirectory().getPath()+"/lifeng/"+PosDataUtils.POS_DATA_NAME); //删除流水
                    //删除原来的数据:
                    /*TransDAO dao = new TransDAO(LoginActivity.this);
                    dao.deleteTransData();
                    PosDataUtils.delFile("/"+PosDataUtils.POS_DATA_NAME); //删除流水
                    PosDataUtils.delFile("/"+PosDataUtils.POS_DATA_NAME_BEFORE);//删除异常流水

                    Intent intentBroard = new Intent(SignInActivity.BROARD_FINISHSIGN);
                    sendBroadcast(intentBroard);*/

                    //intoSystemLifeng();
                    //hwToModel.hwToModelCallBack();
                    LogUtils.d("wang","writeOverCallBack  true");
                   // try {
                      //  throw  new HwException("初始化信息保存失败1","初始化信息保存失败1");
                    //} catch (HwException e) {
                      //  e.printStackTrace();
                    //}

                    //testThrowErr("true");

                   // handler.sendEmptyMessage(1);
                   // JUtils.Toast("初始化信息保存失败2222");

                    Intent intent = new Intent(IntentActions.START_SIGNINACTIVITY);
                    mApplicationContent.sendBroadcast(intent);//传递过去
                }else{
                   // JUtils.Toast("初始化信息保存失败");
                  //  handler.sendEmptyMessage(1);
                  //  testThrowErr("false");
                    //try {
                      //  throw  new HwException("初始化信息保存失败","初始化信息保存失败");
                   // } catch (HwException e) {
                     //   e.printStackTrace();
                   // }

                }
            }
        });
        /*try {
            throw  new HwException("1","22");
        } catch (HwException e) {
            e.printStackTrace();
        }*/
        writeInfoToFile.WriteSomeThingInfoToFile(ConnectURL.APP_PATH , ConnectURL.FILE_REGISTER, responseResult,true);
        //testThrowErr("");

    }




    //写sign.txt文件
    private void  writeSignFileInSystem(final String responseResult) throws HwException {
        WriteInfoToFile writeInfoToFile = new WriteInfoToFile();
        writeInfoToFile.setWriteOverCallBack(new WriteInfoToFile.WriteOverListener() {
            @Override
            public void  writeOverCallBack(boolean writeOK) {
                if (writeOK) {
                    LogUtils.d("wang","writeOverCallBack  true");
                }else{
                    LogUtils.d("wang","writeOverCallBack  false");
                }
            }
        });
        writeInfoToFile.WriteSomeThingInfoToFile(mApplicationContent.getFilesDir().getAbsolutePath()+ConnectURL.PATH_SPECIAL, ConnectURL.FIlE_SIGN, responseResult,true);
    }
    private void testThrowErr(String aTrue) throws HwException {
        throw  new HwException(1,"testThrowErr");
    }


    /**
     * 保存基本信息之后进入系统中
     */
/*    private void intoSystemLifeng() {
        //registerData = MerchantRegisterDataManager.getInstance().getRegisterData();
        WriteInfoToFile writeInfoToFile = new WriteInfoToFile();
        writeInfoToFile.setWriteOverCallBack(new WriteOverListener() {
            @Override
            public void writeOverCallBack(boolean writeOK) {
                if (writeOK) {
                    Intent intent = new Intent(LoginActivity.this,SignInActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    //showErrorInfoDialog(getString(R.string.register_id_error));
                    JUtils.Toast("初始化信息保存失败");
                }
            }
        });
        if (null != registerData) {
            writeInfoToFile.WriteSomeThingInfoToFile(ConnectURL.PATH_SPECIAL_S300, ConnectURL.FILE_AGENTID, registerData.AGENT_ID,false);
        }else{
            showErrorInfoDialog(getString(R.string.register_null));
        }
    }*/


     //接口
     //将hw的信息传递到model
 /*   public HwToModel hwToModel;

    public void sethwToModleCallBack(HwToModel hwToModel){
        this.hwToModel=hwToModel;
    }

    public interface HwToModel{
        public void hwToModelCallBack();
    }*/
}
