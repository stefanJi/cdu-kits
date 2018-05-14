package jiyang.cdu.kits.ui.view.library;

import java.util.List;

import jiyang.cdu.kits.model.enty.LibrarySearchHistory;


public interface LibrarySearchHistoryView {
    void showLoading();

    void hideLoading();

    void success(List<LibrarySearchHistory> searchHistories);

    void error(String error);
}
