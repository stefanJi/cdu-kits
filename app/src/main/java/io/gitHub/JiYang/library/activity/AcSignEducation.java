package io.gitHub.JiYang.library.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import io.gitHub.JiYang.library.R;
import io.gitHub.JiYang.library.util.ToastUtil;

/**
 * Created by jy on 2016/11/9.
 * sign in
 */

public class AcSignEducation extends AcBase implements View.OnClickListener {
    private EditText etPass, etAccount, etKey;
    private ImageView ivCode;
    private AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_education);
        etAccount = (EditText) findViewById(R.id.et_account);
        etPass = (EditText) findViewById(R.id.et_password);
        etKey = (EditText) findViewById(R.id.et_key);
        ivCode = (ImageView) findViewById(R.id.iv_code);
        Button btnSignIn = (Button) findViewById(R.id.btn_sign);
        btnSignIn.setOnClickListener(this);
        ivCode.setOnClickListener(this);
        etAccount.setSingleLine();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_progess, null);
        builder.setView(view);
        builder.setTitle("登录中");
        builder.setCancelable(false);
        dialog = builder.create();
        getCheck();
    }

    private void signIn() {
        etPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        final String ac = etAccount.getText().toString();
        String pa = etPass.getText().toString();
        String k = etKey.getText().toString();
        if (ac.equals("") || pa.equals("") || k.equals("")) {
            ToastUtil.showToast("输入不能有空");
            return;
        }
        dialog.show();
//        OkHttpUtils
//                .post()
//                .url("http://202.115.80.153/default2.aspx")
//                .addHeader("Host", "202.115.80.153")
//                .addHeader("Origin", "http://202.115.80.153")
//                .addHeader("Referer", "http://202.115.80.153/")
//                .addParams("__VIEWSTATE", "dDwyODE2NTM0OTg7Oz7QBx05W486R++11e1KrLTLz5ET2Q==")
//                .addParams("txtUserName", ac)
//                .tag(this)
//                .addParams("TextBox2", pa)
//                .addParams("txtSecretCode", k)
//                .addParams("RadioButtonList1", "%D1%A7%C9%FA")
//                .addParams("Button1", "")
//                .addParams("lbLanguage", "")
//                .addParams("hidPdrs", "")
//                .addParams("hidsc", "")
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e) {
//                        ToastUtil.showToast("错误响应:" + e.getMessage());
//                        etPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//                        call.cancel();
//                        dialog.dismiss();
//                    }
//
//                    @Override
//                    public void onResponse(String response) {
//                        dialog.dismiss();
//                        //请求成功，response就是得到的html文件（网页源代码）
//                        if (response.contains("验证码不正确")) {
//                            //如果源代码包含“验证码不正确”
//                            ToastUtil.showToast("验证码不正确");
//                            getCheck();
//                        } else if (response.contains("密码错误")) {
//                            //如果源代码包含“密码错误”
//                            ToastUtil.showToast("密码错误");
//                            getCheck();
//                            etPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//                        } else if (response.contains("用户名不存在")) {
//                            //如果源代码包含“用户名不存在”
//                            ToastUtil.showToast("用户名不存在");
//                            getCheck();
//                        } else {
//                            //登录成功
//                            ToastUtil.showToast("登录成功");
//                            Document document = Jsoup.parse(response);
//                            String name = document.select("div.info").select("ul").first().select("li").first().select("span").last().text().replace("同学", "");
//                            AppControl.getInstance().getSpUtil().saveName(name);
//                            AppControl.getInstance().getSpUtil().saveAccount(ac);
//                            setResult(1);
//                            finish();
//                            overridePendingTransition(R.anim.left_in, R.anim.right_out);
//                        }
//                    }
//                });
    }

    private void getCheck() {
//        OkHttpUtils
//                .get()
//                .url("http://202.115.80.153/CheckCode.aspx")
//                .addHeader("Host", "202.115.80.153")
//                .addHeader("Origin", "http://202.115.80.153")
//                .addHeader("Referer", "http://202.115.80.153/default2.aspx")
//                .build().execute(new BitmapCallback() {
//            @Override
//            public void onError(Call call, Exception e) {
//                ToastUtil.showToast("获取验证码,错误响应:" + e.getMessage() + "{请点击刷新}");
//            }
//
//            @Override
//            public void onResponse(Bitmap response) {
//                ivCode.setImageBitmap(response);
//            }
//        });
    }

    @Override
    protected void onDestroy() {
//        OkHttpUtils.getInstance().cancelTag(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_code:
                getCheck();
                break;
            case R.id.btn_sign:
                signIn();
                break;
        }
    }
}
