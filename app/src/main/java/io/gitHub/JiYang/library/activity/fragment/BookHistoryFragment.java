package io.gitHub.JiYang.library.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.gitHub.JiYang.library.AppControl;
import io.gitHub.JiYang.library.R;
import io.gitHub.JiYang.library.activity.AcWebViewSearch;
import io.gitHub.JiYang.library.adapter.AdapterBookHistory;
import io.gitHub.JiYang.library.bean.History;
import io.gitHub.JiYang.library.util.NetRequest;
import io.gitHub.JiYang.library.util.ToastUtil;

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
            listHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String title = BookHistoryFragment.this.historyList.get(i).name;
                    String url = BookHistoryFragment.this.historyList.get(i).url;
                    Intent intent = new Intent(getActivity(), AcWebViewSearch.class);
                    intent.putExtra("title", title);
                    intent.putExtra("url", url);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            });
        }
        adapterBookHistory.notifyDataSetChanged();
    }

    @Override
    public void getHistoryFailed() {
        ToastUtil.showToast("获取失败,请重试");
    }

}
