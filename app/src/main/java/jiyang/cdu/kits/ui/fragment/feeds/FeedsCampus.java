package jiyang.cdu.kits.ui.fragment.feeds;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import jiyang.cdu.kits.presenter.BasePresenterImpl;
import jiyang.cdu.kits.ui.common.BaseFragment;
import jiyang.cdu.kits.util.SpUtil;


public class FeedsCampus extends BaseFragment {
    private FragmentFeedsBinding feedsBinding;
    private List<BaseFragment> baseFragments;

    public static FeedsCampus newInstance() {
        return new FeedsCampus();
    }

    @Override
    public String getTitle() {
        return "校园";
    }

    @Override
    public BasePresenterImpl initPresenter() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        feedsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_feeds, container, false);
        baseFragments = new ArrayList<>();
        SpUtil sp = AppControl.getInstance().getSpUtil();
        if (isFirstStart()) {
            for (String tabKey : Constant.FEEDS_TABS) {
                sp.setBool(tabKey, true);
                baseFragments.add(FeedsItemFragment.newInstance(tabKey));
            }
        } else {
            for (int i = 0; i < Constant.FEEDS_TABS.length; i++) {
                boolean load = sp.getBool(Constant.FEEDS_TABS[i], false);
                if (load) {
                    baseFragments.add(FeedsItemFragment.newInstance(Constant.FEEDS_TABS[i]));
                }
            }
        }
        init();
        return feedsBinding.getRoot();
    }

    private void init() {
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
        showFirstComeTip("下拉可刷新，点击可查看详情");
    }

}
