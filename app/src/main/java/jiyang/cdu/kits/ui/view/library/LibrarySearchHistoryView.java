package jiyang.cdu.kits.ui.view.library;

import java.util.List;

import jiyang.cdu.kits.model.enty.LibrarySearchHistory;
import jiyang.cdu.kits.ui.view.BaseView;


public interface LibrarySearchHistoryView extends BaseView {
    void showLoading();

    void hideLoading();

    void success(List<LibrarySearchHistory> searchHistories);

    void error(String error);
}
