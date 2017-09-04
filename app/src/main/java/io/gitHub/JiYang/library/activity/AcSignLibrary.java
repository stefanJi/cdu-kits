package io.gitHub.JiYang.library.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import io.gitHub.JiYang.library.AppControl;
import io.gitHub.JiYang.library.R;
import io.gitHub.JiYang.library.bean.User;
import io.gitHub.JiYang.library.util.NetRequest;
import io.gitHub.JiYang.library.util.ToastUtil;

/**
 * Created by jy on 2016/11/6.
 * 登录图书馆
 */

public class AcSignLibrary extends AppCompatActivity implements NetRequest.SignCallBack {
    private EditText etAccount, etPass;
    private AlertDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sign_library);
        initView();
    }

    private void signIn() {
        String account = etAccount.getText().toString();
        String pass = etPass.getText().toString();
        if (account.equals("") || pass.equals("")) {
            return;
        }
        dialog.show();
        etPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        AppControl.getInstance().getNetRequest().sign(account, pass, this);
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
        etPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    signIn();
                }
                return false;
            }
        });
        etPass.setText(user.password);
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
                setResult(-1);
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

    @Override
    public void signSuccess(String session) {
        dialog.dismiss();
        AppControl.getInstance().sessionLibrary = session;
        setResult(2);
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    @Override
    public void signFailed(String info) {
        dialog.dismiss();
        ToastUtil.showToast("登录失败" + info);
        etPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
    }
}
