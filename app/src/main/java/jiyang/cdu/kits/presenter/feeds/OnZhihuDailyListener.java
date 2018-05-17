package jiyang.cdu.kits.presenter.feeds;

import jiyang.cdu.kits.model.enty.zhihu.DailyThemes;
import jiyang.cdu.kits.presenter.BasePresenterListener;

public interface OnZhihuDailyListener extends BasePresenterListener {
    void onError(String error);

    void onSuccess(DailyThemes themes);
}
