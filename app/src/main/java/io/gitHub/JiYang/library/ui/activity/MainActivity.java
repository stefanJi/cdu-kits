package io.gitHub.JiYang.library.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import io.gitHub.JiYang.library.Constant;
import io.gitHub.JiYang.library.R;
import io.gitHub.JiYang.library.databinding.ActivityMainBinding;
import io.gitHub.JiYang.library.presenter.MainViewPresenter;
import io.gitHub.JiYang.library.presenter.MainViewPresenterImpl;
import io.gitHub.JiYang.library.ui.common.BaseActivity;
import io.gitHub.JiYang.library.ui.fragment.feeds.FeedsFragment;
import io.gitHub.JiYang.library.ui.fragment.library.LibraryFragment;
import io.gitHub.JiYang.library.ui.fragment.platform.PlatformFragment;
import io.gitHub.JiYang.library.ui.view.MainView;
import io.gitHub.JiYang.library.ui.widget.UiUtils;

public class MainActivity extends BaseActivity implements MainView, NavigationView.OnNavigationItemSelectedListener {

    public static final int REQUEST_CODE = 107;
    private FragmentManager mFragmentManager;
    private MainViewPresenter mainViewPresenter;
    private int checkNavId;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        checkPermission();
        init();
    }

    private void init() {
        mFragmentManager = getSupportFragmentManager();
        mainViewPresenter = new MainViewPresenterImpl();

        setSupportActionBar(binding.appbar.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }
        initNav();
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.drawerMenu, binding.appbar.toolbar, R.string.nav_open, R.string.nav_close);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getColor(R.color.white));
        } else {
            actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        }
        binding.drawerMenu.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        switchFragment(R.id.nav_feeds);
    }

    private void initNav() {
        binding.navMenu.navigationView.setNavigationItemSelectedListener(this);
        checkNavId = R.id.nav_feeds;
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
                LibraryFragment libraryFragment = (LibraryFragment) mFragmentManager.findFragmentByTag(LibraryFragment.TAG);
                if (libraryFragment == null) {
                    libraryFragment = LibraryFragment.newInstance();
                    mFragmentManager.beginTransaction().add(containerId, libraryFragment, LibraryFragment.TAG).commit();
                } else {
                    mFragmentManager.beginTransaction().replace(containerId, libraryFragment, LibraryFragment.TAG).commit();
                }
                setTitle(libraryFragment.getTitle());
                checkNavId = menuId;
                break;

            case R.id.nav_feeds:
                FeedsFragment feedsFragment = (FeedsFragment) mFragmentManager.findFragmentByTag(FeedsFragment.TAG);
                if (feedsFragment == null) {
                    feedsFragment = FeedsFragment.newInstance();
                    mFragmentManager.beginTransaction().add(containerId, feedsFragment, FeedsFragment.TAG).commit();
                } else {
                    mFragmentManager.beginTransaction().replace(containerId, feedsFragment, FeedsFragment.TAG).commit();
                }
                setTitle(feedsFragment.getTitle());
                checkNavId = menuId;
                break;

            case R.id.nav_platform:
                PlatformFragment platformFragment = (PlatformFragment) mFragmentManager.findFragmentByTag(PlatformFragment.TAG);
                if (platformFragment == null) {
                    platformFragment = PlatformFragment.newInstance();
                    mFragmentManager.beginTransaction().add(containerId, platformFragment, PlatformFragment.TAG).commit();
                } else {
                    mFragmentManager.beginTransaction().replace(containerId, platformFragment, PlatformFragment.TAG).commit();
                }
                setTitle(platformFragment.getTitle());
                checkNavId = menuId;
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
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse(Constant.CONTACT_EMAIL));
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.about_email_intent));
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    UiUtils.showErrorSnackbar(this, binding.getRoot(), getString(R.string.about_not_found_email));
                }
                break;
            default:
                break;
        }
    }

    private void checkPermission() {
        String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.navMenu.navigationView.setCheckedItem(checkNavId);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:

                break;
        }
    }

}
