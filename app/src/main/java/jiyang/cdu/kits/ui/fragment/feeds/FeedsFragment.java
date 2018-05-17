package jiyang.cdu.kits.ui.fragment.feeds;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import jiyang.cdu.kits.AppControl;
import jiyang.cdu.kits.Constant;
import jiyang.cdu.kits.R;
import jiyang.cdu.kits.databinding.FragmentFeedsBinding;
import jiyang.cdu.kits.model.enty.zhihu.DailyTheme;
import jiyang.cdu.kits.model.enty.zhihu.DailyThemes;
import jiyang.cdu.kits.presenter.feeds.ZhihuDailyPresernterImpl;
import jiyang.cdu.kits.ui.common.BaseFragment;
import jiyang.cdu.kits.ui.view.feeds.ZhihuDailyView;
import jiyang.cdu.kits.ui.widget.UiUtils;
import jiyang.cdu.kits.util.SpUtil;


public class FeedsFragment extends BaseFragment<ZhihuDailyView, ZhihuDailyPresernterImpl> implements ZhihuDailyView {
    public static final String TAG = "feeds_fragment";
    private FragmentFeedsBinding feedsBinding;
    private List<BaseFragment> baseFragments;
    private SpUtil sp;
    private boolean isFirstStart;

    public static FeedsFragment newInstance() {
        return new FeedsFragment();
    }

    @Override
    public String getTitle() {
        return "发现";
    }

    @Override
    public ZhihuDailyPresernterImpl initPresenter() {
        return new ZhihuDailyPresernterImpl();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        feedsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_feeds, container, false);
        baseFragments = new ArrayList<>();
        sp = AppControl.getInstance().getSpUtil();
        init();
        return feedsBinding.getRoot();
    }

    private void init() {
        isFirstStart = sp.getBool(Constant.FIRST_START_FEEDS, true);
        if (isFirstStart) {
            for (int i = 0; i < Constant.FEEDS_TABS.length; i++) {
                sp.setBool(Constant.FEEDS_TABS[i], true);
            }
        }
        for (String name : Constant.FEEDS_TABS) {
            if (sp.getBool(name, false)) {
                baseFragments.add(FeedsItemFragment.newInstance(name));
            }
        }
        if (feedsBinding.viewPager.getAdapter() != null) {
            feedsBinding.viewPager.getAdapter().notifyDataSetChanged();
        }
        presenter.fetch();
        feedsBinding.viewPager.setOffscreenPageLimit(5);
        feedsBinding.viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return baseFragments.get(position);
            }

            @Override
            public int getCount() {
                return baseFragments.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return baseFragments.get(position).getTitle();
            }
        });
        feedsBinding.tabLayout.setupWithViewPager(feedsBinding.viewPager);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (sp.getBool(Constant.NOT_CONFIG_FEEDS_TAB, true)) {
            Snackbar.make(feedsBinding.getRoot(), "设置里可选择关闭标签,下拉可刷新列表,点击查看详情", Snackbar.LENGTH_INDEFINITE)
                    .setAction("知道了", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sp.setBool(Constant.NOT_CONFIG_FEEDS_TAB, false);
                        }
                    })
                    .show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sp.setBool(Constant.FIRST_START_FEEDS, false);
        destoryChildFragment(baseFragments);
    }

    @Override
    public void showLoading() {
        feedsBinding.progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        feedsBinding.progress.setVisibility(View.GONE);
    }

    @Override
    public void setFetchResult(DailyThemes dailyThemes) {
        if (dailyThemes == null || dailyThemes.getDailyThemes() == null) {
            return;
        }
        if (isFirstStart) {
            for (DailyTheme dailyTheme : dailyThemes.getDailyThemes()) {
                sp.setBool(dailyTheme.getName(), true);
            }
        }
        for (DailyTheme dailyTheme : dailyThemes.getDailyThemes()) {
            if (sp.getBool(dailyTheme.getName(), false)) {
                ZhihuDailyItemThemeFragment itemThemeFragment = ZhihuDailyItemThemeFragment.newInstance(dailyTheme);
                baseFragments.add(itemThemeFragment);
            }
        }
        if (feedsBinding.viewPager.getAdapter() != null) {
            feedsBinding.viewPager.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void showError(String error) {
        UiUtils.showErrorSnackbar(getContext(), feedsBinding.getRoot(), error);
    }
}