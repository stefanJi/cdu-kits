package jiyang.cdu.kits.model.library;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.channels.UnresolvedAddressException;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import jiyang.cdu.kits.controller.RestApiManager;
import jiyang.cdu.kits.model.enty.BookHistory;
import jiyang.cdu.kits.presenter.library.history.OnBookHistoryListener;

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
                if (e instanceof UnknownHostException || e instanceof UnresolvedAddressException) {
                    bookHistoryListener.onError("网络不可用,请检查网络设置");
                } else if (e instanceof SocketTimeoutException) {
                    bookHistoryListener.onError("连接超时,请重试");
                } else {
                    bookHistoryListener.onError(e.getMessage());
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
