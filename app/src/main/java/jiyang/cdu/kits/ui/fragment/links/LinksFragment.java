package jiyang.cdu.kits.ui.fragment.links;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import jiyang.cdu.kits.R;
import jiyang.cdu.kits.databinding.CommRecycleListBinding;
import jiyang.cdu.kits.databinding.ItemFeedsListBinding;
import jiyang.cdu.kits.model.enty.CDULinks;
import jiyang.cdu.kits.presenter.links.LinksPresenterImpl;
import jiyang.cdu.kits.ui.activity.WebActivity;
import jiyang.cdu.kits.ui.common.AdapterItem;
import jiyang.cdu.kits.ui.common.BaseFragment;
import jiyang.cdu.kits.ui.common.CommAdapter;
import jiyang.cdu.kits.ui.view.CDULinksView;
import jiyang.cdu.kits.ui.widget.UiUtils;
import jiyang.cdu.kits.util.CommUtil;

public class LinksFragment extends BaseFragment<CDULinksView, LinksPresenterImpl> implements CDULinksView, SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG = "links_fragment";
    private List<CDULinks> cdulinks;

    @Override
    public String getTitle() {
        return "链接集";
    }

    @Override
    public LinksPresenterImpl initPresenter() {
        return new LinksPresenterImpl();
    }

    private CommRecycleListBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.comm_recycle_list, container, false);
        init();
        return binding.getRoot();
    }

    private void init() {
        cdulinks = new ArrayList<>();
        binding.refreshLayout.setOnRefreshListener(this);
        if (getContext() != null) {
            binding.refreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        }
        binding.recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        CommAdapter<CDULinks> cduLinksAdapter = new CommAdapter<CDULinks>(cdulinks) {
            @Override
            public AdapterItem<CDULinks> createItem() {
                return new AdapterItem<CDULinks>() {
                    ItemFeedsListBinding linkBinding;

                    @Override
                    public void handleData(int position) {
                        CDULinks cduLink = cdulinks.get(position);
                        linkBinding.title.setText(cduLink.title);
                        linkBinding.subTitle.setText(cduLink.url);
                        linkBinding.shareButton.setTag(position);
                        linkBinding.shareButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int itemPosition = (int) v.getTag();
                                CDULinks cduLink = cdulinks.get(itemPosition);
                                CommUtil.shareContent(getContext(), cduLink.title + "\n" + cduLink.url);
                            }
                        });
                    }

                    @Override
                    public int getResId() {
                        return R.layout.item_feeds_list;
                    }

                    @Override
                    public void bindViews(View itemView) {
                        linkBinding = DataBindingUtil.bind(itemView);
                    }
                };
            }
        };
        cduLinksAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (binding.recycleView.getAdapter().getItemCount() == 0) {
                    binding.emptyView.setVisibility(View.VISIBLE);
                } else {
                    binding.emptyView.setVisibility(View.GONE);
                }
            }
        });
        cduLinksAdapter.setItemClickListener(new CommAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                CDULinks cduLinks = cdulinks.get(position);
                WebActivity.start(getContext(), cduLinks.url);
            }
        });
        binding.recycleView.setAdapter(cduLinksAdapter);
        presenter.getCDULinks();
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
        UiUtils.showErrorSnackbar(getContext(), binding.getRoot(), error, "重试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getCDULinks();
            }
        });
    }

    @Override
    public void setLinksResult(List<CDULinks> cduLinks) {
        if (cduLinks != null) {
            this.cdulinks.clear();
            this.cdulinks.addAll(cduLinks);
            binding.recycleView.getAdapter().notifyDataSetChanged();
        }
    }

    public static LinksFragment newInstance() {
        return new LinksFragment();
    }

    @Override
    public void onRefresh() {
        presenter.getCDULinks();
    }
}
