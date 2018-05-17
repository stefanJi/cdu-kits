package jiyang.cdu.kits.ui.view;

import java.util.List;

import jiyang.cdu.kits.model.enty.CDULinks;

public interface CDULinksView extends BaseView {
    void showLoading();

    void hideLoading();

    void showError(String error);

    void setLinksResult(List<CDULinks> cduLinks);
}
