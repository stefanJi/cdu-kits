package jiyang.cdu.kits.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.MenuItem;

import jiyang.cdu.kits.AppControl;
import jiyang.cdu.kits.Constant;
import jiyang.cdu.kits.R;
import jiyang.cdu.kits.presenter.BasePresenterImpl;
import jiyang.cdu.kits.ui.common.BaseActivity;
import jiyang.cdu.kits.util.SpUtil;


public class SettingActivity extends BaseActivity {

    public static void start(Activity context) {
        context.startActivityForResult(new Intent(context, SettingActivity.class),
                Constant.SETTING_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PrefsFragment prefsFragment = new PrefsFragment();
        getFragmentManager().beginTransaction().replace(android.R.id.content, prefsFragment).commit();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }
    }

    @Override
    public BasePresenterImpl initPresenter() {
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(Activity.RESULT_OK);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(Activity.RESULT_OK);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static class PrefsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
        public static final String PREF_KEY_LIBRARY_USER_INFO = "library_user_info";
        public static final String PREF_KEY_LIBRARY_LOGOUT = "logout";
        public static final String PREF_KEY_LIBRARY_LOGIN = "login_library";
        private static final String PREF_KEY_CATEGORY_LIBRARY = "library";
        private static final String PREF_KEY_CATEGORY_GENERAL = "general";
        private static final String PREF_KEY_TAB_SHOW = "tabs_to_show";

        private boolean isLogin = false;
        private PreferenceScreen preferenceScreen;
        private SpUtil sp;

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
            Preference tab = findPreference(PREF_KEY_TAB_SHOW);
            tab.setOnPreferenceClickListener(this);
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            switch (preference.getKey()) {
                case PREF_KEY_LIBRARY_LOGOUT:
                    new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.sure_log_out_library)
                            .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    sp.setBool(LoginLibraryActivity.HAD_LOGIN, false);
                                    sp.setString(LoginLibraryActivity.LIBRARY_USER_NAME, "");
                                    sp.setString(LoginLibraryActivity.LIBRARY_ACCOUNT, "");
                                    sp.setString(LoginLibraryActivity.LIBRARY_PASSWORD, "");
                                    sp.setString(LoginLibraryActivity.LIBRARY_ACCOUNT_TYPE, "");
                                    dialog.dismiss();
                                    getActivity().recreate();
                                }
                            })
                            .setNegativeButton(R.string.cancel, null)
                            .show();
                    break;
                case PREF_KEY_LIBRARY_LOGIN:
                    LoginLibraryActivity.start(getActivity());
                    break;
                case PREF_KEY_TAB_SHOW:
                    boolean[] checkedItems = new boolean[Constant.FEEDS_TABS.length];
                    for (int i = 0; i < Constant.FEEDS_TABS.length; i++) {
                        checkedItems[i] = sp.getBool(Constant.FEEDS_TABS[i], false);
                    }
                    Activity activity = getActivity();
                    assert activity != null;
                    new AlertDialog.Builder(activity)
                            .setMultiChoiceItems(
                                    Constant.FEEDS_TABS,
                                    checkedItems,
                                    new DialogInterface.OnMultiChoiceClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                            sp.setBool(Constant.FEEDS_TABS[which], isChecked);
                                        }
                                    })
                            .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton(R.string.cancel, null)
                            .show();
                    break;
            }
            return false;
        }

    }
}
