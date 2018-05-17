package jiyang.cdu.kits.model.feeds;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import jiyang.cdu.kits.controller.RestApiManager;
import jiyang.cdu.kits.model.enty.zhihu.DailyThemes;
import jiyang.cdu.kits.presenter.feeds.OnZhihuDailyListener;

public class ZhihuDailyModelImpl implements ZhihuDailyModel {
    @Override
    public void fetchThemes(final OnZhihuDailyListener listener) {
        RestApiManager.getInstance().fetchZhihuDailyThemes(new Observer<DailyThemes>() {
            @Override
            public void onSubscribe(Disposable d) {
                listener.onSubscribe(d);
            }

            @Override
            public void onNext(DailyThemes themes) {
                listener.onSuccess(themes);
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
