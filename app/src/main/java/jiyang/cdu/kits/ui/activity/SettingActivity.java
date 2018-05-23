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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jiyang.cdu.kits.AppControl;
import jiyang.cdu.kits.Constant;
import jiyang.cdu.kits.R;
import jiyang.cdu.kits.presenter.BasePresenterImpl;
import jiyang.cdu.kits.ui.common.BaseActivity;
import jiyang.cdu.kits.util.SpUtil;

import static jiyang.cdu.kits.Constant.HAD_LOGIN;
import static jiyang.cdu.kits.Constant.LIBRARY_ACCOUNT;
import static jiyang.cdu.kits.Constant.LIBRARY_ACCOUNT_TYPE;
import static jiyang.cdu.kits.Constant.LIBRARY_PASSWORD;
import static jiyang.cdu.kits.Constant.LIBRARY_USER_NAME;
import static jiyang.cdu.kits.Constant.PREF_KEY_CATEGORY_GENERAL;
import static jiyang.cdu.kits.Constant.PREF_KEY_CATEGORY_LIBRARY;
import static jiyang.cdu.kits.Constant.PREF_KEY_LIBRARY_LOGIN;
import static jiyang.cdu.kits.Constant.PREF_KEY_LIBRARY_LOGOUT;
import static jiyang.cdu.kits.Constant.PREF_KEY_LIBRARY_USER_INFO;
import static jiyang.cdu.kits.Constant.PREF_KEY_TAB_SHOW;


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
        Toast.makeText(this, "下次启动将应用新的设置", Toast.LENGTH_SHORT).show();
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

        private boolean isLogin = false;
        private PreferenceScreen preferenceScreen;
        private SpUtil sp;
        private Preference tabPref;
        private PreferenceCategory general;

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
            isLogin = sp.getBool(HAD_LOGIN, false);
            PreferenceCategory library = (PreferenceCategory) findPreference(PREF_KEY_CATEGORY_LIBRARY);
            if (!isLogin) {
                preferenceScreen.removePreference(library);
                return;
            }
            Preference userInfo = findPreference(PREF_KEY_LIBRARY_USER_INFO);
            String userName = sp.getString(LIBRARY_USER_NAME);
            userInfo.setSummary(userName);
            Preference logout = findPreference(PREF_KEY_LIBRARY_LOGOUT);
            logout.setOnPreferenceClickListener(this);
        }

        private void initGeneral() {
            general = (PreferenceCategory) findPreference(PREF_KEY_CATEGORY_GENERAL);
            isLogin = sp.getBool(HAD_LOGIN, false);
            Preference loginLibrary = findPreference(PREF_KEY_LIBRARY_LOGIN);
            if (isLogin) {
                general.removePreference(loginLibrary);
            } else {
                loginLibrary.setOnPreferenceClickListener(this);
            }
            tabPref = findPreference(PREF_KEY_TAB_SHOW);
            tabPref.setOnPreferenceClickListener(this);
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
                                    sp.setBool(HAD_LOGIN, false);
                                    sp.setString(LIBRARY_USER_NAME, "");
                                    sp.setString(LIBRARY_ACCOUNT, "");
                                    sp.setString(LIBRARY_PASSWORD, "");
                                    sp.setString(LIBRARY_ACCOUNT_TYPE, "");
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton(R.string.cancel, null)
                            .show();
                    break;
                case PREF_KEY_LIBRARY_LOGIN:
                    LoginLibraryActivity.start(getActivity());
                    break;
                case PREF_KEY_TAB_SHOW:
                    showTabConfig();
                    break;
            }
            return false;
        }

        public void showTabConfig() {
            List<String> names = new ArrayList<>(Arrays.asList(Constant.FEEDS_TABS));
            boolean[] checkedItems = new boolean[names.size()];
            final String[] titles = new String[names.size()];
            for (int i = 0; i < names.size(); i++) {
                checkedItems[i] = sp.getBool(names.get(i), false);
                titles[i] = names.get(i);
            }
            Activity activity = getActivity();
            assert activity != null;
            new AlertDialog.Builder(activity)
                    .setMultiChoiceItems(titles, checkedItems,
                            new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                    sp.setBool(titles[which], isChecked);
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
        }
    }
}
