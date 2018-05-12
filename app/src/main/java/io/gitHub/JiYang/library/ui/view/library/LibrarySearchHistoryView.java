package io.gitHub.JiYang.library.ui.view.library;

import java.util.List;

import io.gitHub.JiYang.library.model.enty.LibrarySearchHistory;

public interface LibrarySearchHistoryView {
    void showLoading();

    void hideLoading();

    void success(List<LibrarySearchHistory> searchHistories);

    void error(String error);
}
