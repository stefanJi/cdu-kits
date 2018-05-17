package jiyang.cdu.kits.controller;

import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import jiyang.cdu.kits.model.enty.Book;
import jiyang.cdu.kits.model.enty.BookHistory;
import jiyang.cdu.kits.model.enty.CDULinks;
import jiyang.cdu.kits.model.enty.Feed;
import jiyang.cdu.kits.model.enty.LibrarySearchHistory;
import jiyang.cdu.kits.model.enty.LibraryUserInfo;
import jiyang.cdu.kits.model.enty.Release;
import jiyang.cdu.kits.model.enty.zhihu.DailyThemes;
import jiyang.cdu.kits.model.enty.zhihu.Stories;
import jiyang.cdu.kits.model.enty.zhihu.StoryContent;
import okhttp3.ResponseBody;

/**
 * Created by JiYang on 17-9-4.
 * Email: jidaoyang@gmail.com
 */

public class RestApiManager {
    private static final String CDU_FEED_HOST = "http://news.cdu.edu.cn/";
    private static final String CDU_HQC_HOST = "http://hqc.cdu.edu.cn/";
    private static final String ZHIHU_DAILY_HOST = "https://news-at.zhihu.com/api/4/";
    private static final String CDU_HOME = "http://www.cdu.edu.cn/";
    private static final String GITHUB_API = "https://api.github.com/";
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

