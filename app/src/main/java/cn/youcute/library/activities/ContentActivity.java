package cn.youcute.library.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.youcute.library.AppControl;
import cn.youcute.library.R;
import cn.youcute.library.util.NetRequest;

/**
 * Created by jy on 2016/9/26.
 * 详细内容
 */

public class ContentActivity extends BaseActivity implements View.OnClickListener, NetRequest.GetAnnounceContentCallBack {
    private ImageButton imgBtnBack;
    private TextView tvTitle, tvContent;
    private ProgressBar progressBar;
    private String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        initView();
    }

    private void initView() {
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
        imgBtnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_announce_title);
        tvContent = (TextView) findViewById(R.id.tv_announce_content);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        Bundle bundle = getIntent().getExtras();
        String title = bundle.getString("title");
        url = bundle.getString("url");
        tvTitle.setText(title);
        AppControl.getInstance().getNetRequest().getAnnounceContent(url, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_back:
                finish();
                break;
        }
    }

    @Override
    public void getContentSuccess(String content) {
        tvContent.setText(content);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void getContentFailed() {
        progressBar.setVisibility(View.INVISIBLE);
    }
}
