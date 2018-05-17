package jiyang.cdu.kits.ui.common;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import java.util.List;

import jiyang.cdu.kits.AppControl;
import jiyang.cdu.kits.R;
import jiyang.cdu.kits.presenter.BasePresenterImpl;
import jiyang.cdu.kits.ui.view.BaseView;
import jiyang.cdu.kits.util.SpUtil;


public abstract class BaseFragment<V extends BaseView, P extends BasePresenterImpl> extends Fragment {
    public String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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

    protected void showFirstComeTip(String tip) {
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

    public P presenter;

    public abstract P initPresenter();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = initPresenter();
        if (presenter != null) {
            presenter.attachView(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detach();
            presenter = null;
        }
    }

    protected void destoryChildFragment(List<? extends BaseFragment> fragments) {
        destoryChildFragment(null, fragments);
    }

    protected void destoryChildFragment(BaseFragment[] fragments) {
        destoryChildFragment(fragments, null);
    }


    private void destoryChildFragment(BaseFragment[] fragments, List<? extends BaseFragment> baseFragmentList) {
        FragmentManager fragmentManager = getChildFragmentManager();
        if (fragments != null || baseFragmentList != null && !fragmentManager.isDestroyed()) {
            if (fragments != null) {
                for (BaseFragment baseFragment : fragments) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    if (transaction != null) {
                        if (baseFragment != null) {
                            transaction.remove(baseFragment).commitAllowingStateLoss();
                        }
                    }
                }
            } else {
                for (BaseFragment baseFragment : baseFragmentList) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    if (transaction != null) {
                        if (baseFragment != null) {
                            transaction.remove(baseFragment).commitAllowingStateLoss();
                        }
                    }
                }
            }
        }
    }
}
