package io.gitHub.JiYang.library.presenter.library;

import java.util.List;

import io.gitHub.JiYang.library.model.library.LibraryBookListModel;
import io.gitHub.JiYang.library.model.enty.Book;
import io.gitHub.JiYang.library.model.library.LibraryBookListModelImpl;
import io.gitHub.JiYang.library.ui.view.library.LibraryBookListView;

public class LibraryBookListImpl implements LibraryBookListPresenter, OnBookListListener {
    private LibraryBookListModel libraryBookListModel;
    private LibraryBookListView libraryBookListView;

    public LibraryBookListImpl(LibraryBookListView libraryBookListView) {
        this.libraryBookListView = libraryBookListView;
        this.libraryBookListModel = new LibraryBookListModelImpl();
    }

    @Override
    public void fetchBookList() {
        libraryBookListModel.fetchBookList(this);
        libraryBookListView.showLoading();
    }

    @Override
    public void reBook(Book book, int position) {
        libraryBookListView.showLoading();
        libraryBookListModel.reBook(book, position, this);
    }

    @Override
    public void success(List<Book> bookList) {
        libraryBookListView.onSuccess(bookList);
        libraryBookListView.hideLoading();
    }

    @Override
    public void error(String error) {
        libraryBookListView.hideLoading();
        libraryBookListView.showError(error);
    }

    @Override
    public void reBookSuccess(int position) {
        libraryBookListView.hideLoading();
        libraryBookListView.onReBookSuccess(position);
    }
}
