package jiyang.cdu.kits.presenter.library;


import jiyang.cdu.kits.model.enty.Book;

public interface LibraryBookListPresenter {
    void fetchBookList();

    void reBook(Book book, int position);
}
