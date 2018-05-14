package jiyang.cdu.kits.presenter.feeds;

public interface FeedsPresenter {
    int ANNOUNCE = 1;
    int NEWS = 2;
    int NEWS2 = 3;
    int MEDIA = 4;
    int COLOR_CAMPUS = 5;
    int ARTICLE = 75;
    int HQC_ANNOUNCE = 13;

    void fetchFeeds(int page, int type);
}
