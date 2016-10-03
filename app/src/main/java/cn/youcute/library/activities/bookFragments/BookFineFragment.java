package cn.youcute.library.activities.bookFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.youcute.library.AppControl;
import cn.youcute.library.R;
import cn.youcute.library.adapter.AdapterBookFine;
import cn.youcute.library.bean.BookFine;
import cn.youcute.library.diyView.LoadMoreListView;
import cn.youcute.library.util.NetRequest;

/**
 * Created by jy on 2016/9/23.
 * 违章缴款
 */
public class BookFineFragment extends Fragment implements NetRequest.GetBookFineCallBack, SwipeRefreshLayout.OnRefreshListener, LoadMoreListView.OnRefreshListener {
    private View rootView;
    private SwipeRefreshLayout swipeRefreshLayout;  //下拉刷新
    private boolean isSwipeRefresh = false;
    private LoadMoreListView listView;
    private boolean isLoadMore = false;
    private String session;
    private AdapterBookFine adapterBookFine;
    private List<BookFine> bookFines;
    private List<BookFine> oldFines;    //上一次加载更多的备份

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.bookfine_frag, null);
            session = getArguments().getString(NetRequest.PHP_SESSION_ID);
            bookFines = new ArrayList<>();
            initView();
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

    private void initView() {
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, android.R.color.holo_orange_dark, R.color.primary);
        swipeRefreshLayout.setOnRefreshListener(this);
        listView = (LoadMoreListView) rootView.findViewById(R.id.list_book_fine);
        listView.setOnRefreshListener(this);
        AppControl.getInstance().getNetRequest().getBookFine(session, this);
        swipeRefreshLayout.setRefreshing(true);
        isSwipeRefresh = true;
    }

    @Override
    public void getBookFineSuccess(List<BookFine> books) {
        if (adapterBookFine == null) {
            adapterBookFine = new AdapterBookFine(getActivity(), this.bookFines);
            listView.setAdapter(adapterBookFine);
        }
        if (isSwipeRefresh) {
            isSwipeRefresh = false;
            swipeRefreshLayout.setRefreshing(false);
        }
        if (isLoadMore) {
            isLoadMore = false;
            listView.loadMoreComplete();
        }
        if (bookFines.size() >= books.size()) {
            if (bookFines.containsAll(books)) {
                //加载完毕
                listView.noMoreLoad();
            } else
                bookFines.addAll(books);
        } else {
            bookFines.addAll(books);
        }
        adapterBookFine.notifyDataSetChanged();
    }

    @Override
    public void getBookFineFailed() {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        if (!isSwipeRefresh) {
            isSwipeRefresh = true;
            AppControl.getInstance().getNetRequest().getBookFine(session, this);
        }
    }

    @Override
    public void onLoadingMore() {
        if (!isLoadMore) {
            isLoadMore = true;
            AppControl.getInstance().getNetRequest().getBookFine(session, this);
        }
    }
}
