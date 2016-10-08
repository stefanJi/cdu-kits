package cn.youcute.library.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.youcute.library.AppControl;
import cn.youcute.library.bean.Announce;
import cn.youcute.library.bean.Book;
import cn.youcute.library.bean.BookFine;
import cn.youcute.library.bean.History;
import cn.youcute.library.bean.User;
import cn.youcute.library.bean.UserInfo;

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
    private static final String SIGN_JIAO_WO = "http://202.115.80.153/default2.aspx";
    private static final String SIGN_CODE = "http://202.115.80.153/CheckCode.aspx";
    public static final String PHP_SESSION_ID = "PHPSESSID";
    public static final String ASP_NET_SESSION = "ASP.NET_SessionId";
    private static final String FEED_BACK = "http://youcute.cn/jy/library/feedBack.php";
    private static final String TO_SERVER = "http://youcute.cn/jy/library/user.php";
    private Context context;
    private SweetAlertDialog dialog;
    private int signErrorCount = 0;//登录报错计数
    private int getErroorCount = 0;//获取报错计数

    public NetRequest(Context context) {
        this.context = context;
    }

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
                }
                //服务器访问超时
                return "-1";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                switch (s) {
                    case "0":
                        showFailedDialog("登录失败", "检查学号密码，换个姿势，再来一次");
                        signCallBack.signFailed();
                        break;
                    case "-1":
                        signCallBack.signFailed();
                        if (signErrorCount > 3) {
                            showFeedBack();
                            signErrorCount = 0;
                            break;
                        }
                        showErrorDialog("服务器连接失败，请稍候重试");
                        signErrorCount++;
                        break;
                    default:
                        if (!AppControl.getInstance().getSpUtil().getIsSign()) {
                            showOkDialog("登录成功");
                        }
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
            if (!AppControl.getInstance().getSpUtil().getIsSign()) {
                showGetDialog("登录图书馆系统中", "不急，慢慢来");
            }
            new SignTask().execute();
        } else {
            showErrorDialog("网络错误,请检查网络连接");
        }
    }

    /**
     * 登录回调
     */
    public interface SignCallBack {
        void signSuccess(String session);

        void signFailed();
    }

    /**
     * 获得图书馆个人信息
     *
     * @param session         登录之后的session
     * @param getInfoCallBack 回调
     */
    public void getUserInfo(final String session, final GetInfoCallBack getInfoCallBack) {
        class GetTask extends AsyncTask<Void, Void, UserInfo> {

            @Override
            protected UserInfo doInBackground(Void... params) {
                Connection connection = Jsoup.connect(INFO_API);
                connection.cookie(NetRequest.PHP_SESSION_ID, session);
                //获取登录之后的Html
                try {
                    Document document = connection.get();
                    //解析获取到的html.....
                    Map<Integer, String> userInfoMap = new ArrayMap<>();
                    Elements elements = document.select("#mylib_info").select("table").select("tbody").select("tr");
                    int count = 0;
                    for (int i = 0; i < elements.size(); i++) {
                        Elements elements1 = elements.get(i).select("td");
                        for (int j = 0; j < elements1.size(); j++) {
                            String t = elements1.get(j).select("span").text();
                            String all = elements1.get(j).text();
                            String sub = all.replace(t, "");
                            userInfoMap.put(count, sub);
                            count++;
                        }
                    }
                    return new UserInfo(userInfoMap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(UserInfo userInfo) {
                super.onPostExecute(userInfo);
                if (null == userInfo) {
                    getInfoCallBack.getFailed();
                    if (getErroorCount > 3) {
                        showFeedBack();
                        getErroorCount = 0;
                        return;
                    }
                    showFailedDialog("获取失败", "换个姿势，再来一次");
                    getErroorCount++;
                } else {
                    showOkDialog("获取完成");
                    getInfoCallBack.getSuccess(userInfo);
                    AppControl.getInstance().getSpUtil().saveUserInfo(userInfo);
                }
            }
        }
        if (isNetworkConnected()) {
            showGetDialog("获取信息中...", "轻轻的来，轻轻的去....");
            new GetTask().execute();
        } else {
            AppControl.getInstance().showToast("网络连接失败，请检查网络连接");
            getInfoCallBack.getFailed();
        }
    }

    public interface GetInfoCallBack {
        void getSuccess(UserInfo userInfo);

        void getFailed();
    }

    /**
     * 获取当前借阅
     *
     * @param session  登录session
     * @param callBack 回调
     */
    public void getBookList(final String session, final GetBookListCallBack callBack) {
        class GetBookListTask extends AsyncTask<Void, Void, List<Book>> {

            @Override
            protected List<Book> doInBackground(Void... params) {
                Connection connection = Jsoup.connect(GET_BOOK_LIST_API);
                connection.cookie(PHP_SESSION_ID, session);
                List<Book> books = new ArrayList<>();
                try {
                    Document document = connection.get();
                    Elements elements = document.select("table.table_line");
                    elements = elements.select("tbody");
                    elements = elements.select("tr");
                    for (int i = 1; i < elements.size(); i++) {
                        Elements elements1 = elements.get(i).select("td");
                        Map<Integer, String> map = new HashMap<>();
                        Book book = new Book();
                        for (int j = 0; j < elements1.size(); j++) {
                            map.put(j, elements1.get(j).text());
                        }
                        //条码号
                        book.code = map.get(0);
                        //书名
                        book.name = map.get(1);
                        //借阅日期
                        book.getData = "借阅日期:" + map.get(2);
                        //归还日期
                        book.endData = "归还日期:" + map.get(3);
                        //续借量
                        book.getCount = map.get(4);
                        books.add(book);
                    }
                    return books;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Book> list) {
                super.onPostExecute(list);
                if (list != null) {
                    callBack.getBookListSuccess(list);
                } else
                    callBack.getBookListFailed();
            }
        }
        if (isNetworkConnected())
            new GetBookListTask().execute();
        else {
            callBack.getBookListFailed();
            AppControl.getInstance().showToast("网络连接失败，请检查网络连接");
        }
    }

    public interface GetBookListCallBack {
        void getBookListSuccess(List<Book> list);

        void getBookListFailed();
    }

    /**
     * 获得历史借阅
     *
     * @param session  登录session
     * @param callBack 回调
     */
    public void getBookHistory(final String session, final int page, final GetBookHistoryCallBack callBack) {
        class GetBookHistoryTask extends AsyncTask<Void, Void, List<History>> {

            @Override
            protected List<History> doInBackground(Void... params) {
                Connection connection = Jsoup.connect(GET_BOOK_HISTORY + String.valueOf(page));
                connection.cookie(NetRequest.PHP_SESSION_ID, session);
                //获取登录之后的Html
                try {
                    Document document = connection.get();
                    List<History> histories = new ArrayList<>();
                    Elements elements = document.select("#mylib_content").select("table").select("tbody").select("tr");
                    for (int i = 1; i < elements.size(); i++) {
                        Elements elements1 = elements.get(i).select("td");
                        History history = new History();
                        Map<Integer, String> map = new HashMap<>();
                        for (int j = 0; j < elements1.size(); j++) {
                            map.put(j, elements1.get(j).text());
                        }
                        history.historyId = map.get(0);
                        history.name = map.get(2);
                        history.getData = map.get(4);
                        history.endData = map.get(5);
                        histories.add(history);
                    }
                    return histories;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<History> list) {
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
            AppControl.getInstance().showToast("网络连接失败，请检查网络连接");
        }
    }

    public interface GetBookHistoryCallBack {
        void getHistorySuccess(List<History> list);

        void getHistoryFailed();
    }

    /**
     * 获得账目清单
     *
     * @param session  登录session
     * @param callBack 回调
     */
    public void getBookAccount(final String session, final GetBookAccountCallBack callBack) {
        class GetBookAccountTask extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... params) {
                Connection connection = Jsoup.connect(BOOK_ACCOUNT_API);
                connection.cookie(PHP_SESSION_ID, session);
                connection.timeout(5000);
                try {
                    Document document = connection.get();
                    Log.d("TAG", document.baseUri());
                    Elements elements = document.select("table.table_line").select("tbody").select("tr");
                    return elements.get(2).select("td").text();
                } catch (IOException e) {
                    e.printStackTrace();
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
            AppControl.getInstance().showToast("网络错误,请检查网络连接");
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
     * @param session  登录session
     * @param callBack 回调
     */
    public void getBookFine(final String session, final GetBookFineCallBack callBack) {
        class GetBookFineTask extends AsyncTask<Void, Void, List<BookFine>> {

            @Override
            protected List<BookFine> doInBackground(Void... params) {
                Connection connection = Jsoup.connect(BOOK_FINE_API);
                connection.cookie(PHP_SESSION_ID, session);
                try {
                    Document document = connection.get();
                    Elements elements = document.select("table.table_line").select("tbody").select("tr");
                    List<BookFine> bookFines = new ArrayList<>();
                    for (int i = 1; i < elements.size(); i++) {
                        Elements elements1 = elements.get(i).select("td");
                        Map<Integer, String> map = new HashMap<>();
                        for (int j = 0; j < elements1.size(); j++) {
                            map.put(j, elements1.get(j).text());
                        }
                        BookFine bookFine = new BookFine();
                        bookFine.code = map.get(0);
                        bookFine.name = map.get(2);
                        bookFine.getData = map.get(4);
                        bookFine.endData = map.get(5);
                        bookFine.shouldMoney = map.get(7);
                        bookFine.money = map.get(8);
                        bookFine.status = map.get(9);
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
            AppControl.getInstance().showToast("网络错误,请检查网络连接");
            callBack.getBookFineFailed();
        }
    }

    public interface GetBookFineCallBack {
        void getBookFineSuccess(List<BookFine> bookFines);

        void getBookFineFailed();
    }

    /**
     * 搜索
     *
     * @param searchKey 关键字
     * @param page      页码
     * @param callBack  回调
     */
    public void searchBook(final String searchKey, final int page, final SearchBookCallBack callBack) {
        class SearchBookTask extends AsyncTask<Void, Void, List<Book>> {
            private String info = "没有匹配书籍";

            @Override
            protected List<Book> doInBackground(Void... params) {
                Connection connection = Jsoup.connect(SEARCH_BOOK_API + searchKey + SEARCH_BOOK_API2 + page);
                try {
                    Document document = connection.get();
                    Elements elements = document.select("li.book_list_info");
                    List<Book> books = new ArrayList<>();
                    for (int i = 0; i < elements.size(); i++) {
                        Element element = elements.get(i);
                        String name = element.select("h3").select("a").text();
                        String code = element.select("h3").text().replace(name, "");
                        String count = element.select("p").select("span").text();
                        books.add(new Book(code, name, count));
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
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Book> books) {
                super.onPostExecute(books);
                if (books == null) {
                    callBack.searchFailed();
                } else {
                    callBack.searchSuccess(books, info);
                }
            }
        }

        if (isNetworkConnected()) {
            new SearchBookTask().execute();
        } else {
            callBack.searchFailed();
            AppControl.getInstance().showToast("网络未连接，请检查网络连接");
        }
    }

    public interface SearchBookCallBack {
        void searchSuccess(List<Book> books, String info);

        void searchFailed();
    }

    /**
     * 获取公告
     *
     * @param page 页码
     */
    public void getAnnounce(final int page, final GetAnnounceCallBack callBack) {
        class GetAnnounceTask extends AsyncTask<Void, Void, List<Announce>> {

            @Override
            protected List<Announce> doInBackground(Void... params) {
                Connection connection = Jsoup.connect(ANNOUNCE_API + String.valueOf(page));
                try {
                    Document document = connection.get();
                    Elements elements = document.select("ul.w915").select("li");
                    List<Announce> announces = new ArrayList<>();
                    for (int i = 0; i < elements.size(); i++) {
                        String name = elements.get(i).select("a").text();
                        String all = elements.get(i).select("span").text();
                        String data = all.replace("[通知公告]", "");
                        String url = elements.get(i).select("a").attr("href");
                        announces.add(new Announce(name, data, url));
                    }
                    return announces;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Announce> announces) {
                super.onPostExecute(announces);
                if (announces == null) {
                    callBack.getAnnounceFailed();
                    return;
                }
                callBack.getAnnounceSuccess(announces);
            }
        }

        if (isNetworkConnected()) {
            new GetAnnounceTask().execute();
        } else {
            showErrorDialog("网络错误,请检查网络连接");
        }
    }

    public interface GetAnnounceCallBack {
        void getAnnounceSuccess(List<Announce> announces);

        void getAnnounceFailed();
    }

    public void getAnnounceContent(final String url, final GetAnnounceContentCallBack callBack) {
        class GetTask extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... params) {
                Connection connection = Jsoup.connect(url);
                try {
                    Document document = connection.get();
                    Elements elements = document.select("div#news_content");
                    return elements.text();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "-1";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("-1")) {
                    callBack.getContentFailed();
                } else {
                    callBack.getContentSuccess(s);
                }
            }
        }

        if (isNetworkConnected()) {
            new GetTask().execute();
        } else {
            showErrorDialog("网络错误,请检查网络连接");
        }
    }

    public interface GetAnnounceContentCallBack {
        void getContentSuccess(String content);

        void getContentFailed();
    }

    /**
     * 登录教务管理系统
     *
     * @param account   学号
     * @param password  教务管理系统密码
     * @param checkCode 验证码
     */
    public void signJiaoWu(final String account, final String password, final String checkCode, final SignJiaoWuCallback callBack) {

        class SignTask extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... params) {
                Map<String, String> data = new HashMap<>();
                data.put("txtUserName", account);
                data.put("TextBox2", password);
                data.put("txtSecretCode", checkCode);
                data.put("RadioButtonList1", "%D1%A7%C9%FA");
                data.put("Button1", "");
                data.put("lbLanguage", "");
                data.put("hidPdrs", "");
                data.put("hidsc", "");
                data.put("__VIEWSTATE", "dDwyODE2NTM0OTg7Oz7QBx05W486R++11e1KrLTLz5ET2Q==");
                Connection connection = Jsoup.connect(SIGN_JIAO_WO);
                connection.data(data);
                connection.timeout(10000);
                connection.method(Connection.Method.POST);
                try {
                    Connection.Response response = connection.execute();
                    Document document = connection.get();
                    Log.d("TAG", response.url().toString());
                    Log.d("TAG", response.cookie(ASP_NET_SESSION));
                    if (response.url().toString().equals("http://202.115.80.153/xs_main.aspx?xh=" + account)) {
                        return response.cookie(ASP_NET_SESSION);
                    } else {
                        //账号、密码错误
                        Elements elements = document.select("script[language]");
                        for (Element element : elements) {
                            if (element.data().contains("验证码不正确")) {
                                Log.i("TAG", "验证码不正确");
                            } else if (element.data().contains("用户名不能为空")) {
                                Log.i("TAG", "用户名不能为空");
                            } else if (element.data().contains("密码错误")) {
                                Log.i("TAG", "密码或用户名错误");
                            } else if (element.data().contains("密码不能为空")) {
                                Log.i("TAG", "密码不能为空");
                            }
                        }
                        return "0";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return "-1";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                switch (s) {
                    case "0":
                        showFailedDialog("登录失败", "检查学号密码，换个姿势，再来一次");
                        callBack.signFailed();
                        break;
                    case "-1":
                        if (signErrorCount > 3) {
                            showFeedBack();
                            signErrorCount = 0;
                            break;
                        }
                        showErrorDialog("服务器连接失败，请稍候重试");
                        signErrorCount++;
                        callBack.signFailed();
                        break;
                    default:
                        showOkDialog("登录成功");
                        callBack.signSuccess(s);
                        break;
                }
            }
        }

        if (isNetworkConnected()) {
            showGetDialog("登录教务系统中", "连啊，连啊，我的骄傲放纵");
            new SignTask().execute();
        } else {
            showErrorDialog("网络错误,请检查网络连接");
        }
    }

    public interface SignJiaoWuCallback {
        void signSuccess(String s);

        void signFailed();
    }

    /**
     * 获取验证码
     */
    public void getCheckCode(final GetCheckCodeCallBack callBack) {
        ImageRequest imageRequest = new ImageRequest(SIGN_CODE, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                callBack.getCodeSuccess(response, "");
            }
        }, 100, 100, ImageView.ScaleType.CENTER, Bitmap.Config.ARGB_4444, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.getCodeFailed();
            }
        });
        if (isNetworkConnected()) {
            AppControl.getInstance().getRequestQueue().add(imageRequest);
        } else {
            showErrorDialog("网络错误,请检查网络连接");
        }
    }

    public interface GetCheckCodeCallBack {
        void getCodeSuccess(Bitmap bitmap, String session);

        void getCodeFailed();
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
            AppControl.getInstance().getRequestQueue().add(request);
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
                Log.d("TAG", "to server" + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "to server error" + error.toString());
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
        AppControl.getInstance().getRequestQueue().add(request);
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

    private void showGetDialog(String title, String content) {
        if (dialog != null)
            dialog.dismissWithAnimation();
        dialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText(title).setContentText(content);
        dialog.setCancelable(false);
        dialog.show();
    }

    private void showOkDialog(String title) {
        if (dialog != null) {
            dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
            dialog.setTitleText(title);
            dialog.dismissWithAnimation();
        }
    }

    private void showFailedDialog(String title, String content) {
        if (dialog != null)
            dialog.dismissWithAnimation();
        dialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        dialog.setTitleText(title);
        dialog.setContentText(content);
        dialog.setConfirmText("确定");
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                dialog.dismissWithAnimation();
            }
        });
        dialog.show();
    }

    private void showErrorDialog(String error) {
        if (dialog != null)
            dialog.dismissWithAnimation();
        dialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(error)
                .setConfirmText("确定");
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                dialog.dismissWithAnimation();
            }
        });
        dialog.show();
    }

    private void showFeedBack() {
        if (dialog != null)
            dialog.dismissWithAnimation();
        dialog = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText("请向我们反馈这个问题")
                .setConfirmText("跳到反馈");
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                dialog.dismissWithAnimation();
            }
        });
        dialog.show();
    }

}
