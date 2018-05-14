package jiyang.cdu.kits.ui.view.library;

import java.util.List;

import jiyang.cdu.kits.model.enty.FavBook;


public interface FavBookView {
    void showLoading();

    void hideLoading();

    void setBookList(List<FavBook> favBookList);

    void showError(String error);
}
