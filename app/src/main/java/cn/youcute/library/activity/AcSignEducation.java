package cn.youcute.library.activity;

import android.os.Bundle;
import android.security.keystore.KeyInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;

import cn.youcute.library.AppControl;
import cn.youcute.library.R;
import cn.youcute.library.bean.User;
import cn.youcute.library.util.ToastUtil;

/**
 * Created by jy on 2016/11/6.
 */

public class AcSignEducation extends AppCompatActivity {
    private EditText etAccount, etPass, etKey;
    private ImageView ivKey;
    private AlertDialog dialog;
    public static final String SERVER_1 = "";
    public static final int SERVER_2 = 2;
    public static final int SERVER_3 = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sign_net);
        initView();
    }

    private void signIn() {
        String account = etAccount.getText().toString();
        String pass = etPass.getText().toString();
        String code = etKey.getText().toString();
        if (account.equals("") || pass.equals("") || code.equals("")) {
            ToastUtil.showToast("输入不能为空");
            return;
        }
        OkHttpUtils
                .post()

                .url("");
        dialog.show();
    }

    private void initView() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_back);
        }
        findViewById(R.id.btn_sign).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        User user = AppControl.getInstance().getSpUtil().getUser();
        etAccount = (EditText) findViewById(R.id.et_account);
        etAccount.setSingleLine();
        etAccount.setText(user.account);
        etPass = (EditText) findViewById(R.id.et_password);
        etPass.setText(AppControl.getInstance().getSpUtil().getNetPass());
        etKey = (EditText) findViewById(R.id.et_key);
        etKey.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    signIn();
                    return true;
                }
                return false;
            }
        });
        ivKey = (ImageView) findViewById(R.id.iv_code);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_progess, null);
        builder.setView(view);
        builder.setTitle("登录中");
        builder.setCancelable(false);
        dialog = builder.create();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                finish();
                overridePendingTransition(R.anim.right_out, R.anim.left_in);
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(-1);
                finish();
                overridePendingTransition(R.anim.right_out, R.anim.left_in);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
