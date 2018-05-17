package jiyang.cdu.kits.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioGroup;
import android.widget.Toast;

import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import jiyang.cdu.kits.AppControl;
import jiyang.cdu.kits.R;
import jiyang.cdu.kits.databinding.ActivityLoginLibraryBinding;
import jiyang.cdu.kits.model.enty.LibraryUserInfo;
import jiyang.cdu.kits.presenter.library.login.LoginPresenter;
import jiyang.cdu.kits.presenter.library.login.LoginPresenterImpl;
import jiyang.cdu.kits.ui.common.BaseActivity;
import jiyang.cdu.kits.ui.view.library.LoginLibraryView;
import jiyang.cdu.kits.ui.widget.UiUtils;
import jiyang.cdu.kits.util.SpUtil;

import static jiyang.cdu.kits.Constant.HAD_LOGIN;
import static jiyang.cdu.kits.Constant.LIBRARY_ACCOUNT;
import static jiyang.cdu.kits.Constant.LIBRARY_ACCOUNT_TYPE;
import static jiyang.cdu.kits.Constant.LIBRARY_PASSWORD;
import static jiyang.cdu.kits.Constant.LIBRARY_USER_NAME;


public class LoginLibraryActivity extends BaseActivity<LoginLibraryView, LoginPresenterImpl> implements LoginLibraryView, View.OnClickListener {

    public static void start(Context context) {
        context.startActivity(new Intent(context, LoginLibraryActivity.class));
    }

    private ActivityLoginLibraryBinding loginLibraryBinding;
    private String account, password, loginType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginLibraryBinding = DataBindingUtil.setContentView(this, R.layout.activity_login_library);
        SpUtil spUtil = AppControl.getInstance().getSpUtil();
        account = spUtil.getString(LIBRARY_ACCOUNT);
        password = spUtil.getString(LIBRARY_PASSWORD);
        loginType = LoginPresenter.TYPE_CERT_ON;

        if (!TextUtils.isEmpty(account)) {
            loginLibraryBinding.etAccount.setText(account);
        }
        if (!TextUtils.isEmpty(password)) {
            loginLibraryBinding.etPassword.setText(password);
        }

        loginLibraryBinding.btnLoginLibrary.setOnClickListener(this);
        loginLibraryBinding.cancelLogin.setOnClickListener(this);
        loginLibraryBinding.loginLibraryUserTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.loginByCard:
                        loginType = LoginPresenter.TYPE_CERT_ON;
                        break;
                    case R.id.loginByEmail:
                        loginType = LoginPresenter.TYPE_EMAIL;
                        break;
                    case R.id.loginBySerNumber:
                        loginType = LoginPresenter.TYPE_SER_NUM;
                        break;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        JAnalyticsInterface.onPageStart(this, "LoginLibraryActivityStart");
    }

    @Override
    public LoginPresenterImpl initPresenter() {
        return new LoginPresenterImpl();
    }

    private void login() {
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password) || TextUtils.isEmpty(loginType)) {
            UiUtils.showErrorSnackbar(this, loginLibraryBinding.getRoot(), "输入不完整");
            return;
        }
        loginLibraryBinding.etAccount.clearFocus();
        loginLibraryBinding.etPassword.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(loginLibraryBinding.etPassword.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(loginLibraryBinding.etAccount.getWindowToken(), 0);
        }
        presenter.login(account, password, loginType);
    }

    @Override
    public void showLoginProgress() {
        loginLibraryBinding.progress.setVisibility(View.VISIBLE);
        loginLibraryBinding.btnLoginLibrary.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideLoginProgress() {
        loginLibraryBinding.progress.setVisibility(View.INVISIBLE);
        loginLibraryBinding.btnLoginLibrary.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoginError(String error) {
        UiUtils.showErrorSnackbar(this, loginLibraryBinding.getRoot(), error);
    }

    @Override
    public void showLoginSuccess(LibraryUserInfo userInfo) {
        if (userInfo.account != null) {
            SpUtil sp = AppControl.getInstance().getSpUtil();
            sp.setBool(HAD_LOGIN, true);
            sp.setString(LIBRARY_ACCOUNT_TYPE, loginType);
            sp.setString(LIBRARY_ACCOUNT, account);
            sp.setString(LIBRARY_PASSWORD, password);
            sp.setString(LIBRARY_USER_NAME, userInfo.name);
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            UiUtils.showErrorSnackbar(this, loginLibraryBinding.getRoot(), "登录失败");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        JAnalyticsInterface.onPageEnd(this, "LoginLibraryActivityStop");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLoginLibrary:
                account = loginLibraryBinding.etAccount.getText().toString();
                password = loginLibraryBinding.etPassword.getText().toString();
                login();
                break;
            case R.id.cancelLogin:
                finish();
                break;
        }
    }
}
