package jiyang.cdu.kits.ui.fragment.feeds;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jiyang.cdu.kits.R;
import jiyang.cdu.kits.databinding.FragmentFeedsItemBinding;
import jiyang.cdu.kits.databinding.ItemZhihuStoryBinding;
import jiyang.cdu.kits.model.enty.zhihu.DailyTheme;
import jiyang.cdu.kits.model.enty.zhihu.Stories;
import jiyang.cdu.kits.model.enty.zhihu.Story;
import jiyang.cdu.kits.model.enty.zhihu.StoryContent;
import jiyang.cdu.kits.presenter.feeds.ZhihuDailyStoriesImpl;
import jiyang.cdu.kits.presenter.feeds.ZhihuDailyStoryContentImpl;
import jiyang.cdu.kits.presenter.feeds.ZhihuDailyStoryContentPresenter;
import jiyang.cdu.kits.ui.activity.WebActivity;
import jiyang.cdu.kits.ui.common.AdapterItem;
import jiyang.cdu.kits.ui.common.BaseFragment;
import jiyang.cdu.kits.ui.common.CommAdapter;
import jiyang.cdu.kits.ui.view.feeds.ZhihuDailyStoriesView;
import jiyang.cdu.kits.ui.view.feeds.ZhihuDailyStoryContentView;
import jiyang.cdu.kits.ui.widget.UiUtils;

public class ZhihuDailyItemThemeFragment extends BaseFragment<ZhihuDailyStoriesView, ZhihuDailyStoriesImpl>
        implements ZhihuDailyStoriesView,
        ZhihuDailyStoryContentView,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String SERIALIZABLE_KEY_DAILY = "daily";

    @Override
    public ZhihuDailyStoriesImpl initPresenter() {
        return new ZhihuDailyStoriesImpl();
    }


    public static ZhihuDailyItemThemeFragment newInstance(DailyTheme dailyTheme) {
        ZhihuDailyItemThemeFragment fragment = new ZhihuDailyItemThemeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SERIALIZABLE_KEY_DAILY, dailyTheme);
        fragment.setArguments(bundle);
        fragment.setTitle(dailyTheme.getName());
        return fragment;
    }

    private FragmentFeedsItemBinding binding;
    private DailyTheme dailyTheme;
    private List<Story> storyList;
    private ZhihuDailyStoryContentPresenter contentPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dailyTheme = (DailyTheme) getArguments().getSerializable(SERIALIZABLE_KEY_DAILY);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_feeds_item, container, false);
        init();
        return binding.getRoot();
    }

    private void init() {
        storyList = new ArrayList<>();
        if (getContext() != null) {
            binding.refreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        }
        binding.refreshLayout.setOnRefreshListener(this);
        CommAdapter<Story> adapter = new CommAdapter<Story>(storyList) {
            @Override
            public AdapterItem<Story> createItem() {
                return new AdapterItem<Story>() {
                    ItemZhihuStoryBinding storyBinding;

                    @Override
                    public void handleData(int position) {
                        Story story = storyList.get(position);
                        storyBinding.title.setText(story.getTitle());
                        if (story.getImages() != null && story.getImages().size() > 0) {
                            Picasso.get().load(story.getImages().get(0))
                                    .placeholder(R.drawable.ic_image_black)
                                    .into(storyBinding.image);
                        } else {
                            storyBinding.image.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public int getResId() {
                        return R.layout.item_zhihu_story;
                    }

                    @Override
                    public void bindViews(View itemView) {
                        storyBinding = DataBindingUtil.bind(itemView);
                    }
                };
            }
        };
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (binding.recyclerView.getAdapter().getItemCount() == 0) {
                    binding.emptyTip.setVisibility(View.VISIBLE);
                } else {
                    binding.emptyTip.setVisibility(View.GONE);
                }
            }
        });
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (binding.recyclerView.getAdapter().getItemCount() == 0) {
                    binding.emptyTip.setVisibility(View.VISIBLE);
                } else {
                    binding.emptyTip.setVisibility(View.GONE);
                }
            }
        });
        contentPresenter = new ZhihuDailyStoryContentImpl(this);

        adapter.setItemClickListener(new CommAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                Story story = storyList.get(position);
                contentPresenter.fetchContent(story.getId());
            }
        });
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        presenter.fetchStories(dailyTheme.getId());
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
    public void setFetchResult(StoryContent storyContent) {
        WebActivity.start(getContext(), storyContent.getShareUrl());
    }

    @Override
    public void setFetchResult(Stories stories) {
        if (stories != null) {
            this.storyList.clear();
            this.storyList.addAll(stories.getStories());
        }
        binding.recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showError(String error) {
        UiUtils.showErrorSnackbar(getContext(), binding.getRoot(), error, "重试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.fetchStories(dailyTheme.getId());
            }
        }, Snackbar.LENGTH_SHORT);
    }

    @Override
    public void onRefresh() {
        presenter.fetchStories(dailyTheme.getId());
    }
}
