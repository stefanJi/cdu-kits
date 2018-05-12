package io.gitHub.JiYang.library.presenter.library;

import java.util.List;

import io.gitHub.JiYang.library.model.enty.LibrarySearchHistory;
import io.gitHub.JiYang.library.model.library.LibrarySearchHistoryModel;
import io.gitHub.JiYang.library.model.library.LibrarySearchHistoryModelImpl;
import io.gitHub.JiYang.library.ui.view.library.LibrarySearchHistoryView;

public class LibrarySearchHistoryHistoryImpl implements LibrarySearchHistoryPresenter, OnSearchHistoryListener {
    private LibrarySearchHistoryModel model;
    private LibrarySearchHistoryView view;

    public LibrarySearchHistoryHistoryImpl(LibrarySearchHistoryView view) {
        this.view = view;
        this.model = new LibrarySearchHistoryModelImpl();
    }

    @Override
    public void fetchSearchList() {
        view.showLoading();
        model.fetchSearchHistory(this);
    }

    @Override
    public void onSuccess(List<LibrarySearchHistory> searchHistories) {
        view.success(searchHistories);
        view.hideLoading();
    }

    @Override
    public void onError(String error) {
        view.hideLoading();
        view.error(error);
    }
}
