package jiyang.cdu.kits.presenter.feeds;

import jiyang.cdu.kits.model.enty.zhihu.Stories;
import jiyang.cdu.kits.presenter.BasePresenterListener;

public interface OnZhihuDailyStoriesListener extends BasePresenterListener {
    void error(String error);

    void success(Stories stories);
}
