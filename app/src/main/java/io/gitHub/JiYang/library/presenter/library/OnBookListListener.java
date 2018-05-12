package io.gitHub.JiYang.library.presenter.library;

import java.util.List;

import io.gitHub.JiYang.library.model.enty.Book;

public interface OnBookListListener {
    void success(List<Book> bookList);

    void error(String error);

    void reBookSuccess(int position);
}
