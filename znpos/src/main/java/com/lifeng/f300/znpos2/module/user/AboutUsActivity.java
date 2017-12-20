package com.lifeng.f300.znpos2.module.user;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lifeng.beam.bijection.RequiresPresenter;
import com.lifeng.beam.expansion.BeamBaseActivity;
import com.lifeng.f300.common.entites.TransResponse;
import com.lifeng.f300.common.utils.LogUtils;
import com.lifeng.f300.common.utils.ToastMakeUtils;
import com.lifeng.f300.config.ConnectURL;
import com.lifeng.f300.znpos2.R;
import com.lifeng.f300.znpos2.utils.Utils;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by happen on 2017/8/25.
 */
@RequiresPresenter(AboutUsPresenter.class)
public class AboutUsActivity extends BeamBaseActivity<AboutUsPresenter> {

    @InjectView(R.id.pos_image)
    ImageView posImage;
    @InjectView(R.id.show_version)
    TextView showVersion;
    @InjectView(R.id.check_update)
    TextView checkUpdate;
    @InjectView(R.id.questions_info)
    TextView questionsInfo;
    @InjectView(R.id.company_lifeng_info)
    TextView companyLifengInfo;
    @InjectView(R.id.service_agreement)
    TextView serviceAgreement;

    final static int MSG_LOAD_DOWN_SHOW_DIALOG  = 1;
    private static final int COMEINTO_SYSTEM = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            *//*window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);*//*
            window.setStatusBarColor(Color.TRANSPARENT);
            //window.setNavigationBarColor(Color.TRANSPARENT);
        }*/



        setContentView(R.layout.about_us_layout);
        ButterKnife.inject(this);
    }

    private void initData() {
        setVersion();

    }

    private void initEvent() {
        //版本更新
        checkUpdate.setOnClickListener(v -> loadCheckUpdateSystem());
        //常见问题
        questionsInfo.setOnClickListener(v->startShowCompanyInfo(getString(R.string.question_lists), ConnectURL.questionsInfoWebUrl+System.currentTimeMillis()+""));
        //公司介绍
        companyLifengInfo.setOnClickListener(v -> startShowCompanyInfo(getString(R.string.company_info),ConnectURL.companyLifengInfoWebUrl+System.currentTimeMillis()+""));
        //服务协议
        serviceAgreement.setOnClickListener(v -> startShowCompanyInfo(getString(R.string.service_info),ConnectURL.serviceAgreementWebUrl+System.currentTimeMillis()+""));
    }

    /**
     * 显示公司介绍/常见问题/服务协议
     * @param title
     * @param webUrl
     */
    private void startShowCompanyInfo(String title,String webUrl){
        Intent intent;
        intent = new Intent(this,AboutUsItemActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("webUrl", webUrl);
        startActivity(intent);
    }

    private void loadCheckUpdateSystem() {
        getPresenter().ploadCheckUpdateSystem();
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

    public void sendMsgLoadDownShowDialog(TransResponse transResponse){
        Message msg = Message.obtain();
        Bundle data = new Bundle();
        data.putSerializable("msgTransResponse",transResponse);
        msg.what = MSG_LOAD_DOWN_SHOW_DIALOG;
        handler.sendMessage(msg);
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DOWN_SHOW_DIALOG:
                    Bundle data = msg.getData();
                    if (null != data) {
                        loadDownNewApk((TransResponse) data.getSerializable("msgTransResponse"));
                    }
                    break;

                default:
                    break;
            }
        }


    };


    /**
     * 开始下载
     */
    private void loadDownNewApk(TransResponse selectEntity) {
        HttpUtils utils = new HttpUtils();
        final String target= Environment.getExternalStorageDirectory().getPath()+"/"+selectEntity.UPDATEFILE;
        String downPath=selectEntity.UPDATEADDR+":"+selectEntity.UPDATEPORT+"/"+selectEntity.UPDATEPATH+"/"+selectEntity.UPDATEFILE;
        LogUtils.d("wang", "保存的地址:"+target+"下载的地址："+downPath);
        File file=new File(target);
        /**
         * 先判断如果文件存在就先干掉，再重新进行下载就行了
         */
        if (file.isFile() && file.exists()) {
            LogUtils.d("wang", "delete");
            file.delete();
        }

        utils.download(downPath,target,true,new RequestCallBack<File>() {
            private Dialog dialog;
            private ProgressBar progressBar;
            private TextView showCurrent;

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                dialog.dismiss();
                ToastMakeUtils.showToast(AboutUsActivity.this, getString(R.string.down_error));
            }

            @Override
            public void onSuccess(ResponseInfo<File> arg0) {
                dialog.dismiss();
                ToastMakeUtils.showToast(AboutUsActivity.this, getString(R.string.down_success));
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
//				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.fromFile(new File(target)), "application/vnd.android.package-archive");
                startActivityForResult(intent, COMEINTO_SYSTEM);
//		        android.os.Process.killProcess(android.os.Process.myPid());
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                LogUtils.d("wang", "总量"+ total +"当前"+current+"是否在下载中"+isUploading);
                progressBar.setMax(new Long(total).intValue());
                progressBar.setProgress(new Long(current).intValue());
                showCurrent.setText(getString(R.string.down_loading)+(int)((new Long(current).intValue()/(new Long(total).intValue()/1.0))*100)+"%"+"…");
            }

            @Override
            public void onStart() {
                dialog = Utils.setDialog(AboutUsActivity.this, R.layout.progress_dialog, 0.96, 0);
                progressBar = (ProgressBar) dialog.findViewById(R.id.progress_bar);
                showCurrent = (TextView) dialog.findViewById(R.id.show_current);
                dialog.show();
            }
        });
    }

    /**
     * 重新进入系统中
     */
    private void comeIntoSystem(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == COMEINTO_SYSTEM){
            LogUtils.d("wang", "进入系统中");
            comeIntoSystem();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
