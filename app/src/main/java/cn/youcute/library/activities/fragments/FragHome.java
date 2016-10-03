package cn.youcute.library.activities.fragments;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.youcute.library.R;
import cn.youcute.library.activities.MenuActivity;
import cn.youcute.library.activities.SearchActivity;

/**
 * Created by jy on 2016/10/2.
 * 主页
 */

public class FragHome extends Fragment implements View.OnClickListener {
    private View view;
    private ImageButton buttonLeft, buttonRight;
    private PopupWindow window;
    private View popView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.frag_home, container, false);
            initView();
        }
        return view;
    }

    private void initView() {
        buttonLeft = (ImageButton) view.findViewById(R.id.imgBtn_left);
        buttonRight = (ImageButton) view.findViewById(R.id.imgBtn_right);

        buttonLeft.setOnClickListener(this);
        buttonRight.setOnClickListener(this);

        popView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_home_drop, null, true);
        window = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        window.setAnimationStyle(android.R.anim.fade_in);
        window.setTouchable(true);
        window.setBackgroundDrawable(new ColorDrawable(0x00000000));
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                buttonLeft.setSelected(false);
            }
        });
        TextView announce = (TextView) popView.findViewById(R.id.pop_announce);
        TextView education = (TextView) popView.findViewById(R.id.pop_education);
        TextView netEducation = (TextView) popView.findViewById(R.id.pop_net_education);
        TextView feedBack = (TextView) popView.findViewById(R.id.pop_feedback);
        TextView help = (TextView) popView.findViewById(R.id.pop_help);
        TextView about = (TextView) popView.findViewById(R.id.pop_about);
        class OnClickPop implements View.OnClickListener {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                switch (v.getId()) {
                    case R.id.pop_announce:
                        intent.putExtra("action", MenuActivity.ANNOUNCE);
                        break;
                    case R.id.pop_education:
                        intent.putExtra("action", MenuActivity.EDUCATION);
                        break;
                    case R.id.pop_net_education:
                        intent.putExtra("action", MenuActivity.NET_EDUCATION);
                        break;
                    case R.id.pop_feedback:
                        intent.putExtra("action", MenuActivity.FEEDBACK);
                        break;
                    case R.id.pop_help:
                        intent.putExtra("action", MenuActivity.HELP);
                        break;
                    case R.id.pop_about:
                        intent.putExtra("action", MenuActivity.ABOUT);
                        break;
                }
                startActivity(intent);
            }
        }

        OnClickPop clickPop = new OnClickPop();
        announce.setOnClickListener(clickPop);
        education.setOnClickListener(clickPop);
        netEducation.setOnClickListener(clickPop);
        feedBack.setOnClickListener(clickPop);
        help.setOnClickListener(clickPop);
        about.setOnClickListener(clickPop);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != view) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_left:
                if (window.isShowing()) {
                    window.dismiss();
                    return;
                }
                showDropMenu();
                break;
            case R.id.imgBtn_right:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void showDropMenu() {
        buttonLeft.setSelected(true);
        window.showAsDropDown(buttonLeft, 20, 20);
    }

}
