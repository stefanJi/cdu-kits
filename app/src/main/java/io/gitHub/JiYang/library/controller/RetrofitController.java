package io.gitHub.JiYang.library.controller;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by JiYang on 17-7-27.
 * Email: jiyang@idealens.com
 */
class RetrofitController {
    private static final long DEF_TIME_OUT_DELAY = 5;   // seconds
    private Retrofit mRetrofit;
    private RestApis mRestApis;
    private OkHttpClient mClient;
    private static String mBaseUrl;

    private static RetrofitController retrofitInstance;

    static RetrofitController getRetrofitInstance() {
        String baseUrl = "http://202.115.80.170:8080/";
        if (TextUtils.isEmpty(baseUrl)) {
            try {
                throw new Exception("retrofit base url empty!!!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (retrofitInstance == null) {
            retrofitInstance = new RetrofitController(baseUrl, RestApis.class);
        } else {
            if (!baseUrl.equals(mBaseUrl)) {
                retrofitInstance.refreshSelf(baseUrl, RestApis.class);
            }
        }
        return retrofitInstance;
    }

    private RetrofitController(String baseUrl, Class restApis) {
        mBaseUrl = baseUrl;
        initRetrofit(mBaseUrl, restApis);
    }

    private void initRetrofit(String mBaseUrl, Class restApis) {
        mRetrofit = new Retrofit.Builder()
                .client(getOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(mBaseUrl)
                .build();
        mRestApis = (RestApis) mRetrofit.create(restApis);
    }

    private OkHttpClient getOkHttpClient() {
        if (mClient == null) {
            mClient = new OkHttpClient.Builder()
                    .connectTimeout(DEF_TIME_OUT_DELAY, TimeUnit.SECONDS)
                    .writeTimeout(DEF_TIME_OUT_DELAY, TimeUnit.SECONDS)
                    .readTimeout(DEF_TIME_OUT_DELAY, TimeUnit.SECONDS)
                    .build();
        }
        return mClient;
    }

    private void refreshSelf(String baseUrl, Class apiClass) {
        releaseRetrofit();
        initRetrofit(baseUrl, apiClass);
    }

    private void releaseRetrofit() {
        mRetrofit = null;
        mRestApis = null;
    }

    RestApis getRestApis() {
        return mRestApis;
    }

    private class AddCookiesInterceptor implements Interceptor {

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            return null;
        }
    }
}
