package jiyang.cdu.kits.presenter.library.search;

import java.util.List;

import io.reactivex.disposables.Disposable;
import jiyang.cdu.kits.model.enty.Book;
import jiyang.cdu.kits.model.library.LibrarySearchBookModel;
import jiyang.cdu.kits.model.library.LibrarySearchBookModelImpl;
import jiyang.cdu.kits.presenter.BasePresenterImpl;
import jiyang.cdu.kits.ui.view.library.LibrarySearchView;


public class LibrarySearchImpl extends BasePresenterImpl<LibrarySearchView> implements LibrarySearchPresenter, OnSearchBookListener {
    private LibrarySearchBookModel model;

    public LibrarySearchImpl() {
        this.model = new LibrarySearchBookModelImpl();
    }

    @Override
    public void searchBook(String key, String searchType, String docType, String matchFlag, int page) {
        if (getView() != null) {
            getView().showLoading();
        }
        model.searchBook(key, searchType, docType, matchFlag, page, this);
    }

    @Override
    public void onSuccess(List<Book> bookList) {
        if (getView() != null) {
            getView().hideLoading();
            getView().success(bookList);
        }
    }

    @Override
    public void onError(String error) {
        if (getView() != null) {
            getView().hideLoading();
            getView().showError(error);
        }
    }

    @Override
    public void onSubscribe(Disposable disposable) {
        addDisposable(disposable);
    }
}
