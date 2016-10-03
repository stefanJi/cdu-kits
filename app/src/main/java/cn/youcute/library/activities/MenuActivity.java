package cn.youcute.library.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import cn.youcute.library.R;
import cn.youcute.library.activities.menuFragments.FragAbout;
import cn.youcute.library.activities.menuFragments.FragAnnounce;
import cn.youcute.library.activities.menuFragments.FragFeedBack;
import cn.youcute.library.activities.menuFragments.FragHelp;

/**
 * Created by jy on 2016/10/3.
 * 菜单
 */

public class MenuActivity extends BaseActivity implements View.OnClickListener {
    int action = 0;
    public static final int ANNOUNCE = 0;
    public static final int EDUCATION = 1;
    public static final int NET_EDUCATION = 2;
    public static final int FEEDBACK = 3;
    public static final int HELP = 4;
    public static final int ABOUT = 5;
    private TextView title;
    private FragmentManager manager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        action = getIntent().getIntExtra("action", 0);
        initView();
        showMenu();
    }

    private void initView() {
        ImageButton back = (ImageButton) findViewById(R.id.imgBtn_back);
        title = (TextView) findViewById(R.id.tv_menu_title);
        manager = getSupportFragmentManager();
        back.setOnClickListener(this);
    }

    private void showMenu() {
        switch (action) {
            case ANNOUNCE:
                title.setText("通知公告");
                FragAnnounce announce = new FragAnnounce();
                manager.beginTransaction().add(R.id.frame_menu, announce).commit();
                break;
            case EDUCATION:
                title.setText("网络教务信息");

                break;
            case NET_EDUCATION:
                title.setText("网络教学平台");
                break;
            case FEEDBACK:
                title.setText("反馈");
                FragFeedBack feedBack = new FragFeedBack();
                manager.beginTransaction().add(R.id.frame_menu, feedBack).commit();
                break;
            case HELP:
                title.setText("帮助");
                FragHelp help = new FragHelp();
                manager.beginTransaction().add(R.id.frame_menu, help).commit();
                break;
            case ABOUT:
                title.setText("关于");
                FragAbout about = new FragAbout();
                manager.beginTransaction().add(R.id.frame_menu, about).commit();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imgBtn_back) {
            finish();
        }
    }
}
