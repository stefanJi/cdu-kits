package jiyang.cdu.kits.presenter.library.favBook;

import java.util.List;

import io.reactivex.disposables.Disposable;
import jiyang.cdu.kits.model.enty.FavBook;
import jiyang.cdu.kits.model.library.FavBookModel;
import jiyang.cdu.kits.model.library.FavBookModelImpl;
import jiyang.cdu.kits.presenter.BasePresenterImpl;
import jiyang.cdu.kits.ui.view.library.FavBookView;


public class FavBookPresenterImpl extends BasePresenterImpl<FavBookView> implements FavBookPresenters.OnFavBookListener, FavBookPresenters.FavBookPresenter {
    private FavBookModel model;

    public FavBookPresenterImpl() {
        this.model = new FavBookModelImpl();
    }

    @Override
    public void fetchFevBooks() {
        if (getView() != null) {
            getView().showLoading();
        }
        model.fetchFavBooks(this);
    }

    @Override
    public void success(List<FavBook> favBooks) {
        if (getView() != null) {
            getView().setBookList(favBooks);
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
    public void onSubscribe(Disposable disposable) {
        addDisposable(disposable);
    }
}
