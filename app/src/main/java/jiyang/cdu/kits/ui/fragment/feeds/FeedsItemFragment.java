package jiyang.cdu.kits.ui.fragment.feeds;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import jiyang.cdu.kits.R;
import jiyang.cdu.kits.databinding.FragmentFeedsItemBinding;
import jiyang.cdu.kits.databinding.ItemFeedsListBinding;
import jiyang.cdu.kits.model.enty.Feed;
import jiyang.cdu.kits.presenter.feeds.FeedsPresenter;
import jiyang.cdu.kits.presenter.feeds.FeedsPresenterImpl;
import jiyang.cdu.kits.ui.activity.WebActivity;
import jiyang.cdu.kits.ui.common.AdapterItem;
import jiyang.cdu.kits.ui.common.BaseFragment;
import jiyang.cdu.kits.ui.common.CommAdapter;
import jiyang.cdu.kits.ui.view.FeedsView;
import jiyang.cdu.kits.ui.widget.EndlessRecyclerOnScrollListener;
import jiyang.cdu.kits.ui.widget.UiUtils;

public class FeedsItemFragment extends BaseFragment implements FeedsView,
        SwipeRefreshLayout.OnRefreshListener,
        CommAdapter.OnItemClickListener {

    public static FeedsItemFragment newInstance(int feedsType) {
        FeedsItemFragment feedsItemFragment = new FeedsItemFragment();
        feedsItemFragment.setFeedsType(feedsType);
        return feedsItemFragment;
    }

    private FragmentFeedsItemBinding binding;
    private CommAdapter<Feed> feedsAdapter;
    private ArrayList<Feed> feeds;
    private FeedsPresenter feedsPresenter;

    private int feedsPage = 1;

    private int feedsType;

    public void setFeedsType(int feedsType) {
        this.feedsType = feedsType;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feeds_item, container, false);
        binding = DataBindingUtil.bind(view);
        init();
        return view;
    }

    private void init() {
        feeds = new ArrayList<>();

        setRefreshLayoutColor(binding.refreshLayout);
        binding.refreshLayout.setOnRefreshListener(this);

        feedsAdapter = new CommAdapter<Feed>(feeds) {

            @Override
            public AdapterItem<Feed> createItem() {
                return new AdapterItem<Feed>() {
                    ItemFeedsListBinding itemBinding;

                    @Override
                    public void handleData(int position) {
                        Feed data = feeds.get(position);
                        itemBinding.feedTitle.setText(data.title);
                        itemBinding.feedDate.setText(data.date);
                    }

                    @Override
                    public int getResId() {
                        return R.layout.item_feeds_list;
                    }

                    @Override
                    public void bindViews(View itemView) {
                        itemBinding = DataBindingUtil.bind(itemView);
                    }
                };
            }
        };

        feedsAdapter.setItemClickListener(this);


        binding.recyclerView.setAdapter(feedsAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                feedsPage = current_page;
                feedsPresenter.fetchFeeds(current_page, feedsType);
            }
        });
        feedsPresenter = new FeedsPresenterImpl(this);
        feedsPresenter.fetchFeeds(feedsPage, feedsType);
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
        binding.refreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        binding.refreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(String error) {
        if (this.feeds.size() > 0) {
            binding.emptyTip.setVisibility(View.GONE);
        } else {
            binding.emptyTip.setVisibility(View.VISIBLE);
        }
        UiUtils.showErrorSnackbar(this.getActivity(), binding.recyclerView, error);
    }

    @Override
    public void setAnnounceList(List<Feed> feeds) {
        if (feeds == null || feeds.size() == 0) {
            Snackbar.make(binding.getRoot(), "无更多", Snackbar.LENGTH_SHORT).show();
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
        if (this.feeds.size() > 0) {
            binding.emptyTip.setVisibility(View.GONE);
        } else {
            binding.emptyTip.setVisibility(View.VISIBLE);
        }
        feedsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        feedsPresenter.fetchFeeds(feedsPage, feedsType);
    }

    @Override
    public void OnItemClick(int position) {
        Feed feed = feeds.get(position);
        WebActivity.start(this.getActivity(), feed.url);
    }
}
