package jiyang.cdu.kits.ui.view.feeds;

import java.util.List;

import jiyang.cdu.kits.model.enty.Feed;
import jiyang.cdu.kits.ui.view.BaseView;


public interface FeedsView extends BaseView {
    void showLoading();

    void hideLoading();

    void showError(String error);

    void setFeedsList(List<Feed> feeds);
}
