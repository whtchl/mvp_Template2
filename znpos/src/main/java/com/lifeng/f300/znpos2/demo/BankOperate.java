package com.lifeng.f300.znpos2.demo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.witsi.arqII.api.EmvParam;

import com.lifeng.f300.common.utils.LogUtils;
import com.lifeng.f300.common.utils.NumberConvertUtils;
import com.lifeng.f300.common.utils.ToastMakeUtils;
import com.lifeng.f300.common.widget.AllDialog;
import com.lifeng.f300.common.widget.BankCarshDialog;
import com.lifeng.f300.common.widget.WaitingDialog;
import com.witsi.sdk.mpos.WtCallback;
import com.witsi.sdk.mpos.WtDevContrl;

/**
 * Created by happen on 2017/10/26.
 */

public class BankOperate {
    private WtDevContrl wtDevContrl;
    private double cash=0.00d;
    private Dialog dialog;
    private Context context;
    private boolean isBankCard; //是否是银行卡true就是银行卡,false就是:关闭非接和IC卡插卡监听
    private OnCardExistenceCallback onCardExistenceCallback;
    private String bankCardReadType="";//0刷磁条卡；1插IC卡；2非接
    public BankOperate(WtDevContrl wtDevContrl,Context context,boolean isBankCard) {
        this.wtDevContrl = wtDevContrl;
        this.context=context;
        this.isBankCard=isBankCard;
    }
    public void operateBankCards() {
        //operateRfPboc();
        //operateRf();
        wtDevContrl.cardOperateExec(true, 60, wtCardCheckCallback);
    }

    public void cancel(Dialog dialog){
        this.dialog=dialog;
        wtDevContrl.cancel();
    }

    private void operateICC() {
        wtDevContrl.IccPbocExec(EmvParam.TransType.EMV_TRANS_PAYMENT, cash, true,wtEmvExecCallback);
    }

    private void operateRf() {
        wtDevContrl.QpbocExec(EmvParam.TransType.EMV_TRANS_PAYMENT, cash, true,wtEmvExecCallback);
    }

