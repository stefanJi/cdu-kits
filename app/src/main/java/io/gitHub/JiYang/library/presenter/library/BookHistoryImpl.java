package io.gitHub.JiYang.library.presenter.library;

import java.util.List;

import io.gitHub.JiYang.library.model.enty.BookHistory;
import io.gitHub.JiYang.library.model.library.LibraryHistoryModel;
import io.gitHub.JiYang.library.model.library.LibraryHistoryModelImpl;
import io.gitHub.JiYang.library.ui.view.library.LibraryHistoryView;

public class BookHistoryImpl implements BookHistoryPresenter, OnBookHistoryListener {
    private LibraryHistoryModel libraryHistoryModel;
    private LibraryHistoryView libraryHistoryView;

    public BookHistoryImpl(LibraryHistoryView libraryHistoryView) {
        this.libraryHistoryView = libraryHistoryView;
        this.libraryHistoryModel = new LibraryHistoryModelImpl();
    }

    @Override
    public void fetchHistory() {
        libraryHistoryView.showLoading();
        libraryHistoryModel.fetchHistory(this);
    }

    @Override
    public void onSuccess(List<BookHistory> bookHistories) {
        libraryHistoryView.success(bookHistories);
        libraryHistoryView.hideLoading();
    }

    @Override
    public void onError(String error) {
        libraryHistoryView.hideLoading();
        libraryHistoryView.error(error);
    }
}
