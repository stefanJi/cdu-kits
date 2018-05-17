package jiyang.cdu.kits.ui.view.feeds;

import jiyang.cdu.kits.model.enty.zhihu.StoryContent;

public interface ZhihuDailyStoryContentView {
    void showLoading();

    void hideLoading();

    void setFetchResult(StoryContent storyContent);

    void showError(String error);
}
