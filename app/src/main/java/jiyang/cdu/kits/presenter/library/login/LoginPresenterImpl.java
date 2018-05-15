package jiyang.cdu.kits.presenter.library.login;


import io.reactivex.disposables.Disposable;
import jiyang.cdu.kits.model.enty.LibraryUserInfo;
import jiyang.cdu.kits.model.library.LoginModel;
import jiyang.cdu.kits.model.library.LoginModelImpl;
import jiyang.cdu.kits.presenter.BasePresenterImpl;
import jiyang.cdu.kits.ui.view.library.LoginLibraryView;

public class LoginPresenterImpl extends BasePresenterImpl<LoginLibraryView>
        implements LoginPresenter, OnLoginListener {
    // presenter作为中间层，持有 View 和 Model 的引用
    private LoginModel loginModel;


    public LoginPresenterImpl() {
        loginModel = new LoginModelImpl();
    }

    @Override
    public void login(String account, String password, String type) {
        if (getView() != null) {
            getView().showLoginProgress();
        }
        loginModel.login(account, password, type, this);
    }

    @Override
    public void onSuccess(LibraryUserInfo userInfo) {
        if (getView() != null) {
            getView().hideLoginProgress();
            getView().showLoginSuccess(userInfo);
        }
    }

    @Override
    public void onError(String error) {
        if (getView() != null) {
            getView().hideLoginProgress();
            getView().showLoginError(error);
        }
    }

    @Override
    public void onSubscribe(Disposable disposable) {
        addDisposable(disposable);
    }
}
