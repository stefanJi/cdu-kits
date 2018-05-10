package io.gitHub.JiYang.library.presenter.feeds;

import java.util.List;

import io.gitHub.JiYang.library.model.enty.Feed;

public interface OnFeedsListener {
    void onSuccess(List<Feed> feeds);

    void onError(String error);
}
