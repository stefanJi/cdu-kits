package cn.youcute.library.activities.menuFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

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
 * Created by jy on 2016/9/21.
 * 用户信息
 */
public class FragLibrary extends Fragment implements NetRequest.GetInfoCallBack, NetRequest.SignCallBack {
    private View rootView;
    private SpUtil spUtil;
    private Bundle bundle;
    private String sessionId;
    private FragmentManager manager;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LinearLayout signLayout;
    private Fragment[] fragments;
    private MyFragmentAdapter fragmentAdapter;
    private BookListFragment bookListFragment;
    private BookHistoryFragment bookHistoryFragment;
    private BookAccountFragment bookAccountFragment;
    private BookFineFragment bookFineFragment;
    private NetRequest.GetInfoCallBack getInfoCallBack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_library, container, false);
            spUtil = AppControl.getInstance().getSpUtil();
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
        return rootView;
    }

    //初始化UI
    private void initView() {
        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager_book);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabLayout_book);
        signLayout = (LinearLayout) rootView.findViewById(R.id.layout_sign);
        Button btnSign = (Button) rootView.findViewById(R.id.btn_sign);
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SignActivity.class);
                intent.putExtra("sign_action", 0);
                startActivityForResult(intent, 107);
            }
        });
        manager = getFragmentManager();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != rootView) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!spUtil.getIsSign()) {
            signLayout.setVisibility(View.VISIBLE);
        } else {
            signLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void setGetInfoCallBack(NetRequest.GetInfoCallBack callBack) {
        this.getInfoCallBack = callBack;
    }

    //后台自动登录成功，返回登录session
    @Override
    public void signSuccess(String session) {
        bundle = new Bundle();
        bundle.putString(NetRequest.PHP_SESSION_ID, session);
        sessionId = session;
        //获取个人信息
        AppControl.getInstance().getNetRequest().getUserInfo(sessionId, this);
    }

    @Override
    public void signFailed() {
        AppControl.getInstance().showToast("登录失败，请重试");
    }


    //获取图书馆信息回调
    @Override
    public void getSuccess(UserInfo userInfo) {
        setData();
        signLayout.setVisibility(View.INVISIBLE);
        //向MainActivity告知获取用户信息成功
        getInfoCallBack.getSuccess(userInfo);
    }

    @Override
    public void getFailed() {
        signLayout.setVisibility(View.VISIBLE);
        getInfoCallBack.getFailed();
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
            fragmentAdapter = new MyFragmentAdapter(manager, fragments, titles);
        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    //登录Activity登录成功
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("TAG", "Activity Result,code:" + String.valueOf(requestCode));
        if (requestCode == 107) {
            sessionId = data.getStringExtra("session");
            bundle = new Bundle();
            bundle.putString(NetRequest.PHP_SESSION_ID, sessionId);
            AppControl.getInstance().getNetRequest().getUserInfo(sessionId, FragLibrary.this);
        }

    }
}
