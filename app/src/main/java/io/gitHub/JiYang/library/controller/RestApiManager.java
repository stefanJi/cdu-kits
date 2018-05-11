package io.gitHub.JiYang.library.controller;

import android.util.SparseArray;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.gitHub.JiYang.library.model.enty.Feed;
import io.gitHub.JiYang.library.model.enty.LibraryUserInfo;
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


    public void loginLib(Observer<LibraryUserInfo> observer, final String account, String password, String type) {
        RetrofitController.getRetrofitInstance()
                .getRestApis()
                .loginLib(account, password, type, "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<ResponseBody, LibraryUserInfo>() {
                    @Override
                    public LibraryUserInfo apply(ResponseBody body) throws Exception {
                        String page = body.string();
                        LibraryUserInfo userInfo = new LibraryUserInfo();
                        if (page.contains("登录我的图书馆")) {
                            return userInfo;
                        }
                        Document document = Jsoup.parse(page);
                        Elements elements = document.select("div#mylib_info td");
                        SparseArray<String> array = new SparseArray<>();
                        for (int i = 1; i < elements.size(); i++) {
                            Element element = elements.get(i);
                            String line = element.text();
                            String[] columns = line.split("：");
                            if (columns.length > 1) {
                                array.put(i, columns[1].replace(" ", ""));
                            }
                        }
                        userInfo.setData(array);
                        return userInfo;
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
