package jiyang.cdu.kits.controller;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 */
class RetrofitController {
    private static final long DEF_TIME_OUT_DELAY = 20;   // seconds
    private Retrofit mRetrofit;
    private RestApis mRestApis;
    private OkHttpClient mClient;
    private static Map<String, RetrofitController> retrofitControllerMap = new HashMap<>();

    static RetrofitController getRetrofitInstance() {
        String baseUrl = "http://202.115.80.170:8080/";
        return getRetrofitInstance(baseUrl);
    }

    static RetrofitController getRetrofitInstance(String baseUrl) {
        if (TextUtils.isEmpty(baseUrl)) {
            try {
                throw new Exception("retrofit base url empty!!!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        RetrofitController retrofitController = retrofitControllerMap.get(baseUrl);
        if (retrofitController == null) {
            retrofitController = new RetrofitController(baseUrl, RestApis.class);
            retrofitControllerMap.put(baseUrl, retrofitController);
        }
        return retrofitController;
    }

    private RetrofitController(String baseUrl, Class restApis) {
        initRetrofit(baseUrl, restApis);
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
                            if (re.body() != null)
                                Log.i("TAG", re.body().toString());
                            Log.i("TAG", re.headers().toString());
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

    private void releaseRetrofit() {
        mRetrofit = null;
        mRestApis = null;
    }

    RestApis getRestApis() {
        return mRestApis;
    }
}
