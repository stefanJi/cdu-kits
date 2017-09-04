package io.gitHub.JiYang.library.util;

import android.util.Log;
import android.widget.Toast;

import io.gitHub.JiYang.library.AppControl;

/**
 * Created by jy on 2016/11/9.
 */

public class ToastUtil {
    public static void showToast(String info) {
        Toast.makeText(AppControl.getInstance(), info, Toast.LENGTH_SHORT).show();
    }

    public static void showLog(String info) {
        Log.d("TAG", info);
    }
}
