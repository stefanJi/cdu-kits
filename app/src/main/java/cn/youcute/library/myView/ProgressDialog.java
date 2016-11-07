package cn.youcute.library.myView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.view.View;

/**
 * Created by jy on 2016/11/6.
 */

public class ProgressDialog extends AlertDialog {
    protected ProgressDialog(@NonNull Context context) {
        super(context);
    }

    protected ProgressDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected ProgressDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

}
