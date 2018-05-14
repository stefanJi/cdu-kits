package jiyang.cdu.kits.model.library;


import jiyang.cdu.kits.presenter.library.OnSearchBookListener;

public interface LibrarySearchBookModel {
    void searchBook(String key, String searchType, String docType, String matchFlag, int page, final OnSearchBookListener searchBookListener);
}
