package jiyang.cdu.kits.model.feeds;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import jiyang.cdu.kits.controller.RestApiManager;
import jiyang.cdu.kits.model.enty.zhihu.StoryContent;
import jiyang.cdu.kits.presenter.feeds.OnZhihuDailyStoryContentListener;

public class ZhihuDailyStoryContentModelImpl implements ZhihuDailyStoryContentModel {

    @Override
    public void fetchContent(long id, final OnZhihuDailyStoryContentListener listener) {
        RestApiManager.getInstance().fetchZhihuDailyStory(new Observer<StoryContent>() {
            @Override
            public void onSubscribe(Disposable d) {
                listener.onSubscribe(d);
            }

            @Override
            public void onNext(StoryContent content) {
                listener.success(content);
            }

            @Override
            public void onError(Throwable e) {
                listener.error(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        }, id);
    }
}
