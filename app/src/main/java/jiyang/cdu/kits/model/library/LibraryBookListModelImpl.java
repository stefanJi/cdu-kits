package jiyang.cdu.kits.model.library;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.channels.UnresolvedAddressException;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import jiyang.cdu.kits.controller.RestApiManager;
import jiyang.cdu.kits.model.enty.Book;
import jiyang.cdu.kits.presenter.library.currentBook.OnBookListListener;

public class LibraryBookListModelImpl implements LibraryBookListModel {
    @Override
    public void fetchBookList(final OnBookListListener onBookListListener) {
        RestApiManager.getInstance().getBookList(new Observer<List<Book>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Book> bookList) {
                onBookListListener.success(bookList);
            }

            @Override
            public void onError(Throwable e) {
                onBookListListener.error(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void reBook(Book book, final int position, final OnBookListListener bookListListener) {
        RestApiManager.getInstance().reBook(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                bookListListener.reBookSuccess(position);
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof UnknownHostException || e instanceof UnresolvedAddressException) {
                    bookListListener.error("网络不可用,请检查网络设置");
                } else if (e instanceof SocketTimeoutException) {
                    bookListListener.error("连接超时,请重试");
                } else {
                    bookListListener.error(e.getMessage());
                }
            }

            @Override
            public void onComplete() {

            }
        }, book);
    }
}
