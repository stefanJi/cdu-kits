package jiyang.cdu.kits.model.library;


import jiyang.cdu.kits.presenter.library.OnSearchHistoryListener;

public interface LibrarySearchHistoryModel {
    void fetchSearchHistory(final OnSearchHistoryListener onSearchHistoryListener);
}
