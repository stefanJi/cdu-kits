package io.gitHub.JiYang.library.model.feeds;

import java.util.List;

import io.gitHub.JiYang.library.controller.RestApiManager;
import io.gitHub.JiYang.library.model.feeds.FeedsModel;
import io.gitHub.JiYang.library.model.enty.Feed;
import io.gitHub.JiYang.library.presenter.feeds.OnFeedsListener;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class FeedsModelImpl implements FeedsModel {

    @Override
    public void fetchFeeds(String newsType, int page, int type, final OnFeedsListener onFeedsListener) {
        RestApiManager.getInstance().getFeeds(new Observer<List<Feed>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Feed> feeds) {
                onFeedsListener.onSuccess(feeds);
            }

            @Override
            public void onError(Throwable e) {
                onFeedsListener.onError(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        }, newsType, page, type);
    }

    @Override
    public void fetchHQCFeeds(int page, final OnFeedsListener onFeedsListener) {
        RestApiManager.getInstance().getHQCFeeds(new Observer<List<Feed>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Feed> feeds) {
                onFeedsListener.onSuccess(feeds);
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
