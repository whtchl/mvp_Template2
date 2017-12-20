package com.lifeng.testlib;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.tchl.server3.http.apitest.model.GankModel;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @InjectView(R.id.btn_server3)
    Button btnServer3;
    @InjectView(R.id.btn_getRecentlyGanHuo)
    Button btnGetRecentlyGanHuo;
    @InjectView(R.id.btn_sms)
    Button btnSms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.btn_server3, R.id.btn_getRecentlyGanHuo,R.id.btn_sms})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_server3:
                GankModel.loadData(1);
                break;
            case R.id.btn_getRecentlyGanHuo:
                GankModel.mgetRecentlyGanHuo();
                break;
            case R.id.btn_sms:
                GankModel.mSms();
                break;
        }
    }

   /* @OnClick(R.id.btn_server3)
    public void onClick() {
        GankModel.loadData(1);
    }*/
}
