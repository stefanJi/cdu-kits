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

import java.util.List;

import cn.youcute.library.AppControl;
import cn.youcute.library.R;
import cn.youcute.library.adapter.AdapterBookList;
import cn.youcute.library.bean.Book;
import cn.youcute.library.util.NetRequest;

/**
 * Created by jy on 2016/9/23.
 * 当前借阅
 */
public class BookListFragment extends Fragment implements NetRequest.GetBookListCallBack {
    private View rootView;
    private ListView listView;
    private TextView tvInfo;
    private AdapterBookList adapterBookList;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.booklist_frag, null);
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
        listView = (ListView) rootView.findViewById(R.id.list_book);
        tvInfo = (TextView) rootView.findViewById(R.id.tv_info);
        tvInfo.setVisibility(View.INVISIBLE);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress);
        AppControl.getInstance().getNetRequest().getBookList(this);
    }

    @Override
    public void getBookListSuccess(List<Book> list) {
        progressBar.setVisibility(View.INVISIBLE);
        if (list.size() == 0) {
            tvInfo.setVisibility(View.VISIBLE);
        } else {
            tvInfo.setVisibility(View.INVISIBLE);
        }
        if (adapterBookList == null) {
            adapterBookList = new AdapterBookList(getActivity(), list);
            listView.setAdapter(adapterBookList);
        }
        adapterBookList.notifyDataSetChanged();
    }

    @Override
    public void getBookListFailed(String info) {
        tvInfo.setText(info);
        progressBar.setVisibility(View.INVISIBLE);
    }

}
