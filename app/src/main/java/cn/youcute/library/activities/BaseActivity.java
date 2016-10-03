package cn.youcute.library.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.youcute.library.AppControl;

/**
 * Created by jy on 2016/9/27.
 * Activity基类
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppControl.getInstance().initNetRequest(BaseActivity.this);//要使用Dialog必须先初始化单例
    }

    /**
     * 隐藏叫车对话框软键盘
     */
    public void hideKeyBord() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
