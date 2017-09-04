package io.gitHub.JiYang.library.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import io.gitHub.JiYang.library.bean.User;
import io.gitHub.JiYang.library.controller.RestApiManager;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by JiYang on 17-9-4.
 * Email: jiyang@idealens.com
 */

public class AcHome extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Observer<Boolean> observer = new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Boolean value) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        User user = new User("201410421111", "jiyang147852");
        RestApiManager.getInstance().loginLib(observer, user);
    }


}
