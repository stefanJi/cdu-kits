package jiyang.cdu.kits.presenter.feeds;

import jiyang.cdu.kits.model.enty.zhihu.StoryContent;
import jiyang.cdu.kits.presenter.BasePresenterListener;

public interface OnZhihuDailyStoryContentListener extends BasePresenterListener {
    void error(String error);

    void success(StoryContent content);
}
