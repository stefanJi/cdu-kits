package cn.youcute.library.bookFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
    private String session;
    private AdapterBookHistory adapterBookHistory;
    int failedCount = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.bookhistory_frag, container, false);
            session = getArguments().getString(NetRequest.PHP_SESSION_ID);
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
        AppControl.getInstance().getNetRequest().getBookHistory(session, this);
    }


    @Override
    public void getHistorySuccess(List<History> list) {
        if (adapterBookHistory == null) {
            adapterBookHistory = new AdapterBookHistory(list, getActivity());
            listHistory.setAdapter(adapterBookHistory);
        }
        adapterBookHistory.setHistoryList(list);
        adapterBookHistory.notifyDataSetChanged();
    }

    @Override
    public void getHistoryFailed() {
        AppControl.getInstance().showToast("获取历史借阅失败");
        failedCount++;
        if (failedCount <= 3)
            //获取历史借阅
            AppControl.getInstance().getNetRequest().getBookHistory(session, this);
    }

}
