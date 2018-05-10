package io.gitHub.JiYang.library.ui.view;

public interface LoginLibraryView {
    void showLoginProgress();

    void hideLoginProgress();

    void showLoginError(String error);

    void showLoginSuccess();
}
