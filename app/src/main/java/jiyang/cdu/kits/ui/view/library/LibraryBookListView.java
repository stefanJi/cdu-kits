package jiyang.cdu.kits.ui.view.library;

import java.util.List;

import jiyang.cdu.kits.model.enty.Book;


public interface LibraryBookListView {
    void onSuccess(List<Book> bookList);

    void showLoading();

    void hideLoading();

    void showError(String error);

    void onReBookSuccess(int position);
}
