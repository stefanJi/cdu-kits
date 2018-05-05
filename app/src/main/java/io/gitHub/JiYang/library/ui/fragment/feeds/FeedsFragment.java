package io.gitHub.JiYang.library.ui.fragment.feeds;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.gitHub.JiYang.library.R;
import io.gitHub.JiYang.library.model.enty.Announce;
import io.gitHub.JiYang.library.presenter.feeds.FeedsPresenter;
import io.gitHub.JiYang.library.presenter.feeds.FeedsPresenterImpl;
import io.gitHub.JiYang.library.ui.fragment.BaseFragment;
import io.gitHub.JiYang.library.ui.view.FeedsView;

public class FeedsFragment extends BaseFragment implements FeedsView, SwipeRefreshLayout.OnRefreshListener {

    public static String TAG = "feeds_fragment";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private AnnounceAdapter mAnnounceAdapter;
    private ArrayList<Announce> announces;
    private FeedsPresenter feedsPresenter;

    public static FeedsFragment newInstance() {
        return new FeedsFragment();
    }

    private int page = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feeds, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        mSwipeRefreshLayout = view.findViewById(R.id.feedRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        feedsPresenter = new FeedsPresenterImpl(this);
        announces = new ArrayList<>();
        mAnnounceAdapter = new AnnounceAdapter(this.getContext(), announces);
        RecyclerView recyclerView = view.findViewById(R.id.feed_list);
        recyclerView.setAdapter(mAnnounceAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    @Override
    public void onStart() {
        super.onStart();
        feedsPresenter.fetchAnnounceFeeds(page);
    }

    @Override
    public void showLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(String error) {
        mSwipeRefreshLayout.setRefreshing(false);
        Toast.makeText(this.getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setAnnounceList(List<Announce> announces) {
        final ArrayList<Announce> update = new ArrayList<>(this.announces);
        for (Announce announce : announces) {
            if (!this.announces.contains(announce)) {
                update.add(announce);
            }
        }
        mAnnounceAdapter.setAnnounce(update);
    }

    @Override
    public void onRefresh() {
        feedsPresenter.fetchAnnounceFeeds(page);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i("TAG2", "feeds ---> " + isVisibleToUser);
    }

    @Override
    public String getTitle() {
        return "发现";
    }
}
