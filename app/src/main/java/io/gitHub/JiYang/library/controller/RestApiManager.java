package io.gitHub.JiYang.library.controller;

import android.util.Log;

import io.gitHub.JiYang.library.bean.User;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by JiYang on 17-9-4.
 * Email: jiyang@idealens.com
 */

public class RestApiManager {
    private static RestApiManager mInstance;

    private RestApiManager() {

    }

    public synchronized static RestApiManager getInstance() {
        if (mInstance == null) {
            mInstance = new RestApiManager();
        }
        return mInstance;
    }


    public void loginLib(Observer<Boolean> observer, User user) {
        RetrofitController.getRetrofitInstance()
                //登录类型：cert_no学号 email邮箱
                .getRestApis()
                .loginLib(user.account, "cert_no", user.password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<ResponseBody, Boolean>() {
                    @Override
                    public Boolean apply(ResponseBody body) throws Exception {
                        Log.i("TAG", body.string());
                        return null;
                    }
                })
                .subscribe(observer);
    }
}
