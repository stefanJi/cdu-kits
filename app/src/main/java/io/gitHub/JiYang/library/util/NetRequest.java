package io.gitHub.JiYang.library.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.gitHub.JiYang.library.AppControl;
import io.gitHub.JiYang.library.bean.Announce;
import io.gitHub.JiYang.library.model.enty.BookHistory;
import io.gitHub.JiYang.library.model.enty.Book;
import io.gitHub.JiYang.library.bean.BookFine;
import io.gitHub.JiYang.library.bean.User;

/**
 * Created by jy on 2016/9/22.
 * 网络请求
 */
public class NetRequest {
    private static final String SIGN_API = "http://202.115.80.170:8080/reader/redr_verify.php";
    private static final String INFO_API = "http://202.115.80.170:8080/reader/redr_info.php";
    private static final String GET_BOOK_LIST_API = "http://202.115.80.170:8080/reader/book_lst.php";
    private static final String GET_BOOK_HISTORY = "http://202.115.80.170:8080/reader/book_hist.php?page=";
    private static final String BOOK_ACCOUNT_API = "http://202.115.80.170:8080/reader/account.php";
    private static final String BOOK_FINE_API = "http://202.115.80.170:8080/reader/fine_pec.php";
    private static final String SEARCH_BOOK_API = "http://202.115.80.170:8080/opac/openlink.php?strSearchType=title&match_flag=forward&historyCount=1&strText=";
    private static final String SEARCH_BOOK_API2 = "&doctype=ALL&with_ebook=on&displaypg=10&showmode=list&sort=CATA_DATE&orderby=desc&location=ALL&page=";
    private static final String ANNOUNCE_API = "http://news.cdu.edu.cn/index.php?m=announce&a=slist&cat_id=1&page=";
    private static final String SIGN_JIAO_WO = "http://202.115.80.220:8080/default2.aspx";
    private static final String SIGN_CODE = "http://202.115.80.220:8080/CheckCode.aspx";
    public static final String PHP_SESSION_ID = "PHPSESSID";
    public static final String ASP_NET_SESSION = "ASP.NET_SessionId";

    private static final String FEED_BACK = "http://youcute.cn/jy/library/feedBack.php";
    private static final String TO_SERVER = "http://youcute.cn/jy/library/user.php";
    private static final String HOME = "http://news.cdu.edu.cn/index.php?m=album&a=index";

