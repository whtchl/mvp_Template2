package com.lifeng.f300.znpos2.module.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.utils.JUtils;
import com.lifeng.beam.bijection.RequiresPresenter;
import com.lifeng.beam.expansion.BeamBaseActivity;
import com.lifeng.f300.common.entites.TransResponse;
import com.lifeng.f300.common.utils.FileUtils;
import com.lifeng.f300.common.utils.LogUtils;
import com.lifeng.f300.common.utils.PosDataUtils;
import com.lifeng.f300.common.utils.ToastMakeUtils;
import com.lifeng.f300.config.ConnectURL;
import com.lifeng.f300.config.IPPORT;
import com.lifeng.f300.config.IntentActions;
import com.lifeng.f300.znpos2.R;
import com.lifeng.f300.znpos2.utils.MerchantRegisterDataManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * pos 初始化类
 * Created by happen on 2017/8/4.
 */
@RequiresPresenter(LoginPresenter.class)
public class LoginActivity extends BeamBaseActivity<LoginPresenter> {

    @InjectView(R.id.iv_YesOrNO)
    ImageView ivYesOrNO;
    @InjectView(R.id.et_jhm)
    EditText etJhm;
    @InjectView(R.id.iv_jhm_yesorno)
    ImageView ivJhmYesorno;
    @InjectView(R.id.iv_read)
    ImageView ivRead;
    @InjectView(R.id.tv_service_agree)
    TextView tvServiceAgree;
    @InjectView(R.id.btn_activate)
    Button btnActivate;
    @InjectView(R.id.et_ipAddress)
    EditText etIpAddress;
    private final int MSG_LOADMASTERKEY = 0;//写主秘钥
    @InjectView(R.id.sjzx)
    TextView sjzx;
    private boolean flag = false;
    private MerchantRegisterDataManager.RegisterData registerData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        ButterKnife.inject(this);
        btnActivate.setOnClickListener(v -> checkInput());
        ivRead.setOnClickListener(v -> IVRead());
        sjzx.setOnClickListener(v->sjzx());
        initData();
    }

    /*商家中心*/
    private void sjzx() {
        if (null == registerData) {
            JUtils.Toast(getString(R.string.connect_sign));
            return;
        }
        Intent intent = new Intent(this,BusinessCenterActivity.class);
        startActivity(intent);
    }


    /**
     * 初始化数据：如果是“重新初始化”的操作，进入isReset。否则进入另外一个。
     */
    private void initData() {
      registerReceiver(clsReceiverStartSignInActivity, new IntentFilter(IntentActions.START_SIGNINACTIVITY));

        etIpAddress.setText("111.1.41.104:8080");
        IPPORT.getInstance().setIpPort(ipAddressReplaceChinaColon(etIpAddress.getText().toString().trim()));
        setRegisterDate();
        boolean isReset = getIntent().getBooleanExtra("isReset", false);
        //重新初始化

        if (null != registerData && !TextUtils.isEmpty(registerData.MERCHANT_CODE) && !isReset) {
            JUtils.Log("registerData.MERCHANT_CODE:"+registerData.MERCHANT_CODE);
            Intent intent = new Intent(LoginActivity.this,SignInActivity.class);
            startActivity(intent);
            finish();
        }else if(isReset){
            LogUtils.d("wang","isReset: set ip:"+registerData.IP+":"+registerData.PORT);
            etJhm.setText("");
        }

        //del1
        /*Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();*/

        /*Intent intent = new Intent(LoginActivity.this,SignInActivity.class);
        startActivity(intent);
        finish();*/
        //del2

    }

    /**
     * 自动替换中午的冒号
     * @param ip
     */
    private String ipAddressReplaceChinaColon(String ip){
        if(ip.contains("：")){
            LogUtils.d("wang","替换中文冒号");
            ip = ip.replace("：", ":");
        }
        return ip;
    }

    private void setRegisterDate(){
        registerData = MerchantRegisterDataManager.getInstance().getRegisterData();
    }

    /**
     * 初始化参数校验
     */
    private void checkInput() {
        //onWrite("str");
        //testReadfile();
        if (TextUtils.isEmpty(etIpAddress.getText().toString().trim())) {
            ToastMakeUtils.showToast(this, getString(R.string.jhdress_nothing));
            return;
        }
        if (TextUtils.isEmpty(etJhm.getText().toString().trim())) {
            ToastMakeUtils.showToast(this, getString(R.string.jhcode_nothing));
            return;
        }
        if (flag) {
            ToastMakeUtils.showToast(this, getString(R.string.read_xieyi));
            return;
        }


        getPresenter().activate(etJhm.getText().toString().trim());
    }




    //test readfile
    void testReadfile(){
       String content = FileUtils.readFile("/data/data/com.lifeng.f300.znpos/"+ConnectURL.FILE_REGISTER, PosDataUtils.CHARSET_UTF_8);
        if (TextUtils.isEmpty(content)) {
            LogUtils.d("wang",ConnectURL.FILE_REGISTER+"的内容是空");
          /*  MerchantRegisterDataManager.RegisterData data = new MerchantRegisterDataManager.RegisterData();
            data.merchantRegisterIsOk = false;
            registerData = data;*/

        }else{
            LogUtils.d("wang",content);
        }
    }


    //向sdcard写文件
    /**
     * 写文件
     */

    //private final static String PATH = getApplicationContext().getFilesDir().getAbsolutePath() ;//"/data/data/com.lifeng.f300.znpos/file";//Environment.getExternalStorageDirectory().getPath();
    private final static String FILENAME = "/notes.txt";
    String text1 ="888888888888888888888888888888888";
    private void onWrite(String str) {
        try {
            String PATH = getApplicationContext().getFilesDir().getAbsolutePath() ;
            Log.e("wang", "PATH"+getApplicationContext().getFilesDir().getAbsolutePath());
            //1.判断是否存在sdcard
          //  if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                //目录
                File path = new File(PATH);
                //文件
                File f = new File(PATH + FILENAME);
                if(!path.exists()){
                    //2.创建目录，可以在应用启动的时候创建
                    path.mkdirs();
                }
                if (!f.exists()) {
                    //3.创建文件
                    f.createNewFile();
                }else if(f.exists()){
                    f.delete();
                    f.createNewFile();
                }
                OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(f));
                //4.写文件，从EditView获得文本值
                osw.write(str);
                osw.close();
           // }
        } catch (Exception e) {
            Log.d("wang", "file create error"+  e.getMessage());

        }

    }

    /**
     * 是否阅读“荔智付POS业务服务协议”
     */
    private void IVRead() {
        if (flag) {
            ivRead.setBackgroundResource(R.drawable.jh_img17);
            flag = false;
        } else {
            ivRead.setBackgroundResource(R.drawable.jh_img18);
            flag = true;
        }
    }

    /**
     * 登录界面
     */
    public void startSignInActivity() {
        LogUtils.d("wang", "startSignInActivity");
        Intent intent = new Intent(LoginActivity.this,SignInActivity.class);
        startActivity(intent);
        finish();
    }

    public void vloadMasterKey(TransResponse transResponse) {
        getPresenter().ploadMasterkey(transResponse);
    }


    public void sendMsgLoadMasterKey(TransResponse transResponse) {
        Message msg = Message.obtain();
        Bundle data = new Bundle();
        data.putSerializable("transResponse", transResponse);
        msg.setData(data);
        msg.what = MSG_LOADMASTERKEY;

        handler.sendMessage(msg);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOADMASTERKEY:
                    Bundle data = msg.getData();
                    if (null != data) {
                        vloadMasterKey((TransResponse) (data.getSerializable("transResponse")));
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private BroadcastReceiver clsReceiverStartSignInActivity = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            JUtils.Log("收到broadcast ,打开 SignInActivity");
            startSignInActivity();
        }
    };
    @Override
    protected void onDestroy() {
        unregisterReceiver(clsReceiverStartSignInActivity);
        super.onDestroy();
    }
}
