package io.gitHub.JiYang.library.presenter.impl;

import io.gitHub.JiYang.library.model.LoginModel;
import io.gitHub.JiYang.library.model.impl.LoginModelImpl;
import io.gitHub.JiYang.library.presenter.LoginPresenter;
import io.gitHub.JiYang.library.presenter.OnLoginListener;
import io.gitHub.JiYang.library.ui.view.LoginView;

public class LoginPresenterImpl implements LoginPresenter, OnLoginListener {
    // presenter作为中间层，持有 View 和 Model 的引用
    private LoginView loginView;
    private LoginModel loginModel;


    public LoginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
        loginModel = new LoginModelImpl();
    }

    @Override
    public void login(String account, String password) {
        loginView.showLoading();
        loginModel.login(account, password, this);
    }

    @Override
    public void onSuccess() {
        loginView.hideLoading();
        loginView.showSuccess();
    }

    @Override
    public void onError(String error) {
        loginView.hideLoading();
        loginView.showError(error);
    }
}
