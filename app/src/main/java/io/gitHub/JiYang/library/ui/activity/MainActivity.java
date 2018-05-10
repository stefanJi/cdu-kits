package io.gitHub.JiYang.library.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import io.gitHub.JiYang.library.R;
import io.gitHub.JiYang.library.presenter.MainViewPresenter;
import io.gitHub.JiYang.library.presenter.MainViewPresenterImpl;
import io.gitHub.JiYang.library.ui.common.BaseActivity;
import io.gitHub.JiYang.library.ui.fragment.feeds.FeedsFragment;
import io.gitHub.JiYang.library.ui.fragment.library.LibraryFragment;
import io.gitHub.JiYang.library.ui.fragment.platform.PlatformFragment;
import io.gitHub.JiYang.library.ui.view.MainView;

public class MainActivity extends BaseActivity implements MainView, NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager mFragmentManager;
    private MainViewPresenter mainViewPresenter;
    private DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);
        init();
    }

    private void init() {
        mFragmentManager = getSupportFragmentManager();
        mainViewPresenter = new MainViewPresenterImpl();
        Toolbar mToolbar = findViewById(R.id.toolbarMain);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        mDrawerLayout = findViewById(R.id.drawer_menu);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_feeds);
        navigationView.setItemIconTintList(null);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.nav_open, R.string.nav_close);
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        switchFragment(R.id.nav_feeds);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switchFragment(item.getItemId());
        mDrawerLayout.closeDrawers();
        item.setCheckable(true);
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
                break;
            default:
                break;
        }
    }
}
