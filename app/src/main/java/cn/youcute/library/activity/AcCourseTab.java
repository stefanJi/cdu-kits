package cn.youcute.library.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import cn.youcute.library.AppControl;
import cn.youcute.library.R;
import cn.youcute.library.util.TextUtil;
import cn.youcute.library.util.ToastUtil;
import okhttp3.Call;

/**
 * Created by jy on 2016/11/11.
 * 课程表
 */

public class AcCourseTab extends AcBase {
    private static final String TABLE_URL = "http://202.115.80.153/xskbcx.aspx";
    private ProgressDialog progressDialog;
    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_course_tab);
        initView();
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setBuiltInZoomControls(true);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        webView.setWebViewClient(new WebViewClient());
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("从教务系统导入课表中");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        String tableStr = AppControl.getInstance().getSpUtil().getTableString();
        if (tableStr.equals("")) {
            new AlertDialog.Builder(this)
                    .setTitle("第一次需要登录教务系统拉取课表")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(AcCourseTab.this, AcSignEducation.class);
                            startActivityForResult(intent, 107);
                            dialogInterface.dismiss();
                        }
                    })
                    .setCancelable(false)
                    .create().show();

            return;
        }
        setTabView(tableStr);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 107) {
            if (resultCode == 1) {
                getTable();
            } else {
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
            }
        }
    }

    private void getTable() {
        if (!isNetworkConnected()) {
            ToastUtil.showToast("网络未连接，请重试");
            return;
        }
        progressDialog.show();
        String url = TABLE_URL + "?xh="
                + AppControl.getInstance().getSpUtil().getUser().account
                + "&xm=" + TextUtil.encoding(AppControl.getInstance().getSpUtil().getName()) +
                "&gnmkdm=N121603";

        OkHttpUtils
                .get()
                .url(url)
                .tag(this)
                .addHeader("Host", "202.115.80.153")
                .addHeader("Referer", "http://202.115.80.153/xs_main.aspx?xh=" + AppControl.getInstance().getSpUtil().getUser().account)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.showToast("错误响应:" + e.getMessage());
                progressDialog.dismiss();
                call.cancel();
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
            }

            @Override
            public void onResponse(String response) {
                if (response != null) {
                    AppControl.getInstance().getSpUtil().saveTableStr(response);
                    setTabView(response);
                }
                progressDialog.dismiss();
            }
        });
    }

    private void setTabView(String response) {
        webView.loadDataWithBaseURL(null, response, "text/html", "utf-8", null);
    }
}
