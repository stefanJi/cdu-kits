package cn.youcute.library.activities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.youcute.library.AppControl;
import cn.youcute.library.R;
import cn.youcute.library.activities.SignActivity;
import cn.youcute.library.activities.bookFragments.BookAccountFragment;
import cn.youcute.library.activities.bookFragments.BookFineFragment;
import cn.youcute.library.activities.bookFragments.BookHistoryFragment;
import cn.youcute.library.activities.bookFragments.BookListFragment;
import cn.youcute.library.adapter.MyFragmentAdapter;
import cn.youcute.library.bean.User;
import cn.youcute.library.bean.UserInfo;
import cn.youcute.library.util.NetRequest;
import cn.youcute.library.util.SpUtil;

/**
 * Created by jy on 2016/10/2.
 * 扫描
 */

public class FragMine extends Fragment implements NetRequest.SignCallBack, NetRequest.GetInfoCallBack {
    private View view;
    private LinearLayout signLayout;
    private SpUtil spUtil;
    private String sessionId;
    private Bundle bundle;
    private TextView tvName, tvAccount;
    private UserInfo userInfo;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Fragment[] fragments;
    private MyFragmentAdapter fragmentAdapter;
    private BookListFragment bookListFragment;
    private BookHistoryFragment bookHistoryFragment;
    private BookAccountFragment bookAccountFragment;
    private BookFineFragment bookFineFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.frag_mine, container, false);
            spUtil = AppControl.getInstance().getSpUtil();
            userInfo = spUtil.getUserInfo();
            initView();
            if (spUtil.getIsSign()) {
                //如果上一次已经登录，直接后台登录
                User user = spUtil.getUser();
                AppControl.getInstance().getNetRequest().sign(user.account, user.password, this);
                signLayout.setVisibility(View.INVISIBLE);
            } else {
                //如果上一次未登录，显示登录界面
                signLayout.setVisibility(View.VISIBLE);
            }
        }
        return view;
    }

    private void initView() {
        signLayout = (LinearLayout) view.findViewById(R.id.layout_sign);
        Button btnSign = (Button) view.findViewById(R.id.btn_sign);
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SignActivity.class);
                intent.putExtra("sign_action", 0);
                startActivityForResult(intent, 107);
            }
        });
        tvName = (TextView) view.findViewById(R.id.tv_user_name);
        tvAccount = (TextView) view.findViewById(R.id.tv_user_account);
        RelativeLayout layoutUserMore = (RelativeLayout) view.findViewById(R.id.layout_user_more);
        layoutUserMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppControl.getInstance().showToast("更多");
            }
        });
        //设置名字、学号
        tvName.setText(userInfo.name);
        tvAccount.setText(userInfo.account);

        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout_mine);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager_mine);
    }

    private void setData() {
        //当前借阅
        if (bookListFragment == null)
            bookListFragment = new BookListFragment();
        //历史借阅
        if (bookHistoryFragment == null)
            bookHistoryFragment = new BookHistoryFragment();
        //账目清单
        if (bookAccountFragment == null)
            bookAccountFragment = new BookAccountFragment();
        //违章缴款
        if (bookFineFragment == null)
            bookFineFragment = new BookFineFragment();
        bookListFragment.setArguments(bundle);
        bookHistoryFragment.setArguments(bundle);
        bookAccountFragment.setArguments(bundle);
        bookFineFragment.setArguments(bundle);
        String[] titles = new String[]{"当前借书", "累计借书", "欠款信息", "账目清单"};
        //设置TabLayout和viewPager
        if (fragments == null)
            fragments = new Fragment[]{bookListFragment, bookHistoryFragment, bookFineFragment, bookAccountFragment};
        if (fragmentAdapter == null)
            fragmentAdapter = new MyFragmentAdapter(getFragmentManager(), fragments, titles);
        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != view) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    //后台登录成功
    @Override
    public void signSuccess(String session) {
        sessionId = session;
        bundle = new Bundle();
        bundle.putString(NetRequest.PHP_SESSION_ID, sessionId);
        signLayout.setVisibility(View.INVISIBLE);
        AppControl.getInstance().getNetRequest().getUserInfo(sessionId, FragMine.this);
    }

    @Override
    public void signFailed() {
        signLayout.setVisibility(View.VISIBLE);
    }

    //登录Activity登录成功
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 107) {
            sessionId = data.getStringExtra("session");
            bundle = new Bundle();
            bundle.putString(NetRequest.PHP_SESSION_ID, sessionId);
            signLayout.setVisibility(View.INVISIBLE);
            AppControl.getInstance().getNetRequest().getUserInfo(sessionId, FragMine.this);
        }

    }

    @Override
    public void getSuccess(UserInfo userInfo) {
        this.userInfo = userInfo;
        //设置名字、学号
        tvName.setText(userInfo.name);
        tvAccount.setText(userInfo.account);
        setData();
    }

    @Override
    public void getFailed() {

    }

}
