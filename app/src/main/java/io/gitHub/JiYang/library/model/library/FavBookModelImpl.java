package io.gitHub.JiYang.library.model.library;

import com.litesuits.orm.LiteOrm;

import java.util.List;

import io.gitHub.JiYang.library.AppControl;
import io.gitHub.JiYang.library.model.enty.FavBook;
import io.gitHub.JiYang.library.presenter.library.FavBookPresenters;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FavBookModelImpl implements FavBookModel {
    @Override
    public void fetchFavBooks(final FavBookPresenters.OnFavBookListener favBookListener) {
        Observable<List<FavBook>> observable = new Observable<List<FavBook>>() {
            @Override
            protected void subscribeActual(Observer<? super List<FavBook>> observer) {
                try {
                    LiteOrm liteOrm = AppControl.getInstance().getLiteOrm();
                    List<FavBook> favBooks = liteOrm.query(FavBook.class);
                    observer.onNext(favBooks);
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        };
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<FavBook>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<FavBook> favBooks) {
                        favBookListener.success(favBooks);
                    }

                    @Override
                    public void onError(Throwable e) {
                        favBookListener.error(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
