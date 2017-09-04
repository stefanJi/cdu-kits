package io.gitHub.JiYang.library.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.gitHub.JiYang.library.AppControl;
import io.gitHub.JiYang.library.R;
import io.gitHub.JiYang.library.util.NetRequest;

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
        AppControl.getInstance().getNetRequest().getBookAccount(this);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppControl.getInstance().getNetRequest().getBookAccount(BookAccountFragment.this);
            }
        });
    }

    @Override
    public void getBookAccountSuccess(String info) {
        if (info != null) {
            textView.setText(info);
        }
    }

    @Override
    public void getBookAccountFailed() {
        textView.setText("获取失败,请重试");
    }
}