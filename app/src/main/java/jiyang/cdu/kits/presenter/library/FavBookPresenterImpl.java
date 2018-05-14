package jiyang.cdu.kits.presenter.library;

import java.util.List;

import jiyang.cdu.kits.model.enty.FavBook;
import jiyang.cdu.kits.model.library.FavBookModel;
import jiyang.cdu.kits.model.library.FavBookModelImpl;
import jiyang.cdu.kits.ui.view.library.FavBookView;



public class FavBookPresenterImpl implements FavBookPresenters.OnFavBookListener, FavBookPresenters.FavBookPresenter {
    private FavBookView view;
    private FavBookModel model;

    public FavBookPresenterImpl(FavBookView view) {
        this.view = view;
        this.model = new FavBookModelImpl();
    }

    @Override
    public void fetchFevBooks() {
        view.showLoading();
        model.fetchFavBooks(this);
    }

    @Override
    public void success(List<FavBook> favBooks) {
        view.setBookList(favBooks);
        view.hideLoading();
    }

    @Override
    public void error(String error) {
        view.hideLoading();
        view.showError(error);
    }
}
