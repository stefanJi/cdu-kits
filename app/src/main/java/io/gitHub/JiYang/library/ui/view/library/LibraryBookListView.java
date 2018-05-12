package io.gitHub.JiYang.library.ui.view.library;

import java.util.List;

import io.gitHub.JiYang.library.model.enty.Book;

public interface LibraryBookListView {
    void onSuccess(List<Book> bookList);

    void showLoading();

    void hideLoading();

    void showError(String error);

    void onReBookSuccess(int position);
}
