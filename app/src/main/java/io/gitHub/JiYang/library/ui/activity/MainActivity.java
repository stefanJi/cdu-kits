package io.gitHub.JiYang.library.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import io.gitHub.JiYang.library.R;
import io.gitHub.JiYang.library.presenter.MainViewPresenter;
import io.gitHub.JiYang.library.presenter.MainViewPresenterImpl;
import io.gitHub.JiYang.library.ui.common.BaseActivity;
import io.gitHub.JiYang.library.ui.fragment.BaseFragment;
import io.gitHub.JiYang.library.ui.fragment.feeds.FeedsFragment;
import io.gitHub.JiYang.library.ui.fragment.library.LibraryFragment;
import io.gitHub.JiYang.library.ui.view.MainView;

public class MainActivity extends BaseActivity implements MainView {

    private FragmentManager mFragmentManager;
    private MainViewPresenter mainViewPresenter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<BaseFragment> mFragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);
        mFragmentManager = getSupportFragmentManager();
        mainViewPresenter = new MainViewPresenterImpl();
        init();
    }

    private void init() {
        Toolbar toolbar = findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.s_actionbar_like);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.viewPager);
        mFragments = new ArrayList<>();
        mFragments.add(FeedsFragment.newInstance());
        mFragments.add(LibraryFragment.newInstance());
        viewPager.setAdapter(new FragmentPagerAdapter(mFragmentManager) {

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return mFragments.get(position).getTitle();
            }

            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }
}
