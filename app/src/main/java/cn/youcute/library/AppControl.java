package cn.youcute.library;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import net.youmi.android.AdManager;

import cn.youcute.library.util.NetRequest;
import cn.youcute.library.util.SpUtil;

/**
 * Created by jy on 2016/9/22.
 * 单例类
 */
public class AppControl extends Application {
    private static AppControl appControl;
    private static NetRequest netRequest;
    private static SpUtil spUtil;
    private Toast toast;
    private RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        appControl = this;
//        AdManager.getInstance(this).init("2cc49376cb40eedd", "ff1e699d9b499f89", true, true);
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
        return netRequest;
    }

    public void initNetRequest(Context context) {
        netRequest = new NetRequest(context);
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
}
