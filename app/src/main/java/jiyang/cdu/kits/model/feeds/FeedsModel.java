package jiyang.cdu.kits.model.feeds;


import jiyang.cdu.kits.presenter.feeds.OnFeedsListener;

public interface FeedsModel {

    void fetchFeeds(String newsType, int page, int type, final OnFeedsListener onFeedsListener);

    void fetchHQCFeeds(int page, final OnFeedsListener onFeedsListener);
}
