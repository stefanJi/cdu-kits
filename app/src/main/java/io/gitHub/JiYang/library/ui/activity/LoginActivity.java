package io.gitHub.JiYang.library.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.gitHub.JiYang.library.R;
import io.gitHub.JiYang.library.presenter.LoginLibrary.LoginPresenter;
import io.gitHub.JiYang.library.presenter.LoginLibrary.LoginPresenterImpl;
import io.gitHub.JiYang.library.ui.common.BaseActivity;
import io.gitHub.JiYang.library.ui.view.LoginView;

public class LoginActivity extends BaseActivity implements LoginView, View.OnClickListener {
    private ProgressDialog loadingDialog;

    private LoginPresenter loginPresenter;
    private EditText passwordInput;
    private EditText accountInput;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sign_library);
        init();
    }

    private void init() {
        accountInput = findViewById(R.id.et_account);
        passwordInput = findViewById(R.id.et_password);
        Button loginBtn = findViewById(R.id.btn_sign);
        loginBtn.setOnClickListener(this);
        loadingDialog = new ProgressDialog(this);
        loadingDialog.setTitle("登录中");
        loadingDialog.setCancelable(false);
        loginPresenter = new LoginPresenterImpl(this);
    }


    @Override
    public void showLoading() {
        loadingDialog.show();
    }

    @Override
    public void hideLoading() {
        loadingDialog.dismiss();
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccess() {
        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign:
                String account = accountInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
                    Toast.makeText(this, "输入空", Toast.LENGTH_SHORT).show();
                    return;
                }
                loginPresenter.login(account, password);
                break;
        }
    }
}
