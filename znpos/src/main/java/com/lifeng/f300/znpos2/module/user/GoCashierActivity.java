package com.lifeng.f300.znpos2.module.user;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jude.utils.JUtils;
import com.lifeng.beam.bijection.RequiresPresenter;
import com.lifeng.beam.expansion.BeamBaseActivity;
import com.lifeng.f300.common.entites.BankEntity;
import com.lifeng.f300.common.entites.TransRequest;
import com.lifeng.f300.common.entites.TransResponse;
import com.lifeng.f300.common.utils.CharacterOperationUtils;
import com.lifeng.f300.common.utils.DeviceUtils;
import com.lifeng.f300.common.utils.JsonValidator;
import com.lifeng.f300.common.utils.LogUtils;
import com.lifeng.f300.common.utils.PayType;
import com.lifeng.f300.common.utils.PosDataUtils;
import com.lifeng.f300.common.utils.StringUtil;
import com.lifeng.f300.config.PayScanType;
import com.lifeng.f300.znpos2.R;
import com.lifeng.f300.znpos2.utils.MerchantRegisterDataManager;
import com.lifeng.f300.znpos2.utils.MerchantSignDataManager;
import com.lifeng.f300.znpos2.utils.Utils;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by happen on 2017/8/30.
 */
@RequiresPresenter(GoCashierPresenter.class)
public class GoCashierActivity extends BeamBaseActivity<GoCashierPresenter> {

