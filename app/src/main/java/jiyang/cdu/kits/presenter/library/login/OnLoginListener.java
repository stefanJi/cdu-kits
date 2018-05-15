package jiyang.cdu.kits.presenter.library.login;


import jiyang.cdu.kits.model.enty.LibraryUserInfo;
import jiyang.cdu.kits.presenter.BasePresenterListener;

public interface OnLoginListener extends BasePresenterListener {
    void onSuccess(LibraryUserInfo userInfo);

    void onError(String error);
}
