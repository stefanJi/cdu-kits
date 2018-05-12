package io.gitHub.JiYang.library.ui.view.library;

import java.util.List;

import io.gitHub.JiYang.library.model.enty.BookHistory;

public interface LibraryHistoryView {
    void showLoading();

    void hideLoading();

    void success(List<BookHistory> bookHistories);

    void error(String error);
}
