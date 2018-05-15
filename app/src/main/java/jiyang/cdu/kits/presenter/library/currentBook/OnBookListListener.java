package jiyang.cdu.kits.presenter.library.currentBook;

import java.util.List;

import jiyang.cdu.kits.model.enty.Book;
import jiyang.cdu.kits.presenter.BasePresenterListener;


public interface OnBookListListener extends BasePresenterListener {
    void success(List<Book> bookList);

    void error(String error);

    void reBookSuccess(int position);
}
