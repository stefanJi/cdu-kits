package cn.youcute.library.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import cn.youcute.library.R;
import cn.youcute.library.activities.fragments.FragHome;
import cn.youcute.library.activities.fragments.FragMine;

/**
 * Created by jy on 2016/10/2.
 * 主界面
 */

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private FragmentManager manager;
    private FragHome home;
    private FragMine mine;
    private ImageButton buttonLeft, buttonRight;
    private TextView tvTitle;
    private PopupWindow window;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        ((RadioButton) findViewById(R.id.radio_home)).setChecked(true);
        buttonLeft = (ImageButton) findViewById(R.id.imgBtn_left);
        buttonRight = (ImageButton) findViewById(R.id.imgBtn_right);
        tvTitle = (TextView) findViewById(R.id.tv_main_title);

        buttonLeft.setOnClickListener(this);
        buttonRight.setOnClickListener(this);
        //PopWindow
        View popView = LayoutInflater.from(this).inflate(R.layout.pop_home_drop, null, true);
        window = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        window.setAnimationStyle(android.R.anim.fade_in);
        window.setTouchable(true);
        window.setBackgroundDrawable(new ColorDrawable(0x00000000));
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                buttonLeft.setSelected(false);
            }
        });
        TextView announce = (TextView) popView.findViewById(R.id.pop_announce);
        TextView education = (TextView) popView.findViewById(R.id.pop_education);
        TextView netEducation = (TextView) popView.findViewById(R.id.pop_net_education);
        TextView feedBack = (TextView) popView.findViewById(R.id.pop_feedback);
        TextView help = (TextView) popView.findViewById(R.id.pop_help);
        TextView about = (TextView) popView.findViewById(R.id.pop_about);
        class OnClickPop implements View.OnClickListener {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                switch (v.getId()) {
                    case R.id.pop_announce:
                        intent.putExtra("action", MenuActivity.ANNOUNCE);
                        break;
                    case R.id.pop_education:
                        intent.putExtra("action", MenuActivity.EDUCATION);
                        break;
                    case R.id.pop_net_education:
                        intent.putExtra("action", MenuActivity.NET_EDUCATION);
                        break;
                    case R.id.pop_feedback:
                        intent.putExtra("action", MenuActivity.FEEDBACK);
                        break;
                    case R.id.pop_help:
                        intent.putExtra("action", MenuActivity.HELP);
                        break;
                    case R.id.pop_about:
                        intent.putExtra("action", MenuActivity.ABOUT);
                        break;
                }
                startActivity(intent);
            }
        }
        OnClickPop clickPop = new OnClickPop();
        announce.setOnClickListener(clickPop);
        education.setOnClickListener(clickPop);
        netEducation.setOnClickListener(clickPop);
        feedBack.setOnClickListener(clickPop);
        help.setOnClickListener(clickPop);
        about.setOnClickListener(clickPop);
        RadioGroup bottomBar = (RadioGroup) findViewById(R.id.radioGroup_bottom);
        manager = getSupportFragmentManager();
        bottomBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                hideAllFragment();
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.radio_home:
                        showHome();
                        break;
                    case R.id.radio_mine:
                        showMine();
                        break;
                }
            }
        });
        showHome();//显示首页
    }

    private void hideAllFragment() {
        if (home != null) {
            manager.beginTransaction().hide(home).commit();
        }
        if (mine != null) {
            manager.beginTransaction().hide(mine).commit();
        }

    }

    private void showHome() {
        if (home == null) {
            home = new FragHome();
            manager.beginTransaction().add(R.id.main_frame, home).commit();
        } else {
            manager.beginTransaction().show(home).commit();
        }
        tvTitle.setText("图书馆首页");
    }


    private void showMine() {
        if (mine == null) {
            mine = new FragMine();
            manager.beginTransaction().add(R.id.main_frame, mine).commit();
        } else {
            manager.beginTransaction().show(mine).commit();
        }
        tvTitle.setText("我的");
    }

    private void showDropMenu() {
        buttonLeft.setSelected(true);
        window.showAsDropDown(buttonLeft, 20, 10);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (window.isShowing()) {
                window.dismiss();
                return true;
            }
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_left:
                if (window.isShowing()) {
                    window.dismiss();
                    return;
                }
                showDropMenu();
                break;
            case R.id.imgBtn_right:
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
        }
    }

}
