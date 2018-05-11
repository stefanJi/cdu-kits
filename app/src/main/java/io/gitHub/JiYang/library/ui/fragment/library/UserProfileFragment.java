package io.gitHub.JiYang.library.ui.fragment.library;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.gitHub.JiYang.library.R;
import io.gitHub.JiYang.library.model.enty.LibraryUserInfo;
import io.gitHub.JiYang.library.ui.common.BaseFragment;

public class UserProfileFragment extends BaseFragment {
    @Override
    public String getTitle() {
        return "个人";
    }

    public static UserProfileFragment instance(LibraryUserInfo userInfo) {
        UserProfileFragment userProfileFragment = new UserProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("userInfo", userInfo);
        userProfileFragment.setArguments(bundle);
        return userProfileFragment;
    }

    private TextView name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library_profile, container, false);
        LibraryUserInfo userInfo = (LibraryUserInfo) getArguments().getSerializable("userInfo");
        if (userInfo != null) {
            init(view, userInfo);
        }
        return view;
    }

    private void init(View view, LibraryUserInfo userInfo) {
        name = view.findViewById(R.id.name);
        name.setText(userInfo.name);
    }
}
