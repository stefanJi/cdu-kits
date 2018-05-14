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
import jiyang.cdu.kits.ui.common.BaseFragment;
import jiyang.cdu.kits.databinding.FragmentFeedsBinding;

public class FeedsFragment extends BaseFragment {
    public static String TAG = "feeds_fragment";
    private BaseFragment[] baseFragments;
    private FragmentFeedsBinding feedsBinding;

    public static FeedsFragment newInstance() {
        return new FeedsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feeds, container, false);
        feedsBinding = DataBindingUtil.bind(view);
        init();
        return view;
    }

    private void init() {
        baseFragments = new BaseFragment[]{
                FeedsCampus.newInstance(),
                ExploreFragment.newInstance()
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


    @Override
    public String getTitle() {
        return "发现";
    }
}
