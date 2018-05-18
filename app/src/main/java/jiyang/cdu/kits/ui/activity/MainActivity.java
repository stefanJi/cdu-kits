package jiyang.cdu.kits.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import jiyang.cdu.kits.BuildConfig;
import jiyang.cdu.kits.Constant;
import jiyang.cdu.kits.R;
import jiyang.cdu.kits.databinding.ActivityMainBinding;
import jiyang.cdu.kits.databinding.DialogVersionUpdateBinding;
import jiyang.cdu.kits.model.enty.Release;
import jiyang.cdu.kits.presenter.main.MainViewPresenterImpl;
import jiyang.cdu.kits.presenter.version.VersionPresenter;
import jiyang.cdu.kits.presenter.version.VersionPresenterImpl;
import jiyang.cdu.kits.ui.common.BaseActivity;
import jiyang.cdu.kits.ui.fragment.feeds.FeedsFragment;
import jiyang.cdu.kits.ui.fragment.library.LibraryFragment;
import jiyang.cdu.kits.ui.fragment.links.LinksFragment;
import jiyang.cdu.kits.ui.view.MainView;
import jiyang.cdu.kits.ui.view.VersionView;
import jiyang.cdu.kits.ui.widget.UiUtils;


public class MainActivity extends BaseActivity<MainView, MainViewPresenterImpl> implements
        MainView, NavigationView.OnNavigationItemSelectedListener, VersionView {

    public static final int REQUEST_CODE = 107;
    private FragmentManager mFragmentManager;
    private int checkNavId;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        checkPermission();
        init();
        JAnalyticsInterface.init(getApplicationContext());
    }

    @Override
    public MainViewPresenterImpl initPresenter() {
        return new MainViewPresenterImpl();
    }

    private void init() {
        mFragmentManager = getSupportFragmentManager();

        setSupportActionBar(binding.appbar.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }
        initNav();
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                binding.drawerMenu, binding.appbar.toolbar, R.string.nav_open, R.string.nav_close);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getColor(R.color.white));
        } else {
            actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        }
        binding.drawerMenu.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        switchFragment(R.id.nav_library);
        VersionPresenter versionPresenter = new VersionPresenterImpl(this);
        versionPresenter.fetchVersion();
    }

    private void initNav() {
        binding.navMenu.navigationView.setNavigationItemSelectedListener(this);
        checkNavId = R.id.nav_feeds;
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.navMenu.navigationView.setCheckedItem(checkNavId);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switchFragment(item.getItemId());
        binding.drawerMenu.closeDrawers();
        item.setCheckable(true);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_library_search:
                LibrarySearchActivity.start(this);
                break;
        }
        return true;
    }

    private void switchFragment(final int menuId) {
        final int containerId = R.id.mainContainer;
        switch (menuId) {
            case R.id.nav_library:
                hideFragments();
                LibraryFragment libraryFragment = (LibraryFragment) mFragmentManager.findFragmentByTag(LibraryFragment.TAG);
                if (libraryFragment == null) {
                    libraryFragment = LibraryFragment.newInstance();
                    mFragmentManager.beginTransaction().add(containerId, libraryFragment, LibraryFragment.TAG).commit();
                } else {
                    mFragmentManager.beginTransaction().show(libraryFragment).commit();
                }
                setTitle(libraryFragment.getTitle());
                checkNavId = menuId;
                break;
            case R.id.nav_feeds:
                hideFragments();
                FeedsFragment feedsFragment = (FeedsFragment) mFragmentManager.findFragmentByTag(FeedsFragment.TAG);
                if (feedsFragment == null) {
                    feedsFragment = FeedsFragment.newInstance();
                    mFragmentManager.beginTransaction().add(containerId, feedsFragment, FeedsFragment.TAG).commit();
                } else {
                    mFragmentManager.beginTransaction().show(feedsFragment).commit();
                }
                setTitle(feedsFragment.getTitle());
                checkNavId = menuId;
                break;
            case R.id.navLinks:
                hideFragments();
                LinksFragment linksFragment = (LinksFragment) mFragmentManager.findFragmentByTag(LinksFragment.TAG);
                if (linksFragment == null) {
                    linksFragment = LinksFragment.newInstance();
                    mFragmentManager.beginTransaction().add(containerId, linksFragment, LinksFragment.TAG).commit();
                } else {
                    mFragmentManager.beginTransaction().show(linksFragment).commit();
                }
                setTitle(linksFragment.getTitle());
                checkNavId = menuId;
                break;
            case R.id.nav_share:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, Constant.SHARE_CONTENT);
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, getString(R.string.share_with)));
                binding.navMenu.navigationView.setCheckedItem(checkNavId);
                break;
            case R.id.nav_search:
                LibrarySearchActivity.start(this);
                break;
            case R.id.nav_fav:
                FavBookActivity.start(this);
                break;
            case R.id.nav_setting:
                SettingActivity.start(this);
                break;
            case R.id.nav_about:
                AboutActivity.start(this);
                break;
            case R.id.nav_feedBack:
                Intent feedbackIntent = new Intent();
                feedbackIntent.setAction(Intent.ACTION_SENDTO);
                feedbackIntent.setData(Uri.parse(Constant.CONTACT_EMAIL));
                feedbackIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.about_email_intent));
                try {
                    startActivity(feedbackIntent);
                } catch (Exception e) {
                    UiUtils.showErrorSnackbar(this, binding.getRoot(), getString(R.string.about_not_found_email));
                }
                binding.navMenu.navigationView.setCheckedItem(checkNavId);
                break;
            default:
                break;
        }
    }

    private void hideFragments() {
        List<Fragment> fragments = mFragmentManager.getFragments();
        for (Fragment f : fragments) {
            mFragmentManager.beginTransaction().hide(f).commit();
        }
    }

    private void checkPermission() {
        String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE
        };
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }
        permissions = new String[permissionList.size()];
        for (int i = 0; i < permissionList.size(); i++) {
            permissions[i] = permissionList.get(i);
        }
        if (permissions.length > 0) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
        }
        binding.navMenu.navigationView.setCheckedItem(checkNavId);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constant.SETTING_ACTIVITY_REQUEST_CODE:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccess(final Release release) {
        float versionNameNum = 0;
        float releaseVersion = 0;
        try {
            versionNameNum = Float.parseFloat(BuildConfig.VERSION_NAME);
            releaseVersion = Float.parseFloat(release.tagName);
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
        }
        if (versionNameNum < releaseVersion) {
            DialogVersionUpdateBinding versionUpdateBinding = DataBindingUtil.inflate(getLayoutInflater(),
                    R.layout.dialog_version_update, null, false);
            versionUpdateBinding.description.setText(release.name);
            versionUpdateBinding.versionName.setText(release.name);
            versionUpdateBinding.getUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(Constant.APP_WEB_SITE));
                    startActivity(intent);
                }
            });
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setView(versionUpdateBinding.getRoot()).create();
            dialog.show();
            Window window = dialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawable(new ColorDrawable(0x00000000));
            }
        }
    }

    @Override
    public void onError(String error) {
        Log.e("TAG", error);
    }
}
