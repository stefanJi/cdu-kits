package io.gitHub.JiYang.library.presenter.library;

import java.util.List;

import io.gitHub.JiYang.library.model.enty.BookHistory;

public interface OnBookHistoryListener {

    void onSuccess(List<BookHistory> bookHistories);

    void onError(String error);
}
