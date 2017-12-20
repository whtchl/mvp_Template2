package com.lifeng.f300.znpos2.viewholder;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.utils.JUtils;
import com.lifeng.f300.common.contract.OnLoginDialogInterface;
import com.lifeng.f300.common.entites.OperatorInfoBrief;
import com.lifeng.f300.common.widget.AllDialog;
import com.lifeng.f300.config.SharePreferenceKey;
import com.lifeng.f300.model.OperatorListViewHolderModel;
import com.lifeng.f300.znpos2.R;
import com.lifeng.f300.znpos2.view.OperatorDialog;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by happen on 2017/8/28.
 */

public class OperatorListViewHolder extends BaseViewHolder<OperatorInfoBrief> {
    @InjectView(R.id.operator_id)
    TextView operatorId;
    @InjectView(R.id.update_operator)
    Button updateOperator;
    @InjectView(R.id.delete_operator)
    Button deleteOperator;
    @InjectView(R.id.operator_pwd)
    TextView operatorPwd;
    @InjectView(R.id.index)
    TextView index;

    //当前登录的操作员
    String currentOperator="";

    public OperatorListViewHolder(ViewGroup parent) {
        super(parent, R.layout.operator_item);
        ButterKnife.inject(this, itemView);
        updateOperator.setOnClickListener(v -> updatePwd());
        deleteOperator.setOnClickListener(v->deleteOperator());
        itemView.setOnClickListener(v -> {
            JUtils.Log("OperatorListViewHolder  itemView click");
        });
        currentOperator = JUtils.getSharedPreference().getString(SharePreferenceKey.OPETATOR_NUMBER,"");

    }

    private void deleteOperator() {
        AllDialog.showDialog(getContext() ,R.style.CustomProgressDialog, getContext().getString(R.string.common_hint),
                getContext().getString(R.string.isdelete_operator) + operatorId.getText().toString()+ getContext().getString(R.string.wh),
                0, R.layout.network_error_vip, new OnLoginDialogInterface() {
            @Override
            public void onConfirmClick(EditText etUserName, EditText etOldPassword, EditText etPassword,
                                       EditText etPasswordSure) {
            }

            @Override
            public void onCancelClick() {
                AllDialog.closeDialog();
            }

            @Override
            public void onSureClick() {
                AllDialog.closeDialog();
                if(OperatorListViewHolderModel.getInstance().mDeleteOperator(operatorId.getText().toString())>0){
                    JUtils.Toast(getContext().getString(R.string.delete_operator_ok));
                    //setData(new OperatorInfoBrief(6,"06","1234"));
                    Intent intent = new Intent();
                    intent.setAction("com.refresh.operators");
                    getContext().sendBroadcast(intent);


                }else{
                    JUtils.Toast(getContext().getString(R.string.delete_operator_no));
                }

            }
        });

    }

    private void updatePwd() {
        OperatorDialog.showDialog(false, getContext(), R.drawable.f300_operator, getContext().getString(R.string.operator_manager),
                getContext().getString(R.string.update_operator), operatorId.getText().toString(), new OnLoginDialogInterface() {
                    @Override
                    public void onConfirmClick(EditText etUserName, EditText etOldPassword, EditText etPassword, EditText etPasswordSure) {
                        String password = etPassword.getText().toString().trim();
                        String oldPassword = etOldPassword.getText().toString().trim();
                        String passwordSure = etPasswordSure.getText().toString().trim();
                        if (TextUtils.isEmpty(oldPassword)) {
                            JUtils.Toast(getContext().getString(R.string.input_password_old));
                            return;
                        }
                        if (TextUtils.isEmpty(password)) {
                            JUtils.Toast(getContext().getString(R.string.input_password_new));
                            return;
                        }
                        if (TextUtils.isEmpty(passwordSure)) {
                            JUtils.Toast(getContext().getString(R.string.input_password_sure));
                            return;
                        }
                        if (!password.equals(passwordSure)) {
                            etPassword.setText("");
                            etPasswordSure.setText("");
                            JUtils.Toast(getContext().getString(R.string.input_password_nosame));
                            return;
                        }
                        //modify database
                        JUtils.Log("oldPwd:" + operatorPwd.getText().toString().trim() + " oldPassword  " + oldPassword);
                        if (!operatorPwd.getText().toString().trim().equals(oldPassword)) {
                            etOldPassword.setText("");
                            JUtils.Toast(getContext().getString(R.string.password_wrong));
                            return;
                        }
                        if (OperatorListViewHolderModel.getInstance().mUpdateOperatorPwd(index.getText().toString().trim(), etUserName.getText().toString().trim(), passwordSure)>0) {
                            JUtils.Toast(getContext().getString(R.string.update_operator_ok));

                       /*     SignInModel.getInstance().mQueryOperators(JUtils.getSharedPreference().getString("operatorId","")).unsafeSubscribe
                                    (
                                            getContext().
                                            getRefreshSubscriber()
                                    );*/
                        } else {
                            JUtils.Toast(getContext().getString(R.string.update_operator_no));
                        }
                        OperatorDialog.closeDialog(); // 关闭修改框
                    }

                    @Override
                    public void onCancelClick() {

                    }

                    @Override
                    public void onSureClick() {

                    }
                });
    }

    @Override
    public void setData(OperatorInfoBrief data) {
        operatorId.setText(data.getOperatorId());
        operatorPwd.setText(data.getPassword());
        index.setText( String.valueOf(data.getId()));

        if(currentOperator.equals("99")){
            if(data.getOperatorId().equals("99")){
                JUtils.Log("set invisible delete button:"+data.getOperatorId());
                deleteOperator.setVisibility(View.INVISIBLE);
            }


        }
        else {
            deleteOperator.setVisibility(View.INVISIBLE);
            //JUtils.Toast(getContext().getString(R.string.sigin_again));
        }



        JUtils.Log("pwd:" + data.getPassword() + " operator:" + data.getPassword() + " index:" + data.getId());
    }

}
