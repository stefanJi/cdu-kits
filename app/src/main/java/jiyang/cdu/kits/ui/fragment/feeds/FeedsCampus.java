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

import jiyang.cdu.kits.R;
import jiyang.cdu.kits.presenter.feeds.FeedsPresenter;
import jiyang.cdu.kits.ui.common.BaseFragment;
import jiyang.cdu.kits.databinding.FragmentFeedsBinding;


public class FeedsCampus extends BaseFragment {
    private FragmentFeedsBinding feedsBinding;
    private BaseFragment[] baseFragments;

    public static FeedsCampus newInstance() {
        return new FeedsCampus();
    }

    @Override
    public String getTitle() {
        return "校园";
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        feedsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_feeds, container, false);
        init();
        return feedsBinding.getRoot();
    }

    private void init() {
        baseFragments = new BaseFragment[]{
                FeedsItemFragment.newInstance(FeedsPresenter.ANNOUNCE),
                FeedsItemFragment.newInstance(FeedsPresenter.HQC_ANNOUNCE),
                FeedsItemFragment.newInstance(FeedsPresenter.NEWS),
                FeedsItemFragment.newInstance(FeedsPresenter.NEWS2),
                FeedsItemFragment.newInstance(FeedsPresenter.MEDIA),
                FeedsItemFragment.newInstance(FeedsPresenter.COLOR_CAMPUS),
                FeedsItemFragment.newInstance(FeedsPresenter.ARTICLE)
        };
        feedsBinding.viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return baseFragments[position];
            }

            @Override
            public int getCount() {
                return baseFragments.length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return baseFragments[position].getTitle();
            }
        });
        feedsBinding.tabLayout.setupWithViewPager(feedsBinding.viewPager);
        isFirstCome("下拉可刷新，点击可查看详情");
    }

}
