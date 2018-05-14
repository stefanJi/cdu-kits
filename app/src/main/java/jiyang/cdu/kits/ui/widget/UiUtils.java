package jiyang.cdu.kits.ui.widget;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import jiyang.cdu.kits.R;


public class UiUtils {
    public static void showErrorSnackbar(Context context, View container, String error) {
        Snackbar snackbar = Snackbar.make(container, error, Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.error_bg));
        TextView tv = snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(ContextCompat.getColor(context, R.color.white));
        snackbar.show();
    }

    public static void showErrorSnackbar(Context context, View container, String error, String action,
                                         View.OnClickListener actionClickListener) {
        Snackbar snackbar = Snackbar.make(container, error, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(action, actionClickListener);
        snackbar.setActionTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.error_bg));
        TextView tv = snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(ContextCompat.getColor(context, R.color.white));
        snackbar.show();
    }
}
