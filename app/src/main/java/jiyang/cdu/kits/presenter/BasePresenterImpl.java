package jiyang.cdu.kits.presenter;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class BasePresenterImpl<V> {
    private Reference<V> mViewReference;
    private List<Disposable> mDisposables;

    public void attachView(V mView) {
        mDisposables = new ArrayList<>();
        mViewReference = new WeakReference<>(mView);
    }

    public V getView() {
        if (mViewReference != null) {
            return mViewReference.get();
        }
        return null;
    }

    public void detach() {
        if (mDisposables != null && mDisposables.size() > 0) {
            for (Disposable d : mDisposables) {
                d.dispose();
            }
        }
        if (mViewReference != null) {
            mViewReference.clear();
            mViewReference = null;
        }
    }

    public void addDisposable(Disposable disposable) {
        mDisposables.add(disposable);
    }
}
