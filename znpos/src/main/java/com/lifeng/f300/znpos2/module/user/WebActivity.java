package com.lifeng.f300.znpos2.module.user;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.jude.utils.JUtils;
import com.lifeng.beam.bijection.RequiresPresenter;
import com.lifeng.beam.expansion.BeamBaseActivity;
import com.lifeng.f300.common.utils.DeviceUtils;
import com.lifeng.f300.common.utils.MD5Utils;
import com.lifeng.f300.common.widget.WaitingDialog;
import com.lifeng.f300.znpos2.R;
import com.lifeng.f300.znpos2.utils.MerchantRegisterDataManager;
import com.lifeng.f300.znpos2.utils.MerchantSignDataManager;

/**
 * Created by happen on 2017/10/13.
 */
@RequiresPresenter(WebPresenter.class)
public class WebActivity extends BeamBaseActivity<WebPresenter> {

/*    @InjectView(R.id.webview)
    WebView mWebView;*/


    private WebView mWebView = null;
    private int type;// 4: 历史结算流水查询; 5: 历史结算查询
    private String url = "";
    private MerchantSignDataManager.SignData signData;
    private MerchantRegisterDataManager.RegisterData registerData;
    private String operatorId;// 操作员
    private WaitingDialog waitingDialog;
    private String params = "";// 具体参数
    private String cardNo = "";
    private String mememId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }


    private void initView() {
        setContentView(R.layout.html5_webview);
        mWebView = (WebView) findViewById(R.id.webview);
    }

    private void initData() {
        signData = MerchantSignDataManager.getInstance().getSignData();
        registerData = MerchantRegisterDataManager.getInstance().getRegisterData();
        url = signData.ORDER_SERVER_URL;
        if (getIntent().getSerializableExtra("type") != null) {
            type = getIntent().getIntExtra("type", 0);
            switch (type) {
                case 4:
                    url += "/checkOutList";// 历史结算查询
                    break;
                case 5:
                    url += "/invoiceHistory";// 历史结算交易流水查询
                    break;
                default:
                    break;
            }
        }

        waitingDialog = new WaitingDialog(this, R.style.CustomProgressDialog, "正在加载中");
        waitingDialog.show();
        showWebView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void showWebView() {
        try {
            mWebView.requestFocus();
            // 监听加载的进度
            mWebView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    if (newProgress == 100) {
                        if (null != waitingDialog) {
                            waitingDialog.dismiss();
                            waitingDialog = null;
                        }
                    }
                    super.onProgressChanged(view, newProgress);
                }
            });

            mWebView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
                        mWebView.goBack();
                        return true;
                    }
                    return false;
                }
            });

            if (Build.VERSION.SDK_INT >= 19) {
                // 有缓存就用,没有就自动加载
                mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            }

            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setDefaultTextEncodingName("utf-8");

            String poseCode = registerData.POSCODE;// 终端号
            String merchantcode = registerData.MERCHANT_CODE;// 商户号
            operatorId =  JUtils.getSharedPreference().getString("operatorId", "");

            mWebView.addJavascriptInterface(getHtmlObject(), "jsObj");
            JUtils.Log("加密之前：" + merchantcode + poseCode + DeviceUtils.getDeviceSn(WebActivity.this));
            String mac = MD5Utils.md52Pass(merchantcode + poseCode + DeviceUtils.getDeviceSn(WebActivity.this));
            JUtils.Log("加密之后：" + mac);
            params = "?merchant_code=" + merchantcode + "&pos_code=" + poseCode + "&emp_no=" + operatorId + "&mac=" + mac
                    + "&card_no=" + cardNo + "&member_id=" + mememId + "&batch_no=" + signData.BATCHNUM;

            JUtils.Log("url：" + url + params);
            //url= "http://testwebapi.leefengpay.com/mobile/consume/invoiceHistory?merchant_code=100000000000003&pos_code=10000349&emp_no=01&mac=054f4bb960c97685cc9b9e8b3e826e47&card_no=&member_id=&batch_no=000173";  //del
            mWebView.loadUrl(url + params + "&request_time=" + System.currentTimeMillis());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Object getHtmlObject() {
        // android和html5中javaScript的交互

        Object insertObj = new Object() {

            @SuppressWarnings("unused")
            @JavascriptInterface
            public String HtmlcallJava() {
                return "Html call Java";
            }

            @SuppressWarnings("unused")
            @JavascriptInterface
            public String HtmlcallJava2(final String param) {
                return "Html call Java : " + param;
            }

            // 给获取历史交易的具体信息
            @SuppressWarnings("unused")
            @JavascriptInterface
            public void getInvoiceInfo(final String oldTransUrl) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JUtils.Log("oldTrans Url:" + oldTransUrl);
                        mWebView.loadUrl(oldTransUrl + "?request_time=" + System.currentTimeMillis());
                    }
                });
            }

            // 给获取批次号的具体信息
            @SuppressWarnings("unused")
            @JavascriptInterface
            public void getBatchInfo(final String batchUrl) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JUtils.Log("batchInfo Url:" + batchUrl);
                        mWebView.loadUrl(batchUrl + "?request_time=" + System.currentTimeMillis());
                    }
                });
            }



            // 返回键
            // 0是结束，1回退，2回退加刷新
            @SuppressWarnings("unused")
            @JavascriptInterface
            public void back(final int code) {
                JUtils.Log("返回码：" + code);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (code) {
                            case 0:
                                finish();
                                break;
                            case 1:
                                JUtils.Log("goback:" + mWebView.getUrl());
                               /* if (!TextUtils.isEmpty(settlementUrl)) {
                                    finish();
                                } else {*/
                                    mWebView.goBack();
                                //}
                                break;
                            case 2:
                               /* if (!TextUtils.isEmpty(settlementUrl)) {
                                    finish();
                                } else {*/
                                    mWebView.loadUrl(url + params + "&request_time=" + System.currentTimeMillis());
                               // }
                                break;
                            default:
                                break;
                        }
                    }
                });
            }
        };

        return insertObj;
    }
}
