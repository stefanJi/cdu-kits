package io.gitHub.JiYang.library.model.library;

import io.gitHub.JiYang.library.model.enty.Book;
import io.gitHub.JiYang.library.presenter.library.OnBookListListener;

public interface LibraryBookListModel {
    void fetchBookList(final OnBookListListener onBookListListener);

    void reBook(Book book, int position, final OnBookListListener onBookListListener);
}
