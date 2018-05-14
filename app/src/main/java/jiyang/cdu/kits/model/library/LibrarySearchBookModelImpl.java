package jiyang.cdu.kits.model.library;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import jiyang.cdu.kits.controller.RestApiManager;
import jiyang.cdu.kits.model.enty.Book;
import jiyang.cdu.kits.presenter.library.OnSearchBookListener;

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
                searchBookListener.onError(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        }, key, searchType, docType, matchFlag, page);
    }
}
