package jiyang.cdu.kits.model.library;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import jiyang.cdu.kits.controller.RestApiManager;
import jiyang.cdu.kits.model.enty.BookHistory;
import jiyang.cdu.kits.presenter.library.OnBookHistoryListener;

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
