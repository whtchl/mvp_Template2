package com.lifeng.szx.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jude.utils.JUtils;
import com.lifeng.beam.bijection.RequiresPresenter;
import com.lifeng.beam.expansion.BeamBaseActivity;
import com.lifeng.f300.common.entites.LpResponse;
import com.lifeng.szx.R;
import com.lifeng.szx.entity.Images;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import szx.entites.SzxTransResponse;

/**
 * Created by happen on 2017/12/6.
 */
@RequiresPresenter(LpDetailPresenter.class)
public class LpDetailActivity extends BeamBaseActivity<LpDetailPresenter> {
    @InjectView(R.id.toolbar_lp_detail)
    Toolbar toolbarLpDetail;
    @InjectView(R.id.tv_address)
    TextView tvAddress;
    @InjectView(R.id.tv_report_time)
    TextView tvReportTime;
    @InjectView(R.id.tv_informant)
    TextView tvInformant;
    @InjectView(R.id.tv_informant_phone)
    TextView tvInformantPhone;
    @InjectView(R.id.tv_informant_open_id)
    TextView tvInformantOpenId;
    @InjectView(R.id.tv_cause)
    TextView tvCause;
    @InjectView(R.id.tv_report_money)
    TextView tvReportMoney;
    @InjectView(R.id.tv_happen_time)
    TextView tvHappenTime;
    @InjectView(R.id.tv_policy_code)
    TextView tvPolicyCode;
    @InjectView(R.id.tv_paid_money)
    TextView tvPaidMoney;
    @InjectView(R.id.tv_status)
    TextView tvStatus;
    @InjectView(R.id.tv_close_time)
    TextView tvCloseTime;
    @InjectView(R.id.tv_close_money)
    TextView tvCloseMoney;
    @InjectView(R.id.tv_remark)
    TextView tvRemark;


    String third = "", mchcode = "";
    String id;
    @InjectView(R.id.iv_scene)
    ImageView ivScene;
    @InjectView(R.id.iv_paid)
    ImageView ivPaid;
    @InjectView(R.id.iv_shakes)
    ImageView ivShakes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lp_detail);
        ButterKnife.inject(this);
        initEvent();
        initData();

    }

    private void initData() {
        if (getIntent().getSerializableExtra("id") != null) {
            id = getIntent().getStringExtra("id");
        }
    }

    private void initEvent() {
        setSupportActionBar(toolbarLpDetail);
        toolbarLpDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        third = JUtils.getSharedPreference().getString("sp_third", "");
        mchcode = JUtils.getSharedPreference().getString("sp_mchcode", "");

        if (TextUtils.isEmpty(third)
                || TextUtils.isEmpty(mchcode)) {
            JUtils.ToastLong("请设置支付公司标识或商户号");
        } else {
            //查询列表
            getPresenter().pQueryLpDetail(id, third, mchcode);
        }

    }

    public void UpdateDetail(SzxTransResponse mSzxTransResponse) {

        LpResponse lpResponse1 = mSzxTransResponse.data.get(0);
        //JSONObject jsStr =  JSONObject
        //JSONObject jsonObject=JSONObject.fromObject(mSzxTransResponse.data.get(0).report_imgs);

        Gson gson = new Gson();
        Images im = new Images();
        if(!TextUtils.isEmpty(mSzxTransResponse.data.get(0).report_imgs)){
            Log.i("wang", mSzxTransResponse.data.get(0).report_imgs);
            im = gson.fromJson(mSzxTransResponse.data.get(0).report_imgs, Images.class);
            Log.i("wang", "im:" + im.scene);
        }

        if (lpResponse1 != null) {
            tvAddress.setText(lpResponse1.address);
            tvReportTime.setText(lpResponse1.report_time);
            tvInformant.setText(lpResponse1.informant);
            tvInformantPhone.setText(lpResponse1.informant_phone);
            tvInformantOpenId.setText(lpResponse1.informant_open_id);
            tvCause.setText(lpResponse1.cause);
            if (!TextUtils.isEmpty(lpResponse1.report_money)) {
                tvReportMoney.setText(lpResponse1.report_money + "元");
            }
            tvHappenTime.setText(lpResponse1.happen_time);
            tvPolicyCode.setText(lpResponse1.policy_code);
            if (!TextUtils.isEmpty(lpResponse1.paid_money)) {
                tvPaidMoney.setText(lpResponse1.paid_money + "元");
            }

            if (lpResponse1.status.equals("1")) {
                tvStatus.setText(getString(R.string.status1));
            } else if (lpResponse1.status.equals("3")) {
                tvStatus.setText(getString(R.string.status3));
            } else if (lpResponse1.status.equals("5")) {
                tvStatus.setText(getString(R.string.status5));
            } else if (lpResponse1.status.equals("7")) {
                tvStatus.setText(getString(R.string.status7));
            } else if (lpResponse1.status.equals("9")) {
                tvStatus.setText(getString(R.string.status9));
            } else if (lpResponse1.status.equals("11")) {
                tvStatus.setText(getString(R.string.status11));
            }


            tvCloseTime.setText(lpResponse1.close_time);
            if (!TextUtils.isEmpty(lpResponse1.close_money)) {
                tvCloseMoney.setText(lpResponse1.close_money + "元");
            }
            tvRemark.setText(lpResponse1.remark);
            if(!TextUtils.isEmpty(im.scene)){
                Picasso.with(LpDetailActivity.this).load(im.scene).into(ivScene);

            }else{
                Picasso.with(LpDetailActivity.this).load(R.drawable.circle_image_fail_round).into(ivScene);
            }
            if(!TextUtils.isEmpty(im.shake)){
                Picasso.with(LpDetailActivity.this).load(im.shake).into(ivShakes);

            }else{
                Picasso.with(LpDetailActivity.this).load(R.drawable.circle_image_fail_round).into(ivShakes);
            }
            if(!TextUtils.isEmpty(im.paid)){
                Picasso.with(LpDetailActivity.this).load(im.paid).into(ivPaid);

            }else{
                Picasso.with(LpDetailActivity.this).load(R.drawable.circle_image_fail_round).into(ivPaid);
            }
        }




    }
}
