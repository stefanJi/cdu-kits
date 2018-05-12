package io.gitHub.JiYang.library.ui.fragment.library;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.gitHub.JiYang.library.R;
import io.gitHub.JiYang.library.databinding.CommRecycleListBinding;
import io.gitHub.JiYang.library.databinding.ItemBookhistoryBinding;
import io.gitHub.JiYang.library.model.enty.BookHistory;
import io.gitHub.JiYang.library.presenter.library.BookHistoryImpl;
import io.gitHub.JiYang.library.presenter.library.BookHistoryPresenter;
import io.gitHub.JiYang.library.ui.activity.WebActivity;
import io.gitHub.JiYang.library.ui.common.AdapterItem;
import io.gitHub.JiYang.library.ui.common.BaseFragment;
import io.gitHub.JiYang.library.ui.common.CommAdapter;
import io.gitHub.JiYang.library.ui.view.library.LibraryHistoryView;
import io.gitHub.JiYang.library.ui.widget.UiUtils;

public class BookHistoryFragment extends BaseFragment implements LibraryHistoryView, CommAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    @Override
    public String getTitle() {
        return "历史借阅";
    }


    public static BookHistoryFragment instance() {
        return new BookHistoryFragment();
    }


    private CommRecycleListBinding binding;
    private List<BookHistory> bookHistoryList;

    private BookHistoryPresenter bookHistoryPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.comm_recycle_list, container, false);
        init();
        return binding.getRoot();
    }

    private void init() {
        bookHistoryList = new ArrayList<>();
        CommAdapter<BookHistory> bookHistoryAdapter = new CommAdapter<BookHistory>(bookHistoryList) {
            @Override
            public AdapterItem<BookHistory> createItem() {
                return new AdapterItem<BookHistory>() {
                    ItemBookhistoryBinding itemBookhistoryBinding;

                    @Override
                    public void handleData(int position) {
                        BookHistory history = bookHistoryList.get(position);
                        itemBookhistoryBinding.tvHistoryId.setText(history.historyId);
                        itemBookhistoryBinding.tvHistoryBookName.setText(history.name);
                        itemBookhistoryBinding.tvGetData.setText(history.getData);
                        itemBookhistoryBinding.tvEndData.setText(history.endData);
                    }

                    @Override
                    public int getResId() {
                        return R.layout.item_bookhistory;
                    }

                    @Override
                    public void bindViews(View itemView) {
                        itemBookhistoryBinding = DataBindingUtil.bind(itemView);
                    }
                };
            }
        };
        binding.recycleView.setAdapter(bookHistoryAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        binding.recycleView.setLayoutManager(linearLayoutManager);
        bookHistoryAdapter.setItemClickListener(this);

        binding.refreshLayout.setOnRefreshListener(this);
        setRefreshLayoutColor(binding.refreshLayout);

        bookHistoryPresenter = new BookHistoryImpl(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bookHistoryPresenter.fetchHistory();
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
    public void success(List<BookHistory> bookHistories) {
        bookHistoryList.clear();
        bookHistoryList.addAll(bookHistories);
        binding.recycleView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void error(String error) {
        UiUtils.showErrorSnackbar(this.getActivity(), binding.getRoot(), error);
    }

    @Override
    public void OnItemClick(int position) {
        BookHistory bookHistory = bookHistoryList.get(position);
        WebActivity.start(getContext(), bookHistory.url);
    }

    @Override
    public void onRefresh() {
        this.bookHistoryPresenter.fetchHistory();
    }
}
