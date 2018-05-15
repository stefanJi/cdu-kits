package jiyang.cdu.kits.presenter.library.favBook;

import java.util.List;

import jiyang.cdu.kits.model.enty.FavBook;
import jiyang.cdu.kits.presenter.BasePresenterListener;


public class FavBookPresenters {

    public interface FavBookPresenter {
        void fetchFevBooks();
    }

    public interface OnFavBookListener extends BasePresenterListener {
        void success(List<FavBook> favBooks);

        void error(String error);
    }
}
