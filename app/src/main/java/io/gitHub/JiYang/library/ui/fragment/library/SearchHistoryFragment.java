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
import io.gitHub.JiYang.library.databinding.ItemLibrarySearchHistoryListBinding;
import io.gitHub.JiYang.library.model.enty.LibrarySearchHistory;
import io.gitHub.JiYang.library.presenter.library.LibrarySearchHistoryHistoryImpl;
import io.gitHub.JiYang.library.presenter.library.LibrarySearchHistoryPresenter;
import io.gitHub.JiYang.library.ui.common.AdapterItem;
import io.gitHub.JiYang.library.ui.common.BaseFragment;
import io.gitHub.JiYang.library.ui.common.CommAdapter;
import io.gitHub.JiYang.library.ui.view.library.LibrarySearchHistoryView;
import io.gitHub.JiYang.library.ui.widget.UiUtils;

public class SearchHistoryFragment extends BaseFragment implements LibrarySearchHistoryView, SwipeRefreshLayout.OnRefreshListener {
    @Override
    public String getTitle() {
        return "搜索历史";
    }


    public static SearchHistoryFragment instance() {
        return new SearchHistoryFragment();
    }

    private List<LibrarySearchHistory> searchHistories;
    private CommRecycleListBinding binding;
    private LibrarySearchHistoryPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.comm_recycle_list, container, false);
        init();
        return binding.getRoot();
    }

    private void init() {
        searchHistories = new ArrayList<>();
        CommAdapter<LibrarySearchHistory> adapter = new CommAdapter<LibrarySearchHistory>(searchHistories) {
            @Override
            public AdapterItem<LibrarySearchHistory> createItem() {
                return new AdapterItem<LibrarySearchHistory>() {

                    ItemLibrarySearchHistoryListBinding searchListBinding;

                    @Override
                    public void handleData(int position) {
                        LibrarySearchHistory history = searchHistories.get(position);
                        searchListBinding.searchDate.setText(history.searchDate);
                        searchListBinding.searchKey.setText(history.searchKey);
                    }

                    @Override
                    public int getResId() {
                        return R.layout.item_library_search_history_list;
                    }

                    @Override
                    public void bindViews(View itemView) {
                        searchListBinding = DataBindingUtil.bind(itemView);
                    }
                };
            }
        };
        binding.recycleView.setAdapter(adapter);
        binding.recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.refreshLayout.setOnRefreshListener(this);
        setRefreshLayoutColor(binding.refreshLayout);
        presenter = new LibrarySearchHistoryHistoryImpl(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.fetchSearchList();
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
    public void success(List<LibrarySearchHistory> searchHistories) {
        if (searchHistories == null) {
            return;
        }
        this.searchHistories.clear();
        this.searchHistories.addAll(searchHistories);
        binding.recycleView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void error(String error) {
        UiUtils.showErrorSnackbar(this.getContext(), binding.getRoot(), error);
    }

    @Override
    public void onRefresh() {
        presenter.fetchSearchList();
    }
}
