package jiyang.cdu.kits.ui.fragment.library;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jiyang.cdu.kits.AppControl;
import jiyang.cdu.kits.R;
import jiyang.cdu.kits.databinding.FragmentLibraryBinding;
import jiyang.cdu.kits.model.enty.LibraryUserInfo;
import jiyang.cdu.kits.presenter.library.login.LoginPresenterImpl;
import jiyang.cdu.kits.ui.activity.LoginLibraryActivity;
import jiyang.cdu.kits.ui.common.BaseFragment;
import jiyang.cdu.kits.ui.view.library.LoginLibraryView;
import jiyang.cdu.kits.ui.widget.UiUtils;
import jiyang.cdu.kits.util.SpUtil;

import static jiyang.cdu.kits.Constant.HAD_LOGIN;
import static jiyang.cdu.kits.Constant.LIBRARY_ACCOUNT;
import static jiyang.cdu.kits.Constant.LIBRARY_ACCOUNT_TYPE;
import static jiyang.cdu.kits.Constant.LIBRARY_PASSWORD;

public class LibraryFragment extends BaseFragment<LoginLibraryView, LoginPresenterImpl> implements View.OnClickListener, LoginLibraryView {

    public static final String TAG = "LIBRARY_FRAGMENT";

    private FragmentLibraryBinding binding;
    private boolean hasLoadViewPage = false;
    private BaseFragment[] baseFragments;

    public static LibraryFragment newInstance() {
        return new LibraryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        binding = DataBindingUtil.bind(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        SpUtil sp = AppControl.getInstance().getSpUtil();
        boolean loginEd = sp.getBool(HAD_LOGIN, false);
        if (!loginEd) {
            binding.loginTip.setVisibility(View.VISIBLE);
            binding.loginTip.setOnClickListener(this);
            binding.viewPager.setVisibility(View.GONE);
            binding.tabLayout.setVisibility(View.GONE);
        } else {
            binding.loginTip.setVisibility(View.GONE);
            binding.viewPager.setVisibility(View.VISIBLE);
            binding.tabLayout.setVisibility(View.VISIBLE);
            if (!hasLoadViewPage) {
                fetchUserInfo(sp);
            }
        }
    }

    private void fetchUserInfo(SpUtil sp) {
        String account = sp.getString(LIBRARY_ACCOUNT);
        String password = sp.getString(LIBRARY_PASSWORD);
        String loginType = sp.getString(LIBRARY_ACCOUNT_TYPE);
        presenter.login(account, password, loginType);
    }

    private void login() {
        Context context = getContext();
        if (context != null) {
            LoginLibraryActivity.start(context);
        }
    }

    @Override
    public String getTitle() {
        return "图书馆";
    }

    @Override
    public LoginPresenterImpl initPresenter() {
        return new LoginPresenterImpl();
    }


    public void showLoginEdLayout(LibraryUserInfo userInfo) {
        baseFragments = new BaseFragment[]{
                UserProfileFragment.instance(userInfo),
                BookHistoryFragment.instance(),
                SearchHistoryFragment.instance()
        };
        binding.viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
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
        binding.viewPager.setOffscreenPageLimit(3);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginTip:
                login();
                break;
        }
    }

    @Override
    public void showLoginProgress() {
        binding.progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoginProgress() {
        binding.progress.setVisibility(View.GONE);
    }

    @Override
    public void showLoginError(String error) {
        if (!hasLoadViewPage) {
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SpUtil sp = AppControl.getInstance().getSpUtil();
                    fetchUserInfo(sp);
                }
            });
        }
        hasLoadViewPage = false;
        UiUtils.showErrorSnackbar(getContext(), binding.getRoot(), error, "重试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpUtil sp = AppControl.getInstance().getSpUtil();
                fetchUserInfo(sp);
            }
        });
    }

    @Override
    public void showLoginSuccess(LibraryUserInfo userInfo) {
        showLoginEdLayout(userInfo);
        hasLoadViewPage = true;
        binding.getRoot().setOnClickListener(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FragmentManager fragmentManager = getChildFragmentManager();
        destoryChildFragment(baseFragments);
    }
}