    /**
     * 登录
     */
    public void sign(final String account, final String password, final SignCallBack signCallBack) {
        class SignTask extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... params) {
                Map<String, String> data = new HashMap<>();
                data.put("number", account);
                data.put("select", "cert_no");  //登录类型：cert_no学号 email邮箱
                data.put("passwd", password);
                //登录
                Connection.Response response;
                try {
                    response = Jsoup.connect(SIGN_API)
                            .data(data)
                            .method(Connection.Method.POST)
                            .timeout(10000)
                            .execute();
                    //通过判断返回的接口是否改变判断是否登录成功
                    if (response.url().toString().equals(SIGN_API)) {
                        //账号、密码错误
                        return "0";
                    } else {
                        return response.cookie(PHP_SESSION_ID);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    signCallBack.signFailed("错误响应:" + e.getMessage());
                }
                //服务器访问超时
                return "-1";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                switch (s) {
                    case "0":
                        signCallBack.signFailed("账号或密码错误");
                        break;
                    case "-1":
                        signCallBack.signFailed("服务器访问超时");
                        break;
                    default:
                        signCallBack.signSuccess(s);
                        User user = new User(account, password);
                        AppControl.getInstance().getSpUtil().saveUser(user);
                        AppControl.getInstance().getSpUtil().setIsSign(true);
                        saveUserToServer(account, password);
                        break;
                }
            }
        }
        if (isNetworkConnected()) {
            new SignTask().execute();
        } else {
            ToastUtil.showToast("网络未连接，请检查网络连接");
        }
    }

    /**
     * 登录回调
     */
    public interface SignCallBack {
        void signSuccess(String session);

        void signFailed(String info);
    }


    /**
     * 获取当前借阅
     *
     * @param callBack 回调
     */
    public void getBookList(final GetBookListCallBack callBack) {
        class GetBookListTask extends AsyncTask<Void, Void, List<Book>> {

            @Override
            protected List<Book> doInBackground(Void... params) {
                Connection connection = Jsoup.connect(GET_BOOK_LIST_API);
                connection.cookie(PHP_SESSION_ID, AppControl.getInstance().sessionLibrary);
                List<Book> books = new ArrayList<>();
                try {
                    Document document = connection.get();
                    Elements elements = document.select("table.table_line");
                    elements = elements.select("tbody");
                    elements = elements.select("tr");
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
                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.getBookListFailed(e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Book> list) {
                super.onPostExecute(list);
                if (list != null) {
                    callBack.getBookListSuccess(list);
                } else
                    callBack.getBookListFailed("获取失败,请重试");
            }
        }
        if (isNetworkConnected())
            new GetBookListTask().execute();
        else {
            callBack.getBookListFailed("网络连接失败，请检查网络连接");
        }
    }

    public interface GetBookListCallBack {
        void getBookListSuccess(List<Book> list);

        void getBookListFailed(String info);
    }

    /**
     * 获得历史借阅
     *
     * @param callBack 回调
     */
    public void getBookHistory(final int page, final GetBookHistoryCallBack callBack) {
        class GetBookHistoryTask extends AsyncTask<Void, Void, List<BookHistory>> {

            @Override
            protected List<BookHistory> doInBackground(Void... params) {
                Connection connection = Jsoup.connect(GET_BOOK_HISTORY + String.valueOf(page));
                connection.cookie(NetRequest.PHP_SESSION_ID, AppControl.getInstance().sessionLibrary);
                //获取登录之后的Html
                try {
                    Document document = connection.get();
                    List<BookHistory> histories = new ArrayList<>();
                    Elements elements = document.select("#mylib_content").select("table").select("tbody").select("tr");
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<BookHistory> list) {
                super.onPostExecute(list);
                if (list != null) {
                    callBack.getHistorySuccess(list);
                } else {
                    callBack.getHistoryFailed();
                }
            }
        }
        if (isNetworkConnected())
            new GetBookHistoryTask().execute();
        else {
            callBack.getHistoryFailed();
            ToastUtil.showToast("网络连接失败，请检查网络连接");
        }
    }

    public interface GetBookHistoryCallBack {
        void getHistorySuccess(List<BookHistory> list);

        void getHistoryFailed();
    }

    /**
     * 获得账目清单
     *
     * @param callBack 回调
     */
    public void getBookAccount(final GetBookAccountCallBack callBack) {
        class GetBookAccountTask extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... params) {
                Connection connection = Jsoup.connect(BOOK_ACCOUNT_API);
                connection.cookie(PHP_SESSION_ID, AppControl.getInstance().sessionLibrary);
                connection.timeout(5000);
                try {
                    Document document = connection.get();
                    Log.d("TAG", document.baseUri());
                    Elements elements = document.select("table.table_line").select("tbody").select("tr");
                    return elements.get(2).select("td").text();
                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.getBookAccountFailed();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                callBack.getBookAccountSuccess(s);
            }
        }

        if (isNetworkConnected()) {
            new GetBookAccountTask().execute();
        } else {
            ToastUtil.showToast("网络错误,请检查网络连接");
            callBack.getBookAccountFailed();
        }
    }

    public interface GetBookAccountCallBack {
        void getBookAccountSuccess(String info);

        void getBookAccountFailed();
    }

    /**
     * 获得欠款
     *
     * @param callBack 回调
     */
    public void getBookFine(final GetBookFineCallBack callBack) {
        class GetBookFineTask extends AsyncTask<Void, Void, List<BookFine>> {

            @Override
            protected List<BookFine> doInBackground(Void... params) {
                Connection connection = Jsoup.connect(BOOK_FINE_API);
                connection.cookie(PHP_SESSION_ID, AppControl.getInstance().sessionLibrary);
                try {
                    Document document = connection.get();
                    Elements elements = document.select("table.table_line").select("tbody").select("tr");
                    List<BookFine> bookFines = new ArrayList<>();
                    for (int i = 1; i < elements.size(); i++) {
                        Elements elements1 = elements.get(i).select("td");
                        SparseArray<String> data = new SparseArray<>();
                        for (int j = 0; j < elements1.size(); j++) {
                            data.put(j, elements1.get(j).text());
                        }
                        BookFine bookFine = new BookFine();
                        bookFine.code = data.get(0);
                        bookFine.name = data.get(2);
                        bookFine.getData = data.get(4);
                        bookFine.endData = data.get(5);
                        bookFine.shouldMoney = data.get(7);
                        bookFine.money = data.get(8);
                        bookFine.status = data.get(9);
                        bookFines.add(bookFine);
                    }
                    return bookFines;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<BookFine> bookFines) {
                super.onPostExecute(bookFines);
                if (null == bookFines) {
                    callBack.getBookFineFailed();
                } else
                    callBack.getBookFineSuccess(bookFines);
            }
        }
        if (isNetworkConnected()) {
            new GetBookFineTask().execute();
        } else {
            ToastUtil.showToast("网络错误,请检查网络连接");
            callBack.getBookFineFailed();
        }
    }

    public interface GetBookFineCallBack {
        void getBookFineSuccess(List<BookFine> bookFines);

        void getBookFineFailed();
    }

    /**
     * 续借
     *
     * @param barCode 条码号
     * @param check   续借号
     * @param call    回调
     */
    private static final String RENEW = "http://202.115.80.170:8080/reader/ajax_renew.php";

    public void renewBook(final String barCode, final String check, final RenewBookCall call) {
        class Task extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                Connection connection = Jsoup.connect(RENEW);
                connection.method(Connection.Method.GET);
                connection.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                connection.header("Accept-Encoding", "gzip, deflate, sdch");
                connection.header("Accept-Language", "zh-CN,zh;q=0.8");
                connection.header("Cache-Control", "max-age=0");
                connection.header("Connection", "keep-alive");
                connection.header("Cookie", "PHPSESSID=" + AppControl.getInstance().sessionLibrary);
                connection.header("Host", "202.115.80.170:8080");
                connection.header("Upgrade-Insecure-Requests", "1");
                connection.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36");
                connection.data("bar_code", barCode);
                connection.data("check", check);
                long time = System.currentTimeMillis();
                connection.data("time", String.valueOf(time));
                try {
                    Document document = connection.get();
                    return document.getElementsByTag("body").first().select("font").first().text();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "错误响应:" + e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String string) {
                super.onPostExecute(string);
                call.renewCall(string);
            }
        }
        if (isNetworkConnected()) {
            new Task().execute();
        } else {
            ToastUtil.showToast("网络未连接,请重试");
        }
    }

    public interface RenewBookCall {
        void renewCall(String info);
    }

    /**
     * 搜索
     *
     * @param searchKey 关键字
     * @param page      页码
     * @param callBack  回调
     */
    public void searchBook(final int action, final String searchKey, final int page, final SearchBookCallBack callBack) throws UnsupportedEncodingException {
        final String key = URLEncoder.encode(searchKey, "UTF-8");
        class SearchBookTask extends AsyncTask<Void, Void, List<Book>> {
            private String info = "没有匹配书籍";

            @Override
            protected List<Book> doInBackground(Void... params) {
                Connection connection = Jsoup.connect(SEARCH_BOOK_API + key + SEARCH_BOOK_API2 + page);
                try {
                    Document document = connection.get();
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
                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.searchFailed("错误响应:" + e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Book> books) {
                super.onPostExecute(books);
                if (books == null) {
                    callBack.searchFailed("无数据,请重试");
                } else {
                    callBack.searchSuccess(books, info);
                }
            }
        }
        String url = "https://api.douban.com/v2/book/search?q=" + key + "&start=" + page;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int total = jsonObject.getInt("total");
                    List<Book> books = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("books");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.getString("title");
                        String url = jsonObject.getString("alt");
                        String price = jsonObject.getString("price");
                        String publisher = jsonObject.getString("publisher");
                        Book book = new Book(publisher, name, price);
                        book.url = url;
                        books.add(book);
                    }
                    callBack.searchSuccess(books, "搜索到" + total + "本" + ",与" + searchKey + "有关的书籍");
                } catch (JSONException e) {
                    e.printStackTrace();
                    callBack.searchFailed("错误响应:" + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.searchFailed("错误响应:" + error);
            }
        });
        if (isNetworkConnected()) {
            if (action == 0) {
                new SearchBookTask().execute();
            } else if (action == 1) {

            }
        } else {
            callBack.searchFailed("网络未连接，请检查网络连接");
            ToastUtil.showToast("网络未连接，请检查网络连接");
        }
    }

    public interface SearchBookCallBack {
        void searchSuccess(List<Book> books, String info);

        void searchFailed(String info);
    }

    /**
     * 获取公告
     *
     * @param page 页码
     */
    public void getAnnounce(final int page, final GetAnnounceCallBack callBack) {
        if (!isNetworkConnected()) {
            ToastUtil.showToast("网络错误,请检查网络连接");
            callBack.getAnnounceFailed("网络错误,请检查网络连接");
            return;
        }
//        OkHttpUtils
//                .get()
//                .url(ANNOUNCE_API + String.valueOf(page))
//                .build()
//                .connTimeOut(50000)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e) {
//                        callBack.getAnnounceFailed("错误响应:" + e.getMessage());
//                        call.cancel();
//                    }
//
//                    @Override
//                    public void onResponse(String response) {
//                        Document document = Jsoup.parse(response);
//                        if (document == null) {
//                            callBack.getAnnounceFailed("错误响应:" + "空白返回，请重试");
//                            return;
//                        }
//                        Elements elements = document.select("ul.w915").select("li");
//                        List<Feed> announces = new ArrayList<>();
//                        for (int i = 0; i < elements.size(); i++) {
//                            String name = elements.get(i).select("a").text();
//                            String all = elements.get(i).select("span").text();
//                            String date = all.replace("[通知公告]", "");
//                            String url = elements.get(i).select("a").attr("href");
//                            announces.add(new Feed(name, date, url));
//                        }
//                        callBack.getAnnounceSuccess(announces);
//                    }
//                });

    }

    public interface GetAnnounceCallBack {
        void getAnnounceSuccess(List<Announce> announces);

        void getAnnounceFailed(String info);
    }


    public void signNet(final String account, final String pass, final SignNetCall signNetCall) {
        if (!isNetworkConnected()) {
            ToastUtil.showToast("网络未连接，请检查网络连接");
            return;
        }
//        OkHttpUtils
//                .post()
//                .addParams("IPT_LOGINUSERNAME", account)
//                .addParams("IPT_LOGINPASSWORD", pass)
//                .addHeader("Host", "kcxt.cdu.edu.cn")
//                .addHeader("Referer", "kcxt.cdu.edu.cn/eol/homepage/common/index.jsp")
//                .url("http://kcxt.cdu.edu.cn/eol/homepage/common/login.jsp")
//                .build().connTimeOut(5000)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e) {
//                        signNetCall.signNetFailed("错误响应:" + e.getMessage());
//                        call.cancel();
//                    }
//
//                    @Override
//                    public void onResponse(String response) {
//                        if (response.contains("登录失败")) {
//                            signNetCall.signNetFailed("登录失败,请重试");
//                        } else {
//                            signNetCall.signNetOk();
//                            AppControl.getInstance().getSpUtil().setIsSignNet(true);
//                            AppControl.getInstance().getSpUtil().saveAccount(account);
//                            AppControl.getInstance().getSpUtil().saveNetPass(pass);
//                            Document document = Jsoup.parse(response);
//                            Element element = document.select("div.login-text").select("span").first();
//                            ToastUtil.showToast(element.date() + "欢迎登录");
//                        }
//                    }
//                });
    }

    public interface SignNetCall {
        void signNetOk();

        void signNetFailed(String info);
    }

    public void feedBack(final String content, final String contact, final FeedBackCallBack callBack) {
        StringRequest request = new StringRequest(Request.Method.POST, FEED_BACK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG-response", response);
                        if (response.equals("1")) {
                            callBack.feedBackSuccess();
                        } else {
                            callBack.feedBackError("反馈失败，请重试");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callBack.feedBackError("反馈失败，请重试" + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("feed_content", content);
                map.put("feed_contact", contact);
                return map;
            }
        };
        if (isNetworkConnected()) {

        } else {
            ToastUtil.showToast("网络未连接，请检查网络连接");
        }

    }

    public interface FeedBackCallBack {
        void feedBackSuccess();

        void feedBackError(String error);
    }

    public void saveUserToServer(final String account, final String key) {
        StringRequest request = new StringRequest(Request.Method.POST, TO_SERVER
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TAG", "传送登录信息到服务器,响应码:" + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "传送登录信息到服务器,响应码:" + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("user_account", account);
                map.put("user_key", key);
                return map;
            }
        };

    }

    /**
     * 判断网络是否连接
     *
     * @return 连接返回true, 未连接返回false
     */
    private boolean isNetworkConnected() {
        NetworkInfo networkInfo = ((ConnectivityManager) AppControl.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable();
    }

}
