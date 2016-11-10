package cn.youcute.library.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.youcute.library.R;

/**
 * Created by jy on 2016/11/9.
 */

public class GuideFrag2 extends Fragment {
    private View view;
    private ImageView ivBack;
    private ImageView ivIcon;
    private TextView tvTitle, tvContent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.layout_guide_fragment, container, false);
            initView();
        }
        return view;
    }

    private void initView() {
        ivBack = (ImageView) view.findViewById(R.id.iv_back);
        ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvContent = (TextView) view.findViewById(R.id.tv_content);
        ivBack.setImageResource(R.mipmap.color_notice);
        ivIcon.setImageResource(R.mipmap.img_notice);
        tvTitle.setText("通知公告");
        tvContent.setText("便捷浏览通知公告,停水、停电、重要通知，一手掌握");
    }

}
