package jiyang.cdu.kits.presenter.feeds;

import java.util.List;

import jiyang.cdu.kits.model.enty.Feed;
import jiyang.cdu.kits.model.feeds.FeedsModel;
import jiyang.cdu.kits.model.feeds.FeedsModelImpl;
import jiyang.cdu.kits.ui.view.FeedsView;


public class FeedsPresenterImpl implements OnFeedsListener, FeedsPresenter {
    private FeedsView feedsView;
    private FeedsModel feedsModel;

    public FeedsPresenterImpl(FeedsView feedsView) {
        this.feedsView = feedsView;
        this.feedsModel = new FeedsModelImpl();
    }


    @Override
    public void onSuccess(List<Feed> feeds) {
        feedsView.hideLoading();
        feedsView.setAnnounceList(feeds);
    }

    @Override
    public void onError(String error) {
        feedsView.hideLoading();
        feedsView.showError(error);
    }

    @Override
    public void fetchFeeds(int page, int category) {
        feedsView.showLoading();
        if (category == HQC_ANNOUNCE) {
            feedsModel.fetchHQCFeeds(page, this);
            return;
        }
        String newsType;
        switch (category) {
            case ANNOUNCE:
            case ARTICLE:
                newsType = "announce";
                break;
            case NEWS:
            case NEWS2:
            case MEDIA:
            case COLOR_CAMPUS:
                newsType = "news";
                break;
            default:
                newsType = "announce";
                break;
        }
        feedsModel.fetchFeeds(newsType, page, category, this);
    }
}
