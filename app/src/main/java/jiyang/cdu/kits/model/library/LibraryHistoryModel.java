package jiyang.cdu.kits.model.library;


import jiyang.cdu.kits.presenter.library.OnBookHistoryListener;

public interface LibraryHistoryModel {
    void fetchHistory(OnBookHistoryListener bookHistoryListener);
}
