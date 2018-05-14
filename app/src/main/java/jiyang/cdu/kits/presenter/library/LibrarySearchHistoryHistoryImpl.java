package jiyang.cdu.kits.presenter.library;

import java.util.List;

import jiyang.cdu.kits.model.enty.LibrarySearchHistory;
import jiyang.cdu.kits.model.library.LibrarySearchHistoryModel;
import jiyang.cdu.kits.model.library.LibrarySearchHistoryModelImpl;
import jiyang.cdu.kits.ui.view.library.LibrarySearchHistoryView;


public class LibrarySearchHistoryHistoryImpl implements LibrarySearchHistoryPresenter, OnSearchHistoryListener {
    private LibrarySearchHistoryModel model;
    private LibrarySearchHistoryView view;

    public LibrarySearchHistoryHistoryImpl(LibrarySearchHistoryView view) {
        this.view = view;
        this.model = new LibrarySearchHistoryModelImpl();
    }

    @Override
    public void fetchSearchList() {
        view.showLoading();
        model.fetchSearchHistory(this);
    }

    @Override
    public void onSuccess(List<LibrarySearchHistory> searchHistories) {
        view.success(searchHistories);
        view.hideLoading();
    }

    @Override
    public void onError(String error) {
        view.hideLoading();
        view.error(error);
    }
}
