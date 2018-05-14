package jiyang.cdu.kits.model.library;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import jiyang.cdu.kits.controller.RestApiManager;
import jiyang.cdu.kits.model.enty.LibrarySearchHistory;
import jiyang.cdu.kits.presenter.library.OnSearchHistoryListener;

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
