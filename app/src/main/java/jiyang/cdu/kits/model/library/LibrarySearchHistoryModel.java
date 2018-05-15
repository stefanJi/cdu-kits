package jiyang.cdu.kits.model.library;


import jiyang.cdu.kits.presenter.library.searchHistory.OnSearchHistoryListener;

public interface LibrarySearchHistoryModel {
    void fetchSearchHistory(final OnSearchHistoryListener onSearchHistoryListener);
}
