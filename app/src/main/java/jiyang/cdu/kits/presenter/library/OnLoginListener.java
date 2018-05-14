package jiyang.cdu.kits.presenter.library;


import jiyang.cdu.kits.model.enty.LibraryUserInfo;

public interface OnLoginListener {
    void onSuccess(LibraryUserInfo userInfo);

    void onError(String error);
}
