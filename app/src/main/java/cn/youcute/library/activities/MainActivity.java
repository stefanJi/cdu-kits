package cn.youcute.library.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import cn.youcute.library.AppControl;
import cn.youcute.library.R;
import cn.youcute.library.activities.menuFragments.FragAbout;
import cn.youcute.library.activities.menuFragments.FragAnnounce;
import cn.youcute.library.activities.menuFragments.FragFeedBack;
import cn.youcute.library.activities.menuFragments.FragHelp;
import cn.youcute.library.activities.menuFragments.FragLibrary;
import cn.youcute.library.bean.UserInfo;
import cn.youcute.library.util.NetRequest;
import cn.youcute.library.util.SpUtil;

/**
 * Created by jy on 2016/9/21.
 * 主界面
 */
public class MainActivity extends BaseActivity implements View.OnClickListener, NetRequest.GetInfoCallBack {
    //左侧菜单
    private DrawerLayout leftMenu;
    private TextView mainTitle;
    private TextView userName, userAccount;
    public static final int LIBRARY_ACTION = 0;
    public static final int EDUCATION_ACTION = 1;
    public static final int NET_EDUCATION_ACTION = 2;
    public static final int ANNOUNCE_ACTION = 3;
    public static final int SETTING_ACTION = 4;
    public static final int FEEDBACK_ACTION = 5;
    public static final int HELP_ACTION = 6;
    public static final int ABOUT_ACTION = 7;
    private int fragmentAction = ANNOUNCE_ACTION;
    private SpUtil spUtil;
    private FragmentManager manager;
    //通知公告
    FragAnnounce fragAnnounce;
    //图书信息
    FragLibrary fragLibrary;
    //反馈
    FragFeedBack fragFeedBack;
    //帮助
    FragHelp fragHelp;
    //关于
    FragAbout fragAbout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spUtil = AppControl.getInstance().getSpUtil();
        fragmentAction = spUtil.getStartFragment();
        initView();
    }

    private void initView() {
        mainTitle = (TextView) findViewById(R.id.tv_main_title);
        leftMenu = (DrawerLayout) findViewById(R.id.drawerLayout);
        ImageButton imgBtnLeft = (ImageButton) findViewById(R.id.imgBtn_left);
        ImageButton imgBtnRight = (ImageButton) findViewById(R.id.imgBtn_right);
        imgBtnLeft.setOnClickListener(this);
        imgBtnRight.setOnClickListener(this);
        //侧滑菜单控件
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup_1);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                leftMenu.closeDrawers();
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.menu_announce:
                        fragmentAction = ANNOUNCE_ACTION;
                        showFrag();
                        break;
                    case R.id.menu_library:
                        fragmentAction = LIBRARY_ACTION;
                        showFrag();
                        break;
                    case R.id.menu_education:
                        fragmentAction = EDUCATION_ACTION;
                        showFrag();
                        break;
                    case R.id.menu_net_education:
                        fragmentAction = NET_EDUCATION_ACTION;
                        showFrag();
                        break;
                    case R.id.menu_setting:
                        fragmentAction = SETTING_ACTION;
                        showFrag();
                        break;
                    case R.id.menu_feedback:
                        fragmentAction = FEEDBACK_ACTION;
                        showFrag();
                        break;
                    case R.id.menu_about:
                        fragmentAction = ABOUT_ACTION;
                        showFrag();
                        break;
                    case R.id.menu_help:
                        fragmentAction = HELP_ACTION;
                        showFrag();
                        break;
                }
            }
        });
        userName = (TextView) findViewById(R.id.tv_user_name);
        userAccount = (TextView) findViewById(R.id.tv_user_account);
        UserInfo userInfo = spUtil.getUserInfo();
        userName.setText(userInfo.name);
        userAccount.setText(userInfo.account);
        manager = getSupportFragmentManager();
        showFrag();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_left:
                //菜单
                leftMenu.openDrawer(GravityCompat.START);
                break;
            case R.id.imgBtn_right:
                //搜索
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void showFrag() {
        hideAllFrag();
        switch (fragmentAction) {
            case ANNOUNCE_ACTION:
                mainTitle.setText("通知公告");
                if (fragAnnounce == null) {
                    fragAnnounce = new FragAnnounce();
                    manager.beginTransaction().add(R.id.frame_main, fragAnnounce).commit();
                } else {
                    manager.beginTransaction().show(fragAnnounce).commit();
                }
                break;
            case LIBRARY_ACTION:
                mainTitle.setText("图书借阅信息");
                if (fragLibrary == null) {
                    fragLibrary = new FragLibrary();
                    fragLibrary.setGetInfoCallBack(MainActivity.this);
                    manager.beginTransaction().add(R.id.frame_main, fragLibrary).commit();
                } else {
                    manager.beginTransaction().show(fragLibrary).commit();
                }
                break;
            case EDUCATION_ACTION:
                mainTitle.setText("教务信息");
                break;
            case NET_EDUCATION_ACTION:
                mainTitle.setText("网络教学平台信息");
                break;
            case SETTING_ACTION:
                mainTitle.setText("设置");
                break;
            case FEEDBACK_ACTION:
                mainTitle.setText("反馈");
                if (fragFeedBack == null) {
                    fragFeedBack = new FragFeedBack();
                    manager.beginTransaction().add(R.id.frame_main, fragFeedBack).commit();
                } else {
                    manager.beginTransaction().show(fragFeedBack).commit();
                }
                break;
            case HELP_ACTION:
                mainTitle.setText("帮助");
                if (fragHelp == null) {
                    fragHelp = new FragHelp();
                    manager.beginTransaction().add(R.id.frame_main, fragHelp).commit();
                } else {
                    manager.beginTransaction().show(fragHelp).commit();
                }
                break;
            case ABOUT_ACTION:
                mainTitle.setText("关于");
                if (fragAbout == null) {
                    fragAbout = new FragAbout();
                    manager.beginTransaction().add(R.id.frame_main, fragAbout).commit();
                } else {
                    manager.beginTransaction().show(fragAbout).commit();
                }
                break;
        }
    }

    private void hideAllFrag() {
        if (fragAnnounce != null) {
            manager.beginTransaction().hide(fragAnnounce).commit();
        }
        if (fragLibrary != null) {
            manager.beginTransaction().hide((fragLibrary)).commit();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (leftMenu.isDrawerOpen(GravityCompat.START)) {
                leftMenu.closeDrawer(GravityCompat.START);
                return true;
            }
            moveTaskToBack(true);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (leftMenu.isDrawerOpen(GravityCompat.START)) {
                leftMenu.closeDrawer(GravityCompat.START);
            } else {
                leftMenu.openDrawer(GravityCompat.START);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void getSuccess(UserInfo userInfo) {
        userName.setText(userInfo.name);
        userAccount.setText(userInfo.account);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            if (userInfo.sex.equals("男")) {
                {
                    userName.setCompoundDrawables(null, null, null, getDrawable(R.mipmap.boy));
                }
            } else {
                userName.setCompoundDrawables(null, null, null, getDrawable(R.mipmap.girl));
            }
    }

    @Override
    public void getFailed() {
        userName.setText("未登录");
        userAccount.setText("");
    }

}
