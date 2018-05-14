package jiyang.cdu.kits.presenter.library;

import java.util.List;

import jiyang.cdu.kits.model.enty.BookHistory;


public interface OnBookHistoryListener {

    void onSuccess(List<BookHistory> bookHistories);

    void onError(String error);
}
