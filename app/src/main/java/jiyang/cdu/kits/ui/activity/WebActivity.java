package jiyang.cdu.kits.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import jiyang.cdu.kits.R;
import jiyang.cdu.kits.databinding.ActivityWebBinding;
import jiyang.cdu.kits.ui.common.BaseActivity;

public class WebActivity extends BaseActivity {

    private ActivityWebBinding binding;


    public static void start(Context context, String url) {
        if (url == null) {
            throw new NullPointerException("Url must not null");
        }
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web);
        init();
    }

    private void init() {
        String url = getIntent().getStringExtra("url");
        Toolbar toolbar = findViewById(R.id.toolbarWebActivity);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Drawable navIcon = toolbar.getNavigationIcon();
        if (navIcon != null) {
            navIcon.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.loadUrl(request.getUrl().toString());
                } else {
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                binding.progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                binding.progress.setVisibility(View.GONE);
            }
        };
        WebChromeClient chromeClient = new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                binding.progress.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                setTitle(title);
            }
        };
        binding.webView.setWebViewClient(webViewClient);
        binding.webView.setWebChromeClient(chromeClient);
        WebSettings webSettings = binding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        binding.webView.loadUrl(url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        if (binding.webView != null) {
            binding.webView.clearHistory();
            binding.webView.destroy();
        }
        super.onDestroy();
    }
}
