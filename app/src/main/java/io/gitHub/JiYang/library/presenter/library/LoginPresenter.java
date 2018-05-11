package io.gitHub.JiYang.library.presenter.library;

public interface LoginPresenter {
    //登录类型：cert_no证件号 email邮箱 bar_no 条码号
    String TYPE_CERT_ON = "cert_no";
    String TYPE_SER_NUM = "bar_no";
    String TYPE_EMAIL = "email";

    void login(String account, String password, String type);
}
