package jiyang.cdu.kits.model.library;


import jiyang.cdu.kits.model.enty.Book;
import jiyang.cdu.kits.presenter.library.currentBook.OnBookListListener;

public interface LibraryBookListModel {
    void fetchBookList(final OnBookListListener onBookListListener);

    void reBook(Book book, int position, final OnBookListListener onBookListListener);
}
