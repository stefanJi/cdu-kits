package io.gitHub.JiYang.library.presenter.library;

import io.gitHub.JiYang.library.model.enty.LibraryUserInfo;

public interface OnLoginListener {
    void onSuccess(LibraryUserInfo userInfo);

    void onError(String error);
}
