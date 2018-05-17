package jiyang.cdu.kits.ui.view.feeds;

import jiyang.cdu.kits.model.enty.zhihu.DailyThemes;
import jiyang.cdu.kits.ui.view.BaseView;

public interface ZhihuDailyView extends BaseView {
    void showLoading();

    void hideLoading();

    void setFetchResult(DailyThemes dailyThemes);

    void showError(String error);
}
