package jiyang.cdu.kits.model.library;


import jiyang.cdu.kits.presenter.library.login.OnLoginListener;

public interface LoginModel {
    void login(String account, String password, String type, OnLoginListener loginListener);
}
