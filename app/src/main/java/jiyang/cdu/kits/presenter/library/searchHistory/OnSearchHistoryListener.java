package jiyang.cdu.kits.presenter.library.searchHistory;

import java.util.List;

import jiyang.cdu.kits.model.enty.LibrarySearchHistory;
import jiyang.cdu.kits.presenter.BasePresenterListener;


public interface OnSearchHistoryListener extends BasePresenterListener {
    void onSuccess(List<LibrarySearchHistory> searchHistories);

    void onError(String error);
}
