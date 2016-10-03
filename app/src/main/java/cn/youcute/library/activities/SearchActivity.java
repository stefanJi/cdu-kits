package cn.youcute.library.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.youcute.library.AppControl;
import cn.youcute.library.R;
import cn.youcute.library.adapter.AdapterSearch;
import cn.youcute.library.bean.Book;
import cn.youcute.library.diyView.LoadMoreListView;
import cn.youcute.library.util.NetRequest;

/**
 * Created by jy on 2016/9/24.
 * 搜索
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener, LoadMoreListView.OnRefreshListener, NetRequest.SearchBookCallBack {
    private EditText etSearch;
    private LoadMoreListView listSearch;
    private ProgressBar progressBar;
    private ImageButton imgBtnSearch;
    private TextView tvSearchInfo;
    private AdapterSearch adapter;
    private String searchKey;
    private List<Book> books;
    private int page;
    private boolean isLoading = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }

    private void initView() {
        ImageButton imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
        imgBtnSearch = (ImageButton) findViewById(R.id.imgBtn_search);
        etSearch = (EditText) findViewById(R.id.et_search);
        listSearch = (LoadMoreListView) findViewById(R.id.list_search);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        tvSearchInfo = (TextView) findViewById(R.id.tv_search_info);

        imgBtnBack.setOnClickListener(this);
        imgBtnSearch.setOnClickListener(this);
        listSearch.setOnRefreshListener(this);
        progressBar.setVisibility(View.INVISIBLE);

        books = new ArrayList<>();
        adapter = new AdapterSearch(SearchActivity.this, books);
        listSearch.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_back:
                finish();
                break;
            case R.id.imgBtn_search:
                searchKey = etSearch.getText().toString();
                if (searchKey.equals("")) {
                    AppControl.getInstance().showToast("搜索内容不能为空");
                    break;
                }
                page = 1;
                books.clear();
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.VISIBLE);
                imgBtnSearch.setVisibility(View.INVISIBLE);
                hideKeyBord();
                AppControl.getInstance().getNetRequest().searchBook(searchKey, page, this);
                break;
        }
    }

    @Override
    public void onLoadingMore() {
        if (isLoading) {
            return;
        }
        isLoading = true;
        page++;
        AppControl.getInstance().getNetRequest().searchBook(searchKey, page, this);
    }

    @Override
    public void searchSuccess(List<Book> data, String info) {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.INVISIBLE);
        }
        imgBtnSearch.setVisibility(View.VISIBLE);
        if (info != null) {
            tvSearchInfo.setText(info);
        }
        if (data.size() == 0) {
            listSearch.noMoreLoad();
            return;
        }
        books.addAll(data);
        adapter.notifyDataSetChanged();
        if (isLoading) {
            isLoading = false;
            listSearch.loadMoreComplete();
        }
    }

    @Override
    public void searchFailed() {
        AppControl.getInstance().showToast("搜索出错");
        if (isLoading) {
            isLoading = false;
            listSearch.loadMoreComplete();
        }
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.INVISIBLE);
        }
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.INVISIBLE);
        }
        imgBtnSearch.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
