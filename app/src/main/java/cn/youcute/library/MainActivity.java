package cn.youcute.library;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;

import cn.youcute.library.adapter.MyFragmentAdapter;
import cn.youcute.library.fragments.NewsFragment;
import cn.youcute.library.fragments.UserInfoFragment;

/**
 * Created by jy on 2016/9/21.
 * 主界面
 */
public class MainActivity extends AppCompatActivity {
    //图书信息
    UserInfoFragment userInfoFragment;
    //新闻公告
    NewsFragment newsFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        //图书信息
        userInfoFragment = new UserInfoFragment();
        //新闻公告
        newsFragment = new NewsFragment();
        Fragment[] fragments = new Fragment[]{userInfoFragment, newsFragment};
        String[] fragmentTitles = new String[]{"校园身份", "校内新闻"};
        MyFragmentAdapter myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(), fragments, fragmentTitles);
        viewPager.setAdapter(myFragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);

        ImageButton imageButton = (ImageButton) findViewById(R.id.imgBtn_right);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppControl.getInstance().getSpUtil().setIsSign(false);
                AppControl.getInstance().showToast("退出登录");
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
