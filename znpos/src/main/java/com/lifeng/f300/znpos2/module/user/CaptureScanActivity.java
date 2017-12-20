package com.lifeng.f300.znpos2.module.user;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lifeng.beam.bijection.RequiresPresenter;
import com.lifeng.beam.expansion.BeamBaseActivity;
import com.lifeng.f300.common.entites.TransRequest;
import com.lifeng.f300.common.entites.TransResponse;
import com.lifeng.f300.common.utils.LogUtils;
import com.lifeng.f300.common.utils.StringUtil;
import com.lifeng.f300.config.PayScanType;
import com.lifeng.f300.znpos2.R;
import com.lifeng.f300.znpos2.scan.decode.DecodeHandler;
import com.lifeng.f300.znpos2.scan.zbar.CameraManager;
import com.lifeng.f300.znpos2.scan.zbar.CameraPreview;

import java.io.IOException;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by happen on 2017/9/4.
 */
@RequiresPresenter(CaptureScanPresenter.class)
public class CaptureScanActivity extends BeamBaseActivity<CaptureScanPresenter> implements View.OnClickListener {
    @InjectView(R.id.capture_preview)
    FrameLayout capturePreview;
    @InjectView(R.id.capture_mask_top)
    ImageView captureMaskTop;
    @InjectView(R.id.capture_scan_line)
    ImageView captureScanLine;
    @InjectView(R.id.capture_crop_view)
    RelativeLayout captureCropView;
    @InjectView(R.id.tv_trans_active_tag)
    TextView tvTransActiveTag;
    @InjectView(R.id.tv_active_amount)
    TextView tvActiveAmount;
    @InjectView(R.id.re_scan_title)
    RelativeLayout reScanTitle;
    @InjectView(R.id.tv_scan_msg)
    TextView tvScanMsg;
    @InjectView(R.id.capture_mask_bottom)
    ImageView captureMaskBottom;
    @InjectView(R.id.capture_mask_left)
    ImageView captureMaskLeft;
    @InjectView(R.id.capture_mask_right)
    ImageView captureMaskRight;
    @InjectView(R.id.tv_prompt)
    TextView tvPrompt;
    @InjectView(R.id.capture_container)
    RelativeLayout captureContainer;
    @InjectView(R.id.capture_restart_scan)
    Button captureRestartScan;
    @InjectView(R.id.capture_scan_result)
    TextView captureScanResult;
    @InjectView(R.id.iv_scan_sweep)
    ImageView ivScanSweep;
    @InjectView(R.id.re_sweep)
    RelativeLayout reSweep;
    @InjectView(R.id.iv_scan_swept)
    ImageView ivScanSwept;
    @InjectView(R.id.re_swept)
    RelativeLayout reSwept;
    @InjectView(R.id.no1_pay)
    ImageView no1Pay;
    @InjectView(R.id.no1_pay_view)
    RelativeLayout no1PayView;
    @InjectView(R.id.re_scan_bottom)
    LinearLayout reScanBottom;

    private String payScanType = "0";//0没有、2微信、3支付宝
    private String transAmount;
    //private CameraPreview mPreview;
    private Handler autoFocusHandler;
    private CameraManager mCameraManager;


    TransRequest transRequest;

