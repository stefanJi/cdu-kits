package jiyang.cdu.kits.ui.view.library;

import java.util.List;

import jiyang.cdu.kits.model.enty.BookHistory;


public interface LibraryHistoryView {
    void showLoading();

    void hideLoading();

    void success(List<BookHistory> bookHistories);

    void error(String error);
}
