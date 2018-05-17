package jiyang.cdu.kits.presenter.feeds;

import java.util.List;

import io.reactivex.disposables.Disposable;
import jiyang.cdu.kits.model.enty.Feed;
import jiyang.cdu.kits.model.feeds.FeedsModel;
import jiyang.cdu.kits.model.feeds.FeedsModelImpl;
import jiyang.cdu.kits.presenter.BasePresenterImpl;
import jiyang.cdu.kits.ui.view.feeds.FeedsView;


public class FeedsPresenterImpl extends BasePresenterImpl<FeedsView> implements OnFeedsListener, FeedsPresenter {
    private FeedsModel feedsModel;

    public FeedsPresenterImpl() {
        this.feedsModel = new FeedsModelImpl();
    }


    @Override
    public void onSuccess(List<Feed> feeds) {
        if (getView() != null) {
            getView().hideLoading();
            getView().setFeedsList(feeds);
        }
    }

    @Override
    public void onError(String error) {
        if (getView() != null) {
            getView().hideLoading();
            getView().showError(error);
        }
    }

    @Override
    public void fetchFeeds(int page, int category) {
        if (getView() != null) {
            getView().showLoading();
        }
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

    @Override
    public void onSubscribe(Disposable disposable) {
        addDisposable(disposable);
    }
}
