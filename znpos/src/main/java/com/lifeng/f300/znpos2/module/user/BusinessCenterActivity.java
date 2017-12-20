package com.lifeng.f300.znpos2.module.user;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.utils.JUtils;
import com.lifeng.beam.bijection.RequiresPresenter;
import com.lifeng.beam.expansion.BeamBaseActivity;
import com.lifeng.f300.common.utils.DeviceUtils;
import com.lifeng.f300.common.utils.LogUtils;
import com.lifeng.f300.common.utils.MyAsynaTask;
import com.lifeng.f300.znpos2.R;
import com.lifeng.f300.znpos2.utils.MerchantRegisterDataManager;
import com.lifeng.f300.znpos2.utils.MerchantSignDataManager;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by happen on 2017/8/21.
 */
@RequiresPresenter(BusinessCenterPresenter.class)
public class BusinessCenterActivity extends BeamBaseActivity<BusinessCenterPresenter> {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.company_image)
    ImageView companyImage;
    @InjectView(R.id.merchant_name)
    TextView merchantName;
    @InjectView(R.id.pos_name)
    TextView posName;
    @InjectView(R.id.merchant_id)
    TextView merchantId;
    @InjectView(R.id.terminal_id)
    TextView terminalId;
    @InjectView(R.id.sn_id)
    TextView snId;
    @InjectView(R.id.area_address)
    TextView areaAddress;
    @InjectView(R.id.system_version)
    TextView systemVersion;


    private MerchantRegisterDataManager.RegisterData registerData;
    private MyAsynaTask mTask;
    private MerchantSignDataManager.SignData signData;

    Context mContext;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();

    }

    private void initView() {
        setContentView(R.layout.business_center);
        ButterKnife.inject(this);
    }

    private void initData() {
        mContext = this;
        registerData = MerchantRegisterDataManager.getInstance().getRegisterData();
        signData = MerchantSignDataManager.getInstance().getSignData();
        if(registerData != null){
            terminalId.setText(registerData.POSCODE);
            merchantId.setText(registerData.MERCHANT_CODE);
        }

        setVersion();
        setCompanyImage();
        snId.setText(DeviceUtils.getDeviceSn(mContext));
        if (null != signData) {
            JUtils.Log("signdata is not null:"+ signData.POSNAME);
            merchantName.setText(signData.MERCHANTNAME);
            posName.setText(signData.POSNAME);
            areaAddress.setText(signData.AREA_DESC);
        }else{
            JUtils.Log("signdata is null:"+ registerData.AREA_DESC);
            merchantName.setText(registerData.MERCHANTNAME);
            posName.setText(getString(R.string.no_pos_name));
            areaAddress.setText(registerData.AREA_DESC);
        }

    }

    private void setCompanyImage() {
        if (!TextUtils.isEmpty(registerData.LOGO)) {
            mTask = new MyAsynaTask(companyImage,null,null,R.drawable.f300logo);
            mTask.execute(registerData.LOGO);
        }else{
            companyImage.setImageResource(R.drawable.f300logo);
        }
    }


    private void setVersion(){
        PackageInfo packInfo = null;
        try {
            packInfo =getPackageManager().getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version=packInfo.versionName;
        LogUtils.d("wang","version:"+version);
        systemVersion.setText(getString(R.string.app_name)+" "+version);
    }
}
