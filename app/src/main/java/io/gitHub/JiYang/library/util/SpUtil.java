package io.gitHub.JiYang.library.util;

import android.content.Context;
import android.content.SharedPreferences;

import io.gitHub.JiYang.library.bean.User;

/**
 * Created by jy on 2016/9/21.
 * SharedPreferences工具类
 */
public class SpUtil {
    private SharedPreferences preferences;

    public SpUtil(Context context) {
        preferences = context.getSharedPreferences("jyLibrary", Context.MODE_PRIVATE);
    }

    public User getUser() {
        String account = preferences.getString("user_account", "");
        String password = preferences.getString("user_password", "");
        return new User(account, password);
    }

    public void saveUser(User user) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_account", user.account);
        editor.putString("user_password", user.password);
        editor.apply();
    }

    public void saveName(String name) {
        preferences.edit().putString("name", name).apply();
    }

    public String getName() {
        return preferences.getString("name", "");
    }

    public boolean getIsSign() {
        return preferences.getBoolean("is_sign", false);
    }

    public void setIsSign(boolean isSign) {
        preferences.edit().putBoolean("is_sign", isSign).apply();
    }

    public boolean getIsSignNet() {
        return preferences.getBoolean("is_sign_net", false);
    }

    public void setIsSignNet(boolean isSign) {
        preferences.edit().putBoolean("is_sign_net", isSign).apply();
    }

    public void saveAccount(String account) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_account", account);
        editor.apply();
    }

    public String getNetPass() {
        return preferences.getString("net_pass", "");
    }

    public void saveNetPass(String pass) {
        preferences.edit().putString("net_pass", pass).apply();
    }


    public void saveBook(String bookName, String bookUrl) {
        preferences.edit().putString("book_name", bookName).apply();
        preferences.edit().putString("book_url", bookUrl).apply();
    }

    public String[] getBook() {
        String name = preferences.getString("book_name", "");
        String url = preferences.getString("book_url", "");
        return new String[]{name, url};
    }

    public void setFirst(boolean is) {
        preferences.edit().putBoolean("first_start", is).apply();
    }

    public boolean getIsFirst() {
        return preferences.getBoolean("first_start", true);
    }

    public String getTableString() {
        return preferences.getString("table", "");
    }

    public void saveTableStr(String str) {
        preferences.edit().putString("table", str).apply();
    }
}
