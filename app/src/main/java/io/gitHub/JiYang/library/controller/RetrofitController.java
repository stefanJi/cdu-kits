package io.gitHub.JiYang.library.controller;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by JiYang on 17-7-27.
 * Email: jiyang@idealens.com
 */
class RetrofitController {
    private static final long DEF_TIME_OUT_DELAY = 10;   // seconds
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
                    .readTimeout(DEF_TIME_OUT_DELAY, TimeUnit.SECONDS)
                    .addNetworkInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request re = chain.request();
                            Log.i("TAG1", "*********REQUEST********");
                            Log.i("TAG1", re.headers().toString());
                            return chain.proceed(re);
                        }
                    }).cookieJar(new CookieJar() {
                        private final HashMap<String, List<Cookie>> cookieMap = new HashMap<>();

                        @Override
                        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                            cookieMap.put(url.host(), cookies);
                        }

                        @Override
                        public List<Cookie> loadForRequest(HttpUrl url) {
                            List<Cookie> cookies = cookieMap.get(url.host());
                            return cookies != null ? cookies : new ArrayList<Cookie>();
                        }
                    })
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
}
