package jiyang.cdu.kits.model.cdu_links;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import jiyang.cdu.kits.controller.RestApiManager;
import jiyang.cdu.kits.model.enty.CDULinks;
import jiyang.cdu.kits.presenter.links.OnLinksListener;

public class CDULinksModelImpl implements CDULinksModel {
    @Override
    public void getLinks(final OnLinksListener linksListener) {
        RestApiManager.getInstance().fetchCDULinks(new Observer<List<CDULinks>>() {
            @Override
            public void onSubscribe(Disposable d) {
                linksListener.onSubscribe(d);
            }

            @Override
            public void onNext(List<CDULinks> cduLinks) {
                linksListener.onSuccess(cduLinks);
            }

            @Override
            public void onError(Throwable e) {
                linksListener.onError(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
