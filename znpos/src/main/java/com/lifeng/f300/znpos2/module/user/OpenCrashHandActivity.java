package com.lifeng.f300.znpos2.module.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.utils.JUtils;
import com.lifeng.beam.bijection.RequiresPresenter;
import com.lifeng.beam.expansion.BeamBaseActivity;
import com.lifeng.f300.common.entites.TransResponse;
import com.lifeng.f300.common.utils.DecodeBitmap;
import com.lifeng.f300.common.utils.ZipUtil;
import com.lifeng.f300.znpos2.R;
import com.lifeng.f300.znpos2.view.HandWriteView;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by happen on 2017/9/5.
 */
@RequiresPresenter(OpenCrashHandPresenter.class)
public class OpenCrashHandActivity extends BeamBaseActivity<OpenCrashHandPresenter> {
    final static int MSG_LOAD_BITMAP = 1;//打开resultActivity，同时传递bitmap

    @InjectView(R.id.clear_tv)
    TextView clearTv;
    @InjectView(R.id.amount_all_tv)
    TextView amountAllTv;
    @InjectView(R.id.next_tv)
    TextView nextTv;

    @InjectView(R.id.agreen_baoxin)
    TextView agreenBaoxin;
    @InjectView(R.id.read_biaoxin_link)
    TextView readBiaoxinLink;
    @InjectView(R.id.show_signature_baoxian)
    LinearLayout showSignatureBaoxian;
    @InjectView(R.id.date_time)
    TextView dateTime;


    // 以下是必须完成签名，不能返回或者终止的操作，主要正对银行卡的操作
    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000; // 为了屏蔽home键
    private HandWriteView bhw;
    private Bitmap bitmap; // 电子签名

    private byte[] bitmapByte = null;


    private int type = -1;
    private int transType = -1;
    private TransResponse transResponse;
    /**
     * 签名板
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);// 关键代码(监听home)
        setContentView(R.layout.open_signature);
        ButterKnife.inject(this);
        bhw = (HandWriteView) this.findViewById(R.id.hand_writing_view);
        nextTv.setEnabled(true);
    }

    private void initData() {
        /*if (getIntent().getExtras().get("result") != null) {
            transResponse = (TransResponse) getIntent().getExtras().get("result");
        }*/
    }

    private void initEvent() {
        clearTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bhw.clear();
                bitmap = null;
                nextTv.setEnabled(true);
            }
        });


        nextTv.setOnClickListener(new View.OnClickListener() {
            private ByteArrayOutputStream baos;
            private String serStr;
            @Override
            public void onClick(View v) {
                nextTv.setEnabled(false);
                bitmap = bhw.getWriteBitmap();
                // bitmap = bhw.getCachebBitmap();
                // 上传图片的操作：
                // 为了解决bitmap对象太大被回收情况，用数组进行传递
                if (null != transResponse) {
                    if (null != bitmap) {
                        baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 10, baos);
                        bitmapByte = baos.toByteArray();
                        bitmap = DecodeBitmap.decodeThumbBitmapForFile(bitmapByte, 550, 353);// 压缩

                        baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 10, baos);
                        bitmapByte = baos.toByteArray();
                        JUtils.Log(bitmapByte.length + "压缩之后图片的byte长度");
                        try {
                            serStr = baos.toString("ISO-8859-1");
                            JUtils.Log( serStr.length() + "编码前压缩的string长度");
                            serStr = ZipUtil.zip(serStr);
                            serStr = java.net.URLEncoder.encode(serStr, "UTF-8");// 编码
                            JUtils.Log( serStr.length() + "编码后压缩的string长度");

                            if (serStr.length() > 180000) {
                                JUtils.ToastLong( getString(R.string.sign_error));
                                bhw.clear();
                                bitmap = null;
                                nextTv.setEnabled(true);
                                return ;
                            }
                            uploadSignPic(serStr, transResponse);
                        } catch (UnsupportedEncodingException e) {
                            nextTv.setEnabled(true);
                            e.printStackTrace();
                        }
                    } /*else {
                        bitmapByte = null;
                        loadSubmit(bitmapByte);
                    }*/
                } else {
                    JUtils.Log("支付成功返回数据异常");
                    nextTv.setEnabled(true);
                    JUtils.ToastLong(getString(R.string.trans_app_error));
                }
            }
        });
    }

    private void uploadSignPic(final String signatureStr, final TransResponse transResponse) {
        getPresenter().pUploadSignPic(signatureStr,transResponse);
    }

    /** 确认签名的操作 */
    private void loadSubmit(byte[] bitmapByte) {
        JUtils.Log( "签名中交易的类型："+type);
        nextTv.setEnabled(true);
        if (type == 0 && null == bitmapByte) {
            JUtils.ToastLong(getString(R.string.sign_continue));
            return;
        }

        if (transType == 2 && null == bitmapByte) {//自定义的类型:1现金 2、银行卡支付 3、微信4、储值 5积分  6优惠券 7 计次 8 支付宝
            JUtils.ToastLong(getString(R.string.sign_continue_add));
            return;
        }

        /*if (isTransFlow) {
            transResponse.signBitmap=bitmapByte;
        }*/
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("bitmapByte", bitmapByte);
        intent.putExtra("result", transResponse);
/*        intent.putExtra("isRecharge", isRecharge);
        intent.putExtra("isTransFlow", isTransFlow);
        intent.putExtra(ResultActivity.VIP_CARD, vipCard);
        intent.putExtra(ResultActivity.COM_PAY_TYPE, isCombinePay);
        intent.putExtra(ResultActivity.TYPE, type);
        intent.putExtra("timesType", timesType);
        intent.putExtra("orderJson",orderJson);
        intent.putExtra("shopJson",shopJson);*/

        startActivity(intent);
        finish();
    }

    public void sendMsgloadSubmit(){
        /*Message msg = Message.obtain();
        msg.what = MSG_LOAD_BITMAP;*/
        handler.sendEmptyMessage(MSG_LOAD_BITMAP);


    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_BITMAP:
                    loadSubmit(bitmapByte);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        JUtils.ToastLong(getString(R.string.sign_continue));
        nextTv.setEnabled(true);
    }

    // 监听home键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            JUtils.ToastLong(getString(R.string.sign_continue));
            nextTv.setEnabled(true);
            return false;
        }

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            JUtils.ToastLong(getString(R.string.sign_continue));

            nextTv.setEnabled(true);
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }
}


