package jiyang.cdu.kits.presenter.library;

import java.util.List;

import jiyang.cdu.kits.model.enty.BookHistory;
import jiyang.cdu.kits.model.library.LibraryHistoryModel;
import jiyang.cdu.kits.model.library.LibraryHistoryModelImpl;
import jiyang.cdu.kits.ui.view.library.LibraryHistoryView;


public class BookHistoryImpl implements BookHistoryPresenter, OnBookHistoryListener {
    private LibraryHistoryModel libraryHistoryModel;
    private LibraryHistoryView libraryHistoryView;

    public BookHistoryImpl(LibraryHistoryView libraryHistoryView) {
        this.libraryHistoryView = libraryHistoryView;
        this.libraryHistoryModel = new LibraryHistoryModelImpl();
    }

    @Override
    public void fetchHistory() {
        libraryHistoryView.showLoading();
        libraryHistoryModel.fetchHistory(this);
    }

    @Override
    public void onSuccess(List<BookHistory> bookHistories) {
        libraryHistoryView.success(bookHistories);
        libraryHistoryView.hideLoading();
    }

    @Override
    public void onError(String error) {
        libraryHistoryView.hideLoading();
        libraryHistoryView.error(error);
    }
}
