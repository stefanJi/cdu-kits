package jiyang.cdu.kits.presenter.feeds;

import java.util.List;

import jiyang.cdu.kits.model.enty.Feed;
import jiyang.cdu.kits.presenter.BasePresenterListener;


public interface OnFeedsListener extends BasePresenterListener {
    void onSuccess(List<Feed> feeds);

    void onError(String error);
}
