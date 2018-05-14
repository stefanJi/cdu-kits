package jiyang.cdu.kits.presenter.library;

import java.util.List;

import jiyang.cdu.kits.model.enty.Book;


public interface OnBookListListener {
    void success(List<Book> bookList);

    void error(String error);

    void reBookSuccess(int position);
}
