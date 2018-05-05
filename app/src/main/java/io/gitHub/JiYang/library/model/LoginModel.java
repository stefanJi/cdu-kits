package io.gitHub.JiYang.library.model;

import io.gitHub.JiYang.library.presenter.LoginLibrary.OnLoginListener;

public interface LoginModel {
    void login(String account, String password, OnLoginListener loginListener);
}
