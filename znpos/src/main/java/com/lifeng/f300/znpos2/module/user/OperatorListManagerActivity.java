package com.lifeng.f300.znpos2.module.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.utils.JUtils;
import com.lifeng.beam.bijection.RequiresPresenter;
import com.lifeng.beam.expansion.list.BeamListActivity;
import com.lifeng.beam.expansion.list.ListConfig;
import com.lifeng.f300.common.contract.OnLoginDialogInterface;
import com.lifeng.f300.common.entites.OperatorInfoBrief;
import com.lifeng.f300.common.utils.LogUtils;
import com.lifeng.f300.config.SharePreferenceKey;
import com.lifeng.f300.znpos2.R;
import com.lifeng.f300.znpos2.view.AddOperatorDialog;
import com.lifeng.f300.znpos2.viewholder.OperatorListViewHolder;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by happen on 2017/8/28.
 */
@RequiresPresenter(OperatorListManagerPresenter.class)
public class OperatorListManagerActivity extends BeamListActivity<OperatorListManagerPresenter, OperatorInfoBrief> {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.recycler)
    EasyRecyclerView recycler;

    final static int MSG_ADD_OPERATOR = 1;  //添加操作人员

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";
            switch (menuItem.getItemId()) {
                case R.id.action_add_operator:
                    msg += "Click edit";
                    JUtils.Log("000000000000000");
                    loadAddOperator();
                    break;
            }

            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this, getWindow().getDecorView());
        registerReceiver(finishBroadcastReceiver, new IntentFilter("com.refresh.operators"));
        if(toolbar == null)LogUtils.d("wang","22");
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
    }

    @Override
    protected  void onDestroy(){
        super.onDestroy();
        unregisterReceiver(finishBroadcastReceiver);
    }

    @Override
    public BaseViewHolder<OperatorInfoBrief> getViewHolder(ViewGroup parent, int viewType) {
        return new OperatorListViewHolder(parent);
    }

    @Override
    public int getLayout() {
        return R.layout.operator_manager_layout;
    }

    @Override
    protected ListConfig getConfig() {
        return super.getConfig().setRefreshAble(false);
    }

    private BroadcastReceiver finishBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            JUtils.Log("onReceive");
            getPresenter().onRefresh();
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(JUtils.getSharedPreference().getString(SharePreferenceKey.OPETATOR_NUMBER,"").equals("99")){
            getMenuInflater().inflate(R.menu.menu_operator, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 添加操作员： 先检查输入的操作员是否已经存在，然后发送MSG_ADD_OPERATOR添加操作人员
     */
    private void loadAddOperator() {
        AddOperatorDialog.showDialog(true, this, R.drawable.f300_operator, getString(R.string.operator_manager),
                getString(R.string.add_operator), null, new OnLoginDialogInterface() {
                    @Override
                    public void onConfirmClick(EditText etUserName, EditText etOldPassword, EditText etPassword,
                                               EditText etPasswordSure) {
                        String operator = etUserName.getText().toString().trim();
                        String password = etPassword.getText().toString().trim();
                        String passwordSure = etPasswordSure.getText().toString().trim();
                        if (TextUtils.isEmpty(operator)) {
                            JUtils.Toast(getString(R.string.input_operatorId));
                            return;
                        }
                        if (TextUtils.isEmpty(password)) {
                            JUtils.Toast(getString(R.string.input_password));
                            return;
                        }
                        if (TextUtils.isEmpty(passwordSure)) {
                            JUtils.Toast(getString(R.string.input_password_sure));
                            return;
                        }
                        if (!password.equals(passwordSure)) {
                            etPassword.setText("");
                            etPasswordSure.setText("");
                            JUtils.Toast(getString(R.string.input_password_nosame));
                            return;
                        }
                        getPresenter().pQueryOperatorByOperator(new OperatorInfoBrief(operator,password));
                        AddOperatorDialog.closeDialog(); // 关闭添加框
                    }

                    @Override
                    public void onCancelClick() {
                    }

                    @Override
                    public void onSureClick() {
                    }
                });
    }


    void sendMsgAddOperator(OperatorInfoBrief operator){
        Message msg = Message.obtain();
        Bundle data = new Bundle();
        data.putSerializable("OperatorInfoBrief",operator);
        msg.setData(data);
        msg.what = MSG_ADD_OPERATOR;
        handler.sendMessage(msg);
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_ADD_OPERATOR:
                    Bundle data = msg.getData();
                    if (null != data) {
                        getPresenter().pAddOperator((OperatorInfoBrief) (data.getSerializable("OperatorInfoBrief")));
                    }
                    break;
                default:
                    break;
            }
        }
    };
/*    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle data = msg.getData();
            switch (msg.what) {
                case msg.setData(data);:
                    Bundle data = msg.getData();
                    if (null != data) {
                        if（getPresenter().pAddOperator()
                        *//*if(OperatorListManagerModel.getInstance().maddOperator(operator)>0){
                                JUtils.Toast(getView().getString(R.string.add_operator_ok));
                                //onRefresh();
                            }else {
                                JUtils.Toast(getView().getString(R.string.add_operator_no));
                            }*//*
                       // vloadMasterKey((TransResponse) (data.getSerializable("transResponse")));
                    }
                    break;
                default:
                    break;
            }
        }
    }*/

/*

    public void sendMessage(TransResponse transResponse) {
        Message msg = Message.obtain();
        Bundle data = new Bundle();
        data.putSerializable("transResponse", transResponse);
        msg.setData(data);
        msg.what = MSG_LOADMASTERKEY;

        handler.sendMessage(msg);
    }

    ;*/
}
