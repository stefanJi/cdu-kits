package cn.youcute.library.bookFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
    private String session;
    private AdapterBookFine adapterBookFine;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.bookfine_frag, null);
            session = getArguments().getString(NetRequest.PHP_SESSION_ID);
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
        listView = (ListView) rootView.findViewById(R.id.list_book_fine);
        AppControl.getInstance().getNetRequest().getBookFine(session, this);
    }

    @Override
    public void getBookFineSuccess(List<BookFine> bookFines) {
        if (adapterBookFine == null) {
            adapterBookFine = new AdapterBookFine(getActivity(), bookFines);
            listView.setAdapter(adapterBookFine);
        }
        adapterBookFine.setBookFines(bookFines);
        adapterBookFine.notifyDataSetChanged();
    }

    @Override
    public void getBookFineFailed() {

    }
}
