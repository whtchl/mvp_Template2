package com.lifeng.f300.znpos2.module.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jude.utils.JUtils;
import com.lifeng.beam.bijection.RequiresPresenter;
import com.lifeng.beam.expansion.BeamBaseActivity;
import com.lifeng.f300.znpos2.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by happen on 2017/8/25.
 */
@RequiresPresenter(AboutUsItemPresenter.class)
public class AboutUsItemActivity extends BeamBaseActivity<AboutUsItemPresenter> {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.webview)
    WebView webview;

    String title="";
    String webUrl="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle(title);

    }

    private void initView() {

        setContentView(R.layout.register_webview);
        ButterKnife.inject(this);

        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });

        //监听加载的进度
        webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    getExpansion().dismissProgressDialog();
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        //启用支持javascript
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);

    }

    private void initData() {
        Intent intent = getIntent();
        title=intent.getStringExtra("title");
        webUrl=intent.getStringExtra("webUrl");
        getExpansion().showProgressDialog(getString(R.string.waiting));
        JUtils.Log("title:"+title);
        //toolbar.setTitle(title);
        webview.loadUrl(TextUtils.isEmpty(webUrl) ? "" : webUrl);
    }

    private void initEvent() {
    }
    //改写物理按键——返回的逻辑
    public void Back(View v){
        if(webview.canGoBack()){
            webview.goBack();//返回上一页面
        }else{
            System.exit(0);//退出程序
        }
    }
}
