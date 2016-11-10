package cn.youcute.library.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import cn.youcute.library.AppControl;
import cn.youcute.library.R;
import cn.youcute.library.util.NetRequest;
import cn.youcute.library.util.ToastUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NetRequest.FeedBackCallBack {
    private AlertDialog dialogProgress, dialogCount;
    private LinearLayout changeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        if (AppControl.getInstance().getSpUtil().getIsFirst()) {
            Intent intent = new Intent(MainActivity.this, AcGuide.class);
            startActivity(intent);
        }
    }

    private void initView() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_good);
        }
        findViewById(R.id.library).setOnClickListener(this);
        findViewById(R.id.notice).setOnClickListener(this);
        findViewById(R.id.education).setOnClickListener(this);
        findViewById(R.id.net).setOnClickListener(this);
        findViewById(R.id.search).setOnClickListener(this);
        changeCount = (LinearLayout) findViewById(R.id.change);
        changeCount.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (AppControl.getInstance().getSpUtil().getIsSignNet() || AppControl.getInstance().getSpUtil().getIsSign()) {
            changeCount.setVisibility(View.VISIBLE);
        } else {
            changeCount.setVisibility(View.INVISIBLE);
        }
    }

    private AlertDialog dialogFeedBack;
    private View viewFeed;
    private EditText etContact;
    private EditText etFeed;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                new AlertDialog.Builder(this)
                        .setTitle("欢迎使用橙子助手")
                        .setMessage("查询图书借阅信息 图书续借 搜索图书\n浏览学校最新通知公告\n查询个人教务系统\n就用橙子助手\n\n作者:阳仔 @新浪微博:道_阳")
                        .setNeutralButton("帮助", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(MainActivity.this, AcGuide.class);
                                startActivity(intent);
                            }
                        })
                        .setPositiveButton("反馈", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                if (dialogFeedBack == null) {
                                    viewFeed = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_feed_back, null, false);
                                    etFeed = (EditText) viewFeed.findViewById(R.id.et_feed_back);
                                    etContact = (EditText) viewFeed.findViewById(R.id.et_feed_contact);
                                    dialogFeedBack = new AlertDialog.Builder(MainActivity.this).setView(viewFeed)
                                            .setPositiveButton("发送反馈", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    String content = etFeed.getText().toString();
                                                    String contact = etContact.getText().toString();
                                                    if (content.equals("")) {
                                                        ToastUtil.showToast("反馈内容不能为空");
                                                        return;
                                                    }
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                                    View viewProgress = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_progess, null);
                                                    builder.setView(viewProgress);
                                                    builder.setTitle("反馈中");
                                                    builder.setCancelable(false);
                                                    dialogProgress = builder.create();
                                                    AppControl.getInstance().getNetRequest().feedBack(content, contact, MainActivity.this);
                                                }
                                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                }
                                            }).create();
                                }
                                dialogFeedBack.show();
                            }
                        })
                        .create().show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.library:
                intent = new Intent(MainActivity.this, AcLibrary.class);
                startActivity(intent);
                break;
            case R.id.notice:
                intent = new Intent(MainActivity.this, AcNotice.class);
                startActivity(intent);
                break;
            case R.id.education:
                intent = new Intent(MainActivity.this, AcEducation.class);
                startActivity(intent);
                break;
            case R.id.net:
                intent = new Intent(MainActivity.this, AcNet.class);
                startActivity(intent);
                break;
            case R.id.search:
                intent = new Intent(MainActivity.this, AcSearch.class);
                startActivity(intent);
                break;
            case R.id.change:
                if (dialogCount == null) {
                    dialogCount = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("确定切换账户?")
                            .setMessage("切换账户会退出当前已登录账户")
                            .setPositiveButton("确定切换", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    AppControl.getInstance().getSpUtil().setIsSignNet(false);
                                    AppControl.getInstance().getSpUtil().setIsSign(false);
                                    ToastUtil.showToast("当前账户已退出登录");
                                    changeCount.setVisibility(View.INVISIBLE);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogCount.dismiss();
                                }
                            })
                            .create();
                }
                dialogCount.show();
                break;
        }
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @Override
    public void feedBackSuccess() {
        dialogProgress.dismiss();
        ToastUtil.showToast("反馈成功，谢谢反馈");
        etFeed.setText("");
        etContact.setText("");
    }

    @Override
    public void feedBackError(String error) {
        dialogProgress.dismiss();
        ToastUtil.showToast("反馈失败" + error);
    }
}
