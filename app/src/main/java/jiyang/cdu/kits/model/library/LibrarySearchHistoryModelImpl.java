package jiyang.cdu.kits.model.library;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.channels.UnresolvedAddressException;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import jiyang.cdu.kits.controller.RestApiManager;
import jiyang.cdu.kits.model.enty.LibrarySearchHistory;
import jiyang.cdu.kits.presenter.library.searchHistory.OnSearchHistoryListener;

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
                if (e instanceof UnknownHostException || e instanceof UnresolvedAddressException) {
                    onSearchHistoryListener.onError("网络不可用,请检查网络设置");
                } else if (e instanceof SocketTimeoutException) {
                    onSearchHistoryListener.onError("连接超时,请重试");
                } else {
                    onSearchHistoryListener.onError(e.getMessage());
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
