package jiyang.cdu.kits;

import android.app.Application;
import android.content.Context;

import com.litesuits.orm.LiteOrm;

import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import jiyang.cdu.kits.util.SpUtil;


/**
 * Created by jy on 2016/11/6.
 * my application
 */
public class AppControl extends Application {
    private static AppControl appControl;
    private static SpUtil spUtil;
    private static LiteOrm liteOrm;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appControl = this;
        JAnalyticsInterface.init(this);
        JAnalyticsInterface.setDebugMode(true);
        JAnalyticsInterface.initCrashHandler(this);
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

    public LiteOrm getLiteOrm() {
        if (liteOrm == null) {
            liteOrm = this.newSingleInstance();
            liteOrm.setDebugged(true);
        }
        return liteOrm;
    }

    private LiteOrm newSingleInstance() {
        return LiteOrm.newSingleInstance(this, "cdu_kits.db");
    }

    @Override
    public void onLowMemory() {
        JAnalyticsInterface.stopCrashHandler(this);
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        JAnalyticsInterface.stopCrashHandler(this);
        super.onTerminate();
    }
}
