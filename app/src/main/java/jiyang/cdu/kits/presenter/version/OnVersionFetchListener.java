package jiyang.cdu.kits.presenter.version;

import jiyang.cdu.kits.model.enty.Version;
import jiyang.cdu.kits.presenter.BasePresenterListener;

public interface OnVersionFetchListener extends BasePresenterListener {
    void onSuccess(Version version);

    void onError(String error);
}
