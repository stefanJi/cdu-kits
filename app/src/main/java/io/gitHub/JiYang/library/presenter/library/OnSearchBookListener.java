package io.gitHub.JiYang.library.presenter.library;

import java.util.List;

import io.gitHub.JiYang.library.model.enty.Book;

public interface OnSearchBookListener {
    void onSuccess(List<Book> bookList);

    void onError(String error);
}
