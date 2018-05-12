package io.gitHub.JiYang.library.ui.view.library;

import java.util.List;

import io.gitHub.JiYang.library.model.enty.FavBook;

public interface FavBookView {
    void showLoading();

    void hideLoading();

    void setBookList(List<FavBook> favBookList);

    void showError(String error);
}
