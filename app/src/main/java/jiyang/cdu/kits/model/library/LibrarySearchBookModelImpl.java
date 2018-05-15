package jiyang.cdu.kits.model.library;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.channels.UnresolvedAddressException;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import jiyang.cdu.kits.controller.RestApiManager;
import jiyang.cdu.kits.model.enty.Book;
import jiyang.cdu.kits.presenter.library.search.OnSearchBookListener;

public class LibrarySearchBookModelImpl implements LibrarySearchBookModel {

    @Override
    public void searchBook(String key, String searchType, String docType, String matchFlag, int page, final OnSearchBookListener searchBookListener) {
        RestApiManager.getInstance().searchBook(new Observer<List<Book>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Book> bookList) {
                searchBookListener.onSuccess(bookList);
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof UnknownHostException || e instanceof UnresolvedAddressException) {
                    searchBookListener.onError("网络不可用,请检查网络设置");
                } else if (e instanceof SocketTimeoutException) {
                    searchBookListener.onError("连接超时,请重试");
                } else {
                    searchBookListener.onError(e.getMessage());
                }
            }

            @Override
            public void onComplete() {

            }
        }, key, searchType, docType, matchFlag, page);
    }
}
