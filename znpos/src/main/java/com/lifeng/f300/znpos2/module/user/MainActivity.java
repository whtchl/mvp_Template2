package com.lifeng.f300.znpos2.module.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jude.utils.JUtils;
import com.lifeng.beam.bijection.RequiresPresenter;
import com.lifeng.beam.expansion.BeamBaseActivity;
import com.lifeng.f300.znpos2.R;

import butterknife.ButterKnife;

/**
 * Created by happen on 2017/8/25.
 */
@RequiresPresenter(MainPresenter.class)
public class MainActivity extends BeamBaseActivity<MainPresenter>  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mains_layout2);
        ButterKnife.inject(this);

    }

    /**
     * 设置
     *
     * @param view
     */
    public void setting(View view) {
        Intent intent = new Intent(this, OperatorManagerActivity.class);
        startActivity(intent);
    }


    public void IvBtnCashier(View view){
        Intent intent = new Intent(this,GoCashierActivity.class);
        startActivity(intent);
    }

    /**
     * 交易流水
     * @param view
     */
    public void DealFlow(View view){
        Intent intent = new Intent(this, LocalTransRecordActivity.class);
        startActivity(intent);
    }

    /**
     * 保险
     */
    public void Bx(View view){
        JUtils.ToastLong(getString(R.string.not_support));
    }

    /**
     * 彩票
     */
    public void Cp(View view){
        JUtils.ToastLong(getString(R.string.not_support));
    }
}
