package com.lifeng.f300.znpos2.module.user;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jude.utils.JUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lifeng.beam.bijection.RequiresPresenter;
import com.lifeng.beam.expansion.BeamBaseActivity;
import com.lifeng.f300.common.contract.OnLoginDialogInterface;
import com.lifeng.f300.common.entites.SettlementRequest;
import com.lifeng.f300.common.entites.SingleFlowEntity;
import com.lifeng.f300.common.entites.TransResponse;
import com.lifeng.f300.common.utils.Constants;
import com.lifeng.f300.common.utils.DateUtil;
import com.lifeng.f300.common.utils.DeviceUtils;
import com.lifeng.f300.common.utils.LogUtils;
import com.lifeng.f300.common.utils.MyAsynaTask;
import com.lifeng.f300.common.utils.OnCusDialogInterface;
import com.lifeng.f300.common.utils.PosDataUtils;
import com.lifeng.f300.common.utils.StringUtil;
import com.lifeng.f300.common.utils.ToastMakeUtils;
import com.lifeng.f300.common.widget.AllDialog;
import com.lifeng.f300.common.widget.LoadDialog;
import com.lifeng.f300.config.ConnectURL;
import com.lifeng.f300.config.SharePreferenceKey;
import com.lifeng.f300.znpos2.R;
import com.lifeng.f300.znpos2.demo.ReadCardActivity;
import com.lifeng.f300.znpos2.utils.MerchantRegisterDataManager;
import com.lifeng.f300.znpos2.utils.MerchantSignDataManager;
import com.lifeng.f300.znpos2.utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.lifeng.f300.config.ConnectURL.ENVIRONMENT;

/**
 * 登录
 * Created by happen on 2017/8/21.
 */
@RequiresPresenter(SignInPresenter.class)
public class SignInActivity extends BeamBaseActivity<SignInPresenter> {
    @InjectView(R.id.iv_logo)
    ImageView ivLogo;
    @InjectView(R.id.et1_signin)
    EditText et1Signin;
    @InjectView(R.id.iv1_signin)
    ImageView iv1Signin;
    @InjectView(R.id.et2_signin)
    EditText et2Signin;
    @InjectView(R.id.iv2_signin)
    ImageView iv2Signin;
    @InjectView(R.id.iv1_btn_signin)
    ImageView iv1BtnSignin;
    @InjectView(R.id.iv2_btn_signin)
    ImageView iv2BtnSignin;

    private static final int INSTALL_APP = 999;//安装app的请求

    private final static int MSG_SIGNIN_SERVER = 10;  //服务器签到
    private final static int MSG_WRITE_SIGN_FILE = 2;//写sign.txt
    private final static int MSG_SHOW_UPDATE_APP_DIALOG = 3;//显示更新app对话框
    private final static int MSG_LOAD_WORK_KEY = 4;// 导入工作秘钥
    private final static int MSG_START_DOWNLOAD_APP = 5;//开始下载app
    private final static int MSG_GOTO_LIFENG_SYSTEM = 6;//进入MainActivity.java
    private static final int INIT_TRANS_DELAY = 600;//本地交易流水查询
    private static final int SELECT_TRANS_INIT = 299;  //查询交易流水的耗时操作
    private static final int MSG_SETTLEMENT = 7;        //结算
    private static final int MSG_GOTO_READBANKINFO = 8;        //结算

    public static SignInActivity instance;
    private TransResponse classTransResponse;


    private MerchantRegisterDataManager.RegisterData registerData;
    private MyAsynaTask mTask;
    private boolean flag1 = false;
    private boolean flag2 = false;
    private String operatorNumber;
    private String operatorPwd;
    private String signName;
    private String signPwd;

    private LoadDialog loadDialog;
    private MerchantSignDataManager.SignData signData;

    public HttpUtils http = new HttpUtils(ConnectURL.TIMEOUT);

    //一个批次下的所有流水
    private ArrayList<TransResponse> mFlowTransResponseList = new ArrayList<TransResponse>();

