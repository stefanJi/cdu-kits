package jiyang.cdu.kits.presenter.feeds;

import io.reactivex.disposables.Disposable;
import jiyang.cdu.kits.model.enty.zhihu.StoryContent;
import jiyang.cdu.kits.model.feeds.ZhihuDailyStoryContentModel;
import jiyang.cdu.kits.model.feeds.ZhihuDailyStoryContentModelImpl;
import jiyang.cdu.kits.presenter.BasePresenterImpl;
import jiyang.cdu.kits.ui.view.feeds.ZhihuDailyStoryContentView;

public class ZhihuDailyStoryContentImpl extends BasePresenterImpl<ZhihuDailyStoryContentView>
        implements ZhihuDailyStoryContentPresenter, OnZhihuDailyStoryContentListener {
    private ZhihuDailyStoryContentModel model;

    public ZhihuDailyStoryContentImpl() {
        model = new ZhihuDailyStoryContentModelImpl();
    }

    public ZhihuDailyStoryContentImpl(ZhihuDailyStoryContentView view) {
        this();
        attachView(view);
    }

    @Override
    public void fetchContent(long id) {
        model.fetchContent(id, this);
    }

    @Override
    public void error(String error) {
        if (getView() != null) {
            getView().showError(error);
        }
    }

    @Override
    public void success(StoryContent content) {
        if (getView() != null) {
            getView().hideLoading();
            getView().setFetchResult(content);
        }
    }

    @Override
    public void onSubscribe(Disposable disposable) {
        addDisposable(disposable);
    }
}
