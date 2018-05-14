package jiyang.cdu.kits.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;

import com.litesuits.orm.LiteOrm;

import java.util.ArrayList;
import java.util.List;

import jiyang.cdu.kits.AppControl;
import jiyang.cdu.kits.R;
import jiyang.cdu.kits.databinding.ActivityFavBookBinding;
import jiyang.cdu.kits.databinding.ItemSearchBinding;
import jiyang.cdu.kits.model.enty.FavBook;
import jiyang.cdu.kits.presenter.library.FavBookPresenterImpl;
import jiyang.cdu.kits.presenter.library.FavBookPresenters;
import jiyang.cdu.kits.ui.common.AdapterItem;
import jiyang.cdu.kits.ui.common.BaseActivity;
import jiyang.cdu.kits.ui.common.CommAdapter;
import jiyang.cdu.kits.ui.view.library.FavBookView;
import jiyang.cdu.kits.ui.widget.UiUtils;


public class FavBookActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, FavBookView, CommAdapter.OnItemClickListener {

    public static void start(Context context) {
        context.startActivity(new Intent(context, FavBookActivity.class));
    }

    ActivityFavBookBinding binding;
    List<FavBook> favBooks;
    FavBookPresenters.FavBookPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fav_book);
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Drawable navIcon = binding.toolbar.getNavigationIcon();
        if (navIcon != null) {
            navIcon.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }
        binding.refreshLayout.setOnRefreshListener(this);
        this.favBooks = new ArrayList<>();
        CommAdapter<FavBook> adapter = new CommAdapter<FavBook>(favBooks) {
            @Override
            public AdapterItem<FavBook> createItem() {
                return new AdapterItem<FavBook>() {
                    ItemSearchBinding searchBinding;

                    @Override
                    public void handleData(int position) {
                        FavBook book = favBooks.get(position);
                        searchBinding.tvSearchName.setText(book.bookName);
                        searchBinding.tvSearchCode.setText(book.bookCode);
                        searchBinding.favBookImageButton.setVisibility(View.GONE);
                        searchBinding.tvSearchCount.setVisibility(View.GONE);
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
        adapter.setItemClickListener(this);
        binding.recycleView.setAdapter(adapter);
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper.Callback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                removeFave(position);
            }
        };
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(binding.recycleView);
        presenter = new FavBookPresenterImpl(this);
        presenter.fetchFevBooks();
    }

    @Override
    protected void onStart() {
        super.onStart();
        isFirstCome(binding.getRoot(), "左划可删除收藏，点击可查看详情");
    }

    public void removeFave(final int position) {
        final LiteOrm liteOrm = AppControl.getInstance().getLiteOrm();
        final FavBook favBook = favBooks.get(position);
        liteOrm.delete(favBook);
        favBooks.remove(position);
        binding.recycleView.getAdapter().notifyItemRemoved(position);
        Snackbar.make(binding.getRoot(), "删除成功", Snackbar.LENGTH_LONG)
                .setAction("撤销", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        liteOrm.save(favBook);
                        favBooks.add(favBook);
                        binding.recycleView.getAdapter().notifyItemInserted(position);
                    }
                }).show();
    }

    @Override
    public void onRefresh() {
        presenter.fetchFevBooks();
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
    public void setBookList(List<FavBook> favBookList) {
        this.favBooks.clear();
        this.favBooks.addAll(favBookList);
        binding.recycleView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showError(String error) {
        UiUtils.showErrorSnackbar(this, binding.getRoot(), error);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnItemClick(int position) {
        FavBook favBook = favBooks.get(position);
        WebActivity.start(this, favBook.bookUrl);
    }
}
