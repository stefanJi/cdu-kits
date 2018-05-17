package jiyang.cdu.kits.model.version;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import jiyang.cdu.kits.controller.RestApiManager;
import jiyang.cdu.kits.model.enty.Release;
import jiyang.cdu.kits.presenter.version.OnVersionFetchListener;

public class FetchVersionModelImpl implements FetchVersionModel {

    @Override
    public void fetchVersion(final OnVersionFetchListener listener) {
        RestApiManager.getInstance().fetchReleasesVersion(new Observer<Release>() {
            @Override
            public void onSubscribe(Disposable d) {
                listener.onSubscribe(d);
            }

            @Override
            public void onNext(Release release) {
                listener.onSuccess(release);
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
