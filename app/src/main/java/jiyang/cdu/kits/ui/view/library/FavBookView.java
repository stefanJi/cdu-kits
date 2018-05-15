package jiyang.cdu.kits.ui.view.library;

import java.util.List;

import jiyang.cdu.kits.model.enty.FavBook;
import jiyang.cdu.kits.ui.view.BaseView;


public interface FavBookView extends BaseView {
    void showLoading();

    void hideLoading();

    void setBookList(List<FavBook> favBookList);

    void showError(String error);
}
