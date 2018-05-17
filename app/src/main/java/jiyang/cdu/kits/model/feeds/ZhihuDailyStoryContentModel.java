package jiyang.cdu.kits.model.feeds;

import jiyang.cdu.kits.presenter.feeds.OnZhihuDailyStoryContentListener;

public interface ZhihuDailyStoryContentModel {
    void fetchContent(long id, final OnZhihuDailyStoryContentListener listener);
}
