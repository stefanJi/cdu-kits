package jiyang.cdu.kits.ui.view;

import jiyang.cdu.kits.model.enty.Release;

public interface VersionView extends BaseView {
    void onGotVersion(Release release);

    void onError(String error);
}
