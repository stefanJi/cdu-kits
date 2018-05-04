package io.gitHub.JiYang.library.controller;

import android.util.Log;

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


    public void loginLib(Observer<Boolean> observer, String account, final String password) {
        RetrofitController.getRetrofitInstance()
                //登录类型：cert_no学号 email邮箱
                .getRestApis()
                .loginLib(account, "cert_no", "", password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<ResponseBody, Boolean>() {
                    @Override
                    public Boolean apply(ResponseBody body) throws Exception {
                        String page = body.string().replace(" ", "").replace("\n", "");
                        return !page.contains("登录");
                    }
                })
                .subscribe(observer);
    }
}
