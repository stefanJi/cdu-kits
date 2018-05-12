package io.gitHub.JiYang.library.model.library;

import io.gitHub.JiYang.library.presenter.library.OnSearchBookListener;

public interface LibrarySearchBookModel {
    void searchBook(String key, String searchType, String docType, String matchFlag, int page, final OnSearchBookListener searchBookListener);
}