    //go_cashier1_layout
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.tv_rmb)
    TextView tvRmb;
    @InjectView(R.id.show_delete_icon)
    ImageView showDeleteIcon;
    @InjectView(R.id.re_clear)
    RelativeLayout reClear;
    @InjectView(R.id.show_ll1)
    LinearLayout showLl1;
    @InjectView(R.id.relayout_one)
    RelativeLayout relayoutOne;
    @InjectView(R.id.re_four)
    RelativeLayout reFour;
    @InjectView(R.id.tv_seven)
    TextView tvSeven;
    @InjectView(R.id.re_seven)
    RelativeLayout reSeven;
    @InjectView(R.id.tv_two_zero)
    TextView tvTwoZero;
    @InjectView(R.id.re_two)
    RelativeLayout reTwo;
    @InjectView(R.id.tv_five)
    TextView tvFive;
    @InjectView(R.id.re_five)
    RelativeLayout reFive;
    @InjectView(R.id.tv_eight)
    TextView tvEight;
    @InjectView(R.id.re_eight)
    RelativeLayout reEight;
    @InjectView(R.id.re_one_zero)
    RelativeLayout reOneZero;
    @InjectView(R.id.tv_three)
    TextView tvThree;
    @InjectView(R.id.re_three)
    RelativeLayout reThree;
    @InjectView(R.id.tv_sex)
    TextView tvSex;
    @InjectView(R.id.re_sex)
    RelativeLayout reSex;
    @InjectView(R.id.re_nine)
    RelativeLayout reNine;
    @InjectView(R.id.iv_item_aliay)
    ImageView ivItemAliay;
    @InjectView(R.id.tv_aliay)
    TextView tvAliay;
    @InjectView(R.id.ll_aliay)
    LinearLayout llAliay;
    @InjectView(R.id.iv_item_cash)
    ImageView ivItemCash;
    @InjectView(R.id.tv_cash)
    TextView tvCash;
    @InjectView(R.id.ll_cash)
    LinearLayout llCash;
    @InjectView(R.id.line_cash)
    TextView lineCash;
    @InjectView(R.id.iv_item_coupon)
    ImageView ivItemCoupon;
    @InjectView(R.id.btn_coupon_pay_size)
    RelativeLayout btnCouponPaySize;
    @InjectView(R.id.btn_coupon_pay)
    LinearLayout btnCouponPay;
    @InjectView(R.id.iv_item_bank)
    ImageView ivItemBank;
    @InjectView(R.id.tv_bank)
    TextView tvBank;
    @InjectView(R.id.ll_bank)
    LinearLayout llBank;
    @InjectView(R.id.line_code)
    TextView lineCode;
    @InjectView(R.id.iv_item_wchant)
    ImageView ivItemWchant;
    @InjectView(R.id.tv_wchant)
    TextView tvWchant;
    @InjectView(R.id.ll_wchant)
    LinearLayout llWchant;
    @InjectView(R.id.linear_choose_type)
    LinearLayout linearChooseType;

    private TransRequest transRequest = null;
    private String rmb = "0";

    //cash_dialog
    TextView tvCashDialogRmb;
    EditText etCashDailogCash;
    Button btnCashDailogSure;
    RelativeLayout rlCashDailogCancel;
    private Dialog cashDialog;

    //银行卡
    private String transAmt; // 银行卡最终的交易金额
    private Dialog dialogError;
    TextView tvErrorDialogTitle;
    Button btnErrorDialogCancel;
    Button btnErrorDialogSure;
    View vErrorDialogCenterLine;

    private MerchantSignDataManager.SignData signData;


    private String payType = "-1";


    final static int MSG_BANK_CONSUME = 1;  //银行卡消费


    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

     Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
        initEvent();
        /*setContentView(R.layout.login_layout);
        ButterKnife.inject(this);
        btnActivate.setOnClickListener(v -> checkInput());
        ivRead.setOnClickListener(v -> IVRead());
        sjzx.setOnClickListener(v->sjzx());
        initData();*/
    }

    private void initView() {

        setContentView(R.layout.go_cashier1_layout);
        ButterKnife.inject(this);

        signData = MerchantSignDataManager.getInstance().getSignData();

        //初始化tvRmb为0-->收银（现金，银行卡，微信，支付宝）为disable
        tvRmb.setText("0");
        // 监听金额文本变化（是否显示回退键）
        tvRmb.addTextChangedListener(watcherRmb);


    }

    private void initData() {
      switchBtn(false);
        mContext = this;
     //   tvRmb.setText("0");
    }

    private void initEvent() {
    }


    /**
     * 是否点亮图标
     */
    private void switchBtn(boolean isChangeBtn) {
        if (isChangeBtn) {
            JUtils.Log("enbale 图标");
            //findViewById(R.id.ll_bank).setEnabled(isChangeBtn);
            findViewById(R.id.btn_coupon_pay).setEnabled(isChangeBtn);
            findViewById(R.id.ll_cash).setEnabled(isChangeBtn);
            //ivItemBank.setImageResource(R.drawable.card_pay_btn);
            ivItemCoupon.setImageResource(R.drawable.coupon_pay_icon);
            ivItemCash.setImageResource(R.drawable.cash_pay_btn);


            //0->支付宝不可用，否则可用
            if ("0".equals(signData.ZHIFUBAO_ZS) && "0".equals(signData.ZHIFUBAO_BS)) {
                ivItemAliay.setImageResource(R.drawable.aliay_pay_btngray);
                findViewById(R.id.ll_aliay).setEnabled(false);

            } else {
                ivItemAliay.setImageResource(R.drawable.aliay_pay_btn);
                findViewById(R.id.ll_aliay).setEnabled(true);
            }

            //0->微信不可用，否则可用
            if ("0".equals(signData.WEIXIN_ZS) && "0".equals(signData.WEIXIN_BS)) {
                ivItemWchant.setImageResource(R.drawable.wx_pay_btngray);
                findViewById(R.id.ll_wchant).setEnabled(false);
                findViewById(R.id.ll_wchant).setEnabled(isChangeBtn);
            } else {
                ivItemWchant.setImageResource(R.drawable.wx_pay_btn);
                findViewById(R.id.ll_wchant).setEnabled(isChangeBtn);
            }

            LogUtils.d("wang"," signDate"+ "signData.BM."+ signData.BM  +" "  );  //del
            if(signData.BM!=null && signData.BM.equals("1")){  // BM控制YR银行卡收单通道是否设置了。如果设置了，则按钮可以点击；否则disable
                findViewById(R.id.ll_bank).setEnabled(isChangeBtn);
                ivItemBank.setImageResource(R.drawable.card_pay_btn);
            }else{
                findViewById(R.id.ll_bank).setEnabled(false);
                ivItemBank.setImageResource(R.drawable.card_pay_btngray);
            }

        } else {
            JUtils.Log("disable 图标");
            findViewById(R.id.ll_bank).setEnabled(isChangeBtn);
            findViewById(R.id.ll_aliay).setEnabled(isChangeBtn);
            findViewById(R.id.ll_wchant).setEnabled(isChangeBtn);
            findViewById(R.id.btn_coupon_pay).setEnabled(isChangeBtn);
            findViewById(R.id.ll_cash).setEnabled(isChangeBtn);
            ivItemBank.setImageResource(R.drawable.card_pay_btngray);
            ivItemAliay.setImageResource(R.drawable.aliay_pay_btngray);
            ivItemWchant.setImageResource(R.drawable.wx_pay_btngray);
            ivItemCoupon.setImageResource(R.drawable.coupon_pay_icongray);
            ivItemCash.setImageResource(R.drawable.cash_pay_btngray);
        }
    }

    /**
     * 收款金额控件监听
     */
    TextWatcher watcherRmb = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String rmb = s.toString();
            JUtils.Log("当前的交易金额：" + rmb);
            if ("0".equals(rmb)) {
                findViewById(R.id.re_clear).setVisibility(View.GONE);
                switchBtn(false);
            } else {
                showDeleteIcon.setImageResource(R.drawable.left_delete);
                findViewById(R.id.re_clear).setVisibility(View.VISIBLE);
                switchBtn(true);
            }
        }
    };

    /***
     * 键盘的监听
     *
     * @param v
     */
    public void numClick(View v) {
        int code = Integer.parseInt(v.getTag().toString());// 获取点击的按钮数据
        switch (code) {
            case -2:
                //isCombinePay = false;
                rmb = Utils.deleteNumber(rmb);
                tvRmb.setText(rmb.contains(".") ? Utils.getSpannableString(rmb) : rmb);
                break;
            case -1:
                rmb = Utils.addPoint(rmb);
                tvRmb.setText(rmb.contains(".") ? Utils.getSpannableString(rmb) : rmb);
                break;
            default:
                if ("00".equals(v.getTag().toString().trim())) {
                    rmb = Utils.calculator(rmb, tvRmb, "00");
                } else {
                    rmb = Utils.calculator(rmb, tvRmb, code + "");
                }

        }
    }


    /**
     * 现金支付
     *
     * @param v
     */
    public void cashPayClick(View v) {
        loadCrashTrans();
    }

    /**
     * 银行卡支付
     *
     * @param v
     */
    public void BankCardPayClick(View v) {
        loadBankTrans();
    }

    /**
     * 微信支付
     *
     * @param v
     */
    public void WeChatPayClick(View v) {
        startResultActivity(null);

        // loadWxTrans();
    }

    /**
     * 支付宝支付
     *
     * @param v
     */
    public void AliPayClick(View v) {
        Intent intent = new Intent(this, OpenCrashHandActivity.class);
        startActivity(intent);
        //loadZfbTrans();
    }

    private void loadZfbTrans() {
        if (checkData()) {
           /* int code = 0;
            LogUtils.d("wang", "passwordText：" + passwordText);
            if (passwordText != null) {
                code = Integer.parseInt(passwordText.getTag().toString());
                if (code == 3) {
                    showErrorInfoDialog(getString(R.string.blance_alipay_cantcombinePay));
                    return;
                }
            }
            if ("1".equals(signData.ZHIFUBAO_ZS)) {
                switchIntentSweep("3");
            } else {
                switchIntentSwept("6");
            }*/
            switchIntentScan(PayScanType.ALI_SCAN);
        }
    }

    //分为主扫和被扫
    private void loadWxTrans() {
        if (checkData()) {
            /*if ("1".equals(signData.WEIXIN_ZS)) {
                switchIntentSweep("2");
            } else {
                switchIntentSwept("5");
            }*/
            switchIntentScan(PayScanType.WECHAT_SCAN);
        }
    }

    /**
     * 跳到主扫界面
     *
     * @param bankType 2是微信，3是支付宝
     */
    private void switchIntentScan(String payType) {
        Intent intent = new Intent(GoCashierActivity.this, CaptureScanActivity.class);
        String transAmount = tvRmb.getText().toString();
        intent.putExtra("transAmount", transAmount);
        intent.putExtra("payType", payType);
        startActivity(intent);
       /*
        String transAmount = (isCombinePay) ? tvNeedPayValue.getText().toString() : tvRmb.getText().toString();
        intent.putExtra("transAmount", transAmount);
        intent.putExtra("result", queryMemberResult);
        if (transRequest != null) {
            intent.putExtra("transRequest", gson.toJson(transRequest).toString());
        }
        intent.putExtra("isRecharge", isRecharge);
        String cardNo = "";
        if (tvMemberCard.getTag() != null) {
            transRequest.isSweep = "1";
            cardNo = tvMemberCard.getTag().toString();
        }
        intent.putExtra(OpenCrashHand.COM_PAY_TYPE, isCombinePay);
        intent.putExtra(ResultActivity.VIP_CARD, cardNo);
        intent.putExtra("timesType", timesType);
        intent.putExtra("timesOrder", timesOrder);
        intent.putExtra("bankType", bankType);
        if (null != bundleTemp) {
            intent.putExtra(ParameterTemp.BUNDLE_TEMP, bundleTemp);
            startActivityForResult(intent, SEND_TO_RESULT);
        } else {
            startActivity(intent);
        }*/
    }

    /**
     * 跳到被扫界面
     *
     * @param bankType 5是微信，6是支付宝
     */
    private void switchIntentBeScanned(String payType) {
        Intent intent = new Intent(GoCashierActivity.this, CaptureBeScannedActivity.class);
        String transAmount = tvRmb.getText().toString();
        intent.putExtra("transAmount", transAmount);
        intent.putExtra("payType", payType);
        startActivity(intent);
        /*String transAmount = (isCombinePay) ? tvNeedPayValue.getText().toString() : tvRmb.getText().toString();
        if (isRecharge) {
            TransRequest transRequest = new TransRequest();
            transRequest.bankType = bankType;
            transRequest.bankAmount = transAmount;
        } else {
            if (transRequest == null) {
                transRequest = new TransRequest();
            }
            transRequest.bankType = bankType;
            transRequest.bankAmount = transAmount;
        }
        if (transRequest != null) {
            transRequest.isSweep = "2";
            intent.putExtra("transRequest", gson.toJson(transRequest).toString());
        }
        String cardNo = "", memberId = "", billNo = "";
        if (tvMemberCard.getTag() != null) {
            cardNo = tvMemberCard.getTag().toString();
        }
        if (null != queryMember && queryMember.MEMBERID != 0) {
            memberId = queryMember.MEMBERID + "";
        } else {
            memberId = "";
        }
        if (timesOrder != null) {
            billNo = timesOrder.order_no + "";
        } else {
            billNo = "";
        }
        intent.putExtra("transAmount", transAmount);
        intent.putExtra("cardNo", cardNo);
        intent.putExtra("memberId", memberId);
        intent.putExtra("billNo", billNo);
        inranent.putExtra("isRecharge", isRecharge);
        intent.putExtra("isCombinePay", isCombinePay);
        intent.putExtra("result", queryMemberResult);
        intent.putExtra("timesType", timesType);// 商品的交易类型
        intent.putExtra("bankType", bankType);// 5微信支付（被扫）、6支付宝（被扫）
        if (null != bundleTemp) {
            intent.putExtra(ParameterTemp.BUNDLE_TEMP, bundleTemp);
            startActivityForResult(intent, SEND_TO_RESULT);
        } else {
            startActivity(intent);
        }*/
    }


    //银行卡刷卡
    private void loadBankTrans() {
        transRequest = null;
        transRequest = new TransRequest();
        if (checkData()) {
            String payAmount = tvRmb.getText().toString().trim();
            transAmt = StringUtil.formatterAmount(payAmount);
            if ((int) (Double.valueOf(transAmt) * 100) < 200) {
                transRequest.bankType = ""; // 还原为默认的交易类型
                transRequest.bankAmount = "";// 银行卡交易金额为空
                showErrorDialog(getString(R.string.lowest_money));
            } else {
                loadCrashBankCard(transAmt);
            }
        }
    }

    /**
     * 刷银行卡
     *
     * @param transAmt
     */

    private void loadCrashBankCard(String transAmt) {

        getPresenter().pLoadCrashbankCard(transAmt);
    }

    /**
     * 现金支付
     */
    private void loadCrashTrans() {
        transCashShowDialog();
    }

    /***
     * 交易弹出现金对话框
     *
     * @param
     */
    @SuppressLint("ResourceAsColor")
    private void transCashShowDialog() {
        if (checkData()) {
            if (null != cashDialog) {
                cashDialog.dismiss();
                cashDialog = null;
            }
            initViewCashShowDialog();
            initDataCashShowDialog();
            initEventCashShowDialog();
        }
    }

    //初始化现金弹出对话框的控件
    private void initViewCashShowDialog() {
        cashDialog = Utils.setDialog(this, R.layout.cash_dialog, 1, 0);
        cashDialog.getWindow().setGravity(Gravity.CENTER);
        tvCashDialogRmb = (TextView) cashDialog.findViewById(R.id.tv_cash_dialog_rmb);
        etCashDailogCash = (EditText) cashDialog.findViewById(R.id.et_cash);
        btnCashDailogSure = (Button) cashDialog.findViewById(R.id.btn_sure);
        rlCashDailogCancel = (RelativeLayout) cashDialog.findViewById(R.id.rl_cash_dialog_cancel);
    }

    //初始化现金弹出对话框的数据
    private void initDataCashShowDialog() {
        tvCashDialogRmb.setText(StringUtil.formatterAmount(tvRmb.getText().toString().trim()));
        etCashDailogCash.setText(StringUtil.formatterAmount(tvRmb.getText().toString().trim()));
    }

    //初始化现金弹出对话框的点击事件
    private void initEventCashShowDialog() {
        rlCashDailogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cashDialog.cancel();
            }
        });

        btnCashDailogSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String payCash = etCashDailogCash.getText().toString().trim();
                String consumM = tvRmb.getText().toString();
                transRequest = null;
                transRequest = new TransRequest();
                if (payCash.length() > 0) {
                    //设置交易类型
                    payType = PayType.PAY_TYEP_CASH;
                    cashDialog.cancel();
                    transRequest.setCash(consumM);
                    transPostCash(transRequest);
                    transRequest = null;
                }
            }
        });
        cashDialog.show();

    }

    /**
     * 现金消费
     *
     * @param mTransRequest
     */
    private void transPostCash(final TransRequest mTransRequest) {


    }


    /**
     * 银行卡消费
     *
     * @param mBankEntity
     */
    private void transPostBank(final BankEntity mBankEntity) {
        //DisableHomeMenuBar.operatorAll(1);


        TransRequest transBean = new TransRequest();
        JUtils.Log("Secondkey:"+ transBean.SECONDARYKEY);
        transBean.SECONDARYKEY = MerchantSignDataManager.getInstance().getSignData().SECONDARYKEY;

        transBean.bankAmount = "1";                                                                                  //交易金额
        transBean.bankCardReadType = mBankEntity.getBANKCARDREADTYPE();                                              //读卡方式：0刷磁条卡；1插IC卡；2非接
        transBean.cardDate = mBankEntity.getCARDDATE();                                                              //银行卡有效期
        transBean.data23 = mBankEntity.getDATA23();                                                                  //Data 23
        transBean.iccData = mBankEntity.getICCDATA();                                                                //Data 55
        transBean.pan = mBankEntity.getPAN();                                                                        //银行卡号
        transBean.pin = mBankEntity.getPIN();                                                                        //密码
        transBean.track2 = mBankEntity.getTRANK2();                                                                  //二磁道
        transBean.track3 = mBankEntity.getTRANK3();                                                                  //三磁道

        transBean.poseCode = MerchantRegisterDataManager.getInstance().getRegisterData().POSCODE;                    // 终端号
        transBean.merchantcode = MerchantRegisterDataManager.getInstance().getRegisterData().MERCHANT_CODE;          // 商户号
        transBean.operatorId = JUtils.getSharedPreference().getString("operatorId", "");                             // 操作员号
        transBean.batchno = MerchantSignDataManager.getInstance().getSignData().BATCHNUM;                            // 批次号
        transBean.deviceSn = DeviceUtils.getDeviceSn(mContext);                                                              //设备SN号
        transBean.bankType = "1";                                                                                    //0没有、1银行卡、2微信、3支付宝、4虚拟银行卡

        String serialNumber = PosDataUtils.getPosSerialNumber() + "";                                                // 本地的流水号码
        if (!TextUtils.isEmpty(serialNumber)) {
            transBean.traceNo = serialNumber;
        }

        transBean.batchno = MerchantSignDataManager.getInstance().getSignData().BATCHNUM;                            // 批次号
        JUtils.Log("transPostBank  流水号："+transBean.traceNo+"  批次号："+transBean.batchno);
        getPresenter().pPay(transBean);
    }

    /**
     * 登录检查
     *
     * @return
     */
    private boolean checkData() {
        boolean isCheck = true;
        double getValue = CharacterOperationUtils.getSubtract(tvRmb.getText().toString(), "0");
        if (getValue <= 0) {
            isCheck = false;
        }
        return isCheck;
    }

    /**
     * 提示错误的信息
     *
     * @param msgInfo
     */
    private void showErrorDialog(final String msgInfo) {
        initViewErrorDialog();
        initDataErrorDialog(msgInfo);
        initEventErrorDialog();
    }

    //初始化错误信息对话框控件
    private void initViewErrorDialog() {
        dialogError = Utils.setDialog(this, R.layout.error_dialog, 0.7, 0);
        dialogError.getWindow().setGravity(Gravity.CENTER);
        dialogError.show();
        tvErrorDialogTitle = (TextView) dialogError.findViewById(R.id.tv_error_dialog_title);
        btnErrorDialogCancel = (Button) dialogError.findViewById(R.id.btn_error_dialog_cancel);
        btnErrorDialogSure = (Button) dialogError.findViewById(R.id.btn_error_dialog_sure);
        vErrorDialogCenterLine = dialogError.findViewById(R.id.v_error_dialog_center_line);
        btnErrorDialogCancel.setVisibility(View.GONE);
        vErrorDialogCenterLine.setVisibility(View.GONE);
    }

    //初始化错误信息对话框内容
    private void initDataErrorDialog(final String msg) {
        tvErrorDialogTitle.setText(msg);
    }

    //初始化错误信息对话框事件
    private void initEventErrorDialog() {
        btnErrorDialogSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogError.cancel();
                dialogError = null;
            }
        });
        btnErrorDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogError.cancel();
                dialogError = null;
            }
        });
    }

    public void startOpenCrashHandActvity(TransResponse transResponse) {
        Intent intent = new Intent(this, OpenCrashHandActivity.class);
        finish();
    }

    public void startResultActivity(TransResponse transResponse) {
        String str ="{\n" +
                "  \"AUTH_CODE\": \"636432\",\n" +
                "  \"MEMBERID\": 0,\n" +
                "  \"TRACE_NO\": \"000003\",\n" +
                "  \"CARD_EXPIRE\": \"2108\",\n" +
                "  \"BANK_TYPE\": 1,\n" +
                "  \"BANK_AMOUNT\": 2.0,\n" +
                "  \"MESSAGE\": \"\",\n" +
                "  \"STATUS\": \"00\",\n" +
                "  \"BANK_CARD_TYPE\": \"0\",\n" +
                "  \"INVOICE_ID\": \"000001013187\",\n" +
                "  \"BANK_STARASN\": \"622689******8173\",\n" +
                "  \"DEVICETYPE\": \"HI98\",\n" +
                "  \"IS_BD\": 0,\n" +
                "  \"TRADE_SN\": 100000063573,\n" +
                "  \"REMARK\": \"银行卡消费：2.0;\",\n" +
                "  \"BATCHNO\": \"000159\",\n" +
                "  \"POINT_BALANCE\": 0.0,\n" +
                "  \"BM_NAME\": \"现代金控正式环境\",\n" +
                "  \"TOTAL\": 2.0,\n" +
                "  \"LOCAL_TIME\": \"20170918141653\",\n" +
                "  \"ORDERINFOLIST\": [],\n" +
                "  \"FEE_AMOUNT\": \"0.01\",\n" +
                "  \"TRADE_UNIQUEID\": \"100003491505715413361\",\n" +
                "  \"BM_MERID\": \"834332373720001\",\n" +
                "  \"SUB_POINT\": 0.0,\n" +
                "  \"COUPON_AMOUNT\": 0.0,\n" +
                "  \"BANKDATA15\": \"0918\",\n" +
                "  \"PAID\": 2.0,\n" +
                "  \"BM_POSID\": \"74542074\",\n" +
                "  \"INVOICE_CODE\": \"http://weixin.leefengpay.com/index.php?s=/addon/Member/Member/invoiceInfo/mcode/100000000000003/uniqueid/10000349150571541336100000311\",\n" +
                "  \"SUB_DEPOSIT\": 0.0,\n" +
                "  \"ADD_POINT\": 0.0,\n" +
                "  \"CARD_BANK_NAME\": \"中信银行信用卡中心\",\n" +
                "  \"REQUST_TYPE\": \"consume\",\n" +
                "  \"REFER_NUM\": \"170918302038\",\n" +
                "  \"GOODSINFOLIST\": [],\n" +
                "  \"DEPOSIT_BALANCE\": 0,\n" +
                "  \"RESPONSE_TIME\": \"2017-09-18 14:16:55\",\n" +
                "  \"MD5\": \"e40bc5140d894c755dd6333705b7657a\"\n" +
                "}";
        if (!JsonValidator.validate(str)) {
            JUtils.Log(str);
            JUtils.ToastLong(getString(R.string.service_data_abnormal));
            //showErrorInfoDialog(getString(R.string.service_data_abnormal));
            return;
        }
        //JUtils.Log(str);
        Gson gson = new Gson();
        TransResponse transResponse1 = gson.fromJson(str, TransResponse.class);



        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("result", transResponse1);
        startActivity(intent);
        finish();
    }

    //刷卡后，请求服务器进行消费
    public void sendMsgBankConsume(BankEntity bankEntity) {
        Message msg = Message.obtain();
        msg.what = MSG_BANK_CONSUME;
        Bundle data = new Bundle();
        data.putSerializable("bankEntity", bankEntity);
        msg.setData(data);
        handler.sendMessage(msg);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_BANK_CONSUME:
                    //请求服务器进行消费
                    //先存数据库，在链接服务器。
                    /*TransDataBrief transDataBrief = new TransDataBrief("0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"
                            , "0", "0", "0", "0", "0", "0", "0", "0", "0", "0");
                    DbManager.addTransDataTTB(transDataBrief);
                    TransRequest transBean = null;
                    payType = PayType.PAY_TYEP_CARD;*/


                    // (TransResponse) data.getSerializable("msgTransResponse")
                    Bundle data = msg.getData();
                    BankEntity bankEntity1 = null;
                    if (data != null) {
                        bankEntity1 = (BankEntity) data.getSerializable("bankEntity");
                        transPostBank(bankEntity1);
                    } else {
                        JUtils.Toast(getString(R.string.err_read_bankcard));
                    }

                    //getPresenter().pPay(transDataBrief);  //对接接口时修改参数 20170905
                    break;

                default:
                    break;
            }
        }
    };


}
