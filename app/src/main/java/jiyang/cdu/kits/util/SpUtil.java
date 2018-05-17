package jiyang.cdu.kits.util;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by jy on 2016/9/21.
 * SharedPreferences工具类
 */
public class SpUtil {
    private SharedPreferences preferences;

    public SpUtil(Context context) {
        preferences = context.getSharedPreferences("cdu_kits", Context.MODE_PRIVATE);
    }

    public void setString(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    public String getString(String key) {
        return preferences.getString(key, null);
    }

    public void setBool(String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBool(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }
}
