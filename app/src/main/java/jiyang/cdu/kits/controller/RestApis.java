package jiyang.cdu.kits.controller;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

interface RestApis {

    //    登录图书馆
    @FormUrlEncoded
    @Headers({"user-agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36"})
    @POST("reader/redr_verify.php")
    Observable<ResponseBody> loginLib(@Field("number") String number,
                                      @Field("passwd") String pwd,
                                      @Field("select") String type,
                                      @Field("returnUrl") String returnUrl);

    //通知公告
    @GET("index.php?a=slist")
    Observable<ResponseBody> getFeeds(
            @Query("m") String newsType,
            @Query("page") int page,
            @Query("cat_id") int cat_id
    );

    //后勤处公告
    @GET("index.php?a=lists&catid=13")
    Observable<ResponseBody> getHqcFeeds(
            @Query("page") int page
    );

    //图书馆当前借阅
    @GET("reader/book_lst.php")
    Observable<ResponseBody> getBookList();

    //图书馆续借
    @GET("reader/ajax_renew.php")
    Observable<ResponseBody> reBook(
            @Query("bar_code") String barCode,
            @Query("check") String check
    );

    //图书馆历史借阅
    @FormUrlEncoded
    @Headers({"user-agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36"})
    @POST("reader/book_hist.php")
    Observable<ResponseBody> libraryHistory(
            @Field("para_string") String para,
            @Field("topage") int toPage
    );


    //图书馆搜索历史
    @GET("reader/search_hist.php")
    Observable<ResponseBody> librarySearchHistory();

    //搜索图书
    @GET("opac/openlink.php?historyCount=1&with_ebook=on&displaypg=20&showmode=list&sort=CATA_DATE&orderby=desc&location=ALL")
    Observable<ResponseBody> searchBook(
            @Query("strSearchType") String searchType,
            @Query("match_flag") String matchFlag,
            @Query("doctype") String docType,
            @Query("page") int page,
            @Query("strText") String strText
    );

}
