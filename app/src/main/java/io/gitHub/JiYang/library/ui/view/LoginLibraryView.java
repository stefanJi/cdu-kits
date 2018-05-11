package io.gitHub.JiYang.library.ui.view;

import io.gitHub.JiYang.library.model.enty.LibraryUserInfo;

public interface LoginLibraryView {
    void showLoginProgress();

    void hideLoginProgress();

    void showLoginError(String error);

    void showLoginSuccess(LibraryUserInfo userInfo);
}
