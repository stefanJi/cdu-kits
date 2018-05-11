package io.gitHub.JiYang.library.ui.fragment.library;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import io.gitHub.JiYang.library.AppControl;
import io.gitHub.JiYang.library.R;
import io.gitHub.JiYang.library.model.enty.LibraryUserInfo;
import io.gitHub.JiYang.library.presenter.library.LoginPresenter;
import io.gitHub.JiYang.library.presenter.library.LoginPresenterImpl;
import io.gitHub.JiYang.library.ui.common.BaseFragment;
import io.gitHub.JiYang.library.ui.view.LoginLibraryView;
import io.gitHub.JiYang.library.util.SpUtil;

public class LibraryFragment extends BaseFragment implements LoginLibraryView, View.OnClickListener {

    public static final String TAG = "LIBRARY_FRAGMENT";
    public static final String LOGIN_KEY = "login_library";
    private static final String LIBRARY_ACCOUNT = "lb_ac";
    private static final String LIBRARY_PASSWORD = "lb_ps";
    private static final String LIBRARY_ACCOUNT_TYPE = "lb_ac_type";

    private View root;
    private ProgressBar progressBar;
    private LoginPresenter loginPresenter;
    private String loginLibraryType = LoginPresenter.TYPE_CERT_ON;
    private EditText etAccount, etPassword;
    private String account, password;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewStub stub;

    public static LibraryFragment newInstance() {
        return new LibraryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        root = view;
        init(view);
        return view;
    }

    private void init(View view) {
        progressBar = view.findViewById(R.id.progressLibrary);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.viewPager);
        loginPresenter = new LoginPresenterImpl(this);
        boolean logined = AppControl.getInstance().getSpUtil().getBool(LOGIN_KEY);
        if (!logined) {
            showLoginStub();
        } else {
            SpUtil spUtil = AppControl.getInstance().getSpUtil();
            String account = spUtil.getString(LIBRARY_ACCOUNT);
            String password = spUtil.getString(LIBRARY_PASSWORD);
            String type = spUtil.getString(LIBRARY_ACCOUNT_TYPE);
            loginPresenter.login(account, password, type);
        }
    }

    private void showLoginStub() {
        stub = root.findViewById(R.id.viewStub);
        stub.inflate();
        etAccount = root.findViewById(R.id.et_account);
        etPassword = root.findViewById(R.id.et_password);
        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && actionId == EditorInfo.IME_ACTION_DONE) {
                    login();
                    return true;
                }
                return false;
            }
        });
        etAccount.setText("201410421111");
        etPassword.setText("jiyang147852");
        RadioGroup radioGroup = root.findViewById(R.id.loginLibraryUserTypeGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.loginByCard:
                        loginLibraryType = LoginPresenter.TYPE_CERT_ON;
                        break;
                    case R.id.loginBySerNumber:
                        loginLibraryType = LoginPresenter.TYPE_SER_NUM;
                        break;
                    case R.id.loginByEmail:
                        loginLibraryType = LoginPresenter.TYPE_EMAIL;
                        break;
                }
            }
        });
        root.findViewById(R.id.btnLoginLibrary).setOnClickListener(this);
    }

    @Override
    public String getTitle() {
        return "图书馆";
    }

    @Override
    public void showLoginProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoginProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showLoginError(String error) {
        AppControl.getInstance().getSpUtil().setBool(LOGIN_KEY, false);
        showLoginStub();
        Snackbar.make(root, error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showLoginSuccess(LibraryUserInfo userInfo) {
        Snackbar.make(root, "登录成功", Snackbar.LENGTH_SHORT).show();
        SpUtil spUtil = AppControl.getInstance().getSpUtil();
        spUtil.setBool(LOGIN_KEY, true);
        spUtil.setString(LIBRARY_ACCOUNT, account);
        spUtil.setString(LIBRARY_PASSWORD, password);
        spUtil.setString(LIBRARY_ACCOUNT_TYPE, loginLibraryType);

        if (stub != null) {
            stub.setVisibility(View.GONE);
        }
        final BaseFragment[] baseFragments = new BaseFragment[]{
                UserProfileFragment.instance(userInfo)
        };
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLoginLibrary:
                login();
                break;
        }
    }

    private void login() {
        if (loginPresenter != null) {
            account = etAccount.getText().toString();
            password = etPassword.getText().toString();
            if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
                return;
            }
            loginPresenter.login(account, password, loginLibraryType);
        }
    }
}
