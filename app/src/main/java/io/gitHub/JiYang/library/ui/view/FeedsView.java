package io.gitHub.JiYang.library.ui.view;

import java.util.List;

import io.gitHub.JiYang.library.model.enty.Feed;


public interface FeedsView {
    void showLoading();

    void hideLoading();

    void showError(String error);

    void setAnnounceList(List<Feed> feeds);
}
