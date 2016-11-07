package cn.youcute.library.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.youcute.library.AppControl;
import cn.youcute.library.util.NetRequest;

/**
 * Created by jy on 2016/11/7.
 */

public class AcEducation extends AcBase implements NetRequest.GetEducationInfoCall {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppControl.getInstance().getNetRequest().getEducationInfo(this);
    }

    @Override
    public void getOk() {

    }

    @Override
    public void getFailed(String info) {

    }
}
