package jiyang.cdu.kits.ui.fragment.library;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import jiyang.cdu.kits.R;
import jiyang.cdu.kits.databinding.CommRecycleListBinding;
import jiyang.cdu.kits.databinding.ItemBookhistoryBinding;
import jiyang.cdu.kits.model.enty.BookHistory;
import jiyang.cdu.kits.presenter.library.history.BookHistoryImpl;
import jiyang.cdu.kits.ui.activity.WebActivity;
import jiyang.cdu.kits.ui.common.AdapterItem;
import jiyang.cdu.kits.ui.common.BaseFragment;
import jiyang.cdu.kits.ui.common.CommAdapter;
import jiyang.cdu.kits.ui.view.library.LibraryHistoryView;
import jiyang.cdu.kits.ui.widget.UiUtils;
import jiyang.cdu.kits.util.CommUtil;


public class BookHistoryFragment extends BaseFragment<LibraryHistoryView, BookHistoryImpl>
        implements LibraryHistoryView, CommAdapter.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener {
    @Override
    public String getTitle() {
        return "历史借阅";
    }

    @Override
    public BookHistoryImpl initPresenter() {
        return new BookHistoryImpl();
    }


    public static BookHistoryFragment instance() {
        return new BookHistoryFragment();
    }


    private CommRecycleListBinding binding;
    private List<BookHistory> bookHistoryList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.comm_recycle_list, container, false);
        init();
        return binding.getRoot();
    }

    private void init() {
        bookHistoryList = new ArrayList<>();
        CommAdapter<BookHistory> bookHistoryAdapter = new CommAdapter<BookHistory>(bookHistoryList) {
            @Override
            public AdapterItem<BookHistory> createItem() {
                return new AdapterItem<BookHistory>() {
                    ItemBookhistoryBinding itemBookhistoryBinding;

                    @Override
                    public void handleData(int position) {
                        BookHistory history = bookHistoryList.get(position);
                        itemBookhistoryBinding.tvHistoryId.setText(history.historyId);
                        itemBookhistoryBinding.tvHistoryBookName.setText(history.name);
                        itemBookhistoryBinding.tvGetData.setText(history.getData);
                        itemBookhistoryBinding.tvEndData.setText(history.endData);
                        itemBookhistoryBinding.shareButton.setTag(position);
                        itemBookhistoryBinding.shareButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int itemPosition = (int) v.getTag();
                                BookHistory bookHistory = bookHistoryList.get(itemPosition);
                                String shareContent = String.format("我在%s至%s,读了这本书<<%s>>。推荐给你。\n%s"
                                        , bookHistory.getData,
                                        bookHistory.endData,
                                        bookHistory.name,
                                        bookHistory.url);
                                assert getContext() != null;
                                CommUtil.shareContent(getContext(), shareContent);
                            }
                        });
                    }

                    @Override
                    public int getResId() {
                        return R.layout.item_bookhistory;
                    }

                    @Override
                    public void bindViews(View itemView) {
                        itemBookhistoryBinding = DataBindingUtil.bind(itemView);
                    }
                };
            }
        };
        binding.recycleView.setAdapter(bookHistoryAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        binding.recycleView.setLayoutManager(linearLayoutManager);
        bookHistoryAdapter.setItemClickListener(this);
        binding.refreshLayout.setOnRefreshListener(this);
        setRefreshLayoutColor(binding.refreshLayout);
        presenter.fetchHistory();
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
    public void success(List<BookHistory> bookHistories) {
        if (bookHistories == null || bookHistories.size() == 0) {
            return;
        }
        bookHistoryList.clear();
        bookHistoryList.addAll(bookHistories);
        binding.recycleView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void error(String error) {
        UiUtils.showErrorSnackbar(this.getActivity(), binding.getRoot(), error, "重试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.fetchHistory();
            }
        });
    }

    @Override
    public void OnItemClick(int position) {
        BookHistory bookHistory = bookHistoryList.get(position);
        WebActivity.start(getContext(), bookHistory.url);
    }

    @Override
    public void onRefresh() {
        presenter.fetchHistory();
    }
}
