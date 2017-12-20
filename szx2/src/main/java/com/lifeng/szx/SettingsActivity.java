package com.lifeng.szx;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jude.utils.JUtils;
import com.lifeng.f300.common.utils.LogUtils;

import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity {


    Toolbar toolbar2;
    EditText etThird;
    EditText etMchcode;
    Button btnSave;
    TextView showVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.inject(this);
        toolbar2 = (Toolbar) this.findViewById(R.id.toolbar2);
        etMchcode = (EditText) this.findViewById(R.id.et_mchcode);
        etThird = (EditText)this.findViewById(R.id.et_third);
        btnSave = (Button)this.findViewById(R.id.btn_save);
        showVersion = (TextView)this.findViewById(R.id.et_version);
        setSupportActionBar(toolbar2);

        toolbar2.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setVersion();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(etThird.getText().toString().trim()) ||
                        TextUtils.isEmpty(etMchcode.getText().toString().trim())){
                    JUtils.ToastLong(getString(R.string.remind1));
                    return;
                }
                JUtils.getSharedPreference().edit().putString("sp_third",etThird.getText().toString().trim()).apply();
                JUtils.getSharedPreference().edit().putString("sp_mchcode",etMchcode.getText().toString().trim()).apply();
                JUtils.Toast(getString(R.string.save_success));
                onBackPressed();
            }
        });
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
        showVersion.setText(getString(R.string.app_name)+" "+version);
    }

/*    @OnClick(R.id.btn_save)
    public void onClick() {
        Log.d("wang","23223");
        if(TextUtils.isEmpty(etThird.getText().toString()) ||
                TextUtils.isEmpty(etMchcode.getText().toString())){
            Log.d("wang","233");
            JUtils.ToastLong(getString(R.string.remind1));
            return;
        }
        JUtils.getSharedPreference().edit().putString("sp_third",etThird.getText().toString()).apply();
        JUtils.getSharedPreference().edit().putString("sp_mchcode",etMchcode.getText().toString()).apply();
        JUtils.Toast(getString(R.string.save_success));
    }*/
}
