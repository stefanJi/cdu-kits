package io.gitHub.JiYang.library.model.library;

import java.util.List;

import io.gitHub.JiYang.library.controller.RestApiManager;
import io.gitHub.JiYang.library.model.enty.BookHistory;
import io.gitHub.JiYang.library.presenter.library.OnBookHistoryListener;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LibraryHistoryModelImpl implements LibraryHistoryModel {

    @Override
    public void fetchHistory(final OnBookHistoryListener bookHistoryListener) {
        RestApiManager.getInstance().libraryHistory(new Observer<List<BookHistory>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<BookHistory> bookHistories) {
                bookHistoryListener.onSuccess(bookHistories);
            }

            @Override
            public void onError(Throwable e) {
                bookHistoryListener.onError(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
