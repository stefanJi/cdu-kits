package jiyang.cdu.kits.presenter.version;

import jiyang.cdu.kits.model.enty.Release;
import jiyang.cdu.kits.presenter.BasePresenterListener;

public interface OnVersionFetchListener extends BasePresenterListener {
    void onSuccess(Release release);

    void onError(String error);
}
