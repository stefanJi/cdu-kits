package jiyang.cdu.kits.ui.view;

import java.util.List;

import jiyang.cdu.kits.model.enty.Feed;


public interface FeedsView {
    void showLoading();

    void hideLoading();

    void showError(String error);

    void setAnnounceList(List<Feed> feeds);
}
