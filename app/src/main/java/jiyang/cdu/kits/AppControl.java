package jiyang.cdu.kits;

import android.app.Application;
import android.content.Context;

import com.litesuits.orm.LiteOrm;

import jiyang.cdu.kits.util.BitmapCache;
import jiyang.cdu.kits.util.NetRequest;
import jiyang.cdu.kits.util.SpUtil;


/**
 * Created by jy on 2016/11/6.
 * my application
 */
public class AppControl extends Application {
    private static AppControl appControl;
    private static NetRequest netRequest;
    private static SpUtil spUtil;
    private BitmapCache bitmapCache;
    public String sessionLibrary;
    private static LiteOrm liteOrm;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

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
}
