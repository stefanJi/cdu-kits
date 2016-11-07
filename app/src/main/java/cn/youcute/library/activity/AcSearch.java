package cn.youcute.library.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cn.youcute.library.AppControl;
import cn.youcute.library.R;
import cn.youcute.library.adapter.AdapterSearch;
import cn.youcute.library.bean.Book;
import cn.youcute.library.util.NetRequest;

/**
 * Created by jy on 2016/11/6.
 */

public class AcSearch extends AppCompatActivity implements NetRequest.SearchBookCallBack {
    private RadioGroup radioGroup;
    private EditText etSearch;
    private TextView tvSearch;
    private ProgressBar progressBar;
    private TextView tvSearchInfo;
    private ListView listView;
    private AdapterSearch adapterSearch;
    private int searchAction = 0;
    private int page = 1;
    private String key = "";
    private List<Book> books;
    private ProgressBar progressBarFooter;
    private TextView tvLoadInfo;
    private int maxPage = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_search);
        initView();
    }

    private void initView() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_back);
        }
        tvSearch = (TextView) findViewById(R.id.tv_search);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        tvSearchInfo = (TextView) findViewById(R.id.tv_search_info);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        etSearch = (EditText) findViewById(R.id.et_search);
        listView = (ListView) findViewById(R.id.list_search);
        progressBar.setVisibility(View.INVISIBLE);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radio_library) {
                    searchAction = 0;
                } else if (i == R.id.radio_dou_ban) {
                    searchAction = 1;
                }
            }
        });
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_SEARCH) {
                    page = 1;
                    AcSearch.this.books.clear();
                    search();
                    return true;
                }
                return false;
            }
        });
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = 1;
                AcSearch.this.books.clear();
                search();
            }
        });
        books = new ArrayList<>();
    }

    private void search() {
        key = etSearch.getText().toString();
        if (key.equals("")) {
            return;
        }
        hideKeyBord();
        progressBar.setVisibility(View.VISIBLE);
        try {
            AppControl.getInstance().getNetRequest().searchBook(searchAction, key, page, this);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                break;
            case R.id.action_more:
                Intent intentMore = new Intent();
                intentMore.setClass(AcSearch.this, AcMore.class);
                startActivity(intentMore);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            case R.id.action_refresh:
                try {
                    AppControl.getInstance().getNetRequest().searchBook(searchAction, key, page, this);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void searchSuccess(List<Book> books, String info) {
        progressBar.setVisibility(View.INVISIBLE);
        if (progressBarFooter != null) {
            progressBarFooter.setVisibility(View.INVISIBLE);
        }
        if (books.size() == 0) {
            return;
        }
        if (adapterSearch == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.layout_list_footer, null, false);
            progressBarFooter = (ProgressBar) view.findViewById(R.id.progress_footer);
            tvLoadInfo = (TextView) view.findViewById(R.id.tv_load_more);
            progressBarFooter.setVisibility(View.INVISIBLE);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (searchAction == 0) {
                        if (page >= maxPage) {
                            tvLoadInfo.setText("无更多");
                            return;
                        }
                    }
                    tvLoadInfo.setText("点击加载更多");
                    progressBarFooter.setVisibility(View.VISIBLE);
                    page++;
                    try {
                        AppControl.getInstance().getNetRequest().searchBook(searchAction, key, page, AcSearch.this);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });
            listView.addFooterView(view);
            adapterSearch = new AdapterSearch(this, this.books);
            listView.setAdapter(adapterSearch);
        }
        this.books.addAll(books);
        adapterSearch.notifyDataSetChanged();
        if (info.equals("")) {
            return;
        }
        tvSearchInfo.setText(info);
        if (searchAction == 1) {
            return;
        }
        maxPage = Integer.valueOf(info.replace("检索到", "").replace("条", "").replace("题名", "").replace("=", "").replace(key, "")
                .replace("的结果", "").replace(" ", "").replace("   ", ""));
        maxPage = maxPage / 10;
        if (maxPage % 10 >= 1) {
            maxPage++;
        }
    }

    @Override
    public void searchFailed(String info) {
        progressBar.setVisibility(View.INVISIBLE);
        progressBarFooter.setVisibility(View.INVISIBLE);
        tvSearchInfo.setText(info);
    }

    /**
     * 隐藏软键盘
     */
    public void hideKeyBord() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }
}
