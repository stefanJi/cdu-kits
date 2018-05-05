package io.gitHub.JiYang.library.model.impl;

import java.util.List;

import io.gitHub.JiYang.library.controller.RestApiManager;
import io.gitHub.JiYang.library.model.FeedsModel;
import io.gitHub.JiYang.library.model.enty.Announce;
import io.gitHub.JiYang.library.presenter.feeds.OnFeedsListener;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class FeedsModelImpl implements FeedsModel {

    @Override
    public void fetchAnnounce(int page, final OnFeedsListener onFeedsListener) {
        RestApiManager.getInstance().getAnnounce(new Observer<List<Announce>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Announce> announces) {
                onFeedsListener.onSuccess(announces);
            }

            @Override
            public void onError(Throwable e) {
                onFeedsListener.onError(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        }, page);
    }
}
