package cn.youcute.library.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.youcute.library.AppControl;
import cn.youcute.library.R;
import cn.youcute.library.bean.User;
import cn.youcute.library.diyView.XEditText;
import cn.youcute.library.util.NetRequest;
import cn.youcute.library.util.SpUtil;

/**
 * Created by jy on 2016/9/30.
 * 登录
 */

public class SignActivity extends BaseActivity implements NetRequest.SignCallBack {
    private EditText etAccount;
    private XEditText etPassword;
    private Button btnSign;
    private SpUtil spUtil;
    private String account, password;
    private boolean isShowPassword = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int action = getIntent().getIntExtra("sign_action", 0);
        if (action == 0) {
            setContentView(R.layout.activity_sign_library);
            initView1();
        } else if (action == 1) {
            setContentView(R.layout.activity_sign_education);
            initView2();
        }
    }

    //登录图书馆
    private void initView1() {
        spUtil = AppControl.getInstance().getSpUtil();
        etAccount = (EditText) findViewById(R.id.et_account);
        etPassword = (XEditText) findViewById(R.id.et_password);
        etPassword.setDrawableRightListener(new XEditText.DrawableRightListener() {
            @Override
            public void onDrawableRightClick(View view) {
                if (isShowPassword) {
                    //隐藏密码
                    isShowPassword = false;
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_eye, 0);
                } else {
                    //显示密码
                    isShowPassword = true;
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_eye_close, 0);
                }
            }
        });
        btnSign = (Button) findViewById(R.id.btn_sign);
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account = etAccount.getText().toString();
                password = etPassword.getText().toString();
                if (account.equals("") || password.equals("")) {
                    AppControl.getInstance().showToast("输入有误");
                    return;
                }
                AppControl.getInstance().getNetRequest().sign(account, password, SignActivity.this);
            }
        });
        //将本地保存的账户设置到输入框
        User user = spUtil.getUser();
        if (!user.account.equals("0")) {
            etAccount.setText(user.account);
            etPassword.setText(user.password);
        }
    }

    //登录教务系统
    private void initView2() {

    }

    @Override
    public void signSuccess(String session) {
        Intent intent = new Intent(SignActivity.this, MainActivity.class);
        intent.putExtra("session", session);
        setResult(1, intent);
        finish();
    }

    @Override
    public void signFailed() {
        AppControl.getInstance().showToast("登录失败，请重试");
    }


}
