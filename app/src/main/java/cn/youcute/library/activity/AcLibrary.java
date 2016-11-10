package cn.youcute.library.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import cn.youcute.library.AppControl;
import cn.youcute.library.R;
import cn.youcute.library.activity.fragment.BookAccountFragment;
import cn.youcute.library.activity.fragment.BookFineFragment;
import cn.youcute.library.activity.fragment.BookHistoryFragment;
import cn.youcute.library.activity.fragment.BookListFragment;
import cn.youcute.library.adapter.MyFragmentAdapter;
import cn.youcute.library.bean.User;
import cn.youcute.library.util.NetRequest;
import cn.youcute.library.util.SpUtil;
import cn.youcute.library.util.ToastUtil;

/**
 * Created by jy on 2016/11/6.
 * 图书馆
 */

public class AcLibrary extends AcBase implements NetRequest.SignCallBack {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Fragment[] fragments;
    private MyFragmentAdapter fragmentAdapter;
    private BookListFragment bookListFragment;
    private BookHistoryFragment bookHistoryFragment;
    private BookAccountFragment bookAccountFragment;
    private BookFineFragment bookFineFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_library);
        SpUtil spUtil = AppControl.getInstance().getSpUtil();
        if (!spUtil.getIsSign()) {
            Intent intent = new Intent(AcLibrary.this, AcSignLibrary.class);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        } else {
            User user = spUtil.getUser();
            AppControl.getInstance().getNetRequest().sign(user.account, user.password, this);
        }
    }

    @Override
    public void signSuccess(String session) {
        AppControl.getInstance().sessionLibrary = session;
        initView();
    }

    private void initView() {
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        //当前借阅
        if (bookListFragment == null) {
            bookListFragment = new BookListFragment();
        }
        //历史借阅
        if (bookHistoryFragment == null) {
            bookHistoryFragment = new BookHistoryFragment();
        }
        //账目清单
        if (bookAccountFragment == null)
            bookAccountFragment = new BookAccountFragment();
        //违章缴款
        if (bookFineFragment == null)
            bookFineFragment = new BookFineFragment();
        String[] titles = new String[]{"当前借书", "累计借书", "欠款信息", "账目清单"};
        //设置TabLayout和viewPager
        if (fragments == null)
            fragments = new Fragment[]{bookListFragment, bookHistoryFragment, bookFineFragment, bookAccountFragment};
        if (fragmentAdapter == null)
            fragmentAdapter = new MyFragmentAdapter(this.getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void signFailed(String info) {
        ToastUtil.showToast(info);
        Intent intent = new Intent(AcLibrary.this, AcSignLibrary.class);
        startActivityForResult(intent, 1);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 2) {
                initView();
            } else if (resultCode == -1) {
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
            }
        }
    }
}
