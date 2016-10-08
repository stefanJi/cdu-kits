package cn.youcute.library.util;

import android.content.Context;
import android.content.SharedPreferences;

import cn.youcute.library.bean.User;
import cn.youcute.library.bean.UserInfo;

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
        String account = preferences.getString("user_account", "0");
        String password = preferences.getString("user_password", "0");
        return new User(account, password);
    }

    public void saveUser(User user) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_account", user.account);
        editor.putString("user_password", user.password);
        editor.apply();
    }

    public void saveUserInfo(UserInfo userInfo) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", userInfo.name);
        editor.putString("user_account", userInfo.account);
        editor.putString("lear_type", userInfo.learnType);
        editor.apply();
    }

    public UserInfo getUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.name = preferences.getString("name", "0");
        userInfo.account = preferences.getString("user_account", "0");
        userInfo.learnType = preferences.getString("learn_type", "0");
        return userInfo;
    }

    public boolean getIsSign() {
        return preferences.getBoolean("is_sign", false);
    }

    public void setIsSign(boolean isSign) {
        preferences.edit().putBoolean("is_sign", isSign).apply();
    }


}
