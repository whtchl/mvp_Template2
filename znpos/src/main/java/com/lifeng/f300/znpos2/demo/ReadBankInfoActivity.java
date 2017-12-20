package com.lifeng.f300.znpos2.demo;

import android.os.Bundle;
import android.widget.Button;

import com.lifeng.beam.bijection.RequiresPresenter;
import com.lifeng.beam.expansion.BeamBaseActivity;
import com.lifeng.f300.znpos2.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by happen on 2017/10/25.
 */
@RequiresPresenter(ReadBankInfoPresenter.class)
public class ReadBankInfoActivity extends BeamBaseActivity<ReadBankInfoPresenter> {

    @InjectView(R.id.btn_readbankinfo)
    Button btnReadbankinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.bank_read_info);
        ButterKnife.inject(this);

        btnReadbankinfo.setOnClickListener(v -> ReadBankInfo());
    }

    private void ReadBankInfo(){
        getPresenter().pReadBankInfo();
    }
}
