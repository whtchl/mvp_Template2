package com.lifeng.f300.znpos2.module.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lifeng.beam.bijection.RequiresPresenter;
import com.lifeng.beam.expansion.BeamBaseActivity;
import com.lifeng.f300.znpos2.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by happen on 2017/8/25.
 */
@RequiresPresenter(OperatorManagerPresenter.class)
public class OperatorManagerActivity extends BeamBaseActivity<OperatorManagerPresenter> {
    @InjectView(R.id.operators)
    TextView operators;
    @InjectView(R.id.reset_again)
    TextView resetAgain;
    @InjectView(R.id.print_set)
    TextView printSet;
    @InjectView(R.id.aid_down)
    TextView aidDown;
    @InjectView(R.id.center_businesses_info)
    TextView centerBusinessesInfo;
    @InjectView(R.id.about_us)
    TextView aboutUs;
    @InjectView(R.id.exit)
    TextView exit;
    @InjectView(R.id.container)
    LinearLayout container;


    //private Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        setContentView(R.layout.operator_manager);
        ButterKnife.inject(this);
    }
    private void initData() {
    }
    private void initEvent() {
        centerBusinessesInfo.setOnClickListener(v -> sjzx());
        aboutUs.setOnClickListener(v -> aboutus());
        operators.setOnClickListener(v->czygl());
    }

    //操作员管理
    private void czygl() {
        Intent intent = new Intent(this,OperatorListManagerActivity.class);
        startActivity(intent);
    }

    /*商家中心*/
    private void sjzx() {
        Intent intent = new Intent(this,BusinessCenterActivity.class);
        startActivity(intent);
    }

    /*关于我们*/
    private void aboutus() {
        Intent intent = new Intent(OperatorManagerActivity.this, AboutUsActivity.class);
        startActivity(intent);
    }

}
