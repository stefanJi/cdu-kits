package jiyang.cdu.kits.presenter.library.history;

import java.util.List;

import jiyang.cdu.kits.model.enty.BookHistory;
import jiyang.cdu.kits.presenter.BasePresenterListener;


public interface OnBookHistoryListener extends BasePresenterListener {

    void onSuccess(List<BookHistory> bookHistories);

    void onError(String error);
}
