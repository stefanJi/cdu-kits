package cn.youcute.library.bookFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.youcute.library.AppControl;
import cn.youcute.library.R;
import cn.youcute.library.util.NetRequest;

/**
 * Created by jy on 2016/9/23.
 * 账目清单
 */
public class BookAccountFragment extends Fragment implements NetRequest.GetBookAccountCallBack {
    private View rootView;
    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.bookaccount_frag, null);
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
        textView = (TextView) rootView.findViewById(R.id.tv_book_account);
        String session = getArguments().getString(NetRequest.PHP_SESSION_ID);
        AppControl.getInstance().getNetRequest().getBookAccount(session, this);
    }

    @Override
    public void getBookAccountSuccess(String info) {
        if (!info.equals("")) {
            textView.setText(info);
        }
    }

    @Override
    public void getBookAccountFailed() {

    }
}
