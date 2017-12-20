package com.lifeng.f300.znpos2.demo;

import android.widget.EditText;

import com.jude.utils.JUtils;
import com.lifeng.beam.bijection.Presenter;
import com.lifeng.f300.common.contract.OnLoginDialogInterface;
import com.lifeng.f300.common.entites.BankEntity;
import com.lifeng.f300.common.widget.BankCarshDialog;
import com.lifeng.f300.hardware.common.Hw;
import com.lifeng.f300.hardware.common.HwResponse;
import com.lifeng.f300.model.GoCashierModel;
import com.lifeng.f300.znpos2.R;

import rx.Subscription;
import rx.functions.Action0;

/**
 * Created by happen on 2017/10/25.
 */

public class ReadBankInfoPresenter  extends Presenter<ReadBankInfoActivity> {
    /**
     * 读取银行卡数据
     */
    public void pReadBankInfo(){
            Subscription subscription =  GoCashierModel.getInstance().mReadBankInfo()
                    .finallyDo(new Action0() {
                        @Override
                        public void call() {
                            JUtils.Log("pLoadCrashbankCard "+Thread.currentThread().getName().toString());
                            BankCarshDialog.closeDialog();
                        }
                    })
                    .subscribe(new HwResponse<BankEntity>() {
                        @Override
                        protected void _onError(String message) {
                            JUtils.Log("pLoadCrashbankCard _OnError:" + message + Thread.currentThread().getName().toString());
                        }

                        @Override
                        protected void _onNext(BankEntity bankEntity) {
                            JUtils.Log("pLoadCrashbankCard _onNext:"+Thread.currentThread().getName().toString());
                            JUtils.ToastLong(bankEntity.getPAN()+"  "+bankEntity.CARDDATE);

                        }
                    });
            BankCarshDialog.showDialog(getView(), R.style.CustomProgressDialog, R.layout.cashier_details51, "0",false,false, new OnLoginDialogInterface() {
                @Override
                public void onConfirmClick(EditText etUserName, EditText etOldPassword,
                                           EditText etPassword, EditText etPasswordSure) {
                }

                @Override
                public void onCancelClick() {
                    subscription.unsubscribe();
                    BankCarshDialog.closeDialog();
                    Hw.getInstance().closeReadBankCard();
                }

                @Override
                public void onSureClick() {
                }
            });
    }
}
