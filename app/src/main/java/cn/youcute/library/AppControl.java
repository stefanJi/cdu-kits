package cn.youcute.library;

import android.app.Application;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import cn.youcute.library.util.BitmapCache;
import cn.youcute.library.util.NetRequest;
import cn.youcute.library.util.SpUtil;

/**
 * Created by jy on 2016/11/6.
 */
public class AppControl extends Application {
    private static AppControl appControl;
    private static NetRequest netRequest;
    private static SpUtil spUtil;
    private Toast toast;
    private RequestQueue requestQueue;
    private BitmapCache bitmapCache;
    public String sessionLibrary;
    public String sessionEducation;

    @Override
    public void onCreate() {
        super.onCreate();
        appControl = this;
    }

    /**
     * 返回APP单例
     *
     * @return AppControl
     */
    public static synchronized AppControl getInstance() {
        return appControl;
    }

    /**
     * 返回网络请求
     *
     * @return NetRequest工具类
     */
    public NetRequest getNetRequest() {
        if (netRequest == null) {
            netRequest = new NetRequest();
        }
        return netRequest;
    }

    /**
     * 返回SharedPreferences工具类
     *
     * @return SPUtil
     */
    public SpUtil getSpUtil() {
        if (spUtil == null) {
            spUtil = new SpUtil(appControl);
        }
        return spUtil;
    }

    /**
     * 显示全局toast
     *
     * @param info 显示消息
     */
    public void showToast(String info) {
        if (toast == null) {
            toast = Toast.makeText(appControl, "", Toast.LENGTH_SHORT);
        }
        toast.setText(info);
        toast.show();
    }

    /**
     * @return Volley Request 队列，如果不存在将创建队列
     */
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    /**
     * 获取图片缓存空间
     *
     * @return 缓存空间
     */
    public BitmapCache getBitmapCache() {
        if (bitmapCache == null) {
            bitmapCache = new BitmapCache();
        }
        return bitmapCache;
    }
}
