package jiyang.cdu.kits.presenter.library.currentBook;

import java.util.List;

import io.reactivex.disposables.Disposable;
import jiyang.cdu.kits.model.enty.Book;
import jiyang.cdu.kits.model.library.LibraryBookListModel;
import jiyang.cdu.kits.model.library.LibraryBookListModelImpl;
import jiyang.cdu.kits.presenter.BasePresenterImpl;
import jiyang.cdu.kits.ui.view.library.LibraryBookListView;


public class LibraryBookListImpl extends BasePresenterImpl<LibraryBookListView> implements LibraryBookListPresenter, OnBookListListener {
    private LibraryBookListModel libraryBookListModel;

    public LibraryBookListImpl() {
        this.libraryBookListModel = new LibraryBookListModelImpl();
    }

    public LibraryBookListImpl(LibraryBookListView view) {
        this();
        attachView(view);
    }

    @Override
    public void fetchBookList() {
        libraryBookListModel.fetchBookList(this);
        if (getView() != null) {
            getView().showLoading();
        }
    }

    @Override
    public void reBook(Book book, int position) {
        if (getView() != null) {
            getView().showLoading();
        }
        libraryBookListModel.reBook(book, position, this);
    }

    @Override
    public void success(List<Book> bookList) {
        if (getView() != null) {
            getView().onSuccess(bookList);
            getView().hideLoading();
        }
    }

    @Override
    public void error(String error) {
        if (getView() != null) {
            getView().hideLoading();
            getView().showError(error);
        }
    }

    @Override
    public void reBookSuccess(int position) {
        if (getView() != null) {
            getView().hideLoading();
            getView().onReBookSuccess(position);
        }
    }

    @Override
    public void onSubscribe(Disposable disposable) {
        addDisposable(disposable);
    }
}
