package io.gitHub.JiYang.library.presenter.library;

import io.gitHub.JiYang.library.model.LoginModel;
import io.gitHub.JiYang.library.model.impl.LoginModelImpl;
import io.gitHub.JiYang.library.ui.view.LoginLibraryView;

public class LoginPresenterImpl implements LoginPresenter, OnLoginListener {
    // presenter作为中间层，持有 View 和 Model 的引用
    private LoginLibraryView loginLibraryView;
    private LoginModel loginModel;


    public LoginPresenterImpl(LoginLibraryView loginLibraryView) {
        this.loginLibraryView = loginLibraryView;
        loginModel = new LoginModelImpl();
    }

    @Override
    public void login(String account, String password) {
        loginLibraryView.showLoginProgress();
        loginModel.login(account, password, this);
    }

    @Override
    public void onSuccess() {
        loginLibraryView.hideLoginProgress();
        loginLibraryView.showLoginSuccess();
    }

    @Override
    public void onError(String error) {
        loginLibraryView.hideLoginProgress();
        loginLibraryView.showLoginError(error);
    }
}
