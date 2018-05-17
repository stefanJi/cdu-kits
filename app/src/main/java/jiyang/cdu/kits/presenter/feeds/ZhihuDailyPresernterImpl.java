package jiyang.cdu.kits.presenter.feeds;

import io.reactivex.disposables.Disposable;
import jiyang.cdu.kits.model.enty.zhihu.DailyThemes;
import jiyang.cdu.kits.model.feeds.ZhihuDailyModel;
import jiyang.cdu.kits.model.feeds.ZhihuDailyModelImpl;
import jiyang.cdu.kits.presenter.BasePresenterImpl;
import jiyang.cdu.kits.ui.view.feeds.ZhihuDailyView;

public class ZhihuDailyPresernterImpl extends BasePresenterImpl<ZhihuDailyView> implements ZhihuDailyPresenter, OnZhihuDailyListener {

    private ZhihuDailyModel model;

    public ZhihuDailyPresernterImpl() {
        model = new ZhihuDailyModelImpl();
    }

    public ZhihuDailyPresernterImpl(ZhihuDailyView zhihuDailyView) {
        this();
        attachView(zhihuDailyView);
    }

    @Override
    public void fetch() {
        model.fetchThemes(this);
        if (getView() != null) {
            getView().showLoading();
        }
    }

    @Override
    public void onSubscribe(Disposable disposable) {
        addDisposable(disposable);
    }

    @Override
    public void onError(String error) {
        if (getView() != null) {
            getView().hideLoading();
            getView().showError(error);
        }
    }

    @Override
    public void onSuccess(DailyThemes themes) {
        if (getView() != null) {
            getView().hideLoading();
            getView().setFetchResult(themes);
        }
    }

}
