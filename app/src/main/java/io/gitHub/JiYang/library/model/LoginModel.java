package io.gitHub.JiYang.library.model;

import io.gitHub.JiYang.library.presenter.library.OnLoginListener;

public interface LoginModel {
    void login(String account, String password, String type, OnLoginListener loginListener);
}
