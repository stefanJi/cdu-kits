package jiyang.cdu.kits.ui.view.library;


import jiyang.cdu.kits.model.enty.LibraryUserInfo;
import jiyang.cdu.kits.ui.view.BaseView;

public interface LoginLibraryView extends BaseView {
    void showLoginProgress();

    void hideLoginProgress();

    void showLoginError(String error);

    void showLoginSuccess(LibraryUserInfo userInfo);
}