    private void operateRfPboc(){
        wtDevContrl.RfPbocExec(EmvParam.TransType.EMV_TRANS_PAYMENT, cash, true, wtEmvExecCallback);

    }
    private WtCallback.WtCardCheckCallback wtCardCheckCallback = new WtCallback.WtCardCheckCallback() {
        @Override
        public void onCardCheckErr(WtCallback.WtCardCheckErrResult result, int code) {
            LogUtils.d("wang", "监听到错误三种卡 "+"code"+code);
            if (code == -3045 || code == -3070) {
                ToastMakeUtils.showToast((Activity)context, "错误码："+code+"：获取密钥失败");
                //TODO 需要自动签到
                closeLightAndDialog();
                return ;
            }

            if (code == -1322) {
                //自动导入aid密钥
                loadAidKey();
                return ;
            }

            switch (result) {
                case ERR_ESC:
                    LogUtils.d("wang", "用户取消了操作"+"关闭对话框和灯");
                    closeLightAndDialog();
                    break;
                case ERR_TIME_OUT:
                    LogUtils.d("wang", "超时");
                    closeLightAndDialog();
                    break;
                case ERR_GET_MAG_DATA_FAIL:
                    LogUtils.d("wang", "获取磁道失败");
                    closeLightAndDialog();
                    break;
                case ERR_MAG_DATA_ENCRYPT_FAIL:
                    LogUtils.d("wang", "获取磁道加密数据失败");
                    closeLightAndDialog();
                    break;
                case ERR_IO:
                    LogUtils.d("wang", "IO 操作失败");
                    closeLightAndDialog();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onIccExistence() {
            LogUtils.d("wang", "监听到IC卡");
            if (isBankCard) {
                showWaitingDialog("检测到IC卡，正在处理");
                operateICC();
            }else{
                ToastMakeUtils.showToast((Activity) context, "会员卡不支持插卡");
                closeLightAndDialog();
            }
        }

        //TODO
        @Override
        public void onRfExistence() {
            LogUtils.d("wang", "监听到NF卡  isBankCard:"+isBankCard);
            if (isBankCard) {
                showWaitingDialog("检测到NFC卡，正在处理");
                operateRf();
                //operateRfPboc();
            }else{
                ToastMakeUtils.showToast((Activity) context, "会员卡不支持非接");
                closeLightAndDialog();
            }
        }

        @Override
        public void onTrackExistence(String pan, String expiryDate,String svr, String[] track, boolean isIcCard) {
            LogUtils.d("wang", "磁条卡刷卡完成");
            LogUtils.d("wang", "read magcard success.");
            LogUtils.d("wang", "primary account number: " + pan);
            LogUtils.d("wang", "卡有效期: " + expiryDate);
            LogUtils.d("wang", "svr: " + svr);
            LogUtils.d("wang", "track[0]: " + track[0]);
            LogUtils.d("wang", "track[1]: " + track[1]);
            LogUtils.d("wang", "track[2]: " + track[2]);
            LogUtils.d("wang", "是否是IC卡" + isIcCard);
            bankCardReadType="0";
            if(null != onCardExistenceCallback)
                onCardExistenceCallback.OnTrackExistenceSuccess(pan, expiryDate, svr, track, isIcCard,bankCardReadType);
        }
    };

    private WtCallback.WtEmvExecCallback wtEmvExecCallback = new WtCallback.WtEmvExecCallback() {
        //IC卡
        @Override
        public void onEmvRequestOnline(String pan, String appExpiryDate,String icCardSn, byte[] pin, byte[] track2, byte[] iccData) {
            dismissDialog();
            LogUtils.d("wang", "IC读卡完成");
            if (null != wtDevContrl) {
                LogUtils.d("wang", "IC卡的有效期："+wtDevContrl.getIcCardInfo().getCardExpireDate());
            }
            LogUtils.d("wang", "primary account number: " + pan);
            LogUtils.d("wang", "app的有效期: " + appExpiryDate);
            LogUtils.d("wang", "card svr: " + icCardSn);
            LogUtils.d("wang","track2: " + NumberConvertUtils.byteToHex(track2));
            LogUtils.d("wang","icc data: " + NumberConvertUtils.byteToHex(iccData));
            bankCardReadType="1";
            if(null != onCardExistenceCallback)
                onCardExistenceCallback.OnICCCompleteSuccess(pan, appExpiryDate, icCardSn, pin, track2, iccData,bankCardReadType);
        }

        @Override
        public void onError(WtCallback.WtEmvExecErrResult result, int code) {
            dismissDialog();
            LogUtils.d("wang", "监听到错误:IC卡或者非接卡"+"code"+code);
            if (code == -3045 || code == -3070) {
                //TODO 需要自动签到
                ToastMakeUtils.showToast((Activity)context, "错误码："+code+"：获取密钥失败");
                closeLightAndDialog();
                return ;
            }

            if (code == -1322) {
                //自动导入aid密钥
                loadAidKey();
                return ;
            }
        }

        //NFC  注释的部分是新jar包
//		@Override
//		public void onComplete(TRANS_RESULT result, byte[] iccData, byte[] scriptResult,
//				boolean isSignatureReq, int code) {
//			dismissDialog();
//			LogUtils.d("wang", "NFC读卡完成");
//			LogUtils.d("wang","code: " + code);
//			LogUtils.d("wang","icc data: " + NumberConvertUtils.byteToHex(iccData));
//			bankCardReadType="2";
//			if(null != onCardExistenceCallback)
//				onCardExistenceCallback.OnNFCCompleteSuccess(result, iccData, code,bankCardReadType);
//		}

//		@Override
//		public void onEmvGetPan(String pan) {
//			LogUtils.d("wang", "卡号"+pan);
//			wtDevContrl.ContinuePbocExec(this);
//		}

        /*@Override
        public void onIccComplete(TRANS_RESULT result, byte[] iccData, int code) {
            dismissDialog();
            LogUtils.d("wang", "NFC读卡完成");
            LogUtils.d("wang","code: " + code);
            LogUtils.d("wang","icc data: " + NumberConvertUtils.byteToHex(iccData));
            bankCardReadType="2";
            if(null != onCardExistenceCallback)
                onCardExistenceCallback.OnNFCCompleteSuccess(result, iccData, code,bankCardReadType);
        }
*/
        @Override
        public void onComplete(TRANS_RESULT result, byte[] iccData, byte[] scriptResult,
                               boolean isSignatureReq, int code) {
            // TODO Auto-generated method stub
            dismissDialog();
            LogUtils.d("wang", "NFC读卡完成");
            LogUtils.d("wang","code: " + code);
            LogUtils.d("wang","icc data: " + NumberConvertUtils.byteToHex(iccData));
            bankCardReadType="2";
            if(null != onCardExistenceCallback)
                onCardExistenceCallback.OnNFCCompleteSuccess(result, iccData, code,bankCardReadType);
        }

        @Override
        public void onEmvGetPan(String pan) {
            // TODO Auto-generated method stub
            LogUtils.d("wang", "卡号"+pan);
            wtDevContrl.ContinuePbocExec(this);
        }
    };

    private WaitingDialog waitingDialog;

    private void closeLightAndDialog(){
       /* if(BdPermission.getInstance().demoG100()){
            if (null != wtDevContrl) {
                wtDevContrl.Led(0x01, false);
            }
        }*/


        if (null != dialog) {
            dialog.dismiss();
        }

        AllDialog.closeDialog();  //关闭会员卡的框
        BankCarshDialog.closeDialog();  //关闭银行卡的框
        dismissDialog();
    }

    public interface OnCardExistenceCallback{
        /**
         * 刷卡完成成功回调
         * @param pan
         * @param expiryDate
         * @param svr
         * @param track
         * @param isIcCard
         */
        public void OnTrackExistenceSuccess(String pan, String expiryDate,String svr, String[] track, boolean isIcCard,String bankCardReadType);

        /**
         * NFC卡操作成功回调
         * @param result
         * @param iccData
         * @param code
         */
        public void OnNFCCompleteSuccess(WtCallback.WtEmvExecCallback.TRANS_RESULT result, byte[] iccData, int code, String bankCardReadType);

        /**
         * IC卡操作成功回调
         * @param pan
         * @param appExpiryDate
         * @param icCardSn
         * @param pin
         * @param track2
         * @param iccData
         */
        public void OnICCCompleteSuccess(String pan, String appExpiryDate,String icCardSn, byte[] pin, byte[] track2, byte[] iccData,String bankCardReadType);
    }

    public void setOnCardExistenceCallback(OnCardExistenceCallback onCardExistenceCallback) {
        this.onCardExistenceCallback = onCardExistenceCallback;
    }

    private void showWaitingDialog(String text) {
        if (null != waitingDialog) {
            waitingDialog.setMessage(text);
        } else {
            waitingDialog = new WaitingDialog(context, text);
        }
        if (!waitingDialog.isShowing())
            waitingDialog.show();
    }

    private void dismissDialog() {
        if (null != waitingDialog) {
            waitingDialog.dismiss();
            waitingDialog = null;
        }
    }

    //aid的密码管理
    private void loadAidKey(){
        LoadAidKey loadAidKey = new LoadAidKey(context);
        loadAidKey.setAidCallBack(new LoadAidKey.LoadAidListenear() {
            @Override
            public void getAidSate(boolean flag) {
                closeLightAndDialog();
                if (flag) {
                    LogUtils.d("wang", "导入Aid密钥成功");
                    ToastMakeUtils.showToast((Activity) context, "更新Aid密钥成功，请重新读卡");
                }else{
                    LogUtils.d("wang", "导入Aid密钥失败");
                    ToastMakeUtils.showToast((Activity) context, "更新Aid密钥失败，请重新读卡");
                }
            }
        });
        loadAidKey.loadAidSecretInSystem();
    }
}
