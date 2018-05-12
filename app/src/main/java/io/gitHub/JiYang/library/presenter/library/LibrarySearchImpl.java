package io.gitHub.JiYang.library.presenter.library;

import java.util.List;

import io.gitHub.JiYang.library.model.enty.Book;
import io.gitHub.JiYang.library.model.library.LibrarySearchBookModel;
import io.gitHub.JiYang.library.model.library.LibrarySearchBookModelImpl;
import io.gitHub.JiYang.library.ui.view.library.LibrarySearchView;

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
