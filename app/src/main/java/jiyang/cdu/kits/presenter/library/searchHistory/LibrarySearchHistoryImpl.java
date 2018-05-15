package jiyang.cdu.kits.presenter.library.searchHistory;

import java.util.List;

import io.reactivex.disposables.Disposable;
import jiyang.cdu.kits.model.enty.LibrarySearchHistory;
import jiyang.cdu.kits.model.library.LibrarySearchHistoryModel;
import jiyang.cdu.kits.model.library.LibrarySearchHistoryModelImpl;
import jiyang.cdu.kits.presenter.BasePresenterImpl;
import jiyang.cdu.kits.ui.view.library.LibrarySearchHistoryView;


public class LibrarySearchHistoryImpl extends BasePresenterImpl<LibrarySearchHistoryView>
        implements LibrarySearchHistoryPresenter, OnSearchHistoryListener {
    private LibrarySearchHistoryModel model;

    public LibrarySearchHistoryImpl() {
        this.model = new LibrarySearchHistoryModelImpl();
    }

    @Override
    public void fetchSearchList() {
        if (getView() != null) {
            getView().showLoading();
        }
        model.fetchSearchHistory(this);
    }

    @Override
    public void onSuccess(List<LibrarySearchHistory> searchHistories) {
        if (getView() != null) {
            getView().success(searchHistories);
            getView().hideLoading();
        }
    }

    @Override
    public void onError(String error) {
        if (getView() != null) {
            getView().hideLoading();
            getView().error(error);
        }
    }

    @Override
    public void onSubscribe(Disposable disposable) {
        addDisposable(disposable);
    }
}
