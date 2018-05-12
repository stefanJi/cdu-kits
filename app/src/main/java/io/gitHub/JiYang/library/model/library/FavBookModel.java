package io.gitHub.JiYang.library.model.library;

import io.gitHub.JiYang.library.presenter.library.FavBookPresenters;

public interface FavBookModel {
    void fetchFavBooks(final FavBookPresenters.OnFavBookListener favBookListener);
}
