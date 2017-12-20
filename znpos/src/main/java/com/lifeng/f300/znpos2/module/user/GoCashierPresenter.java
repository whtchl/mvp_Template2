package com.lifeng.f300.znpos2.module.user;

import android.widget.EditText;

import com.jude.utils.JUtils;
import com.lifeng.beam.bijection.Presenter;
import com.lifeng.f300.common.entites.BankEntity;
import com.lifeng.f300.common.entites.TransRequest;
import com.lifeng.f300.common.entites.TransResponse;
import com.lifeng.f300.common.utils.CheckNetIsOk;
import com.lifeng.f300.common.utils.PayType;
import com.lifeng.f300.common.utils.StringUtil;
import com.lifeng.f300.common.widget.BankCarshDialog;
import com.lifeng.f300.common.contract.OnLoginDialogInterface;
import com.lifeng.f300.config.API;
import com.lifeng.f300.hardware.common.HwResponse;
import com.lifeng.f300.hardware.common.Hw;
import com.lifeng.f300.model.GoCashierModel;
import com.lifeng.f300.server.service.ServiceResponse;
import com.lifeng.f300.znpos2.R;

import rx.Subscription;
import rx.functions.Action0;


/**
 * Created by happen on 2017/8/30.
 */

public class GoCashierPresenter extends Presenter<GoCashierActivity> {



    /**
     * 刷银行卡
     *
     * @param transAmt
     */
    public void pLoadCrashbankCard(final String transAmt) {
        // getView().getExpansion().showCrashBankCardDialog(transAmt);
        //final Subscription subscription;


        Subscription subscription =  GoCashierModel.getInstance().mLoadCrashbankCard(transAmt)
                .finallyDo(new Action0() {
                    @Override
                    public void call() {
                        //getView().getExpansion().dismissCrashBankCardDialog(transAmt);
                        JUtils.Log("pLoadCrashbankCard "+Thread.currentThread().getName().toString());
                        BankCarshDialog.closeDialog();
                    }
                })
                .subscribe(new HwResponse<BankEntity>() {
                    @Override
                    protected void _onError(String message) {
                        JUtils.Log("pLoadCrashbankCard _OnError:" + message + Thread.currentThread().getName().toString());
                        JUtils.Toast("!!!!!!!!!!!!");
                    }

                    @Override
                    protected void _onNext(BankEntity bankEntity) {
                        JUtils.Log("pLoadCrashbankCard _onNext:"+Thread.currentThread().getName().toString());
                        /*
09-13 10:32:43.558: D/wang(26700):
        HI98密码: 40D5B86B0DBAF5D9
        交易卡号：6226890091868173
        二磁道：6226890091868173D21082011924490100000
        三磁道：
        icc Data(55)：9F2608032798D791D79D559F2701809F101307020103A00000040A010000000000FA24FDE69F3704631958649F360201C6950500000000009A031709139C01009F02060000000000015F2A02015682027C009F1A0201569F03060000000000009F3303E0F1C89F3501229F1E085465726D696E616C8408A0000003330101029F090200209F410400000004
        卡序列号：001
        third code:
        BankCardReadType:2
        expdate:2108
*/
                        bankEntity.setPIN("40D5B86B0DBAF5D9");
                        bankEntity.setPAN("6226890091868173");
                        bankEntity.setTRANK2("6226890091868173D21082011924490100000");
                        bankEntity.setTRANK3("");
                        bankEntity.setICCDATA("9F2608032798D791D79D559F2701809F101307020103A00000040A010000000000FA24FDE69F3704631958649F360201C6950500000000009A031709139C01009F02060000000000015F2A02015682027C009F1A0201569F03060000000000009F3303E0F1C89F3501229F1E085465726D696E616C8408A0000003330101029F090200209F410400000004");
                        bankEntity.setDATA23("001");
                        bankEntity.setBANKCARDREADTYPE("2");
                        bankEntity.setCARDDATE("2108");

                        getView().sendMsgBankConsume(bankEntity);
                    }
                });
        BankCarshDialog.showDialog(getView(), R.style.CustomProgressDialog, R.layout.cashier_details51, transAmt,false,false, new OnLoginDialogInterface() {
            @Override
            public void onConfirmClick(EditText etUserName, EditText etOldPassword,
                                       EditText etPassword, EditText etPasswordSure) {
            }

            @Override
            public void onCancelClick() {
                //if(null != subscription )
                subscription.unsubscribe();
                BankCarshDialog.closeDialog();
                Hw.getInstance().closeReadBankCard();
                        /*if (null != PayApplication.icHI98 && null != PayApplication.magHI98
                                && null != PayApplication.piccHI98 && null != PayApplication.pedHI98) {
                            bankOperateHI98.closeReadBankCardHI98();
                        }
                        if (null != bundleTemp) {
                            onBackPressed();
                        }*/
                // getView().sendMSGCancelBankCarshDialog();
            }

            @Override
            public void onSureClick() {
            }
        });

    }


/*    public void pLoadWorkKey(){
        getView().getExpansion().showProgressDialog(getView().getString(R.string.load_work_key));
        SignInModel.getInstance().mLoadWorkKey().finallyDo(new Action0() {
            @Override
            public void call() {
                LogUtils.d("wang","call dismissDialog");
                getView().getExpansion().dismissProgressDialog();
            }
        }).subscribe(new HwResponse<String>() {
            @Override
            protected void _onError(String message) {
                LogUtils.d("wang","pLoadWorkKey _OnError:"+message);
                JUtils.Toast(getView().getString(R.string.wrong_load_work_key));
            }

            @Override
            protected void _onNext(String message) {
                LogUtils.d("wang","pLoadWorkKey _onNext:"+message);
                getView().sendMsgGotoLifengSystem();
            }
        });
    }*/


