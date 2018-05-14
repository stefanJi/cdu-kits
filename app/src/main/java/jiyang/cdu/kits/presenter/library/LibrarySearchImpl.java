package jiyang.cdu.kits.presenter.library;

import java.util.List;

import jiyang.cdu.kits.model.enty.Book;
import jiyang.cdu.kits.model.library.LibrarySearchBookModel;
import jiyang.cdu.kits.model.library.LibrarySearchBookModelImpl;
import jiyang.cdu.kits.ui.view.library.LibrarySearchView;


public class LibrarySearchImpl implements LibrarySearchPresenter, OnSearchBookListener {
    private LibrarySearchView view;
    private LibrarySearchBookModel model;

    public LibrarySearchImpl(LibrarySearchView view) {
        this.view = view;
        this.model = new LibrarySearchBookModelImpl();
    }

    @Override
    public void searchBook(String key, String searchType, String docType, String matchFlag, int page) {
        view.showLoading();
        model.searchBook(key, searchType, docType, matchFlag, page, this);
    }

    @Override
    public void onSuccess(List<Book> bookList) {
        view.hideLoading();
        view.success(bookList);
    }

    @Override
    public void onError(String error) {
        view.hideLoading();
        view.showError(error);
    }
}
