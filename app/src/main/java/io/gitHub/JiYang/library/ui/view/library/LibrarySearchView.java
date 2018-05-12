package io.gitHub.JiYang.library.ui.view.library;

import java.util.List;

import io.gitHub.JiYang.library.model.enty.Book;

public interface LibrarySearchView {
    void success(List<Book> bookList);

    void showLoading();

    void hideLoading();

    void showError(String error);
}
