package cn.youcute.library.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.youcute.library.AppControl;
import cn.youcute.library.R;
import cn.youcute.library.adapter.AdapterBookHistory;
import cn.youcute.library.bean.History;
import cn.youcute.library.util.NetRequest;

/**
 * Created by jy on 2016/9/23.
 * 历史借阅视图
 */
public class BookHistoryFragment extends Fragment implements NetRequest.GetBookHistoryCallBack {
    private View rootView;
    private ListView listHistory;
    private AdapterBookHistory adapterBookHistory;
    private List<History> historyList;
    private int page = 1;
    private ProgressBar progressBarFooter;
    private TextView tvLoadInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
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
        listHistory = (ListView) rootView.findViewById(R.id.list_book_history);
        //获取历史借阅
        AppControl.getInstance().getNetRequest().getBookHistory(page, this);
    }

    @Override
    public void getHistorySuccess(List<History> list) {
        if (progressBarFooter != null)
            progressBarFooter.setVisibility(View.INVISIBLE);
        if (list.size() == 0) {
            tvLoadInfo.setText("无更多");
            return;
        }
        historyList.addAll(list);
        if (adapterBookHistory == null) {
            adapterBookHistory = new AdapterBookHistory(historyList, getActivity());
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_list_footer, null, false);
            progressBarFooter = (ProgressBar) view.findViewById(R.id.progress_footer);
            tvLoadInfo = (TextView) view.findViewById(R.id.tv_load_more);
            progressBarFooter.setVisibility(View.INVISIBLE);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressBarFooter.setVisibility(View.VISIBLE);
                    page++;
                    AppControl.getInstance().getNetRequest().getBookHistory(page, BookHistoryFragment.this);
                }
            });
            listHistory.addFooterView(view);
            listHistory.setAdapter(adapterBookHistory);
        }
        adapterBookHistory.notifyDataSetChanged();
    }

    @Override
    public void getHistoryFailed() {
        AppControl.getInstance().showToast("获取失败,请重试");
    }

}
