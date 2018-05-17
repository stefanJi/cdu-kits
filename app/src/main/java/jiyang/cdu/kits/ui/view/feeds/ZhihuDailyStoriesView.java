package jiyang.cdu.kits.ui.view.feeds;

import jiyang.cdu.kits.model.enty.zhihu.Stories;
import jiyang.cdu.kits.ui.view.BaseView;

public interface ZhihuDailyStoriesView extends BaseView {
    void showLoading();

    void hideLoading();

    void setFetchResult(Stories stories);

    void showError(String error);
}
