package jiyang.cdu.kits.ui.common;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import jiyang.cdu.kits.AppControl;
import jiyang.cdu.kits.R;
import jiyang.cdu.kits.util.SpUtil;


public abstract class BaseFragment extends Fragment {

    public abstract String getTitle();

    protected void setRefreshLayoutColor(SwipeRefreshLayout swipeRefreshLayout) {
        Context context = getContext();
        if (context != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                swipeRefreshLayout.setColorSchemeColors(context.getColor(R.color.colorAccent));
            } else {
                swipeRefreshLayout.setColorSchemeColors(context.getResources().getColor(R.color.colorAccent));
            }
        }
    }

    protected void isFirstCome(String tip) {
        final String tag = getClass().getName();
        final SpUtil sp = AppControl.getInstance().getSpUtil();
        boolean isFirstCome = sp.getBool(tag, true);
        Activity activity = getActivity();
        if (isFirstCome && activity != null) {
            Snackbar.make(activity.findViewById(android.R.id.content), tip, Snackbar.LENGTH_INDEFINITE)
                    .setAction("知道了", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sp.setBool(tag, false);
                        }
                    })
                    .show();
        }
    }
}
