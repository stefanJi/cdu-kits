package jiyang.cdu.kits.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jiyang.cdu.kits.AppControl;
import jiyang.cdu.kits.R;
import jiyang.cdu.kits.databinding.ActivitySearchLibraryBinding;
import jiyang.cdu.kits.databinding.ItemSearchBinding;
import jiyang.cdu.kits.model.enty.Book;
import jiyang.cdu.kits.model.enty.FavBook;
import jiyang.cdu.kits.presenter.library.search.LibrarySearchImpl;
import jiyang.cdu.kits.ui.common.AdapterItem;
import jiyang.cdu.kits.ui.common.BaseActivity;
import jiyang.cdu.kits.ui.common.CommAdapter;
import jiyang.cdu.kits.ui.view.library.LibrarySearchView;
import jiyang.cdu.kits.ui.widget.EndlessRecyclerOnScrollListener;
import jiyang.cdu.kits.ui.widget.UiUtils;
import jiyang.cdu.kits.util.CommUtil;

public class LibrarySearchActivity extends BaseActivity<LibrarySearchView, LibrarySearchImpl>
        implements SwipeRefreshLayout.OnRefreshListener,
        LibrarySearchView, CommAdapter.OnItemClickListener {
    ActivitySearchLibraryBinding binding;
    private List<Book> bookList;
    private int page;


    @Override
    public void OnItemClick(int position) {
        Book book = bookList.get(position);
        WebActivity.start(this, book.url);
    }

    static class SEARCH_CONST {
        static final String STR_SEARCH_TYPE_TITLE = "title"; //标题
        static final String STR_SEARCH_TYPE_AUTHOR = "author"; //作者
        static final String STR_SEARCH_TYPE_KEYWORD = "keyword"; //主题词
        static final String STR_SEARCH_TYPE_ISBN = "isbn"; //ISBN/ISSN
        static final String STR_SEARCH_TYPE_ASORDNO = "asordno"; //订购号
        static final String STR_SEARCH_TYPE_CODEN = "coden"; //分类号
        static final String STR_SEARCH_TYPE_CALLNO = "callno"; //索书号
        static final String STR_SEARCH_TYPE_PUBLISHER = "publisher"; //出版社
        static final String STR_SEARCH_TYPE_SERIES = "series"; //从书名

        static final String MATCH_TYPE_FORWARD = "forward"; //前方一致
        static final String MATCH_TYPE_FULL = "full"; //完全匹配
        static final String MATCH_TYPE_ANY = "any"; //任意匹配

        static final String DOC_TYPE_ALL = "ALL"; //所有书刊
        static final String DOC_TYPE_01 = "01"; //中文图书
        static final String DOC_TYPE_02 = "02"; //西文图书
        static final String DOC_TYPE_11 = "11"; //中文期刊
        static final String DOC_TYPE_12 = "12"; //西文期刊
    }

    private String searchType, matchType, docType;

    public static void start(Context context) {
        context.startActivity(new Intent(context, LibrarySearchActivity.class));
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_library);
        page = 1;
        searchType = SEARCH_CONST.STR_SEARCH_TYPE_TITLE;
        matchType = SEARCH_CONST.MATCH_TYPE_FORWARD;
        docType = SEARCH_CONST.DOC_TYPE_ALL;

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitle(getTitle());
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        Drawable navIcon = binding.toolbar.getNavigationIcon();
        if (navIcon != null) {
            navIcon.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        binding.inputSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    bookList.clear();
                    binding.recycleView.getAdapter().notifyDataSetChanged();
                    search();
                }
                return false;
            }
        });
        bookList = new ArrayList<>();

        CommAdapter<Book> adapter = new CommAdapter<Book>(bookList) {
            @Override
            public AdapterItem<Book> createItem() {
                return new AdapterItem<Book>() {
                    ItemSearchBinding searchBinding;

                    @Override
                    public void handleData(int position) {
                        Book book = bookList.get(position);
                        searchBinding.tvSearchName.setText(book.name);
                        searchBinding.tvSearchCode.setText(book.code);
                        searchBinding.tvSearchCount.setText(book.count);
                        searchBinding.favBookImageButton.setTag(position);
                        searchBinding.favBookImageButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int itemPosition = (int) v.getTag();
                                saveFav(itemPosition);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    searchBinding.favBookImageButton.setImageDrawable(getDrawable(R.drawable.ic_round_favorite));
                                } else {
                                    searchBinding.favBookImageButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_round_favorite));
                                }
                                Snackbar.make(binding.getRoot(), "已收藏", Snackbar.LENGTH_SHORT).show();
                            }
                        });
                        searchBinding.shareButton.setTag(position);
                        searchBinding.shareButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int itemPosition = (int) v.getTag();
                                Book b = bookList.get(itemPosition);
                                String shareContent = b.name + "\n" + b.code + "\n" + b.url + "\n(来自" + getString(R.string.app_name) + ")";
                                CommUtil.shareContent(LibrarySearchActivity.this, shareContent);
                            }
                        });
                    }

                    @Override
                    public int getResId() {
                        return R.layout.item_search;
                    }

                    @Override
                    public void bindViews(View itemView) {
                        searchBinding = DataBindingUtil.bind(itemView);
                    }
                };
            }
        };
        binding.recycleView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recycleView.setLayoutManager(layoutManager);
        binding.refreshLayout.setOnRefreshListener(this);
        binding.recycleView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                page = current_page;
                search();
            }
        });
        adapter.setItemClickListener(this);

        binding.spinnerSearchType.setOnItemSelectedListener(spinnerSelectedListener);
        binding.spinnerSearchBookType.setOnItemSelectedListener(spinnerSelectedListener);
        binding.spinnerSearchMatchType.setOnItemSelectedListener(spinnerSelectedListener);
        binding.refreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    @Override
    public LibrarySearchImpl initPresenter() {
        return new LibrarySearchImpl();
    }

    private AdapterView.OnItemSelectedListener spinnerSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            ((TextView) parent.getChildAt(0)).setTextSize(14);
            switch (parent.getId()) {
                case R.id.spinnerSearchType:
                    switch (position) {
                        case 0:
                            searchType = SEARCH_CONST.STR_SEARCH_TYPE_TITLE;
                            break;
                        case 1:
                            searchType = SEARCH_CONST.STR_SEARCH_TYPE_AUTHOR;
                            break;
                        case 2:
                            searchType = SEARCH_CONST.STR_SEARCH_TYPE_KEYWORD;
                            break;
                        case 3:
                            searchType = SEARCH_CONST.STR_SEARCH_TYPE_ISBN;
                            break;
                        case 4:
                            searchType = SEARCH_CONST.STR_SEARCH_TYPE_ASORDNO;
                            break;
                        case 5:
                            searchType = SEARCH_CONST.STR_SEARCH_TYPE_CODEN;
                            break;
                        case 6:
                            searchType = SEARCH_CONST.STR_SEARCH_TYPE_CALLNO;
                            break;
                        case 7:
                            searchType = SEARCH_CONST.STR_SEARCH_TYPE_PUBLISHER;
                            break;
                        case 8:
                            searchType = SEARCH_CONST.STR_SEARCH_TYPE_SERIES;
                            break;
                    }
                    break;
                case R.id.spinnerSearchMatchType:
                    switch (position) {
                        case 0:
                            matchType = SEARCH_CONST.MATCH_TYPE_FORWARD;
                            break;
                        case 1:
                            matchType = SEARCH_CONST.MATCH_TYPE_FULL;
                            break;
                        case 2:
                            matchType = SEARCH_CONST.MATCH_TYPE_ANY;
                            break;
                    }
                    break;
                case R.id.spinnerSearchBookType:
                    switch (position) {
                        case 0:
                            docType = SEARCH_CONST.DOC_TYPE_ALL;
                            break;
                        case 1:
                            docType = SEARCH_CONST.DOC_TYPE_01;
                            break;
                        case 2:
                            docType = SEARCH_CONST.DOC_TYPE_02;
                            break;
                        case 3:
                            docType = SEARCH_CONST.DOC_TYPE_11;
                            break;
                        case 4:
                            docType = SEARCH_CONST.DOC_TYPE_12;
                            break;
                    }
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        isFirstCome(binding.getRoot(), "配置搜索选项，输入搜素内容。点击可查看图书详解");
    }

    private void saveFav(int itemPosition) {
        Book book = bookList.get(itemPosition);
        FavBook favBook = new FavBook(book.name, book.code, book.url);
        AppControl.getInstance().getLiteOrm().save(favBook);
    }

    private void search() {
        String key = binding.inputSearch.getText().toString();
        if (TextUtils.isEmpty(key)) {
            binding.refreshLayout.setRefreshing(false);
            return;
        }
        binding.inputSearch.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(binding.inputSearch.getWindowToken(), 0);
        }
        presenter.searchBook(key, searchType, docType, matchType, page);
    }

    @Override
    public void onRefresh() {
        search();
    }

    @Override
    public void success(List<Book> bookList) {
        if (bookList == null || bookList.size() == 0) {
            return;
        }
        List<Book> update = new ArrayList<>();
        for (Book book : bookList) {
            if (!this.bookList.contains(book)) {
                update.add(book);
            }
        }
        this.bookList.addAll(update);
        binding.recycleView.getAdapter().notifyItemRangeInserted(this.bookList.size() - update.size() - 1,
                update.size());
    }

    @Override
    public void showLoading() {
        binding.refreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        binding.refreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(String error) {
        UiUtils.showErrorSnackbar(this, binding.getRoot(), error, "重试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
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
