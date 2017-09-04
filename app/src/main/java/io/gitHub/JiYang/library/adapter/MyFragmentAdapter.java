package io.gitHub.JiYang.library.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by jy on 2016/9/22.
 * Fragment适配器
 */
public class MyFragmentAdapter extends FragmentPagerAdapter {
    private String[] fragmentTitle;
    private Fragment[] fragments;

    public MyFragmentAdapter(FragmentManager fm, Fragment[] fragments, String[] titles) {
        super(fm);
        this.fragmentTitle = titles;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitle[position];
    }
}
