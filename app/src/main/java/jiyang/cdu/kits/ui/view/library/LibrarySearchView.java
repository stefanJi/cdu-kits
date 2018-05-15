package jiyang.cdu.kits.ui.view.library;

import java.util.List;

import jiyang.cdu.kits.model.enty.Book;
import jiyang.cdu.kits.ui.view.BaseView;


public interface LibrarySearchView extends BaseView {
    void success(List<Book> bookList);

    void showLoading();

    void hideLoading();

    void showError(String error);
}
