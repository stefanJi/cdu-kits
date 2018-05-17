package jiyang.cdu.kits.presenter.version;

import io.reactivex.disposables.Disposable;
import jiyang.cdu.kits.model.enty.Release;
import jiyang.cdu.kits.model.version.FetchVersionModel;
import jiyang.cdu.kits.model.version.FetchVersionModelImpl;
import jiyang.cdu.kits.presenter.BasePresenterImpl;
import jiyang.cdu.kits.ui.view.VersionView;

public class VersionPresenterImpl extends BasePresenterImpl<VersionView> implements VersionPresenter, OnVersionFetchListener {
    private FetchVersionModel fetchVersionModel;

    public VersionPresenterImpl() {
        fetchVersionModel = new FetchVersionModelImpl();
    }

    public VersionPresenterImpl(VersionView versionView) {
        fetchVersionModel = new FetchVersionModelImpl();
        attachView(versionView);
    }

    @Override
    public void fetchVersion() {
        fetchVersionModel.fetchVersion(this);
    }

    @Override
    public void onSuccess(Release release) {
        if (getView() != null) {
            getView().onSuccess(release);
        }
    }

    @Override
    public void onError(String error) {
        if (getView() != null) {
            getView().onError(error);
        }
    }

    @Override
    public void onSubscribe(Disposable disposable) {
        addDisposable(disposable);
    }
}
