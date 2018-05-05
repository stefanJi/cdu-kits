package io.gitHub.JiYang.library.ui.fragment.library;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.gitHub.JiYang.library.R;
import io.gitHub.JiYang.library.ui.fragment.BaseFragment;

public class LibraryFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        init(view);
        return view;
    }

    private void init(View view) {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i("TAG2", "library ---> " + isVisibleToUser);
    }

    public static LibraryFragment newInstance() {
        return new LibraryFragment();
    }

    @Override
    public String getTitle() {
        return "图书馆";
    }
}
