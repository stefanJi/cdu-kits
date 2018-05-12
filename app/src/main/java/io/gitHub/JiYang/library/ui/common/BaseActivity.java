package io.gitHub.JiYang.library.ui.common;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import io.gitHub.JiYang.library.AppControl;
import io.gitHub.JiYang.library.util.SpUtil;

public class BaseActivity extends AppCompatActivity {
    protected void isFirstCome(View container, String tip) {
        final String tag = getClass().getName();
        final SpUtil sp = AppControl.getInstance().getSpUtil();
        boolean isFirstCome = sp.getBool(tag, true);
        if (isFirstCome) {
            Snackbar.make(container, tip, Snackbar.LENGTH_INDEFINITE)
                    .setAction("知道了", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sp.setBool(tag, false);
                        }
                    })
                    .show();
        }
    }
}
