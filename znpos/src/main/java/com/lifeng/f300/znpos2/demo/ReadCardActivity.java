package com.lifeng.f300.znpos2.demo;


import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.jude.utils.JUtils;
import com.lifeng.f300.common.contract.OnLoginDialogInterface;
import com.lifeng.f300.common.utils.LogUtils;
import com.lifeng.f300.common.utils.NumberConvertUtils;
import com.lifeng.f300.common.widget.BankCarshDialog;
import com.lifeng.f300.znpos2.R;
import com.witsi.sdk.mpos.WtCallback;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by happen on 2017/10/26.
 */

public class ReadCardActivity extends BaseReadCardActivity {

    @InjectView(R.id.btn_readbankinfo)
    Button btnReadbankinfo;
    private BankOperate bankOperate;
    private Bundle bundleTemp = null;
    private String  mexpiryDate="";
    private Dialog dialogError;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.bank_read_info);
        ButterKnife.inject(this);

        btnReadbankinfo.setOnClickListener(v -> ReadBankInfo());
    }

    private void ReadBankInfo() {
        loadBankTrans();
    }

    private void loadBankTrans(){

        // F100的刷卡
        if (initEmvOK) {
            LogUtils.d("wang","loadCrashBankCard1");
            wtDevContrl.Led(0x01, true);// 控制灯
            if (bankOperate == null) {
                bankOperate = new BankOperate(wtDevContrl, this, true);
            }
            LogUtils.d("wang","loadCrashBankCard2");
            loadCardListener();
            LogUtils.d("wang","loadCrashBankCard3");
            bankOperate.operateBankCards();
            LogUtils.d("wang","loadCrashBankCard4");
            BankCarshDialog.showDialog(this, R.style.CustomProgressDialog, R.layout.cashier_details51, "12",
                   false,false, new OnLoginDialogInterface() {
                        @Override
                        public void onConfirmClick(EditText etUserName, EditText etOldPassword, EditText etPassword,
                                                   EditText etPasswordSure) {
                        }

                        @Override
                        public void onCancelClick() {
                            bankOperate.cancel(BankCarshDialog.dialog);
                            if (null != bundleTemp) {
                                onBackPressed();
                            }
                        }

                        @Override
                        public void onSureClick() {
                        }
                    });
        }
    }

    /**
     * 卡的成功监听
     */
    private void loadCardListener() {
        bankOperate.setOnCardExistenceCallback(new BankOperate.OnCardExistenceCallback() {
            @Override
            public void OnTrackExistenceSuccess(String pan, String expiryDate, String svr, String[] track,
                                                boolean isIcCard, String bankCardReadType) {
                if (isIcCard || TextUtils.isEmpty(pan)) {
                    //closeLightAndDialog();
                    if (isIcCard) {
                        //showInfoDialog(getString(R.string.chip_card));
                        JUtils.ToastLong("该卡为芯片卡，请插卡交易");
                    } else {

                        JUtils.ToastLong(" 读卡失败，请重新读卡");
                       //showInfoDialog(getString(R.string.read_card_again));
                    }
                    return;
                }
                //closeLightAndDialog();
                String[] trackNow = new String[3];
                trackNow[0] = TextUtils.isEmpty(track[0]) ? "" : track[0];
                trackNow[1] = TextUtils.isEmpty(track[1]) ? "" : track[1];
                trackNow[2] = TextUtils.isEmpty(track[2]) ? "" : track[2];
                String iccDataStr = "";
                mexpiryDate = expiryDate;  ////20170607 pos小票改造 tchl add
                //2017051101 tchl begin 38封顶无卡支付
                /*if(NoCardPosActivity.NoCardPay.isShowPINDialog!=null && NoCardPosActivity.NoCardPay.isShowPINDialog.equals("1")){
                    LogUtils.d("wang","F300，无卡支付，不显示PIN密码框");
                    isRegisterCard(null);
                }else{
                    loadShowDialog(TextUtils.isEmpty(pan) ? "" : pan, trackNow, iccDataStr, "", bankCardReadType);
                }*/
                //2017051101 tchl end  38封顶无卡支付

            }

            @Override
            public void OnNFCCompleteSuccess(WtCallback.WtEmvExecCallback.TRANS_RESULT result, byte[] iccData, int code, String bankCardReadType) {
                String bankCardNumber = wtDevContrl.getIcCardInfo().getPan();
                String data23 = wtDevContrl.getIcCardInfo().getCardSn();
                LogUtils.d("wang", "非接卡号：" + bankCardNumber + "卡序列号：" + data23 + "返回标号：code:" + code);
                if (code == -14 || code == -1323 || TextUtils.isEmpty(bankCardNumber) || code == -1321) {
                    //closeLightAndDialog();
                    if (code == -1321) {
                        //showInfoDialog(getString(R.string.rechang_card_again));
                        JUtils.ToastLong("该卡非银行卡，请换卡");
                    } else if (code == -1323) {
                        JUtils.ToastLong("NFC卡移动过快，请重试");
                        //showInfoDialog(getString(R.string.nfc_error_info));
                    } else {

                        JUtils.ToastLong("读卡失败，请重新读卡");
                       // showInfoDialog(getString(R.string.read_card_again));
                    }
                    return;
                }
                //closeLightAndDialog();
                mexpiryDate = wtDevContrl.getIcCardInfo().getCardExpireDate();//20170607 pos小票改造 tchl add
                String[] track = new String[3];
                track[0] = "";
                track[1] = NumberConvertUtils.byteToHex(wtDevContrl.getIcCardInfo().getTrack2());
                track[2] = "";
                String iccDataStr = NumberConvertUtils.byteToHex(iccData); // iccdate的数据
                data23 = TextUtils.isEmpty(data23) ? "" : (data23.length() == 2 ? "0" + data23 : data23);
                //2017051101 tchl begin 38封顶无卡支付
               /* if(NoCardPosActivity.NoCardPay.isShowPINDialog!=null && NoCardPosActivity.NoCardPay.isShowPINDialog.equals("1")){
                    LogUtils.d("wang","F300，无卡支付，不显示PIN密码框");
                    isRegisterCard(null);
                }else{
                    loadShowDialog(bankCardNumber, track, iccDataStr, data23, bankCardReadType);
                }*/
                //2017051101 tchl end  38封顶无卡支付

            }

            @Override
            public void OnICCCompleteSuccess(String pan, String appExpiryDate, String icCardSn, byte[] pin,
                                             byte[] track2, byte[] iccData, String bankCardReadType) {
                LogUtils.d("wang", "IC卡：" + pan+" date:"+appExpiryDate);
                if (TextUtils.isEmpty(pan)) {
                    //closeLightAndDialog();
                    //showInfoDialog(getString(R.string.read_card_again));
                    return;
                }
                //closeLightAndDialog();
                String[] track = new String[3];
                track[0] = "";
                track[1] = NumberConvertUtils.byteToHex(track2);
                track[2] = "";
                String iccDataStr = NumberConvertUtils.byteToHex(iccData); // iccdate的数据
                icCardSn = TextUtils.isEmpty(icCardSn) ? "" : (icCardSn.length() == 2 ? "0" + icCardSn : icCardSn);
                mexpiryDate = appExpiryDate;  ////20170607 pos小票改造 tchl add
                //2017051101 tchl begin 38封顶无卡支付
                /*if(NoCardPosActivity.NoCardPay.isShowPINDialog!=null && NoCardPosActivity.NoCardPay.isShowPINDialog.equals("1")){
                    LogUtils.d("wang","F300，无卡支付，不显示PIN密码框");
                    isRegisterCard(null);
                }else{
                    loadShowDialog(TextUtils.isEmpty(pan) ? "" : pan, track, iccDataStr, icCardSn, bankCardReadType);
                }*/
                //2017051101 tchl end  38封顶无卡支付

            }
        });
    }


    /**
     * 提示重新刷卡
     *
     * @param msgInfo
     */
   /* private void showInfoDialog(String msgInfo) {
        dialogError = Utils.setDialog(this, R.layout.scan_code_error_dialog, 0.7, 0);
        dialogError.getWindow().setGravity(Gravity.CENTER);
        dialogError.show();
        TextView tvMsg = (TextView) dialogError.findViewById(R.id.tv_scan_error);
        dialogError.findViewById(R.id.btn_cancel).setVisibility(View.GONE);
        dialogError.findViewById(R.id.center_line).setVisibility(View.GONE);
        tvMsg.setText(msgInfo);
        Button btnSure = (Button) dialogError.findViewById(R.id.btn_sure);
        Button btnCancel = (Button) dialogError.findViewById(R.id.btn_cancel);
        btnSure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogError.cancel();
                dialogError = null;
                loadCrashBankCard(transAmt, queryMember, card, isRecharge);// 重新启动刷卡监听
            }
        });
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogError.cancel();
                dialogError = null;
            }
        });
    }*/

}