    public void getBookList(Observer<List<Book>> observer) {
        RetrofitController.getRetrofitInstance().getRestApis()
                .getBookList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, List<Book>>() {
                    @Override
                    public List<Book> apply(ResponseBody responseBody) throws Exception {
                        Document document = Jsoup.parse(responseBody.string());
                        Elements elements = document.select("table.table_line");
                        elements = elements.select("tbody");
                        elements = elements.select("tr");
                        List<Book> books = new ArrayList<>();
                        for (int i = 1; i < elements.size(); i++) {
                            Elements elements1 = elements.get(i).select("td");
                            SparseArray<String> data = new SparseArray<>();
                            Book book = new Book();
                            for (int j = 0; j < elements1.size(); j++) {
                                data.put(j, elements1.get(j).text());
                            }
                            //条码号
                            book.code = data.get(0);
                            //书名
                            book.name = data.get(1);
                            //借阅日期
                            book.getData = data.get(2);
                            //归还日期
                            book.endData = data.get(3);
                            //续借量
                            book.getCount = data.get(4);
                            //抓取续借号
                            Element element = elements1.last();
                            String temp = element.select("input").attr("onclick");
                            temp = temp.replace("getInLib", "");
                            temp = temp.replace("(", "");
                            temp = temp.replace(")", "");
                            temp = temp.replace(",", "");
                            temp = temp.replace(";", "");
                            temp = temp.replace("'", "");
                            temp = temp.replace(book.code, "");
                            temp = temp.substring(0, temp.length() - 1);
                            book.check = temp;
                            books.add(book);
                        }
                        return books;
                    }
                }).subscribe(observer);
    }

    public void reBook(Observer<Boolean> observer, final Book book) {
        RetrofitController.getRetrofitInstance()
                .getRestApis()
                .reBook(book.code, book.check)
                .map(new Function<ResponseBody, Boolean>() {
                    @Override
                    public Boolean apply(ResponseBody responseBody) throws Exception {
                        Document document = Jsoup.parse(responseBody.string());
                        String info = document.getElementsByTag("body").first().select("font").first().text();
                        if (info.contains("不得续借")) {
                            throw new Exception(info);
                        }
                        return Boolean.TRUE;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void libraryHistory(Observer<List<BookHistory>> observer) {
        final String para = "all";
        final int toPage = 1;
        RetrofitController.getRetrofitInstance().getRestApis()
                .libraryHistory(para, toPage)
                .map(new Function<ResponseBody, List<BookHistory>>() {
                    @Override
                    public List<BookHistory> apply(ResponseBody responseBody) throws Exception {
                        List<BookHistory> histories = new ArrayList<>();
                        Document document = Jsoup.parse(responseBody.string());
                        Elements elements = document.select("#mylib_content")
                                .select("table")
                                .select("tbody")
                                .select("tr");
                        for (int i = 1; i < elements.size(); i++) {
                            Elements elements1 = elements.get(i).select("td");
                            BookHistory bookHistory = new BookHistory();
                            SparseArray<String> data = new SparseArray<>();
                            for (int j = 0; j < elements1.size(); j++) {
                                data.put(j, elements1.get(j).text());
                            }
                            bookHistory.historyId = data.get(0);
                            bookHistory.name = data.get(2);
                            bookHistory.getData = data.get(4);
                            bookHistory.endData = data.get(5);
                            String url = elements1.get(2).select("a").first().attr("href");
                            url = url.replace("..", "");
                            url = "http://202.115.80.170:8080" + url;
                            bookHistory.url = url;
                            histories.add(bookHistory);
                        }
                        return histories;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void librarySearchHistory(Observer<List<LibrarySearchHistory>> observer) {
        RetrofitController.getRetrofitInstance().getRestApis()
                .librarySearchHistory()
                .map(new Function<ResponseBody, List<LibrarySearchHistory>>() {
                    @Override
                    public List<LibrarySearchHistory> apply(ResponseBody responseBody) throws Exception {
                        Document document = Jsoup.parse(responseBody.string());
                        Elements elements = document.select("table").select("tbody").select("tr");
                        List<LibrarySearchHistory> histories = new ArrayList<>();
                        for (int i = 1; i < elements.size(); i++) {
                            Element element = elements.get(i);
                            Elements es = element.select("td");
                            LibrarySearchHistory searchHistory = new LibrarySearchHistory();
                            searchHistory.searchKey = es.get(1).text();
                            searchHistory.searchDate = es.get(2).text();
                            histories.add(searchHistory);
                        }
                        return histories;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    public void searchBook(Observer<List<Book>> observer, String key, String searchType,
                           String docType, String matchFlag, int page) {
        RetrofitController.getRetrofitInstance()
                .getRestApis().searchBook(searchType, matchFlag, docType, page, key)
                .map(new Function<ResponseBody, List<Book>>() {
                    @Override
                    public List<Book> apply(ResponseBody responseBody) throws Exception {
                        Document document = Jsoup.parse(responseBody.string());
                        String info = null;
                        Elements elements = document.select("li.book_list_info");
                        List<Book> books = new ArrayList<>();
                        for (int i = 0; i < elements.size(); i++) {
                            Element element = elements.get(i);
                            String name = element.select("h3").select("a").text();
                            String url = element.select("h3").select("a").attr("href");
                            String code = element.select("h3").text().replace(name, "");
                            String count = element.select("p").select("span").text();
                            Book book = new Book(code, name, count);
                            book.url = "http://202.115.80.170:8080/opac/" + url;
                            books.add(book);
                        }
                        if (books.size() > 0) {
                            Elements elements1 = document.select("div.book_article");
                            Element element = elements1.get(0);
                            elements1 = element.select("div");
                            info = elements1.get(1).text();
                        }
                        return books;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void fetchZhihuDailyThemes(Observer<DailyThemes> observer) {
        RetrofitController.getRetrofitInstance(ZHIHU_DAILY_HOST)
                .getRestApis().fetchThemes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void fetchZhihuDailyStories(Observer<Stories> observer, long themeId) {
        RetrofitController.getRetrofitInstance(ZHIHU_DAILY_HOST)
                .getRestApis().fetchStories(themeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void fetchZhihuDailyStory(Observer<StoryContent> observer, long id) {
        RetrofitController.getRetrofitInstance(ZHIHU_DAILY_HOST)
                .getRestApis().fetchStory(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void fetchCDULinks(Observer<List<CDULinks>> observer) {
        RetrofitController.getRetrofitInstance(CDU_HOME)
                .getRestApis().fetchCDULinks("")
                .map(new Function<ResponseBody, List<CDULinks>>() {
                    @Override
                    public List<CDULinks> apply(ResponseBody responseBody) throws Exception {
                        List<CDULinks> links = new ArrayList<>();
                        Document document = Jsoup.parse(responseBody.string());
                        Elements elements = document.select("a");
                        for (Element e : elements) {
                            CDULinks link = new CDULinks();
                            if (e.attr("href") == null) {
                                continue;
                            }
                            String href = e.attr("href");
                            if (TextUtils.isEmpty(href) || href.startsWith("#")) {
                                continue;
                            }
                            if (!href.contains("http")) {
                                href = CDU_HOME + href;
                            }
                            link.url = href;
                            if (e.attr("title") == null) {
                                continue;
                            }
                            String title = e.text();
                            if (TextUtils.isEmpty(title)) {
                                continue;
                            }
                            link.title = e.text();
                            links.add(link);
                        }
                        return links;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void fetchReleasesVersion(Observer<Release> observer) {
        RetrofitController.getRetrofitInstance(GITHUB_API)
                .getRestApis().fetchReleasesVersion()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
