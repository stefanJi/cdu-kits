package jiyang.cdu.kits.model.feeds;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import jiyang.cdu.kits.controller.RestApiManager;
import jiyang.cdu.kits.model.enty.zhihu.Stories;
import jiyang.cdu.kits.presenter.BasePresenterImpl;
import jiyang.cdu.kits.presenter.feeds.OnZhihuDailyStoriesListener;
import jiyang.cdu.kits.ui.view.feeds.ZhihuDailyStoriesView;

public class ZhihuDailyStoriesModelImpl extends BasePresenterImpl<ZhihuDailyStoriesView> implements ZhihuDailyStoriesModel {

    @Override
    public void fetchStories(long themeId, final OnZhihuDailyStoriesListener listener) {
        RestApiManager.getInstance().fetchZhihuDailyStories(new Observer<Stories>() {
            @Override
            public void onSubscribe(Disposable d) {
                listener.onSubscribe(d);
            }

            @Override
            public void onNext(Stories stories) {
                listener.success(stories);
            }

            @Override
            public void onError(Throwable e) {
                listener.error(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        }, themeId);
    }
}
