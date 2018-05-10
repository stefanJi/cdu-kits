package io.gitHub.JiYang.library.ui.fragment.platform;

import io.gitHub.JiYang.library.ui.fragment.BaseFragment;

public class PlatformFragment extends BaseFragment {
    public static final String TAG = "PLATFORM_FRAGMENT";

    public static PlatformFragment newInstance() {
        return new PlatformFragment();
    }

    @Override
    public String getTitle() {
        return "平台";
    }
}
