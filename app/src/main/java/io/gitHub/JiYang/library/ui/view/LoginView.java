package io.gitHub.JiYang.library.ui.view;

public interface LoginView {
    void showLoading();

    void hideLoading();

    void showError(String error);

    void showSuccess();
}
