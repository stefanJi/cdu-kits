package jiyang.cdu.kits.ui.fragment.feeds;


import jiyang.cdu.kits.presenter.BasePresenterImpl;
import jiyang.cdu.kits.ui.common.BaseFragment;

public class ExploreFragment extends BaseFragment {
    @Override
    public String getTitle() {
        return "探索";
    }

    @Override
    public BasePresenterImpl initPresenter() {
        return null;
    }

    public static ExploreFragment newInstance() {
        return new ExploreFragment();
    }
}
