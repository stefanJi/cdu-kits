package io.gitHub.JiYang.library.model.library;

import io.gitHub.JiYang.library.presenter.library.OnSearchHistoryListener;

public interface LibrarySearchHistoryModel {
    void fetchSearchHistory(final OnSearchHistoryListener onSearchHistoryListener);
}
