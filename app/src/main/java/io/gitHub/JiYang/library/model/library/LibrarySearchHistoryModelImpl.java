package io.gitHub.JiYang.library.model.library;

import java.util.List;

import io.gitHub.JiYang.library.controller.RestApiManager;
import io.gitHub.JiYang.library.model.enty.LibrarySearchHistory;
import io.gitHub.JiYang.library.presenter.library.OnSearchHistoryListener;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LibrarySearchHistoryModelImpl implements LibrarySearchHistoryModel {

    @Override
    public void fetchSearchHistory(final OnSearchHistoryListener onSearchHistoryListener) {
        RestApiManager.getInstance().librarySearchHistory(new Observer<List<LibrarySearchHistory>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<LibrarySearchHistory> searchHistories) {
                onSearchHistoryListener.onSuccess(searchHistories);
            }

            @Override
            public void onError(Throwable e) {
                onSearchHistoryListener.onError(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
