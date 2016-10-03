package cn.youcute.library.activities.menuFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.youcute.library.AppControl;
import cn.youcute.library.R;
import cn.youcute.library.activities.MainActivity;
import cn.youcute.library.util.SpUtil;

/**
 * Created by jy on 2016/9/30.
 * 设置
 */

public class FragSetting extends Fragment {
    private View rootView;
    private SpUtil spUtil;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_setting, container, false);
        initView();
        return rootView;
    }

    private void initView() {
        RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);
        spUtil = AppControl.getInstance().getSpUtil();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.menu_announce:
                        spUtil.setFragAction(MainActivity.ANNOUNCE_ACTION);
                        break;
                    case R.id.menu_library:
                        spUtil.setFragAction(MainActivity.LIBRARY_ACTION);
                        break;
                    case R.id.menu_education:
                        spUtil.setFragAction(MainActivity.EDUCATION_ACTION);
                        break;
                    case R.id.menu_net_education:
                        spUtil.setFragAction(MainActivity.NET_EDUCATION_ACTION);
                        break;
                }
                new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("设置成功")
                        .setConfirmText("确定")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                
                            }
                        })
                        .show();
            }
        });
    }
}
