package cn.youcute.library.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.youcute.library.AppControl;
import cn.youcute.library.R;
import cn.youcute.library.bean.User;
import cn.youcute.library.util.NetRequest;

/**
 * Created by jy on 2016/11/6.
 */

public class AcSignEducation extends AppCompatActivity implements NetRequest.SignJiaoWuCallback, NetRequest.GetCheckCodeCallBack {
    private EditText etAccount, etPass, etKey;
    private ImageView ivCode;
    private AlertDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sign_education);
        initView();
    }

    private void signIn() {
        String account = etAccount.getText().toString();
        String pass = etPass.getText().toString();
        String key = etKey.getText().toString();
        if (account.equals("") || pass.equals("") || key.equals("")) {
            return;
        }
        dialog.show();
        AppControl.getInstance().getNetRequest().signJiaoWu(account, pass, key, this);
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
        etKey = (EditText) findViewById(R.id.et_key);
        etKey.setSingleLine();
        etKey.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == keyEvent.ACTION_DOWN) {
                    signIn();
                }
                return false;
            }
        });
        ivCode = (ImageView) findViewById(R.id.iv_code);
        ivCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppControl.getInstance().getNetRequest().reLoadCode(AcSignEducation.this);
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_progess, null);
        builder.setView(view);
        builder.setTitle("登录中");
        builder.setCancelable(false);
        dialog = builder.create();
        AppControl.getInstance().getNetRequest().getCheckCode(null, this);
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
            case R.id.action_more:
                Intent intentMore = new Intent();
                intentMore.setClass(AcSignEducation.this, AcMore.class);
                startActivity(intentMore);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void signSuccess() {
        AppControl.getInstance().showToast("登录成功");
        dialog.dismiss();
        Intent intent = new Intent(AcSignEducation.this, AcEducation.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    @Override
    public void signFailed(String info) {
        dialog.dismiss();
        AppControl.getInstance().showToast(info);
    }

    @Override
    public void getCodeSuccess(Bitmap bitmap, String session) {
        ivCode.setImageBitmap(bitmap);
        AppControl.getInstance().sessionEducation = session;
    }

    @Override
    public void getCodeFailed() {
        AppControl.getInstance().showToast("获取验证码失败");
        ivCode.setImageResource(R.mipmap.ic_refresh);
    }
}
