package jiyang.cdu.kits.ui.view.library;

import java.util.List;

import jiyang.cdu.kits.model.enty.Book;
import jiyang.cdu.kits.ui.view.BaseView;


public interface LibraryBookListView extends BaseView {
    void onSuccess(List<Book> bookList);

    void showLoading();

    void hideLoading();

    void showError(String error);

    void onReBookSuccess(int position);
}
