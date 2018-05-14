package jiyang.cdu.kits.ui.fragment.platform;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jiyang.cdu.kits.R;
import jiyang.cdu.kits.databinding.FragmentPlatformBinding;
import jiyang.cdu.kits.ui.common.BaseFragment;

public class PlatformFragment extends BaseFragment {
    public static final String TAG = "PLATFORM_FRAGMENT";

    public static PlatformFragment newInstance() {
        return new PlatformFragment();
    }

    @Override
    public String getTitle() {
        return "平台";
    }


    private FragmentPlatformBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_platform, container, false);
        return binding.getRoot();
    }
}
