package io.gitHub.JiYang.library.presenter.feeds;

import java.util.List;

import io.gitHub.JiYang.library.model.enty.Announce;

public interface OnFeedsListener {
    void onSuccess(List<Announce> announces);

    void onError(String error);
}
