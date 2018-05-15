package jiyang.cdu.kits.model.feeds;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.channels.UnresolvedAddressException;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import jiyang.cdu.kits.controller.RestApiManager;
import jiyang.cdu.kits.model.enty.Feed;
import jiyang.cdu.kits.presenter.feeds.OnFeedsListener;

public class FeedsModelImpl implements FeedsModel {

    @Override
    public void fetchFeeds(String newsType, int page, int type, final OnFeedsListener onFeedsListener) {
        RestApiManager.getInstance().getFeeds(new Observer<List<Feed>>() {
            @Override
            public void onSubscribe(Disposable d) {
                onFeedsListener.onSubscribe(d);
            }

            @Override
            public void onNext(List<Feed> feeds) {
                onFeedsListener.onSuccess(feeds);
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof UnknownHostException || e instanceof UnresolvedAddressException) {
                    onFeedsListener.onError("网络不可用,请检查网络设置");
                } else if (e instanceof SocketTimeoutException) {
                    onFeedsListener.onError("连接超时,请重试");
                } else {
                    onFeedsListener.onError(e.getMessage());
                }
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
