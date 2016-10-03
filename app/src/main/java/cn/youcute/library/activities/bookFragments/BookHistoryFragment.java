package cn.youcute.library.activities.bookFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.youcute.library.AppControl;
import cn.youcute.library.R;
import cn.youcute.library.adapter.AdapterBookHistory;
import cn.youcute.library.bean.History;
import cn.youcute.library.diyView.LoadMoreListView;
import cn.youcute.library.util.NetRequest;

/**
 * Created by jy on 2016/9/23.
 * 历史借阅视图
 */
public class BookHistoryFragment extends Fragment implements NetRequest.GetBookHistoryCallBack, LoadMoreListView.OnRefreshListener {
    private View rootView;
    private LoadMoreListView listHistory;
    private boolean isLoadMore = false;
    private String session;
    private AdapterBookHistory adapterBookHistory;
    private List<History> historyList;
    private int page = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            session = getArguments().getString(NetRequest.PHP_SESSION_ID);
            historyList = new ArrayList<>();
            rootView = inflater.inflate(R.layout.bookhistory_frag, container, false);
            initViews();
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != rootView) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
    }

    private void initViews() {
        listHistory = (LoadMoreListView) rootView.findViewById(R.id.list_book_history);
        listHistory.setOnRefreshListener(this);
        //获取历史借阅
        AppControl.getInstance().getNetRequest().getBookHistory(session, page, this);
    }

    @Override
    public void getHistorySuccess(List<History> list) {
        if (list.size() == 0) {
            listHistory.noMoreLoad();
        }
        historyList.addAll(list);
        if (adapterBookHistory == null) {
            adapterBookHistory = new AdapterBookHistory(historyList, getActivity());
            adapterBookHistory.setHistoryList(historyList);
            listHistory.setAdapter(adapterBookHistory);
        }
        adapterBookHistory.notifyDataSetChanged();
        page++;
        if (isLoadMore) {
            listHistory.loadMoreComplete();
            isLoadMore = false;
        }
    }

    @Override
    public void getHistoryFailed() {
        AppControl.getInstance().showToast("获取历史借阅失败");
        if (isLoadMore) {
            listHistory.loadMoreComplete();
            isLoadMore = false;
        }
    }

    @Override
    public void onLoadingMore() {
        if (!isLoadMore) {
            isLoadMore = true;
            AppControl.getInstance().getNetRequest().getBookHistory(session, page, this);
        }
    }
}
