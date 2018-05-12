package io.gitHub.JiYang.library.model.library;

import java.util.List;

import io.gitHub.JiYang.library.controller.RestApiManager;
import io.gitHub.JiYang.library.model.library.LibraryBookListModel;
import io.gitHub.JiYang.library.model.enty.Book;
import io.gitHub.JiYang.library.presenter.library.OnBookListListener;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

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
                bookListListener.error(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        }, book);
    }
}
