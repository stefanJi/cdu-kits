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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cn.youcute.library.AppControl;
import cn.youcute.library.R;
import cn.youcute.library.adapter.AdapterSearch;
import cn.youcute.library.bean.Book;
import cn.youcute.library.util.NetRequest;
import cn.youcute.library.util.ToastUtil;

/**
 * Created by jy on 2016/11/6.
 */

public class AcSearch extends AppCompatActivity implements NetRequest.SearchBookCallBack {
    private RadioGroup radioGroup;
    private EditText etSearch;
    private TextView tvSearch;
    private ProgressBar progressBar;
    private TextView tvSearchInfo;
    private TextView tvCollection;
    private ListView listView;
    private AdapterSearch adapterSearch;
    private int searchAction = 0;
    private int page = 1;
    private String key = "";
    private List<Book> books;
    private ProgressBar progressBarFooter;
    private TextView tvLoadInfo;
    private int maxPage = 1;
    private View footerView;

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
        books = new ArrayList<>();
        View headView = LayoutInflater.from(this).inflate(R.layout.item_list_search_head, null, false);
        tvSearch = (TextView) headView.findViewById(R.id.tv_search);
        progressBar = (ProgressBar) headView.findViewById(R.id.progress);
        tvSearchInfo = (TextView) headView.findViewById(R.id.tv_search_info);
        radioGroup = (RadioGroup) headView.findViewById(R.id.radio_group);
        etSearch = (EditText) headView.findViewById(R.id.et_search);
        tvCollection = (TextView) headView.findViewById(R.id.tv_collection);
        listView = (ListView) findViewById(R.id.list_search);
        footerView = LayoutInflater.from(this).inflate(R.layout.layout_list_footer, null, false);
        progressBarFooter = (ProgressBar) footerView.findViewById(R.id.progress_footer);
        tvLoadInfo = (TextView) footerView.findViewById(R.id.tv_load_more);
        progressBarFooter.setVisibility(View.INVISIBLE);
        footerView.setOnClickListener(new View.OnClickListener() {
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
        listView.addHeaderView(headView);
        listView.addFooterView(footerView);
        footerView.setVisibility(View.INVISIBLE);
        adapterSearch = new AdapterSearch(this, this.books);
        listView.setAdapter(adapterSearch);
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
                if (i == EditorInfo.IME_ACTION_SEARCH) {
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (books.size() > 0) {
                    String title = books.get(i - 1).name;
                    String url = books.get(i - 1).url;
                    Intent intent = new Intent(AcSearch.this, AcWebViewSearch.class);
                    intent.putExtra("title", title);
                    intent.putExtra("url", url);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            }
        });
        getAd();
    }

    /**
     * 获取广告
     */
    private void getAd() {
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void search() {
        key = etSearch.getText().toString();
        if (key.equals("")) {
            ToastUtil.showToast("输入不能为空");
            return;
        }
        adapterSearch.setSearchKey(key);
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
    protected void onStart() {
        super.onStart();
        final String[] collection = AppControl.getInstance().getSpUtil().getBook();
        if (collection[0].equals("")) {
            tvCollection.setText("无");
            return;
        }
        tvCollection.setText(collection[0]);
        tvCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AcSearch.this, AcWebViewSearch.class);
                intent.putExtra("title", collection[0]);
                intent.putExtra("url", collection[1]);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                break;
            case R.id.action_refresh:
                search();
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
            tvSearchInfo.setText("无该书记录");
            return;
        }
        footerView.setVisibility(View.VISIBLE);
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
