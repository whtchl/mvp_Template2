package com.lifeng.f300.znpos2.module.user;

import android.widget.EditText;

import com.jude.utils.JUtils;
import com.lifeng.beam.bijection.Presenter;
import com.lifeng.f300.common.contract.OnLoginDialogInterface;
import com.lifeng.f300.common.entites.BankEntity;
import com.lifeng.f300.common.entites.CancelTransBean;
import com.lifeng.f300.common.entites.CancelTransResponBean;
import com.lifeng.f300.common.utils.CheckNetIsOk;
import com.lifeng.f300.common.widget.BankCarshDialog;
import com.lifeng.f300.config.API;
import com.lifeng.f300.hardware.common.HwResponse;
import com.lifeng.f300.hardware.common.Hw;
import com.lifeng.f300.model.CancelTransModel;
import com.lifeng.f300.server.service.ServiceResponse;
import com.lifeng.f300.znpos2.R;

import rx.Subscription;
import rx.functions.Action0;

/**
 * Created by happen on 2017/9/7.
 */

public class CancelTransPresenter extends Presenter<CancelTransActivity> {



    /**
     * 刷银行卡
     *
     * @param transAmt
     */
    public void ploadCrashBank(final String transAmt) {
        // getView().getExpansion().showCrashBankCardDialog(transAmt);
        //final Subscription subscription;


        Subscription subscription = CancelTransModel.getInstance().mLoadCrashbankCard(transAmt)
                .finallyDo(new Action0() {
                    @Override
                    public void call() {
                        //getView().getExpansion().dismissCrashBankCardDialog(transAmt);
                        JUtils.Log("ploadCrashBank "+Thread.currentThread().getName().toString());
                        BankCarshDialog.closeDialog();
                    }
                })
                .subscribe(new HwResponse<BankEntity>() {
                    @Override
                    protected void _onError(String message) {
                        JUtils.Log("ploadCrashBank _OnError:" + message + Thread.currentThread().getName().toString());
                        JUtils.Toast("!!!!!!!!!!!!");
                    }

                    @Override
                    protected void _onNext(BankEntity bankEntity) {
                        JUtils.Log("ploadCrashBank _onNext:"+Thread.currentThread().getName().toString());
                        getView().sendMsgCancelTrans(bankEntity);
                    }
                });
        BankCarshDialog.showDialog(getView(), R.style.CustomProgressDialog, R.layout.cashier_details51, transAmt,false,false, new OnLoginDialogInterface() {
            @Override
            public void onConfirmClick(EditText etUserName, EditText etOldPassword,
                                       EditText etPassword, EditText etPasswordSure) {
            }

            @Override
            public void onCancelClick() {
                if(null != subscription ) {
                    subscription.unsubscribe();
                }
                BankCarshDialog.closeDialog();
                Hw.getInstance().closeReadBankCard();
            }

            @Override
            public void onSureClick() {
            }
        });
    }




    public void pSendCancelRequest(CancelTransBean cancelTransBean){
        if (!CheckNetIsOk.isNetOk( getView())) {
            return;
        }
        getView().getExpansion().showProgressDialog(getView().getString(R.string.waiting));
        CancelTransModel.getInstance().mSendCancelRequest(cancelTransBean)
                .finallyDo(new Action0() {
                    @Override
                    public void call() {
                        getView().getExpansion().dismissProgressDialog();
                    }
                })
                .subscribe(new ServiceResponse<CancelTransResponBean>() {
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
                    protected void _onNext(CancelTransResponBean cancelTransResponBean) {
                        JUtils.Log(" subscribe onNext " + Thread.currentThread().getName());
                        getView().updateTransCancel(cancelTransResponBean);
                    /*    if(getView().getPayType().equals(PayType.PAY_TYEP_CASH)){
                            getView().startResultActivity(transResponse);
                        }else {
                            getView().startOpenCrashHandActvity(transResponse);
                        }
                        getView().setPayType("-1");*/
                        //getView().sendMessage(transResponse);
                    }
                });
    }
}
