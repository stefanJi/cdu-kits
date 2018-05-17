package jiyang.cdu.kits.presenter.links;

import java.util.List;

import jiyang.cdu.kits.model.enty.CDULinks;
import jiyang.cdu.kits.presenter.BasePresenterListener;

public interface OnLinksListener extends BasePresenterListener{
    void onSuccess(List<CDULinks> cduLinksList);

    void onError(String error);
}
