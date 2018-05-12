package io.gitHub.JiYang.library.presenter.library;

import io.gitHub.JiYang.library.model.enty.Book;

public interface LibraryBookListPresenter {
    void fetchBookList();

    void reBook(Book book, int position);
}
