package jiyang.cdu.kits.presenter.feeds;

import java.util.List;

import jiyang.cdu.kits.model.enty.Feed;


public interface OnFeedsListener {
    void onSuccess(List<Feed> feeds);

    void onError(String error);
}
