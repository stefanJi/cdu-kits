package io.gitHub.JiYang.library.model.impl;

import io.gitHub.JiYang.library.controller.RestApiManager;
import io.gitHub.JiYang.library.model.LoginModel;
import io.gitHub.JiYang.library.model.enty.LibraryUserInfo;
import io.gitHub.JiYang.library.presenter.library.OnLoginListener;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LoginModelImpl implements LoginModel {

    @Override
    public void login(String account, String password, String type, final OnLoginListener loginListener) {
        RestApiManager.getInstance().loginLib(new Observer<LibraryUserInfo>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(LibraryUserInfo userInfo) {
                if (userInfo != null && userInfo.account != null) {
                    loginListener.onSuccess(userInfo);
                } else {
                    loginListener.onError("登录失败");
                }
            }

            @Override
            public void onError(Throwable e) {
                loginListener.onError(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        }, account, password, type);
    }
}
