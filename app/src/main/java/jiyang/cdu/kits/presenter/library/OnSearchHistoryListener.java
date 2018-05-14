package jiyang.cdu.kits.presenter.library;

import java.util.List;

import jiyang.cdu.kits.model.enty.LibrarySearchHistory;


public interface OnSearchHistoryListener {
    void onSuccess(List<LibrarySearchHistory> searchHistories);

    void onError(String error);
}
