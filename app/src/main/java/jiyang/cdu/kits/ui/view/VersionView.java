package jiyang.cdu.kits.ui.view;

import jiyang.cdu.kits.model.enty.Version;

public interface VersionView extends BaseView {
    void onSuccess(Version version);

    void onError(String error);
}
