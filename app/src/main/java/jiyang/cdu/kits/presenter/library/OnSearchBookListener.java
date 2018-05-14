package jiyang.cdu.kits.presenter.library;

import java.util.List;

import jiyang.cdu.kits.model.enty.Book;


public interface OnSearchBookListener {
    void onSuccess(List<Book> bookList);

    void onError(String error);
}
