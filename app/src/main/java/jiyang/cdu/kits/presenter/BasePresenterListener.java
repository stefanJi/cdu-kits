package jiyang.cdu.kits.presenter;

import io.reactivex.disposables.Disposable;

public interface BasePresenterListener {
    void onSubscribe(Disposable disposable);
}
