package io.gitHub.JiYang.library.controller;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

interface RestApis {

    @FormUrlEncoded
    @POST("reader/redr_verify.php")
    Observable<ResponseBody> loginLib(@Field("number") String number,
                                      @Field("select") String type,
                                      @Field("passwd") String pwd);
}
