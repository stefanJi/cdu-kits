package jiyang.cdu.kits.presenter.links;

import java.util.List;

import io.reactivex.disposables.Disposable;
import jiyang.cdu.kits.model.cdu_links.CDULinksModel;
import jiyang.cdu.kits.model.cdu_links.CDULinksModelImpl;
import jiyang.cdu.kits.model.enty.CDULinks;
import jiyang.cdu.kits.presenter.BasePresenterImpl;
import jiyang.cdu.kits.ui.view.CDULinksView;

public class LinksPresenterImpl extends BasePresenterImpl<CDULinksView> implements LinksPrensenter, OnLinksListener {

    private CDULinksModel model;

    public LinksPresenterImpl() {
        model = new CDULinksModelImpl();
    }

    public LinksPresenterImpl(CDULinksView view) {
        this();
        attachView(view);
    }

    @Override
    public void getCDULinks() {
        if (getView() != null) {
            getView().showLoading();
        }
        model.getLinks(this);
    }

    @Override
    public void onSuccess(List<CDULinks> cduLinksList) {
        if (getView() != null) {
            getView().hideLoading();
            getView().setLinksResult(cduLinksList);
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
    public void onSubscribe(Disposable disposable) {
        addDisposable(disposable);
    }
}
