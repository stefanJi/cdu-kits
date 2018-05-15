package jiyang.cdu.kits.presenter.library.search;

import java.util.List;

import jiyang.cdu.kits.model.enty.Book;
import jiyang.cdu.kits.presenter.BasePresenterListener;


public interface OnSearchBookListener extends BasePresenterListener {
    void onSuccess(List<Book> bookList);

    void onError(String error);
}
