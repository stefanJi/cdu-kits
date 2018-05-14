package jiyang.cdu.kits.model.library;


import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import jiyang.cdu.kits.controller.RestApiManager;
import jiyang.cdu.kits.model.enty.LibraryUserInfo;
import jiyang.cdu.kits.presenter.library.OnLoginListener;

public class LoginModelImpl implements LoginModel {

    @Override
    public void login(String account, String password, String type, final OnLoginListener loginListener) {
        RestApiManager.getInstance().loginLib(new Observer<LibraryUserInfo>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(LibraryUserInfo userInfo) {
                if (userInfo != null && userInfo.account != null) {
                    loginListener.onSuccess(userInfo);
                } else {
                    loginListener.onError("登录失败");
                }
            }

            @Override
            public void onError(Throwable e) {
                loginListener.onError(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        }, account, password, type);
    }
}
