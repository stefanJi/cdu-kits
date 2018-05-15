package jiyang.cdu.kits.model.library;


import jiyang.cdu.kits.presenter.library.favBook.FavBookPresenters;

public interface FavBookModel {
    void fetchFavBooks(final FavBookPresenters.OnFavBookListener favBookListener);
}
