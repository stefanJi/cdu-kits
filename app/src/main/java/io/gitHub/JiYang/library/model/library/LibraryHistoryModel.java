package io.gitHub.JiYang.library.model.library;

import io.gitHub.JiYang.library.presenter.library.OnBookHistoryListener;

public interface LibraryHistoryModel {
    void fetchHistory(OnBookHistoryListener bookHistoryListener);
}
