package cn.youcute.library.activities.menuFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import cn.youcute.library.AppControl;
import cn.youcute.library.activities.ContentActivity;
import cn.youcute.library.R;
import cn.youcute.library.adapter.AdapterAnnounce;
import cn.youcute.library.bean.Announce;
import cn.youcute.library.diyView.LoadMoreListView;
import cn.youcute.library.util.NetRequest;

/**
 * Created by jy on 2016/9/25.
 * 公告信息
 */

public class FragAnnounce extends Fragment implements NetRequest.GetAnnounceCallBack, LoadMoreListView.OnRefreshListener, SwipeRefreshLayout.OnRefreshListener {
    private View rootView;
    private SwipeRefreshLayout swipe;
    private LoadMoreListView list;
    private List<Announce> announceList;
    private AdapterAnnounce adapter;
    private int page = 1;
    private boolean isLoading = false;
    private boolean isRefresh = false;
    private boolean isNoMore = false;
    private boolean isFirstLoad = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_announce, container, false);
            initView();
        }
        return rootView;
    }

    private void initView() {
        swipe = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe);
        swipe.setOnRefreshListener(this);
        list = (LoadMoreListView) rootView.findViewById(R.id.list_announce);
        list.setOnRefreshListener(this);
        announceList = new ArrayList<>();
        adapter = new AdapterAnnounce(this.announceList, getActivity());
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ContentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", announceList.get(position).title);
                bundle.putString("url", announceList.get(position).url);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });
        AppControl.getInstance().getNetRequest().getAnnounce(page, this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != rootView) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
    }

    @Override
    public void getAnnounceSuccess(List<Announce> announces) {
        if (isFirstLoad) {
            isFirstLoad = false;
            AppControl.getInstance().showToast("取到" + String.valueOf(announces.size()) + "+公告");
            this.announceList.addAll(announces);
            adapter.notifyDataSetChanged();
            return;
        }
        if (isLoading) {
            list.loadMoreComplete();
            isLoading = false;
            if (announces.size() == 0) {
                AppControl.getInstance().showToast("没有更多了");
                isNoMore = true;
            } else {
                AppControl.getInstance().showToast("取到" + String.valueOf(announces.size()) + "公告");
                this.announceList.addAll(announces);
                adapter.notifyDataSetChanged();
            }
        }
        if (isRefresh) {
            isRefresh = false;
            swipe.setRefreshing(false);
            if (!this.announceList.containsAll(announces)) {
                AppControl.getInstance().showToast("获取到" + String.valueOf(announces.size()) + "条公告");
                this.announceList.addAll(0, announces);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void getAnnounceFailed() {
        if (isLoading) {
            isLoading = false;
            list.loadMoreComplete();
            AppControl.getInstance().showToast("获取失败，请重试");
        }
        if (isRefresh) {
            isRefresh = false;
            swipe.setRefreshing(false);
            AppControl.getInstance().showToast("刷新失败，请重试");
        }
    }

    @Override
    public void onLoadingMore() {
        if (!isLoading && !isNoMore) {
            page++;
            AppControl.getInstance().getNetRequest().getAnnounce(page, this);
            Log.d("TAG", "page:" + String.valueOf(page));
            isLoading = true;
        }
    }

    @Override
    public void onRefresh() {
        if (!isRefresh) {
            isRefresh = true;
            swipe.setRefreshing(true);
            AppControl.getInstance().getNetRequest().getAnnounce(1, this);
        }
    }
}
