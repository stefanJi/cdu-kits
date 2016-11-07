package cn.youcute.library.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.sivin.Banner;
import com.sivin.BannerAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.youcute.library.AppControl;
import cn.youcute.library.R;
import cn.youcute.library.bean.BannerBean;
import cn.youcute.library.util.NetRequest;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NetRequest.GetHomeCall {
    private ActionBar actionBar;
    private Banner banner;
    private BannerAdapter<BannerBean> bannerAdapter;
    private List<BannerBean> bannerBeanList;
    private TextView tvImageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_good);
        }
        banner = (Banner) findViewById(R.id.banner);
        findViewById(R.id.tv_library).setOnClickListener(this);
        findViewById(R.id.tv_announce).setOnClickListener(this);
        findViewById(R.id.tv_education).setOnClickListener(this);
        findViewById(R.id.tv_net_education).setOnClickListener(this);
        findViewById(R.id.tv_search).setOnClickListener(this);
        tvImageTitle = (TextView) findViewById(R.id.tv_image_title);
        this.bannerBeanList = new ArrayList<>();
        AppControl.getInstance().getNetRequest().getHomeBanner(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                new AlertDialog.Builder(this)
                        .setTitle("欢迎使用橙子助手")
                        .setMessage("作者:阳仔")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("查看帮助", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setNeutralButton("反馈", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create().show();
                break;
            case R.id.action_search:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AcSearch.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            case R.id.action_more:
                Intent intentMore = new Intent();
                intentMore.setClass(MainActivity.this, AcMore.class);
                startActivity(intentMore);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_library:
                intent = new Intent(MainActivity.this, AcLibrary.class);
                startActivity(intent);
                break;
            case R.id.tv_announce:
                intent = new Intent(MainActivity.this, AcNotice.class);
                startActivity(intent);
                break;
            case R.id.tv_search:
                intent = new Intent();
                intent.setClass(MainActivity.this, AcSearch.class);
                startActivity(intent);
                break;
            case R.id.tv_education:
                intent = new Intent(MainActivity.this, AcSignEducation.class);
                startActivity(intent);
                break;
        }
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void getBannerOk(List<BannerBean> bannerBeanList) {
        if (bannerAdapter == null) {
            bannerAdapter = new BannerAdapter<BannerBean>(this.bannerBeanList) {
                @Override
                protected void bindTips(TextView tv, BannerBean bannerBean) {
                    tvImageTitle.setText(bannerBean.title);
                }

                @Override
                public void bindImage(ImageView imageView, BannerBean bannerBean) {
                    ImageLoader.ImageListener listener =
                            ImageLoader.getImageListener(imageView, R.mipmap.ic_good, R.mipmap.ic_good);
                    ImageLoader imageLoader = new ImageLoader(AppControl.getInstance().getRequestQueue(),
                            AppControl.getInstance().getBitmapCache());
                    imageLoader.get(bannerBean.imageUrl, listener);
                }
            };
            banner.setBannerAdapter(bannerAdapter);
            banner.setOnBannerItemClickListener(new Banner.OnBannerItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(MainActivity.this, AcImageShow.class);
                    intent.putExtra("title", MainActivity.this.bannerBeanList.get(position).title);
                    intent.putExtra("url", MainActivity.this.bannerBeanList.get(position).url);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            });
        }
        this.bannerBeanList.addAll(bannerBeanList);
        banner.notifiDataHasChanged();
    }

    @Override
    public void getBannerFailed(String info) {
        AppControl.getInstance().showToast(info);
    }
}
