package jiyang.cdu.kits.presenter.library;

import java.util.List;

import jiyang.cdu.kits.model.enty.Book;
import jiyang.cdu.kits.model.library.LibraryBookListModel;
import jiyang.cdu.kits.model.library.LibraryBookListModelImpl;
import jiyang.cdu.kits.ui.view.library.LibraryBookListView;


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
