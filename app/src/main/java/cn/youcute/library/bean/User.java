package cn.youcute.library.bean;

/**
 * Created by jy on 2016/9/21.
 * 用户账户，密码
 */
public class User {
    public String account;
    public String password;

    public User() {
    }

    public User(String account, String password) {
        this.account = account;
        this.password = password;
    }
}
