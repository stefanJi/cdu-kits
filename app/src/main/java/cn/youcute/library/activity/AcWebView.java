package cn.youcute.library.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import cn.youcute.library.R;

/**
 * Created by jy on 2016/11/7.
 */

public class AcWebView extends AcBase {
    private ProgressBar progressBar;
    private WebView webView;
    private String url, title;
    private ActionBar actionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_web);
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        Log.d("TAG", title + url);
        initView();
    }

    private void initView() {
        actionBar = getSupportActionBar();
        progressBar = (ProgressBar) findViewById(R.id.progress);
        webView = (WebView) findViewById(R.id.webView);
        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.INVISIBLE);
                actionBar.setTitle(title);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                actionBar.setTitle("---加载中");
            }
        };
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);//支持js
        webSettings.setSupportZoom(true);  //支持缩放
        webView.setWebViewClient(webViewClient);
        webView.loadUrl(url);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
