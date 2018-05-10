package io.gitHub.JiYang.library.presenter.feeds;

import java.util.List;

import io.gitHub.JiYang.library.model.FeedsModel;
import io.gitHub.JiYang.library.model.enty.Feed;
import io.gitHub.JiYang.library.model.impl.FeedsModelImpl;
import io.gitHub.JiYang.library.ui.view.FeedsView;

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
        if (category == FeedsPresenter.HQC_ANNOUNCE) {
            feedsModel.fetchHQCFeeds(page, this);
            return;
        }
        String newsType;
        switch (category) {
            case FeedsPresenter.ANNOUNCE:
            case FeedsPresenter.ARTICLE:
                newsType = "announce";
                break;
            case FeedsPresenter.NEWS:
            case FeedsPresenter.NEWS2:
            case FeedsPresenter.MEDIA:
            case FeedsPresenter.COLOR_CAMPUS:
                newsType = "news";
                break;
            default:
                newsType = "announce";
                break;
        }
        feedsModel.fetchFeeds(newsType, page, category, this);
    }
}
