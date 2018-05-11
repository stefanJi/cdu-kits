package io.gitHub.JiYang.library.presenter.library;

import io.gitHub.JiYang.library.model.LoginModel;
import io.gitHub.JiYang.library.model.enty.LibraryUserInfo;
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
    public void login(String account, String password, String type) {
        loginLibraryView.showLoginProgress();
        loginModel.login(account, password, type, this);
    }

    @Override
    public void onSuccess(LibraryUserInfo userInfo) {
        loginLibraryView.hideLoginProgress();
        loginLibraryView.showLoginSuccess(userInfo);
    }

    @Override
    public void onError(String error) {
        loginLibraryView.hideLoginProgress();
        loginLibraryView.showLoginError(error);
    }
}
