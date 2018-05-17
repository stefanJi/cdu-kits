package jiyang.cdu.kits.model.feeds;

import jiyang.cdu.kits.presenter.feeds.OnZhihuDailyListener;

public interface ZhihuDailyModel {
    void fetchThemes(final OnZhihuDailyListener listener);
}
