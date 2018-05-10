package io.gitHub.JiYang.library.model.impl;

import io.gitHub.JiYang.library.controller.RestApiManager;
import io.gitHub.JiYang.library.model.LoginModel;
import io.gitHub.JiYang.library.presenter.library.OnLoginListener;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LoginModelImpl implements LoginModel {

    @Override
    public void login(String account, String password, final OnLoginListener loginListener) {
        RestApiManager.getInstance().loginLib(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Boolean success) {
                if (success) {
                    loginListener.onSuccess();
                } else {
                    loginListener.onError("帐号密错误");
                }
            }

            @Override
            public void onError(Throwable e) {
                loginListener.onError(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        }, account, password);
    }
}
