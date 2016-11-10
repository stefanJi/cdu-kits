package cn.youcute.library.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;

import cn.youcute.library.AppControl;
import cn.youcute.library.activity.fragment.GuideFrag;
import cn.youcute.library.activity.fragment.GuideFrag2;
import cn.youcute.library.activity.fragment.GuideFrag3;
import cn.youcute.library.activity.fragment.GuideFrag4;

/**
 * Created by jy on 2016/11/9.
 * 引导页
 */

public class AcGuide extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GuideFrag guideFrag1 = new GuideFrag();
        GuideFrag2 guideFrag2 = new GuideFrag2();
        GuideFrag3 guideFrag3 = new GuideFrag3();
        GuideFrag4 guideFrag4 = new GuideFrag4();
        addSlide(guideFrag1);
        addSlide(guideFrag2);
        addSlide(guideFrag3);
        addSlide(guideFrag4);
        setSkipText("");
        setDoneText("进入");
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }

    @Override
    protected void onDestroy() {
        AppControl.getInstance().getSpUtil().setFirst(false);
        super.onDestroy();
    }
}
