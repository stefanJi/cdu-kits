package cn.youcute.library.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import cn.youcute.library.R;
import cn.youcute.library.activities.fragments.FragHome;
import cn.youcute.library.activities.fragments.FragMine;
import cn.youcute.library.activities.fragments.FragScan;

/**
 * Created by jy on 2016/10/2.
 * 主界面
 */

public class ActivityMain extends BaseActivity {
    private FragmentManager manager;
    private FragHome home;
    private FragScan scan;
    private FragMine mine;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
    }

    private void initView() {
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
                    case R.id.radio_scan:
                        showScan();
                        break;
                    case R.id.radio_mine:
                        showMine();
                        break;
                }
            }
        });
        ((RadioButton) findViewById(R.id.radio_home)).setChecked(true);
        showHome();
    }

    private void hideAllFragment() {
        if (home != null) {
            manager.beginTransaction().hide(home).commit();
        }
        if (scan != null) {
            manager.beginTransaction().hide(scan).commit();
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
    }

    private void showScan() {
        if (scan == null) {
            scan = new FragScan();
            manager.beginTransaction().add(R.id.main_frame, scan).commit();
        } else {
            manager.beginTransaction().show(scan).commit();
        }
    }

    private void showMine() {
        if (mine == null) {
            mine = new FragMine();
            manager.beginTransaction().add(R.id.main_frame, mine).commit();
        } else {
            manager.beginTransaction().show(mine).commit();
        }
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
