package cn.youcute.library.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.youcute.library.AppControl;
import cn.youcute.library.R;
import cn.youcute.library.adapter.MyFragmentAdapter;
import cn.youcute.library.bean.User;
import cn.youcute.library.bean.UserInfo;
import cn.youcute.library.bookFragments.BookAccountFragment;
import cn.youcute.library.bookFragments.BookFineFragment;
import cn.youcute.library.bookFragments.BookHistoryFragment;
import cn.youcute.library.bookFragments.BookListFragment;
import cn.youcute.library.util.NetRequest;
import cn.youcute.library.util.SpUtil;

/**
 * Created by jy on 2016/9/21.
 * 用户信息
 */
public class UserInfoFragment extends Fragment implements NetRequest.GetInfoCallBack, NetRequest.SignCallBack, SignFragment.SignFragmentCallBack {
    private View rootView;
    private TextView tvName;
    private TextView tvAccount;
    private TextView tvLearnType;
    private SpUtil spUtil;
    private SweetAlertDialog dialog;
    private Bundle bundle;
    private SignFragment signFragment;
    private String sessionId;
    private FragmentManager manager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_userinfo, container, false);
            spUtil = AppControl.getInstance().getSpUtil();
            manager = getFragmentManager();
            if (spUtil.getIsSign()) {
                User user = spUtil.getUser();
                AppControl.getInstance().getNetRequest().sign(user.account, user.password, this);
            } else {
                showSign();
            }
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != rootView) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
    }

    @Override
    public void signSuccess(String session) {
        bundle = new Bundle();
        bundle.putString(NetRequest.PHP_SESSION_ID, session);
        sessionId = session;
        initView();
        setData();
    }

    @Override
    public void signFailed() {
        showSign();
    }

    private void initView() {
        tvName = (TextView) rootView.findViewById(R.id.tv_name);
        tvAccount = (TextView) rootView.findViewById(R.id.tv_account);
        tvLearnType = (TextView) rootView.findViewById(R.id.tv_learn_type);
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewPager_book);
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tablayout_book);
        //当前借阅
        BookListFragment bookListFragment = new BookListFragment();
        bookListFragment.setArguments(bundle);
        //历史借阅
        BookHistoryFragment bookHistoryFragment = new BookHistoryFragment();
        bookHistoryFragment.setArguments(bundle);
        //账目清单
        BookAccountFragment bookAccountFragment = new BookAccountFragment();
        bookAccountFragment.setArguments(bundle);
        //违章缴款
        BookFineFragment bookFineFragment = new BookFineFragment();
        bookFineFragment.setArguments(bundle);
        //设置TabLayout和viewPager
        Fragment[] fragments = new Fragment[]{bookListFragment, bookFineFragment, bookAccountFragment, bookHistoryFragment};
        String[] fragmentTitles = new String[]{"当前借阅", "违章缴款", "账目清单", "历史借阅"};
        MyFragmentAdapter fragmentAdapter = new MyFragmentAdapter(manager, fragments, fragmentTitles);
        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setData() {
        if (spUtil.getIsSign()) {
            UserInfo userInfo;
            userInfo = spUtil.getUserInfo();
            tvName.setText(userInfo.name);
            tvAccount.setText(userInfo.account);
            tvLearnType.setText(userInfo.learnType);
        }
        dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("获取中");
        dialog.setContentText("不急，咱慢慢来获取");
        dialog.setCancelable(false);
        dialog.show();
        //获取个人信息
        AppControl.getInstance().getNetRequest().getUserInfo(sessionId, this);
    }

    @Override
    public void getSuccess(UserInfo userInfo) {
        tvName.setText(userInfo.name);
        tvAccount.setText(userInfo.account);
        tvLearnType.setText(userInfo.learnType);
        dialog.dismissWithAnimation();
    }

    @Override
    public void getFailed() {
        dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
        dialog.setContentText("获取信息失败，请重试");
        dialog.setConfirmText("重试");
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                dialog.dismissWithAnimation();
            }
        });
    }

    private void showSign() {
        if (signFragment == null) {
            signFragment = new SignFragment();
            signFragment.setCallBack(this);
            manager.beginTransaction().add(R.id.sign_layout, signFragment).commit();
        } else {
            manager.beginTransaction().show(signFragment).commit();
        }
    }


    @Override
    public void signOk(String session) {
        manager.beginTransaction().hide(signFragment).commit();
        bundle = new Bundle();
        bundle.putString(NetRequest.PHP_SESSION_ID, session);
        sessionId = session;
        initView();
        setData();
    }

    @Override
    public void signNo() {

    }
}
