package jiyang.cdu.kits.ui.view.library;

import java.util.List;

import jiyang.cdu.kits.model.enty.BookHistory;
import jiyang.cdu.kits.ui.view.BaseView;


public interface LibraryHistoryView extends BaseView{
    void showLoading();

    void hideLoading();

    void success(List<BookHistory> bookHistories);

    void error(String error);
}
