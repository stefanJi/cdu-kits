package jiyang.cdu.kits.presenter.feeds;

import io.reactivex.disposables.Disposable;
import jiyang.cdu.kits.model.enty.zhihu.Stories;
import jiyang.cdu.kits.model.feeds.ZhihuDailyStoriesModel;
import jiyang.cdu.kits.model.feeds.ZhihuDailyStoriesModelImpl;
import jiyang.cdu.kits.presenter.BasePresenterImpl;
import jiyang.cdu.kits.ui.view.feeds.ZhihuDailyStoriesView;

public class ZhihuDailyStoriesImpl extends BasePresenterImpl<ZhihuDailyStoriesView> implements ZhihuDailyStoriesPresentor, OnZhihuDailyStoriesListener {

    private ZhihuDailyStoriesModel model;

    public ZhihuDailyStoriesImpl() {
        model = new ZhihuDailyStoriesModelImpl();
    }

    @Override
    public void error(String error) {
        if (getView() != null) {
            getView().hideLoading();
            getView().showError(error);
        }
    }

    @Override
    public void success(Stories stories) {
        if (getView() != null) {
            getView().hideLoading();
            getView().setFetchResult(stories);
        }
    }

    @Override
    public void onSubscribe(Disposable disposable) {
        addDisposable(disposable);
    }

    @Override
    public void fetchStories(long themeId) {
        if (getView() != null) {
            getView().showLoading();
        }
        model.fetchStories(themeId, this);
    }
}
