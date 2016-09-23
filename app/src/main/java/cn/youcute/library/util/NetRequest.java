package cn.youcute.library.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.youcute.library.AppControl;
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
    public static final String SIGN_API = "http://202.115.80.170:8080/reader/redr_verify.php";
    public static final String INFO_API = "http://202.115.80.170:8080/reader/redr_info.php";
    public static final String GET_BOOK_LIST_API = "http://202.115.80.170:8080/reader/book_lst.php";
    public static final String GET_BOOK_HISTORY = "http://202.115.80.170:8080/reader/book_hist.php?page=";
    public static final String BOOK_ACCOUNT_API = "http://202.115.80.170:8080/reader/account.php";
    public static final String BOOK_FINE_API = "http://202.115.80.170:8080/reader/fine_pec.php";
    public static final String PHP_SESSION_ID = "PHPSESSID";

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
                            .timeout(5000)
                            .execute();
                    //通过判断返回的接口是否改变判断是否登录成功
                    if (response.url().toString().equals(SIGN_API)) {
                        return "0";
                    } else {
                        return response.cookie(PHP_SESSION_ID);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("0")) {
                    signCallBack.signFailed();
                } else {
                    signCallBack.signSuccess(s);
                    User user = new User(account, password);
                    AppControl.getInstance().getSpUtil().setIsSign(true);
                    AppControl.getInstance().getSpUtil().saveUser(user);
                }
            }
        }
        if (isNetworkConnected())
            new SignTask().execute();
        else {
            showErrorDialog();
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
        Log.d("TAG", "getUserInfo");
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
                } else {
                    getInfoCallBack.getSuccess(userInfo);
                    AppControl.getInstance().getSpUtil().saveUserInfo(userInfo);
                }
            }
        }
        if (isNetworkConnected())
            new GetTask().execute();
        else
            showErrorDialog();
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
        Log.d("TAG", "getBookList");
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
                        book.code = map.get(0);
                        book.name = map.get(1);
                        book.getData = map.get(2);
                        book.endData = map.get(3);
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
        else
            showErrorDialog();
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
    public void getBookHistory(final String session, final GetBookHistoryCallBack callBack) {
        class GetBookHistoryTask extends AsyncTask<Void, Void, List<History>> {

            @Override
            protected List<History> doInBackground(Void... params) {
                Connection connection = Jsoup.connect(GET_BOOK_HISTORY + "1");
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
        else
            showErrorDialog();
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
            showErrorDialog();
        }
    }

    public interface GetBookAccountCallBack {
        void getBookAccountSuccess(String info);

        void getBookAccountFailed();
    }

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
                callBack.getBookFineSuccess(bookFines);
            }
        }
        if (isNetworkConnected()) {
            new GetBookFineTask().execute();
        } else {
            showErrorDialog();
        }
    }

    public interface GetBookFineCallBack {
        void getBookFineSuccess(List<BookFine> bookFines);

        void getBookFineFailed();
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

    private void showErrorDialog() {
        final SweetAlertDialog dialog = new SweetAlertDialog(AppControl.getInstance(), SweetAlertDialog.WARNING_TYPE)
                .setContentText("网络未连接，请检查网络连接")
                .setConfirmText("确定");
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                dialog.dismissWithAnimation();
            }
        });

    }

}
