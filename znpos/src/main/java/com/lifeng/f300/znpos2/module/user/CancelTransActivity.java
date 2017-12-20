package com.lifeng.f300.znpos2.module.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;

import com.jude.utils.JUtils;
import com.lifeng.beam.bijection.RequiresPresenter;
import com.lifeng.beam.expansion.BeamBaseActivity;
import com.lifeng.f300.common.entites.BankEntity;
import com.lifeng.f300.common.entites.CancelTransBean;
import com.lifeng.f300.common.entites.CancelTransResponBean;
import com.lifeng.f300.common.entites.TransResponse;
import com.lifeng.f300.znpos2.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by happen on 2017/9/7.
 */
@RequiresPresenter(CancelTransPresenter.class)
public class CancelTransActivity extends BeamBaseActivity<CancelTransPresenter> {


    private final static int MSG_CANCEL_TANS = 1;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    private TransResponse transResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
    }

    private void initData() {
        if (getIntent().getExtras().get("result") != null) {
            transResponse = (TransResponse) getIntent().getExtras().get("result");
        }
        setChangeView(transResponse);
        //updateTransCancel(null);
    }

    private void initView() {
        setContentView(R.layout.cancel_trans_layout);
        ButterKnife.inject(this);
    }

    private void setChangeView(TransResponse transResponse) {

        loadCrashBank("1000");

    }

    //刷银行卡，进行撤销交易
    private void loadCrashBank(final String amt) {
        getPresenter().ploadCrashBank(amt);
    }


    //从pos获得BankEntity后，链接服务器进行撤销交易
    public void sendMsgCancelTrans(BankEntity bankEntity){
        Message msg = Message.obtain();
        msg.what = MSG_CANCEL_TANS;
        Bundle data = new Bundle();
        data.putSerializable("bankEntity",bankEntity);
        msg.setData(data);
        handler.sendMessage(msg);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CANCEL_TANS:
                    CancelTransBean cancelTransBean = new CancelTransBean();
                    getPresenter().pSendCancelRequest(cancelTransBean);
                    break;

                default:
                    break;
            }
        }
    };

    public void updateTransCancel(CancelTransResponBean cancelTransResponBean){
        JUtils.Log("updateTransCancel");
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();

    }


}
