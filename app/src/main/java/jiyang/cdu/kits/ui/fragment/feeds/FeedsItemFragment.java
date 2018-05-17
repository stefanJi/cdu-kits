package jiyang.cdu.kits.ui.fragment.feeds;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jiyang.cdu.kits.Constant;
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
import jiyang.cdu.kits.ui.view.feeds.FeedsView;
import jiyang.cdu.kits.ui.widget.EndlessRecyclerOnScrollListener;
import jiyang.cdu.kits.ui.widget.UiUtils;
import jiyang.cdu.kits.util.CommUtil;

public class FeedsItemFragment extends BaseFragment<FeedsView, FeedsPresenterImpl>
        implements FeedsView,
        SwipeRefreshLayout.OnRefreshListener,
        CommAdapter.OnItemClickListener {
    private static final String BUNDLE_KEY_TYPE = "type";
    public static Map<String, Integer> TAB_TYPE_MAP;

    static {
        TAB_TYPE_MAP = new HashMap<>();
        TAB_TYPE_MAP.put(Constant.FEEDS_TAB_HQC_ANNOUNCE, FeedsPresenter.HQC_ANNOUNCE);
        TAB_TYPE_MAP.put(Constant.FEEDS_TAB_ANNOUNCE, FeedsPresenter.ANNOUNCE);
        TAB_TYPE_MAP.put(Constant.FEEDS_TAB_COLOR_CAMPUS, FeedsPresenter.COLOR_CAMPUS);
        TAB_TYPE_MAP.put(Constant.FEEDS_TAB_MEDIA, FeedsPresenter.MEDIA);
        TAB_TYPE_MAP.put(Constant.FEEDS_TAB_ARTICLE, FeedsPresenter.ARTICLE);
        TAB_TYPE_MAP.put(Constant.FEEDS_TAB_NEWS, FeedsPresenter.NEWS);
        TAB_TYPE_MAP.put(Constant.FEEDS_TAB_NEWS2, FeedsPresenter.NEWS2);
    }

    public static FeedsItemFragment newInstance(String feedsType) {
        FeedsItemFragment feedsItemFragment = new FeedsItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_TYPE, feedsType);
        feedsItemFragment.setArguments(bundle);
        feedsItemFragment.setTitle(feedsType);
        return feedsItemFragment;
    }

    private FragmentFeedsItemBinding binding;
    private ArrayList<Feed> feeds;

    private int feedsPage;

    private String feedsType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feeds_item, container, false);
        binding = DataBindingUtil.bind(view);
        init();
        return view;
    }

    private void init() {
        feedsType = getArguments().getString(BUNDLE_KEY_TYPE);
        feedsPage = 1;
        feeds = new ArrayList<>();

        setRefreshLayoutColor(binding.refreshLayout);
        binding.refreshLayout.setOnRefreshListener(this);
        CommAdapter<Feed> feedsAdapter = new CommAdapter<Feed>(feeds) {

            @Override
            public AdapterItem<Feed> createItem() {
                return new AdapterItem<Feed>() {
                    ItemFeedsListBinding itemBinding;

                    @Override
                    public void handleData(int position) {
                        Feed data = feeds.get(position);
                        itemBinding.title.setText(data.title);
                        itemBinding.subTitle.setText(data.date);
                        itemBinding.shareButton.setTag(data);
                        itemBinding.shareButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Feed feed = (Feed) v.getTag();
                                assert getContext() != null;
                                CommUtil.shareContent(getContext(), feed.title + "\n"
                                        + feed.date + "\n" + feed.url
                                        + "\n" + getString(R.string.app_name));
                            }
                        });
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
                presenter.fetchFeeds(current_page, TAB_TYPE_MAP.get(feedsType));
            }
        });

        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        presenter.fetchFeeds(feedsPage, TAB_TYPE_MAP.get(feedsType));
    }

    @Override
    public FeedsPresenterImpl initPresenter() {
        return new FeedsPresenterImpl();
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
    public void setFeedsList(List<Feed> feeds) {
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
        binding.recyclerView.getAdapter().notifyItemRangeInserted(
                binding.recyclerView.getAdapter().getItemCount() - 1, update.size());
    }

    @Override
    public void onRefresh() {
        presenter.fetchFeeds(feedsPage, TAB_TYPE_MAP.get(feedsType));
    }

    @Override
    public void OnItemClick(int position) {
        Feed feed = feeds.get(position);
        WebActivity.start(this.getActivity(), feed.url);
    }
}
