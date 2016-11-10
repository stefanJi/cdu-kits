package cn.youcute.library.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.youcute.library.AppControl;
import cn.youcute.library.R;
import cn.youcute.library.adapter.AdapterBookFine;
import cn.youcute.library.bean.BookFine;
import cn.youcute.library.util.NetRequest;

/**
 * Created by jy on 2016/9/23.
 * 违章缴款
 */
public class BookFineFragment extends Fragment implements NetRequest.GetBookFineCallBack {
    private View rootView;
    private ListView listView;
    private AdapterBookFine adapterBookFine;
    private List<BookFine> bookFines;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.bookfine_frag, null);
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
        AppControl.getInstance().getNetRequest().getBookFine(this);
        listView = (ListView) rootView.findViewById(R.id.list_book_fine);
    }

    @Override
    public void getBookFineSuccess(List<BookFine> books) {
        if (adapterBookFine == null) {
            adapterBookFine = new AdapterBookFine(getActivity(), this.bookFines);
            listView.setAdapter(adapterBookFine);
        }
        if (bookFines.size() >= books.size()) {
            if (bookFines.containsAll(books)) {
                return;
            } else
                bookFines.addAll(books);
        } else {
            bookFines.addAll(books);
        }
        adapterBookFine.notifyDataSetChanged();
    }

    @Override
    public void getBookFineFailed() {

    }


}
