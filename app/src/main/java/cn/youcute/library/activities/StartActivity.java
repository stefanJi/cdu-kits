package cn.youcute.library.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import cn.youcute.library.R;

/**
 * Created by jy on 2016/9/29.
 * 启动Activity
 */

public class StartActivity extends BaseActivity {
    private LinearLayout layoutAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initView();
    }

    private void initView() {
        layoutAd = (LinearLayout) findViewById(R.id.linearLayout_ad);
        //加载广告

        //延迟3秒启动主Activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);

    }
}
