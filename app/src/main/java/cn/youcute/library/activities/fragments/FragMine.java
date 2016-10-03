package cn.youcute.library.activities.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.youcute.library.R;
import cn.youcute.library.activities.menuFragments.FragAbout;
import cn.youcute.library.activities.menuFragments.FragFeedBack;
import cn.youcute.library.activities.menuFragments.FragHelp;
import cn.youcute.library.adapter.MyFragmentAdapter;

/**
 * Created by jy on 2016/10/2.
 * 扫描
 */

public class FragMine extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.frag_mine, container, false);
            initView();
        }
        return view;
    }

    private void initView() {
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout_mine);
        Fragment[] fragments = new Fragment[]{new FragAbout(), new FragHelp(), new FragFeedBack()};
        String[] titles = new String[]{"我的借阅", "我的收藏", "我的电子书"};
        MyFragmentAdapter myFragmentAdapter = new MyFragmentAdapter(getFragmentManager(), fragments, titles);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager_mine);
        viewPager.setAdapter(myFragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != view) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

}
