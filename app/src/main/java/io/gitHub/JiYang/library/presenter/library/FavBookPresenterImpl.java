package io.gitHub.JiYang.library.presenter.library;

import java.util.List;

import io.gitHub.JiYang.library.model.enty.FavBook;
import io.gitHub.JiYang.library.model.library.FavBookModel;
import io.gitHub.JiYang.library.model.library.FavBookModelImpl;
import io.gitHub.JiYang.library.ui.view.library.FavBookView;

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
