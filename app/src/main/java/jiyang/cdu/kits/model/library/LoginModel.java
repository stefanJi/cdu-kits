package jiyang.cdu.kits.model.library;


import jiyang.cdu.kits.presenter.library.OnLoginListener;

public interface LoginModel {
    void login(String account, String password, String type, OnLoginListener loginListener);
}
