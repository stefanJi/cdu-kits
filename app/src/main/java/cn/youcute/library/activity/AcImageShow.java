package cn.youcute.library.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import cn.youcute.library.AppControl;
import cn.youcute.library.R;
import cn.youcute.library.adapter.AdapterImage;
import cn.youcute.library.bean.Album;
import cn.youcute.library.util.NetRequest;

/**
 * Created by jy on 2016/11/6.
 */

public class AcImageShow extends AppCompatActivity implements NetRequest.GetAlbumCall {
    private String title, url;
    private ListView listView;
    private ProgressBar progressBar;
    private AdapterImage adapterImage;
    private List<Album> albumList;
    private ActionBar actionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_image_show);
        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
        initView();
    }

    private void initView() {
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_back);
            actionBar.setTitle(title);
        }
        listView = (ListView) findViewById(R.id.list_image);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        albumList = new ArrayList<>();
        adapterImage = new AdapterImage(this.albumList, AcImageShow.this);
        listView.setAdapter(adapterImage);
        AppControl.getInstance().getNetRequest().getImageAlbum(url, this);
    }

    @Override
    public void getAlbumOk(List<Album> albumList) {
        progressBar.setVisibility(View.INVISIBLE);
        if (albumList.size() == 0) {
            return;
        }
        this.albumList.addAll(albumList);
        adapterImage.notifyDataSetChanged();
    }

    @Override
    public void getAlumFailed(String info) {
        progressBar.setVisibility(View.INVISIBLE);
        actionBar.setTitle(info);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                break;
            case R.id.action_refresh:
                this.albumList.clear();
                adapterImage.notifyDataSetChanged();
                progressBar.setVisibility(View.VISIBLE);
                AppControl.getInstance().getNetRequest().getImageAlbum(url, this);
                break;
            case R.id.action_more:
                Intent intentMore = new Intent();
                intentMore.setClass(AcImageShow.this, AcMore.class);
                startActivity(intentMore);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