    public void pPay(TransRequest transRequest){
        if (!CheckNetIsOk.isNetOk( getView())) {
            return;
        }
        getView().getExpansion().showProgressDialog(getView().getString(R.string.waiting));
        GoCashierModel.getInstance().mPay(transRequest)
                .finallyDo(new Action0() {
                    @Override
                    public void call() {
                        getView().getExpansion().dismissProgressDialog();
                    }
                })
                .subscribe(new ServiceResponse<TransResponse>() {
                    @Override
                    protected void _onError(String cause) {
                       /* Gson gson = new Gson();
                        String banckTransBefore = gson.toJson(transBean);
                        JUtils.Log( "保存交易错误超时或者没有返回的数据" + banckTransBefore);
                        FileUtils.writeFile(PosDataUtils.POS_DATA_FILE_PATH + "/" + PosDataUtils.POS_DATA_NAME_BEFORE,
                                banckTransBefore);*/

                        if(cause.contains(API.CODE.TIMEOUT)){
                            JUtils.ToastLong(API.CODE_MEG.TIMEOUT_MEG);
                        }else if (cause.contains(API.CODE.CONN_EXP)){
                            JUtils.ToastLong(API.CODE_MEG.CONN_EXP_MSG);
                        }else{
                            JUtils.ToastLong(cause);
                        }
                    }

                    @Override
                    protected void _onNext(TransResponse transResponse) {
                        JUtils.Log(" subscribe onNext " + Thread.currentThread().getName());


                        /*if("2".equals(transResponse.bank_type)){  //银行卡支付
                            DbManager.deleteTransDataTTB(transResponse.trace_no,transResponse.batch_no);
                        }*/

                        setSaveTransTime(transResponse);

                        if(getView().getPayType().equals(PayType.PAY_TYEP_CASH)){
                            getView().startResultActivity(transResponse);
                        }else {
                            getView().startOpenCrashHandActvity(transResponse);
                        }
                        getView().setPayType("-1");
                        //getView().sendMessage(transResponse);
                    }
                });
    }

    /**
     * 保存POS这个批次的第一个交易的时间
     * @param transResponse
     */
    private void setSaveTransTime(TransResponse transResponse) {
        String saveTransTime = JUtils.getSharedPreference().getString("saveTransTime","");
        if (StringUtil.isEmpty(saveTransTime) && StringUtil.isNotEmpty(transResponse.RESPONSE_TIME)) {
            JUtils.getSharedPreference().edit().putString("saveTransTime",transResponse.RESPONSE_TIME).apply();
        }
    }


}
