package io.gitHub.JiYang.library.ui.fragment.feeds;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.gitHub.JiYang.library.R;
import io.gitHub.JiYang.library.model.enty.Feed;
import io.gitHub.JiYang.library.presenter.feeds.FeedsPresenter;
import io.gitHub.JiYang.library.presenter.feeds.FeedsPresenterImpl;
import io.gitHub.JiYang.library.ui.activity.WebActivity;
import io.gitHub.JiYang.library.ui.fragment.BaseFragment;
import io.gitHub.JiYang.library.ui.view.FeedsView;
import io.gitHub.JiYang.library.ui.widget.EndlessRecyclerOnScrollListener;

public class FeedsItemFragment extends BaseFragment implements FeedsView,
        SwipeRefreshLayout.OnRefreshListener,
        FeedsAdapter.OnItemClickListener {

    public static FeedsItemFragment newInstance(int feedsType) {
        FeedsItemFragment feedsItemFragment = new FeedsItemFragment();
        feedsItemFragment.setFeedsType(feedsType);
        return feedsItemFragment;
    }

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView mRecyclerView;
    private FeedsAdapter feedsAdapter;
    private ArrayList<Feed> feeds;
    private FeedsPresenter announcePresenter;

    private int announcePage = 1;

    private int feedsType;

    public void setFeedsType(int feedsType) {
        this.feedsType = feedsType;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_announce, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        feeds = new ArrayList<>();

        refreshLayout = view.findViewById(R.id.refreshAnnounceLayout);
        refreshLayout.setColorSchemeColors(Color.BLUE);
        refreshLayout.setOnRefreshListener(this);


        feedsAdapter = new FeedsAdapter(this.getContext(), feeds);
        feedsAdapter.setOnItemClickListener(this);

        mRecyclerView = view.findViewById(R.id.announceListView);
        mRecyclerView.setAdapter(feedsAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                announcePage = current_page;
                announcePresenter.fetchFeeds(current_page, feedsType);
            }
        });

        view.findViewById(R.id.backTop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView
                        .getLayoutManager();
                layoutManager.scrollToPositionWithOffset(0, 0);
            }
        });

        announcePresenter = new FeedsPresenterImpl(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        announcePresenter.fetchFeeds(announcePage, feedsType);
    }

    @Override
    public String getTitle() {
        String title;
        switch (feedsType) {
            case FeedsPresenter.ANNOUNCE:
                title = "通知公告";
                break;
            case FeedsPresenter.NEWS:
                title = "成大要文";
                break;
            case FeedsPresenter.NEWS2:
                title = "综合新闻";
                break;
            case FeedsPresenter.MEDIA:
                title = "媒体成大";
                break;
            case FeedsPresenter.COLOR_CAMPUS:
                title = "多彩校园";
                break;
            case FeedsPresenter.ARTICLE:
                title = "学术文化";
                break;
            case FeedsPresenter.HQC_ANNOUNCE:
                title = "后勤公告";
                break;
            default:
                title = "其他";
                break;
        }
        return title;
    }

    @Override
    public void showLoading() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this.getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setAnnounceList(List<Feed> feeds) {
        if (feeds == null || feeds.size() == 0) {
            Toast.makeText(this.getContext(), "无更多", Toast.LENGTH_SHORT).show();
            return;
        }
        final ArrayList<Feed> update = new ArrayList<>();
        for (Feed feed : feeds) {
            if (this.feeds.contains(feed)) {
                continue;
            }
            update.add(feed);
        }
        this.feeds.addAll(update);
        feedsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        announcePresenter.fetchFeeds(announcePage, feedsType);
    }

    @Override
    public void onItemClick(View view, int position) {
        Feed feed = feeds.get(position);
        WebActivity.start(this.getActivity(), feed.url);
    }
}
