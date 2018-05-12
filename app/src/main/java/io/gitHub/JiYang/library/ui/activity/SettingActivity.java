package io.gitHub.JiYang.library.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import io.gitHub.JiYang.library.AppControl;
import io.gitHub.JiYang.library.R;
import io.gitHub.JiYang.library.ui.common.BaseActivity;
import io.gitHub.JiYang.library.util.SpUtil;

public class SettingActivity extends BaseActivity {

    public static void start(Context context) {
        context.startActivity(new Intent(context, SettingActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PrefsFragment prefsFragment = new PrefsFragment();
        getFragmentManager().beginTransaction().replace(android.R.id.content, prefsFragment).commit();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class PrefsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
        public static final String PREF_KEY_LIBRARY_USER_INFO = "library_user_info";
        public static final String PREF_KEY_LIBRARY_LOGOUT = "logout";
        public static final String PREF_KEY_LIBRARY_LOGIN = "login_library";
        private static final String PREF_KEY_CATEGORY_LIBRARY = "library";
        private static final String PREF_KEY_CATEGORY_GENERAL = "general";
        private boolean isLogin = false;
        PreferenceScreen preferenceScreen;
        SpUtil sp;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            getPreferenceManager().setSharedPreferencesName("cdu_kits_setting");
            preferenceScreen = getPreferenceScreen();
            sp = AppControl.getInstance().getSpUtil();
            initGeneral();
            initUser();
        }

        private void initUser() {
            isLogin = sp.getBool(LoginLibraryActivity.HAD_LOGIN, false);
            PreferenceCategory library = (PreferenceCategory) findPreference(PREF_KEY_CATEGORY_LIBRARY);
            if (!isLogin) {
                preferenceScreen.removePreference(library);
                return;
            }
            Preference userInfo = findPreference(PREF_KEY_LIBRARY_USER_INFO);
            String userName = sp.getString(LoginLibraryActivity.LIBRARY_USER_NAME);
            userInfo.setSummary(userName);
            Preference logout = findPreference(PREF_KEY_LIBRARY_LOGOUT);
            logout.setOnPreferenceClickListener(this);
        }

        private void initGeneral() {
            PreferenceCategory general = (PreferenceCategory) findPreference(PREF_KEY_CATEGORY_GENERAL);
            isLogin = sp.getBool(LoginLibraryActivity.HAD_LOGIN, false);
            Preference loginLibrary = findPreference(PREF_KEY_LIBRARY_LOGIN);
            if (isLogin) {
                general.removePreference(loginLibrary);
            } else {
                loginLibrary.setOnPreferenceClickListener(this);
            }
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            switch (preference.getKey()) {
                case PREF_KEY_LIBRARY_LOGOUT:
                    new AlertDialog.Builder(getActivity())
                            .setTitle("确定登出图书馆?")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    sp.setBool(LoginLibraryActivity.HAD_LOGIN, false);
                                    sp.setString(LoginLibraryActivity.LIBRARY_USER_NAME, "");
                                    sp.setString(LoginLibraryActivity.LIBRARY_ACCOUNT, "");
                                    sp.setString(LoginLibraryActivity.LIBRARY_PASSWORD, "");
                                    sp.setString(LoginLibraryActivity.LIBRARY_ACCOUNT_TYPE, "");
                                    getActivity().recreate();
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                    break;
                case PREF_KEY_LIBRARY_LOGIN:
                    LoginLibraryActivity.start(getActivity());
                    break;
            }
            return false;
        }
    }
}
