package jiyang.cdu.kits.model.version;

import jiyang.cdu.kits.presenter.version.OnVersionFetchListener;

public interface FetchVersionModel {
    void fetchVersion(final OnVersionFetchListener listener);
}