    private int day = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        instance = this;
    }

    private void initView() {
        setContentView(R.layout.sign_in_layout);
        ButterKnife.inject(this);

        iv1Signin.setOnClickListener(v -> clearOperator());
        iv2Signin.setOnClickListener(v -> clearPassword());

        et1Signin.addTextChangedListener(watcher);
        et2Signin.addTextChangedListener(watcher);
    }

    private void initData() {
        registerData = MerchantRegisterDataManager.getInstance().getRegisterData();
        setLogoImg();
        getOpeatorAndPwd();
       // DbManager.initialize(getApplication());
        //结算查询
        //isSettlement();

    }

    public void isSettlement() {
        String startDate = JUtils.getSharedPreference().getString("saveTransTime", "");// SharedPreferenceUtil.getStringData(this, "saveTransTime");
        JUtils.Log("startDate "+startDate);//+" signData.RESPONSE_TIME: "+signData.RESPONSE_TIME );
        if (StringUtil.isNotEmpty(startDate) && StringUtil.isNotEmpty(MerchantSignDataManager.getInstance().getSignData().RESPONSE_TIME)) {
            startDate = DateUtil.formatterDate(DateUtil.getDateByDateFormat(startDate), "yyyyMMdd");
            String lastDate = DateUtil.formatterDate(MerchantSignDataManager.getInstance().getSignData().RESPONSE_TIME, Constants.DATE_FORMAT1);
            lastDate = DateUtil.formatterDate(DateUtil.getDateByDateFormat(lastDate), "yyyyMMdd");
            day = DateUtil.getDateDays(lastDate, startDate);
            LogUtils.d("wang", "day:" + day);

            if (day >= 7 || day > 3) {
                Utils.showDialog(SignInActivity.this, getString(R.string.prompt),
                        getString(R.string.settlement_start) + day + getString(R.string.settlement_end),
                        getString(R.string.to_settle_accounts), getString(R.string.cancel),
                        new OnCusDialogInterface() {
                            @Override
                            public void onConfirmClick() {
                                Message obtain = Message.obtain();
                                obtain.what = SELECT_TRANS_INIT;
                                handler.sendMessageDelayed(obtain, INIT_TRANS_DELAY);
                            }

                            @Override
                            public void onCancelClick() {
                                sendMsgGotoLifengSystem();
                            }
                        });
            }else{
                sendMsgGotoLifengSystem();
            }
        }else{
            sendMsgGotoLifengSystem();
        }
    }

    private void getOpeatorAndPwd() {
        operatorNumber = JUtils.getSharedPreference().getString(SharePreferenceKey.OPETATOR_NUMBER, "");
        operatorPwd = JUtils.getSharedPreference().getString(SharePreferenceKey.OPETATOR_PWD, "");
        JUtils.Log("getOpeatorAndPwd " + operatorNumber + "  " + operatorPwd);
        et1Signin.setText(operatorNumber);
        et1Signin.setSelection(operatorNumber.length());
        et2Signin.setText(operatorPwd);
        et2Signin.setSelection(operatorPwd.length());

        if (!TextUtils.isEmpty(operatorNumber)) {
            iv1BtnSignin.setBackgroundResource(R.drawable.jh_img17);
            flag1 = true;
        } else {
            iv1BtnSignin.setBackgroundResource(R.drawable.jh_img18);
            flag1 = false;
        }

        if (!TextUtils.isEmpty(operatorPwd)) {
            iv2BtnSignin.setBackgroundResource(R.drawable.jh_img17);
            flag2 = true;
        } else {
            iv2BtnSignin.setBackgroundResource(R.drawable.jh_img18);
            flag1 = false;
        }
    }

    //将操作员和密码存储到SharePreference
    public void setOpeatorAndPwd() {
        JUtils.Log("call setOpeatorAndPwd");
        if (flag1) {
            LogUtils.d("wang", "set operator number " + et1Signin.getText().toString().trim());
            JUtils.getSharedPreference().edit().putString(SharePreferenceKey.OPETATOR_NUMBER, et1Signin.getText().toString().trim()).apply();
        } else {
            JUtils.getSharedPreference().edit().putString(SharePreferenceKey.OPETATOR_NUMBER, "").apply();
        }

        if (flag2) {
            LogUtils.d("wang", "set OPETATOR_PWD  " + et2Signin.getText().toString().trim());
            JUtils.getSharedPreference().edit().putString(SharePreferenceKey.OPETATOR_PWD, et2Signin.getText().toString().trim()).apply();
        } else {
            JUtils.getSharedPreference().edit().putString(SharePreferenceKey.OPETATOR_PWD, "").apply();
        }
    }

    public void toastWrongOperatorOrPwd() {
        JUtils.Toast(getString(R.string.wrong_operatorid_pwd));
    }

    public void startMainActivity() {
        JUtils.Log("go to MainActivity");
    }

    private void setLogoImg() {
        //JUtils.Log(registerData.LOGO);
        if (!TextUtils.isEmpty(registerData.LOGO)) {
            /*mTask = new MyAsynaTask(ivLogo,null,null,R.drawable.f300logo);
            mTask.execute(registerData.LOGO);*/
            Picasso.with(this).load(registerData.LOGO).into(ivLogo);
        } else {
            ivLogo.setImageResource(R.drawable.f300logo);
        }
    }

    public void signIn(String USERNAME) {

    }

    private void clearOperator() {
        et1Signin.setText("");
    }

    private void clearPassword() {
        et2Signin.setText("");
    }


    TextWatcher watcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (et1Signin != null && et1Signin.getText().length() > 0) {
                iv1Signin.setVisibility(View.VISIBLE);
                iv1Signin.setBackgroundResource(R.drawable.login_close);
            } else {
                iv1Signin.setVisibility(View.GONE);
            }
            if (et2Signin != null && et2Signin.getText().length() > 0) {
                iv2Signin.setVisibility(View.VISIBLE);
                iv2Signin.setBackgroundResource(R.drawable.login_close);
            } else {
                iv2Signin.setVisibility(View.GONE);
            }
        }
    };

    /**
     * 签到
     *
     * @param v
     */
    public void BtnSignIn(View v) {
        signName = et1Signin.getText().toString().trim();
        signPwd = et2Signin.getText().toString().trim();

        if (TextUtils.isEmpty(signName)) {
            ToastMakeUtils.showToast(this, getString(R.string.input_operatorId));
            return;
        }

        if (TextUtils.isEmpty(signPwd)) {
            ToastMakeUtils.showToast(this, getString(R.string.input_password));
            return;
        }
        //判断用户名和密码
        getPresenter().pCheckOperatorAndPwd(signName, signPwd);
    }


    /**
     * 记住操作员
     *
     * @param v
     */
    public void IVBtnSignin1(View v) {
        if (flag1) {
            iv1BtnSignin.setBackgroundResource(R.drawable.jh_img18);
            flag1 = false;
        } else {
            iv1BtnSignin.setBackgroundResource(R.drawable.jh_img17);
            flag1 = true;
        }
    }

    /**
     * 记住密码
     *
     * @param v
     */
    public void IVBtnSignin2(View v) {
        if (flag2) {
            iv2BtnSignin.setBackgroundResource(R.drawable.jh_img18);
            flag2 = false;
        } else {
            iv2BtnSignin.setBackgroundResource(R.drawable.jh_img17);
            flag2 = true;
        }
    }

    /**
     * 重新初始化
     *
     * @param v
     */
    public void resetSystemButton(View v) {
        showResetDialog();
    }

    public MerchantRegisterDataManager.RegisterData getRegisterData() {
        return MerchantRegisterDataManager.getInstance().getRegisterData();
    }


    //服务器签到
    public void vSignInServer(String username) {
        getPresenter().pSignIn(username,
                getRegisterData().MERCHANT_CODE,
                getRegisterData().MASTERKEY,
                getRegisterData().POSCODE);
    }
    /*public void sendMsgSigninServer(String username) {
        JUtils.Log("call sendMsgSigninServer");
        Message msg = Message.obtain();
        Bundle data = new Bundle();
        data.putSerializable("username", username);
        msg.setData(data);
        msg.what = MSG_SIGNIN_SERVER;
        handler.sendMessage(msg);
    }*/

    //写sign.txt
    public void sendMsgWriteSignFile(TransResponse transResponse) {
        SignInActivity.instance.classTransResponse = transResponse;
        Message msg = Message.obtain();
        msg.what = MSG_WRITE_SIGN_FILE;
        handler.sendMessage(msg);
    }

    //显示更新app对话框
    public void sendMsgShowUpdateAppDialog() {
        Message msg = Message.obtain();
        msg.what = MSG_SHOW_UPDATE_APP_DIALOG;
        handler.sendMessage(msg);
    }

    //导入工作秘钥
    public void sendMsgLoadWorkKey(final TransResponse transResponse) {
        Message msg = Message.obtain();
        msg.what = MSG_LOAD_WORK_KEY;
        Bundle data = new Bundle();
        data.putSerializable("transResponse", transResponse);
        msg.setData(data);
        handler.sendMessage(msg);


/*
        Message msg = Message.obtain();
        Bundle data = new Bundle();
        data.putSerializable("transResponse", transResponse);
        msg.setData(data);
        msg.what = MSG_LOADMASTERKEY;*/
    }

    //进入荔智付
    public void sendMsgGotoLifengSystem() {
        Message msg = Message.obtain();
        msg.what = MSG_GOTO_LIFENG_SYSTEM;
        handler.sendMessage(msg);
    }

    //进入读取银行卡信息的界面
    public void sendMsgGotoReadCardActivity(){

        Message msg = Message.obtain();
        msg.what = MSG_GOTO_READBANKINFO;
        handler.sendMessage(msg);


    }

    //开始下载APP
    public void sendMsgStartDownloadApp(final String downurl, final Boolean isMustUpdate) {
        Message msg = Message.obtain();
        msg.what = MSG_START_DOWNLOAD_APP;
        Bundle data = new Bundle();
        data.putString("downurl", downurl);
        data.putBoolean("isMustUpdate", isMustUpdate);
        msg.setData(data);
        handler.sendMessageDelayed(msg, 800);
    }

    /*private void comeIntoLifengSystem(){
        JUtils.Log("comeIntoLifengSystem MainActivity");
       *//* Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        SharedPreferenceUtil.putStringData(SignInActivity.this,"operatorId", signName);
        startActivity(intent);
        finish();*//*
    }*/

    //结算MSG
    public void sendMsgSettlement(){
        Message msg = Message.obtain();
        msg.what = MSG_SETTLEMENT;
        handler.sendMessage(msg);
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                /*case MSG_SIGNIN_SERVER:
                    Bundle data = msg.getData();
                    if (null != data) {
                        getPresenter().pSignIn((String) data.getSerializable("username"),
                                getRegisterData().MERCHANT_CODE,
                                getRegisterData().MASTERKEY,
                                getRegisterData().POSCODE);
                    }
                    break;*/
                case MSG_WRITE_SIGN_FILE:
                    getPresenter().pWriteSignFile(classTransResponse);
                    break;
                case MSG_SHOW_UPDATE_APP_DIALOG:
                    if ("1".equals(classTransResponse.UPDATESTATUS)) {
                        String downPath = classTransResponse.UPDATEADDR + ":" + classTransResponse.UPDATEPORT + "/" + classTransResponse.UPDATEPATH + "/" + classTransResponse.UPDATEFILE;
                        if (null != classTransResponse.FORCE_TO_UP) {//是否进行强制升级
                            showDownDialog(downPath, classTransResponse.FORCE_TO_UP);
                        } else {
                            showDownDialog(downPath, false);
                        }
                        JUtils.Log("app下载地址：" + downPath);
                    } else {
                        sendMsgLoadWorkKey(classTransResponse);
                    }
                    break;
                case MSG_START_DOWNLOAD_APP:
                    Bundle data2 = msg.getData();
                    if (data2 != null) {
                        download(data2.getString("downurl"), data2.getBoolean("isMustUpdate"));
                    }

                    break;
                case MSG_LOAD_WORK_KEY:
                    JUtils.Log("开始导入工作秘钥");
                    Bundle data = msg.getData();
                    getPresenter().pLoadWorkKey((TransResponse) (data.getSerializable("transResponse")));
                    break;
                case MSG_GOTO_LIFENG_SYSTEM:
                    JUtils.Log("comeIntoLifengSystem MainActivity");
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    //SharedPreferenceUtil.putStringData(SignInActivity.this,"operatorId", signName);
                    JUtils.getSharedPreference().edit().putString("operatorId", signName).apply();
                    startActivity(intent);
                    finish();
                    break;
                case SELECT_TRANS_INIT:
                    //Intent intent1 = new Intent(instance, OperatorManagerActivity.class);
                    //startActivity(intent1);
                    closeStartDialog();
                    //开始结算:先从数据库查询该批次下的交易流水，然后在发送结算请求
                    queryFlowInfoByBatchNo();
                    //clearViewData();
                    break;
                case MSG_SETTLEMENT:
                    if(mFlowTransResponseList !=null && mFlowTransResponseList.size()>0){
                        settlement();
                    }else{
                        clearViewData();
                    }
                    break;
                case MSG_GOTO_READBANKINFO:
                    Intent intent2 = new Intent(SignInActivity.this, ReadCardActivity.class);
                    startActivity(intent2);
                    //finish();

                default:
                    break;
            }
        }
    };

    //开始结算
    private void settlement() {

        PackageInfo packInfo = null;
        try {
            packInfo = instance.getPackageManager().getPackageInfo(instance.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        SettlementRequest request = new SettlementRequest();
        request.DEVICEID = DeviceUtils.getDeviceSn(instance);
        request.MERCHANTCODE=  MerchantSignDataManager.getInstance().getSignData().MERCHANTCODE;
        request.POSCODE = MerchantRegisterDataManager.getInstance().getRegisterData().POSCODE;
        request.BATCHNUM = MerchantSignDataManager.getInstance().getSignData().BATCHNUM;
        request.VISION = packInfo.versionName;


        int consume_count_trade = 0;
        StringBuffer consume_bank_amount = new StringBuffer();  //消费总金额减去撤销总金额
        int cancel_count_trade=0;
        StringBuffer cancel_bank_amount = new StringBuffer();   //撤销总金额
        StringBuffer invoice_id_list = new StringBuffer();

        HashMap cancelTrans = new HashMap();   //撤销的流水：AUTH_CODE，BANK_AMOUNT
        HashMap cousumeTrans = new HashMap();   //成功流水：AUTH_CODE，BANK_AMOUNT

        BigDecimal consumeMoney = new BigDecimal("0");
        BigDecimal totalConsumeMoney = new BigDecimal("0");
        BigDecimal cancelMoney = new BigDecimal("0");
        BigDecimal totalCancelMoney = new BigDecimal("0");

        for(int i=0;i<mFlowTransResponseList.size();i++){

            //凭证号列表
            invoice_id_list.append(mFlowTransResponseList.get(i).INVOICE_ID);
            invoice_id_list.append(",");
            //撤销总笔数
            if(mFlowTransResponseList.get(i).INVOICE_TYPE != null && mFlowTransResponseList.get(i).INVOICE_TYPE.equals("20")){
                cancel_count_trade++;
                if(StringUtil.isNotEmpty(mFlowTransResponseList.get(i).AUTH_CODE) &&StringUtil.isNotEmpty(mFlowTransResponseList.get(i).BANK_AMOUNT) ){
                    cancelTrans.put(mFlowTransResponseList.get(i).AUTH_CODE,mFlowTransResponseList.get(i).BANK_AMOUNT);
                }

            }
            //计算撤销的总金额
            if(StringUtil.isNotEmpty(mFlowTransResponseList.get(i).PAID) && StringUtil.isDigit(mFlowTransResponseList.get(i).PAID)){
                BigDecimal decimal = new BigDecimal(mFlowTransResponseList.get(i).PAID);
                totalCancelMoney = totalCancelMoney.add(decimal);
            }

            //消费的总笔数
            if(mFlowTransResponseList.get(i).INVOICE_TYPE != null && mFlowTransResponseList.get(i).INVOICE_TYPE.equals("3")){
                if(StringUtil.isNotEmpty(mFlowTransResponseList.get(i).AUTH_CODE) &&StringUtil.isNotEmpty(mFlowTransResponseList.get(i).BANK_AMOUNT) ){
                    cousumeTrans.put(mFlowTransResponseList.get(i).AUTH_CODE,mFlowTransResponseList.get(i).BANK_AMOUNT);
                }
            }

            //计算消费的总金额
            if(StringUtil.isNotEmpty(mFlowTransResponseList.get(i).PAID) && StringUtil.isDigit(mFlowTransResponseList.get(i).PAID)){
                BigDecimal decimal = new BigDecimal(mFlowTransResponseList.get(i).PAID);
                totalConsumeMoney = totalConsumeMoney.add(decimal);
            }
        }

        Set consumeSet = cousumeTrans.entrySet();
        int sameTrans=0;//撤销和消费中AUTH_CODE相同的笔数
        for(Iterator consumeIter = consumeSet.iterator(); consumeIter.hasNext();)
        {
            Set cancelSet = cancelTrans.entrySet();
            for(Iterator cancelIter = consumeSet.iterator(); cancelIter.hasNext();){
                if(((Map.Entry)consumeIter.next()).getKey().equals(
                        ((Map.Entry)cancelIter.next()).getKey()  )
                        ){
                    sameTrans++;
                }
            }
        }

        //消费总金额
        if(totalConsumeMoney.add(totalCancelMoney).compareTo(BigDecimal.ZERO) == -1){  //消费总金额-撤销总金额小于0 ，那么报错
            JUtils.Log("totalConsumeMoney:"+totalConsumeMoney+" totalCancelMoney:"+totalCancelMoney);
            JUtils.ToastLong(getString(R.string.jecc1));
            return ;
        }else{
            request.CONSUME_BANK_AMOUNT = StringUtil.formatterAmount( totalConsumeMoney.add(totalCancelMoney).toString());
        }

        //撤销总金额
        request.CANCEL_BANK_AMOUNT =  StringUtil.formatterAmount(totalConsumeMoney.toString());

        //消费的笔数（不包括撤销的笔数）
        if((cousumeTrans.size()-sameTrans)>=0){
            request.CANCEL_COUNT_TRADE = String.valueOf(cousumeTrans.size()-sameTrans);
        }else{
            JUtils.ToastLong(getString(R.string.jecc1));
            return ;
        }

        //撤销的笔数
        if(cancel_count_trade>=0){
            request.CANCEL_COUNT_TRADE =  String.valueOf(cancel_count_trade);
        }else{
            request.CANCEL_COUNT_TRADE="";
        }
        //凭证号列表
        if(  request.INVOICE_ID_LIST.length()>0){
            request.INVOICE_ID_LIST = invoice_id_list.substring(0,invoice_id_list.length()-1);
        }else{
            request.INVOICE_ID_LIST="";
        }
        getPresenter().pSettlement(request);
    }



    /**
     * 获取SettlementRequest 的，CONSUME_COUNT_TRADE ，CONSUME_BANK_AMOUNT，CANCEL_COUNT_TRADE，CANCEL_BANK_AMOUNT，INVOICE_ID_LIST
     */
    private void setSettlementInfo(SettlementRequest request) {
        StringBuffer consume_count_trade = new StringBuffer();
        StringBuffer consume_bank_amount = new StringBuffer();
        StringBuffer cancel_count_trade = new StringBuffer();
        StringBuffer cancel_bank_amount = new StringBuffer();
        StringBuffer invoice_id_list = new StringBuffer();

        for(TransResponse transResponse : mFlowTransResponseList){
            invoice_id_list.append(transResponse.INVOICE_ID);
            invoice_id_list.append(",");


        }
        request.INVOICE_ID_LIST = invoice_id_list.substring(0,invoice_id_list.length()-1);

    }

    /**
     * 查询批次下的流水信息
     */
    private void queryFlowInfoByBatchNo() {
        signData = MerchantSignDataManager.getInstance().getSignData();
        if(signData == null || StringUtil.isEmpty(signData.MERCHANTNAME)){
            JUtils.ToastLong(getString(R.string.cxqd));
            return;
        }
        //registerData = MerchantRegisterDataManager.getInstance().getRegisterData();
        JUtils.Log("已经交易的最大流水号：" + PosDataUtils.getYJYMAXPosSerialNumber());
        mFlowTransResponseList.clear();
        //getPresenter().pSettlement();
        getPresenter().pQueryFlowInfoByBatchNoSignInActivity(getListSingleFlowEntity());
    }

    /**
     * 一个批次下所有待查询的流水实例列表
     * @return
     */
    private List<SingleFlowEntity> getListSingleFlowEntity() {
        ArrayList<SingleFlowEntity> singleFlowEntity = new ArrayList();
        List<String> mListSerialNum = PosDataUtils.getPosSerialNumberList();
        String operatorId = JUtils.getSharedPreference().getString("operatorId", "");
        MerchantRegisterDataManager.RegisterData merchantData = MerchantRegisterDataManager.getInstance().getRegisterData();
        MerchantSignDataManager.SignData signData = MerchantSignDataManager.getInstance().getSignData();
        JUtils.Log("size:" + mListSerialNum.size());
        for (int i = 0; i < mListSerialNum.size(); i++) {
            /*singleFlowEntity.add(new SingleFlowEntity(MerchantSignDataManager.getInstance().getSignData().BATCHNUM,mListSerialNum.get(i),operatorId,
                    merchantData.MERCHANT_CODE,merchantData.POSCODE,signData.SECONDARYKEY));*/
            singleFlowEntity.add(new SingleFlowEntity("000159", mListSerialNum.get(i), operatorId,
                    merchantData.MERCHANT_CODE, merchantData.POSCODE, signData.SECONDARYKEY));
            singleFlowEntity.get(i).toString();
        }
        return singleFlowEntity;
    }



    public  void clearViewData(){
        PosDataUtils.delFile(ENVIRONMENT+ConnectURL.PATH_SPECIAL+ConnectURL.FILE_REGISTER);
        Intent intent = new Intent(SignInActivity.this,LoginActivity.class);
        intent.putExtra("isReset", true);
        startActivity(intent);
        finish();
    }


    /**
     * 弹出是否重新初始化的Dialog
     */
    private void showResetDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setCancelable(false);
        ab.setTitle(getString(R.string.risk_warning));
        ab.setMessage(getString(R.string.initialization_propt));
        ab.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });

        ab.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        ab.setPositiveButton(getString(R.string.continues), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                closeStartDialog();
                loadDialog = new LoadDialog(SignInActivity.this, R.style.CustomProgressDialog, getString(R.string.waiting));
                loadDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message obtain = Message.obtain();
                        obtain.what = SELECT_TRANS_INIT;
                        handler.sendMessageDelayed(obtain, INIT_TRANS_DELAY);

                    }
                }).start();
            }
        });
        ab.show();
    }

    /**
     * 弹出是否更新软件的dialog
     *
     * @param downurl
     * @param isMustUpdate
     */
    private void showDownDialog(final String downurl, final boolean isMustUpdate) {
        JUtils.Log("isMustUpate:" + isMustUpdate);
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle(getString(R.string.update_title));
        ab.setMessage(getString(R.string.update_info));
        ab.setCancelable(!isMustUpdate);
        ab.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //loadSystem();
                //sendMsgLoadWorkKey();
            }
        });
        if (!isMustUpdate) {//不是强制升级就显示取消按钮
            ab.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //loadSystem();
                    //sendMsgLoadWorkKey();
                }
            });
        }
        ab.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToastMakeUtils.showToast(SignInActivity.this, getString(R.string.waiting));
                /**
                 * 先判断如果文件存在就先干掉，再重新进行下载就行了
                 */
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        deleteAppInDownFile();
                        sendMsgStartDownloadApp(downurl, isMustUpdate);
                    }
                }) {
                }.start();
            }
        });
        ab.show();
    }

    private void closeStartDialog() {
        if (loadDialog != null) {
            loadDialog.dismiss();
            loadDialog = null;
        }
    }

    /**
     * 删除下载目录中的所有app文件
     */
    private void deleteAppInDownFile() {
        File targetFiles = Environment.getExternalStorageDirectory();
        int number = 0;
        if (targetFiles.exists() && targetFiles.isDirectory()) {
            File[] listFiles = targetFiles.listFiles();
            for (File file : listFiles) {
                if (file.isFile() && file.getName().contains(".apk")) {
                    number++;
                    boolean isDeleteOk = file.delete();
                    JUtils.Log("删除安装包：" + number + ":" + isDeleteOk);
                }
            }
        }
    }

    /**
     * 下载成功就进行安装
     *
     * @param path
     */
    public void download(final String path, final Boolean isMustUpdate) {
        final String target = Environment.getExternalStorageDirectory().getPath() + "/" + classTransResponse.UPDATEFILE;
        JUtils.Log(target);
        http.download(path, target, true, new RequestCallBack<File>() {
            private Dialog dialog;
            private ProgressBar progressBar;
            private TextView showCurrent;

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                dialog.dismiss();
                //ToastMakeUtils.showToast(SignInActivity.this, "下载失败");
                JUtils.Toast(getString(R.string.download_fail));
                if (!isMustUpdate) {
                    //loadSystem();
                    //sendMsgLoadWorkKey();
                }
            }

            @Override
            public void onSuccess(ResponseInfo<File> arg0) {
                dialog.dismiss();
                JUtils.Toast(getString(R.string.download_success));
                //ToastMakeUtils.showToast(SignInActivity.this, );
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
//				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.fromFile(new File(target)), "application/vnd.android.package-archive");
                startActivityForResult(intent, INSTALL_APP);
//		        android.os.Process.killProcess(android.os.Process.myPid());
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                LogUtils.d("wang", "总量" + total + "当前" + current + "是否在下载中" + isUploading);
                progressBar.setMax(new Long(total).intValue());
                progressBar.setProgress(new Long(current).intValue());
                showCurrent.setText(getString(R.string.downloading) + (int) ((new Long(current).intValue() / (new Long(total).intValue() / 1.0)) * 100) + "%" + "…");
            }

            @Override
            public void onStart() {
                dialog = Utils.setDialog(SignInActivity.this, R.layout.progress_dialog, 0.96, 0);
                progressBar = (ProgressBar) dialog.findViewById(R.id.progress_bar);
                showCurrent = (TextView) dialog.findViewById(R.id.show_current);
                dialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case INSTALL_APP:
                if (resultCode == Activity.RESULT_OK) {
                    LogUtils.d("wang", "安装成功");
                } else {
                    /*if (isMustUpdate) {//如果安装过程中强制更新取消，就结束掉程序
                        Intent intent = new Intent(BaseReadCardActivity.BROARD_FILTER1);
                        sendBroadcast(intent);
                        finish();
                    }else{*/
                    //comeIntoLifen   gSystem();
                    //comeIntoLifengSystem();
                    sendMsgGotoLifengSystem();
                    //}
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //写工作秘钥
    private void loadSystem() {
        ///JUtils.Log();
    }


    @Override
    public void onBackPressed() {

        AllDialog.showDialog(this, R.style.CustomProgressDialog, getString(R.string.remind), getString(R.string.tcxt), 0, R.layout.network_error_vip, new OnLoginDialogInterface() {
            @Override
            public void onConfirmClick(EditText etUserName, EditText etOldPassword, EditText etPassword, EditText etPasswordSure) {
            }

            @Override
            public void onCancelClick() {
                AllDialog.closeDialog();
            }

            @Override
            public void onSureClick() {
                AllDialog.closeDialog();
                finish();
            }
        });
    }

    public void addFlowTransResponseList(TransResponse s) {
        mFlowTransResponseList.add(s);
        JUtils.Log("list size:" + mFlowTransResponseList.size());

    }



}
