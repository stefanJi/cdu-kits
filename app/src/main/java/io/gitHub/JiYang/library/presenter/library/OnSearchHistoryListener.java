package io.gitHub.JiYang.library.presenter.library;

import java.util.List;

import io.gitHub.JiYang.library.model.enty.LibrarySearchHistory;

public interface OnSearchHistoryListener {
    void onSuccess(List<LibrarySearchHistory> searchHistories);

    void onError(String error);
}
