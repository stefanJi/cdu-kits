package io.gitHub.JiYang.library.ui.fragment.feeds;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.gitHub.JiYang.library.R;
import io.gitHub.JiYang.library.presenter.feeds.FeedsPresenter;
import io.gitHub.JiYang.library.ui.fragment.BaseFragment;

public class FeedsFragment extends BaseFragment {

    public static String TAG = "feeds_fragment";
    private BaseFragment[] baseFragments;

    public static FeedsFragment newInstance() {
        return new FeedsFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feeds, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        ViewPager viewPager = view.findViewById(R.id.viewPager);
        baseFragments = new BaseFragment[]{
                FeedsItemFragment.newInstance(FeedsPresenter.ANNOUNCE),
                FeedsItemFragment.newInstance(FeedsPresenter.HQC_ANNOUNCE),
                FeedsItemFragment.newInstance(FeedsPresenter.NEWS),
                FeedsItemFragment.newInstance(FeedsPresenter.NEWS2),
                FeedsItemFragment.newInstance(FeedsPresenter.MEDIA),
                FeedsItemFragment.newInstance(FeedsPresenter.COLOR_CAMPUS),
                FeedsItemFragment.newInstance(FeedsPresenter.ARTICLE)
        };
        viewPager.setOffscreenPageLimit(baseFragments.length);
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
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
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public String getTitle() {
        return "发现";
    }
}
