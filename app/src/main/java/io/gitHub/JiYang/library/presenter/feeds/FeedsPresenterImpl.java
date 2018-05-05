package io.gitHub.JiYang.library.presenter.feeds;

import java.util.List;

import io.gitHub.JiYang.library.model.FeedsModel;
import io.gitHub.JiYang.library.model.enty.Announce;
import io.gitHub.JiYang.library.model.impl.FeedsModelImpl;
import io.gitHub.JiYang.library.ui.view.FeedsView;

public class FeedsPresenterImpl implements FeedsPresenter, OnFeedsListener {
    private FeedsView feedsView;
    private FeedsModel feedsModel;

    public FeedsPresenterImpl(FeedsView feedsView) {
        this.feedsView = feedsView;
        this.feedsModel = new FeedsModelImpl();
    }

    @Override
    public void fetchAnnounceFeeds(int page) {
        feedsView.showLoading();
        feedsModel.fetchAnnounce(page, this);
    }

    @Override
    public void onSuccess(List<Announce> announces) {
        feedsView.hideLoading();
        feedsView.setAnnounceList(announces);
    }

    @Override
    public void onError(String error) {
        feedsView.hideLoading();
        feedsView.showError(error);
    }
}
