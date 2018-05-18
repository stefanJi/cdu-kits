package jiyang.cdu.kits.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;

import jiyang.cdu.kits.BuildConfig;
import jiyang.cdu.kits.Constant;
import jiyang.cdu.kits.R;
import jiyang.cdu.kits.databinding.ActivityAboutBinding;
import jiyang.cdu.kits.presenter.BasePresenterImpl;
import jiyang.cdu.kits.ui.common.BaseActivity;
import jiyang.cdu.kits.ui.widget.UiUtils;


public class AboutActivity extends BaseActivity implements View.OnClickListener {
    public static void start(Context context) {
        context.startActivity(new Intent(context, AboutActivity.class));
    }

    private ActivityAboutBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about);
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Drawable navIcon = binding.toolbar.getNavigationIcon();
        if (navIcon != null) {
            navIcon.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }
        binding.version.setText(String.format("版本: %s", BuildConfig.VERSION_NAME));
        binding.comment.setOnClickListener(this);
        binding.feedback.setOnClickListener(this);
        binding.mySite.setOnClickListener(this);
        binding.sourceCode.setOnClickListener(this);
        binding.openSource.setOnClickListener(this);
        binding.share.setOnClickListener(this);
    }

    @Override
    public BasePresenterImpl initPresenter() {
        return null;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.comment:
                intent.setData(Uri.parse(Constant.APP_WEB_SITE));
                intent.setAction(Intent.ACTION_VIEW);
                startActivity(intent);
                break;
            case R.id.feedback:
                intent.setAction(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse(Constant.CONTACT_EMAIL));
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.about_email_intent));
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    UiUtils.showErrorSnackbar(this, binding.getRoot(), getString(R.string.about_not_found_email));
                }
                break;
            case R.id.my_site:
                intent.setData(Uri.parse(Constant.MY_SITE));
                intent.setAction(Intent.ACTION_VIEW);
                startActivity(intent);
                break;
            case R.id.source_code:
                intent.setData(Uri.parse(Constant.GIT_HUB_REPO));
                intent.setAction(Intent.ACTION_VIEW);
                startActivity(intent);
                break;
            case R.id.open_source:
                break;
            case R.id.share:
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, Constant.SHARE_CONTENT);
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, getString(R.string.share_with)));
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