    //震动和音效
    private boolean playBeep;
    private boolean vibrate;
    private static final long VIBRATE_DURATION = 200L;//震动
    private MediaPlayer mediaPlayer;
    private boolean previewing = true;
    private boolean barcodeScanned = false;
    private String userAuthCode;
    private CameraPreview mPreview;
    private FrameLayout scanPreview;
    private Rect mCropRect = null;
    private RelativeLayout scanContainer;
    private RelativeLayout scanCropView;
    private static final float BEEP_VOLUME = 0.80f;//声音0-1之间，左右声道

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initEvent();

    }

    private void initView() {
        setContentView(R.layout.scan_camera);
        ButterKnife.inject(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void initData() {
        if(getIntent().getSerializableExtra("bankType") != null){
            payScanType =  getIntent().getStringExtra("paysType");
        }
        if(getIntent().getSerializableExtra("transAmount") != null){
            transAmount = getIntent().getStringExtra("transAmount");
        }



        if(PayScanType.WECHAT_SCAN.equals(payScanType)){
            tvScanMsg.setText(getString(R.string.wchant_info_msg));
        }else if(PayScanType.ALI_SCAN.equals(payScanType)){
            tvScanMsg.setText(getString(R.string.alipay_info_msg));
        }

        tvActiveAmount.setText("¥"+ StringUtil.formatterAmount(transAmount));
        initCamera();
    }

    private void initEvent() {
    }

    private void initCamera() {
        mCameraManager = new CameraManager(CaptureScanActivity.this);
        autoFocusHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case DecodeHandler.DECODE_OK:
                        //开启声音：
                        playBeepSoundAndVibrate();
                        previewing = false;
                        mCameraManager.stopPreview();
                        barcodeScanned = true;
                        if(msg.obj != null){
                            userAuthCode =  (String) msg.obj;
//						if (userAuthCode.startsWith("13")) {
//							bankType = "2";//微信
//						}else if(userAuthCode.startsWith("28")){
//							bankType = "3";//支付宝
//						}
                            /*if(isRecharge){
                                TransRequest transRequest = new TransRequest();
                                transRequest.bankType = bankType;
                                transRequest.bankAmount = transAmount;
                                transRequest.userAuthCode = userAuthCode;
                                rechargePost(transRequest, transAmount);
                            }else{
                                if(transRequest == null){
                                    transRequest = new TransRequest();
                                }
                                transRequest.bankType = bankType;
                                transRequest.bankAmount = transAmount;
                                transRequest.userAuthCode = userAuthCode;
                                transPost(transRequest);
                            }*/
                            transRequest = new TransRequest();
                            transPost(transRequest);
                        }
                        break;
                    case DecodeHandler.DECODE_FAIL:
                        previewCb.setDecodeHandler(mCameraManager.getDecodeThread().getHandler());
                        break;
                }
            }
        };
        mPreview = new CameraPreview(this, mCameraManager, previewCb, autoFocusCB);
        scanPreview.addView(mPreview);
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                0.85f);
        animation.setDuration(2500);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.REVERSE);
        captureScanLine.startAnimation(animation);
    }

    public Handler getHandler() {
        return autoFocusHandler;
    }

    private DecodePreviewCallback previewCb = new DecodePreviewCallback();
    public class DecodePreviewCallback implements Camera.PreviewCallback {
        public Handler mHandler;
        public void setDecodeHandler(Handler mHandler) {
            this.mHandler = mHandler;
        }
        public void onPreviewFrame(byte[] data, Camera camera) {
            if (mHandler != null) {
                Camera.Size size = camera.getParameters().getPreviewSize();
                mHandler.obtainMessage(DecodeHandler.DECODE, size.width, size.height, data).sendToTarget();
                mHandler = null;
            }
        }
    };

    /**
     * 开始播放声音
     */
    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /*
 * 消费
 */
    public void transPost(final TransRequest transRequest) {

    }
    public Rect getCopRect() {
        return mCropRect;
    }

    /**
     * 初始化截取的矩形区域
     */
    public Rect initCrop() {
        int cameraWidth = mCameraManager.getCameraResolution().y;
        int cameraHeight = mCameraManager.getCameraResolution().x;

        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        scanCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1];
        int cropWidth = scanCropView.getWidth();
        int cropHeight = scanCropView.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = scanContainer.getWidth();
        int containerHeight = scanContainer.getHeight();
        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;
        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;
        /** 生成最终的截取的矩形 */
        return mCropRect = new Rect(x, y, width + x, height + y);
    }

    /**
     * 初始化声音
     */
    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);
            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.code);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        if (barcodeScanned) {
            barcodeScanned = false;
            previewing = true;
            mCameraManager.startPreview(previewCb, autoFocusCB);
        }
        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
        LogUtils.d("wang", "onResume:previewing"+previewing);
        ivScanSweep.setImageResource(R.drawable.scan_sweep_btn);
        ivScanSwept.setImageResource(R.drawable.scan_swept_btngray);
    }

    public void onPause() {
        super.onPause();
        LogUtils.d("wang", "onPause:doAutoFocus："+doAutoFocus);
        previewing = false;
        barcodeScanned = true;
        mCameraManager.stopPreview();
        autoFocusHandler.removeCallbacks(doAutoFocus);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       /* LogUtils.d("wang", "onDestroy");
        mCameraManager.releaseCamera();
        DisableHomeMenuBar.operatorAll(0);
        unregisterReceiver(finishBroadcastReceiver);
        bundleTemp = null;*/
    }
    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing){
                LogUtils.d("wang", "mCameraManager:previewing中"+mCameraManager+"---autoFocusCB中:"+autoFocusCB);
                LogUtils.d("wang", "mCameraManager.getCamera()"+mCameraManager.getCamera());
                if(null != mCameraManager.getCamera()){
                    mCameraManager.getCamera().autoFocus(autoFocusCB);
                }
            }else{
                LogUtils.d("wang", "mCameraManager:previewing外"+mCameraManager);
            }
        }
    };
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1500);
        }
    };


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.re_sweep://主扫
                /*if(null != loadDialog){
                    loadDialog.cancel();
                    loadDialog = null;
                }
                loadDialog = new LoadDialog(this,R.style.CustomProgressDialog,getString(R.string.waiting));
                loadDialog.show();
                payWwitch(loadDialog);
                loadDialog = null;
                ivScanSweep.setImageResource(R.drawable.scan_sweep_btn);
                ivScanSwept.setImageResource(R.drawable.scan_swept_btngray);*/
                break;

            case R.id.re_swept://被扫(跳转到易汇金或者其他)
                /*ivScanSweep.setImageResource(R.drawable.scan_sweep_btngray);
                ivScanSwept.setImageResource(R.drawable.scan_swept_btn);
                intentToYhjPay();*/
                break;

            case R.id.no1_pay_view://1号生活
                /*bankType = "2";
                intentToNo1Pay();*/
                break;
        }
    }
    /**
     * 1号生活生成订单
     */
    private void intentToNo1Pay() {
       /* if(!CheckNetIsOk.isNetOk(CaptureScanActivity.this)){
            return ;
        }
        mCameraManager.releaseCamera();
        DisableHomeMenuBar.operatorAll(0);
        Intent intent=new Intent(CaptureActivity.this,No1PayActivity.class);
        intent.putExtra("transAmount", transAmount);
        startActivity(intent);*/
    }

    /**
     * 易汇金生成订单
     */
    private void intentToYhjPay(){
      /*  if(!CheckNetIsOk.isNetOk(CaptureScanActivity.this)){
            return ;
        }
        mCameraManager.releaseCamera();
        DisableHomeMenuBar.operatorAll(0);
        Intent intent=new Intent(CaptureScanActivity.this,YhjPayActivity.class);

        if(isRecharge){
            TransRequest transRequest = new TransRequest();
            transRequest.bankType = bankType;
            transRequest.bankAmount = transAmount;
        }else{
            if(transRequest == null){
                transRequest = new TransRequest();
            }
            transRequest.bankType = bankType;
            transRequest.bankAmount = transAmount;
        }
        if(transRequest != null){
            transRequest.isSweep = "2";
            intent.putExtra("transRequest",gson.toJson(transRequest).toString());
        }

        intent.putExtra("transAmount", transAmount);
        intent.putExtra("cardNo", cardNo);
        intent.putExtra("memberId", memberId);
        intent.putExtra("billNo", billNo);
        intent.putExtra("isRecharge", isRecharge);
        intent.putExtra("isCombinePay", isCombinePay);
        intent.putExtra("result", queryMemberResult);
        intent.putExtra("timesType", timesType);//商品的交易类型
        if("2".equals(bankType)){
            intent.putExtra("bankType", "5");//5微信支付（被扫）、6支付宝（被扫）
        }else if("3".equals(bankType)){
            intent.putExtra("bankType", "6");//5微信支付（被扫）、6支付宝（被扫）
        }
        if (null != bundleTemp) {
            intent.putExtra(ParameterTemp.BUNDLE_TEMP,bundleTemp);
            startActivity(intent);
        }else{
            startActivity(intent);
        }
        finish();*/
    }
    final Handler handler=new Handler();
    private void payWwitch(final Dialog loadDialog){
        //2，然后创建一个Runnable对象
        Runnable runnable=new Runnable(){
            @Override
            public void run() {
                if(loadDialog != null){
                    loadDialog.cancel();
                }
            }
        };
        handler.postDelayed(runnable,500);
    }
    /*
	 * 充值
	 */
    public void rechargePost(final TransRequest transQuest, String addAmount) {

    }

    private void successView(TransResponse transResponse){
        /*if(dao.deleteTraning(transResponse.TRACE_NO,transResponse.BATCHNO)){
            LogUtils.d("wang","删除saveBeforeState为0的数据成功");
        }
        Intent intent = null;
        intent = new Intent(getApplicationContext(),ResultActivity.class);
        if (queryMember != null) {
            transResponse.MEMBERID = queryMember.MEMBERID;
            transResponse.MEMBERNAME = queryMember.MEMBERNAME;
        } else {
            transResponse.MEMBERID = 0;
        }
        if(isSubmit){
            transResponse.isBackDn = "1";
        }else{
            transResponse.isBackDn = "0";
        }
        intent.putExtra("result", transResponse);
        intent.putExtra("isRecharge",isRecharge);
        intent.putExtra(ResultActivity.VIP_CARD, cardNo);
        intent.putExtra(ResultActivity.TYPE,1);
        intent.putExtra(OpenCrashHand.COM_PAY_TYPE,isCombinePay);
        intent.putExtra("timesType", timesType);//商品的交易类型
        startActivity(intent);
        finish();*/
    }
    /***
     * 交易出错提示
     * @param msg
     */
    private void scanError(String msg){
        /*if(timerTask != null){
            timerTask.cancel();
        }
        if(null != dialog){
            dialog.cancel();
            dialog = null;
        }
        dialog = Utils.setDialog(CaptureActivity.this, R.layout.scan_code_error_dialog, 0.7, 0);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();
        TextView tvMsg =  (TextView) dialog.findViewById(R.id.tv_scan_error);
        tvMsg.setText(msg);
        Button btnSure =  (Button) dialog.findViewById(R.id.btn_sure);
        btnSure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                dialog = null;
                tvPrompt.setText("");
                tvPrompt.setVisibility(View.GONE);
                reStartActitvity();
            }
        });*/
    }
    private int times = 0;
    //0 普通 1 零售；2 计次消费；3 计次购买；4 计次消费+零售（组合）
    private int timesType;
    //支付确认定时触发时间间隔为  30秒、15秒、10秒、  10秒；
    class MyTimerTask extends TimerTask {
        public MyTimerTask() {
        }
        @Override
        public void run() {
            if(StringUtil.isNotEmpty(userAuthCode)){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        /*times++;
                        LogUtils.d("wang","timedTransactionQuery:"+times);
                        if(times == 1){
                            if(orderRequest != null){
                                queryOrder(orderRequest);
                            }
                        } else if(times == 2){
                            LogUtils.d("wang","times2"+times);
                            if(timerTask != null){
                                timerTask.cancel();
                            }
                            if (timer != null){
                                timerTask = new MyTimerTask();  // 新建一个任务
                                timer.schedule(timerTask,10000,1000);
                            }
                            if(orderRequest != null){
                                queryOrder(orderRequest);
                            }
                        }else if(times == 3){
                            times = 0 ;
                            LogUtils.d("wang","times3:"+times);
                            if(orderRequest != null){
                                queryOrder(orderRequest);
                            }
                            if(timerTask != null){
                                timerTask.cancel();
                            }
                        }*/
                    }
                });
            }
        }
    }

    /*
     * 查询订单
     */
    /*public void queryOrder(QueryOrderRequest queryOroder) {
        if(!CheckNetIsOk.isNetOk(CaptureActivity.this)){
            return ;
        }
        queryOroder.POSCODE = registerData.POSCODE;// 终端号
        queryOroder.MERCHANTCODE = registerData.MERCHANT_CODE;// 商户号
        queryOroder.EMPNO = SharedPreferenceUtil.getStringData(CaptureActivity.this, "operatorId");// 操作员号
        http.send(HttpRequest.HttpMethod.POST, webUrl, RequestHttpDatasHelper.queryOrderParams(queryOroder),new RequestCallBack<String>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onLoading(long total, long current,boolean isUploading) {
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.d("wangkeze", "responseInfo:" + responseInfo.result);
                TransResponse transResponse = gson.fromJson(responseInfo.result,TransResponse.class);
                if (null != transResponse && "00".equals(transResponse.STATUS)) {
                    if (!JsonValidator.validate(responseInfo.result)) {
                        ToastMakeUtils.showToast(CaptureActivity.this, "服务器返回数据异常");
                        closeTransDialog();
                        return;
                    }
                    successView(transResponse);
                }else if(null != transResponse && "01".equals(transResponse.STATUS)){
                    tvPrompt.setText(transResponse.MESSAGE+"");
                    tvPrompt.setVisibility(View.VISIBLE);
                }else {
                    scanError(transResponse.MESSAGE);
                }
                closeTransDialog();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                closeTransDialog();
            }
        });
    }*/

  /*  private void closeTransDialog(){
        if (transDialog != null) {
            transDialog.dismiss();
            transDialog = null;
        }
    }
*/
    /*
    * 初始化扫描
    */
    private void reStartActitvity(){
        if (barcodeScanned) {
            previewCb.setDecodeHandler(mCameraManager.getDecodeThread().getHandler());
            barcodeScanned = false;
            previewing = true;
            mCameraManager.startPreview(previewCb, autoFocusCB);
        }
    }

    /**
     * 返回取消的方法
     */
    private void cancelTransApp(){
       /* if(null != bundleTemp){
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
        }*/
    }

    @Override
    public void onBackPressed() {
        //App
        /*SharedPreferenceUtil.putBooleanData(CaptureActivity.this, ConnectURL.BUNDLETEMP_NOTNULL, false);
        cancelTransApp();
        finish();*/
    }

    /*@Override
    protected void onLeftTitleClick() {
        //App
        *//*SharedPreferenceUtil.putBooleanData(CaptureActivity.this, ConnectURL.BUNDLETEMP_NOTNULL, false);
        cancelTransApp();
        finish();*//*
    }*/

    /**
     * 获取交易取消返回的字符串
     */
    private String getCancelTransApp(){
        /*String resultTemp = "";
        if(null != bundleTemp){
            TransResponse transResponse = new TransResponse();
            transResponse.payAmountTemp = bundleTemp.getString(ParameterTemp.PAYAMOUNT_TEMP);
            transResponse.orderNoTemp = bundleTemp.getString(ParameterTemp.ORDERNO_TEMP);
            transResponse.BANK_TYPE = bundleTemp.getString(ParameterTemp.BANK_TYPE);
            transResponse.callBackUrl = bundleTemp.getString(ParameterTemp.CALLBACK_URL);
            resultTemp = RequestHttpDatasHelper.getResultTemp(transResponse,getString(R.string.trans_cancel_app));
            //标记该笔订单是第三方App过来的,便于回调给第三方App
            SharedPreferenceUtil.putBooleanData(CaptureActivity.this, ConnectURL.BUNDLETEMP_NOTNULL, true);
            SharedPreferenceUtil.putStringData(CaptureActivity.this, ParameterTemp.PAYAMOUNT_TEMP, transResponse.payAmountTemp);
            SharedPreferenceUtil.putStringData(CaptureActivity.this, ParameterTemp.ORDERNO_TEMP, transResponse.orderNoTemp);
            SharedPreferenceUtil.putStringData(CaptureActivity.this, ParameterTemp.CALLBACK_URL, transResponse.callBackUrl);
            return resultTemp;
        }else{
            //标记该笔订单是不是第三App过来的
            SharedPreferenceUtil.putBooleanData(CaptureActivity.this, ConnectURL.BUNDLETEMP_NOTNULL, false);
            SharedPreferenceUtil.putStringData(CaptureActivity.this, ParameterTemp.PAYAMOUNT_TEMP, "");
            SharedPreferenceUtil.putStringData(CaptureActivity.this, ParameterTemp.ORDERNO_TEMP, "");
            SharedPreferenceUtil.putStringData(CaptureActivity.this, ParameterTemp.CALLBACK_URL, "");
            return "";
        }*/
        return "";
    }

}
