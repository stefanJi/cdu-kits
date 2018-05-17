package jiyang.cdu.kits.model.feeds;

import jiyang.cdu.kits.presenter.feeds.OnZhihuDailyStoriesListener;

public interface ZhihuDailyStoriesModel {
    void fetchStories(long themeId, final OnZhihuDailyStoriesListener listener);
}
