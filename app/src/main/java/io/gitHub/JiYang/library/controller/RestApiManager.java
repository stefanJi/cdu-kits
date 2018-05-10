package io.gitHub.JiYang.library.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.gitHub.JiYang.library.model.enty.Feed;
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
    private static final String CDU_FEED_HOST = "http://news.cdu.edu.cn/";
    private static final String CDU_HQC_HOST = "http://hqc.cdu.edu.cn/";
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

    public void getFeeds(Observer<List<Feed>> observer, String newsType, int page, int cat) {
        RetrofitController.getRetrofitInstance(CDU_FEED_HOST)
                .getRestApis().getFeeds(newsType, page, cat)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<ResponseBody, List<Feed>>() {
                    @Override
                    public List<Feed> apply(ResponseBody responseBody) throws IOException {
                        Document document = Jsoup.parse(responseBody.string());
                        if (document == null) {
                            return null;
                        }
                        Elements elements = document.select("ul.w915").select("li");
                        List<Feed> feeds = new ArrayList<>();
                        for (int i = 0; i < elements.size(); i++) {
                            String name = elements.get(i).select("a").text();
                            String all = elements.get(i).select("span").text();
                            String data = all.replace("[通知公告]", "");
                            String url = elements.get(i).select("a").attr("href");
                            feeds.add(new Feed(name, data, url));
                        }
                        return feeds;
                    }
                })
                .subscribe(observer);
    }

    public void getHQCFeeds(Observer<List<Feed>> observer, int page) {
        RetrofitController.getRetrofitInstance(CDU_HQC_HOST)
                .getRestApis().getHqcFeeds(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<ResponseBody, List<Feed>>() {
                    @Override
                    public List<Feed> apply(ResponseBody responseBody) throws IOException {
                        Document document = Jsoup.parse(responseBody.string());
                        if (document == null) {
                            return null;
                        }
                        Elements elements = document.select("div.dd").select("ul.list").select("li");
                        List<Feed> feeds = new ArrayList<>();
                        for (int i = 0; i < elements.size(); i++) {
                            String name = elements.get(i).select("a").text();
                            String data = elements.get(i).select("span").text();
                            String url = "http://hqc.cdu.edu.cn" + elements.get(i).select("a").attr("href");
                            feeds.add(new Feed(name, data, url));
                        }
                        return feeds;
                    }
                })
                .subscribe(observer);
    }
}
