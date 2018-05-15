package jiyang.cdu.kits.presenter.library.history;

import java.util.List;

import io.reactivex.disposables.Disposable;
import jiyang.cdu.kits.model.enty.BookHistory;
import jiyang.cdu.kits.model.library.LibraryHistoryModel;
import jiyang.cdu.kits.model.library.LibraryHistoryModelImpl;
import jiyang.cdu.kits.presenter.BasePresenterImpl;
import jiyang.cdu.kits.ui.view.library.LibraryHistoryView;


public class BookHistoryImpl extends BasePresenterImpl<LibraryHistoryView> implements BookHistoryPresenter, OnBookHistoryListener {
    private LibraryHistoryModel libraryHistoryModel;

    public BookHistoryImpl() {
        this.libraryHistoryModel = new LibraryHistoryModelImpl();
    }

    @Override
    public void fetchHistory() {
        if (getView() != null) {
            getView().showLoading();
        }
        libraryHistoryModel.fetchHistory(this);
    }

    @Override
    public void onSuccess(List<BookHistory> bookHistories) {
        if (getView() != null) {
            getView().success(bookHistories);
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
