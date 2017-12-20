package com.lifeng.szx;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jude.utils.JUtils;
import com.lifeng.beam.Utils;
import com.lifeng.beam.bijection.RequiresPresenter;
import com.lifeng.beam.expansion.BeamBaseActivity;
import com.lifeng.f300.common.utils.StringUtil;
import com.lifeng.f300.common.utils.ToastMakeUtils;
import com.lifeng.szx.ui.BaActivity;
import com.lifeng.szx.ui.LpcxActivity;


@RequiresPresenter(MainPresenter.class)
public class MainActivity extends BeamBaseActivity<MainPresenter> {



    Toolbar toolbar1;
    TextView tv,tv_content,tv2;
    TextView tv_ba,tv_lpcx;
    private Dialog dialogFood;
    private EditText foodPhoneEt,moneyEt,nameEt;

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";
            switch (menuItem.getItemId()) {
                case R.id.setting2:
                    //inputCancelTransPwd();
                    //   JUtils.ToastLong("cancel trans");
                    Intent  intent = new Intent(MainActivity.this,SettingsActivity.class);
                    startActivity(intent);
                    break;
                case R.id.launcher:
                    gotoOriLauncher();
                    break;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();



    }
    private void initView() {
        setContentView(R.layout.activity_main);
        tv = (TextView)this.findViewById(R.id.tv);
        toolbar1 = (Toolbar)this.findViewById(R.id.toolbar1);
        tv_content = (TextView)this.findViewById(R.id.tv_content);
        tv_lpcx =  (TextView)this.findViewById(R.id.tv_lpcx);
        tv_ba =  (TextView)this.findViewById(R.id.tv_ba);
        //*ButterKnife.inject(this);
        setSupportActionBar(toolbar1);

        toolbar1.setOnMenuItemClickListener(onMenuItemClick);
        //toolbar1.setNavigationIcon(R.drawable.back_jiantou);

        toolbar1.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadInputFoodPhone();
            }
        });


        tv_ba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,BaActivity.class);
                startActivity(intent);
            }
        });

        tv_lpcx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,LpcxActivity.class);
                startActivity(intent);
                //JUtils.ToastLong("暂不支持该功能");
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadInputFoodPhone();
    }

    private void loadInputFoodPhone() {
        closeDialog();
        dialogFood = Utils.setDialog(this, R.layout.food_input_phone, 0.9, 0);
        foodPhoneEt = (EditText) dialogFood.findViewById(R.id.input_food_phone);
        moneyEt = (EditText) dialogFood.findViewById(R.id.input_money);
        nameEt = (EditText)dialogFood.findViewById(R.id.input_name);
        tv2 = (TextView)dialogFood.findViewById(R.id.tv2);
        tv2.setText(Html.fromHtml(getString(R.string.food_info_customer3)));
        dialogFood.findViewById(R.id.close_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != dialogFood) {
                    dialogFood.dismiss();
                    dialogFood = null;
                }
                //scanError(getString(R.string.food_bd_cancel));
            }
        });
        dialogFood.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != dialogFood) {
                    dialogFood.dismiss();
                    dialogFood = null;
                }
                gotoXdjk2();
            }
        });
        dialogFood.findViewById(R.id.submit_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = foodPhoneEt.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    ToastMakeUtils.showToast(MainActivity.this, getString(R.string.input_phone));
                    return;
                }

                if (!StringUtil.isMobileNo(phone)) {
                    ToastMakeUtils.showToast(MainActivity.this, getString(R.string.phone_error));
                    foodPhoneEt.setText("");
                    return;
                }

                /*if(TextUtils.isEmpty(moneyEt.getText().toString().trim())){
                    JUtils.ToastLong(getString(R.string.input_money));
                    return;
                }

                if(TextUtils.isEmpty(nameEt.getText().toString().trim())){
                    JUtils.ToastLong(getString(R.string.input_name));
                    return;
                }*/

                closeDialog();
                submitFoodInfo(phone,nameEt.getText().toString().trim(),moneyEt.getText().toString().trim());
            }
        });
        dialogFood.show();
    }

    private void submitFoodInfo(String phone,String money,String name) {

        getPresenter().pSubmitFoodInfo(phone,money,name);
    }

    private void closeDialog() {
        if (dialogFood != null) {
            dialogFood.dismiss();
            dialogFood = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void gotoXdjk(){
        //Intent intent  = new Intent(this,"com.xdjk.smartpos.activity.LoginActivity")
        // 通过包名获取要跳转的app，创建intent对象
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.xdjk.smartpos");
        // 这里如果intent为空，就说名没有安装要跳转的应用嘛
        if (intent != null) {
            // 这里跟Activity传递参数一样的嘛，不要担心怎么传递参数，还有接收参数也是跟Activity和Activity传参数一样
           /* intent.putExtra("name", "Liu xiang");
            intent.putExtra("birthday", "1983-7-13");*/
            startActivity(intent);
        } else {
            // 没有安装要跳转的app应用，提醒一下
            //Toast.makeText(getApplicationContext(), "哟，赶紧下载安装这个APP吧", Toast.LENGTH_LONG).show();
            JUtils.ToastLong(getString(R.string.xdjkapp));
        }

    }


    public void gotoXdjk2(){
        //com.xdjk.smartpos.activity.LoginActivity
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        // 设置ComponentName参数1:packagename参数2:MainActivity路径
        ComponentName cn = new ComponentName("com.xdjk.smartpos", "com.xdjk.smartpos.activity.LoginActivity");
        intent.setComponent(cn);
        startActivity(intent);

    }

   /* public void gotoOriLauncher(){
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.android.launcher");
        //com.android.launcher2.Launcher
        Log.i("wang","gotoOriluancher");
        if (intent != null) {
            startActivity(intent);
        }else{
            Log.i("wang","not launcher");
        }
    }*/

    public void gotoOriLauncher(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        // 设置ComponentName参数1:packagename参数2:MainActivity路径
        ComponentName cn = new ComponentName("com.android.launcher", "com.android.launcher2.Launcher");

        intent.setComponent(cn);
        startActivity(intent);
    }
}
