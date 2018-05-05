package io.gitHub.JiYang.library.model;

import io.gitHub.JiYang.library.presenter.feeds.OnFeedsListener;

public interface FeedsModel {

    void fetchAnnounce(int page, OnFeedsListener onFeedsListener);
}
