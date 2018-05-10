package io.gitHub.JiYang.library.ui.fragment.library;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.EditText;

import io.gitHub.JiYang.library.AppControl;
import io.gitHub.JiYang.library.R;
import io.gitHub.JiYang.library.ui.fragment.BaseFragment;
import io.gitHub.JiYang.library.ui.view.LoginLibraryView;

public class LibraryFragment extends BaseFragment implements LoginLibraryView {

    public static final String TAG = "LIBRARY_FRAGMENT";
    public static final String LOGIN_KEY = "login_library";
    private static final String LIBRARY_ACCOUNT = "lb_ac";
    private static final String LIBRARY_PASSWORD = "lb_ps";
    private static final String LIBRARY_ACCOUNT_TYPE = "lb_ac_type";

    public static LibraryFragment newInstance() {
        return new LibraryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        boolean logined = AppControl.getInstance().getSpUtil().getBool(LOGIN_KEY);
        if (!logined) {
            ViewStub stub = view.findViewById(R.id.viewStub);
            stub.inflate();
            EditText etAccount = view.findViewById(R.id.et_account);
            EditText etPassword = view.findViewById(R.id.et_password);
        }
    }

    @Override
    public String getTitle() {
        return "图书馆";
    }

    @Override
    public void showLoginProgress() {

    }

    @Override
    public void hideLoginProgress() {

    }

    @Override
    public void showLoginError(String error) {

    }

    @Override
    public void showLoginSuccess() {

    }
}
