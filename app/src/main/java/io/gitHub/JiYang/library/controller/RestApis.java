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
    @Headers("content-type: application/x-www-form-urlencoded")
    @POST("reader/redr_verify.php")
    Observable<ResponseBody> loginLib(@Field("number") String number,
                                      @Field("select") String type,
                                      @Field("returnUrl") String returnUrl,
                                      @Field("passwd") String pwd);

    @GET("index.php?m=announce&a=slist&cat_id=1")
    Observable<ResponseBody> getAnnounce(@Query("page") int page);
}
