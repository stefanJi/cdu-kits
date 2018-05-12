package io.gitHub.JiYang.library.model.feeds;

import io.gitHub.JiYang.library.presenter.feeds.OnFeedsListener;

public interface FeedsModel {

    void fetchFeeds(String newsType, int page, int type, final OnFeedsListener onFeedsListener);

    void fetchHQCFeeds(int page, final OnFeedsListener onFeedsListener);
}
