package io.gitHub.JiYang.library.presenter.library;

import java.util.List;

import io.gitHub.JiYang.library.model.enty.FavBook;
import io.gitHub.JiYang.library.model.library.FavBookModel;
import io.gitHub.JiYang.library.model.library.FavBookModelImpl;
import io.gitHub.JiYang.library.ui.view.library.FavBookView;

public class FavBookPresenters {

    public interface FavBookPresenter {
        void fetchFevBooks();
    }

    public interface OnFavBookListener {
        void success(List<FavBook> favBooks);

        void error(String error);
    }
}
