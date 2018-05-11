package io.gitHub.JiYang.library.controller;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

interface RestApis {

    @FormUrlEncoded
    @Headers({"user-agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36"})
    @POST("reader/redr_verify.php")
    Observable<ResponseBody> loginLib(@Field("number") String number,
                                      @Field("passwd") String pwd,
                                      @Field("select") String type,
                                      @Field("returnUrl") String returnUrl);

    @GET("index.php?a=slist")
    Observable<ResponseBody> getFeeds(
            @Query("m") String newsType,
            @Query("page") int page,
            @Query("cat_id") int cat_id
    );

    @GET("index.php?a=lists&catid=13")
    Observable<ResponseBody> getHqcFeeds(
            @Query("page") int page
    );
}
