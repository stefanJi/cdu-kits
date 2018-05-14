package jiyang.cdu.kits.ui.view.library;


import jiyang.cdu.kits.model.enty.LibraryUserInfo;

public interface LoginLibraryView {
    void showLoginProgress();

    void hideLoginProgress();

    void showLoginError(String error);

    void showLoginSuccess(LibraryUserInfo userInfo);
}
