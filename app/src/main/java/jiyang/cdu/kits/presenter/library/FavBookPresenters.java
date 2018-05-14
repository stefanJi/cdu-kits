package jiyang.cdu.kits.presenter.library;

import java.util.List;

import jiyang.cdu.kits.model.enty.FavBook;


public class FavBookPresenters {

    public interface FavBookPresenter {
        void fetchFevBooks();
    }

    public interface OnFavBookListener {
        void success(List<FavBook> favBooks);

        void error(String error);
    }
}
