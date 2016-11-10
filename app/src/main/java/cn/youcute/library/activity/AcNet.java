package cn.youcute.library.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import cn.youcute.library.AppControl;
import cn.youcute.library.R;
import cn.youcute.library.adapter.AdapterCourse;
import cn.youcute.library.bean.Course;
import cn.youcute.library.util.NetRequest;
import cn.youcute.library.util.SpUtil;
import cn.youcute.library.util.ToastUtil;
import okhttp3.Call;

/**
 * Created by jy on 2016/11/6.
 * 图书馆
 */

public class AcNet extends AcBase implements NetRequest.SignNetCall {
    private static final String GET_WORK = "http://kcxt.cdu.edu.cn/eol/welcomepage/course/index.jsp?lid=";
    private ListView listView;
    private List<Course> courseList;
    private AdapterCourse adapterCourse;
    private AlertDialog dialogContent;
    private ProgressBar progressBar;
    private TextView tvWork, tvNotice, tvUser;
    private Button button;
    private AlertDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_net);
        SpUtil spUtil = AppControl.getInstance().getSpUtil();
        if (!spUtil.getIsSignNet()) {
            Intent intent = new Intent(AcNet.this, AcSignNet.class);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        } else {
            String account = spUtil.getUser().account;
            String password = spUtil.getNetPass();
            showDialog();
            AppControl.getInstance().getNetRequest().signNet(account, password, this);
        }
        initView();
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.list_net);
        tvUser = (TextView) findViewById(R.id.tv_net_top);
        courseList = new ArrayList<>();
        adapterCourse = new AdapterCourse(courseList, this);
        listView.setAdapter(adapterCourse);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showDialogContent();
                getWork(courseList.get(i).lid);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 2) {
                getCourse();
                getUerInfo();
            } else if (resultCode == -1) {
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
            }
        }
    }

    /**
     * 后台登录成功
     */
    @Override
    public void signNetOk() {
        dialog.dismiss();
        getUerInfo();
        getCourse();
    }

    @Override
    public void signNetFailed(String info) {
        ToastUtil.showToast(info);
        dialog.dismiss();
        Intent intent = new Intent(AcNet.this, AcSignNet.class);
        startActivityForResult(intent, 1);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    private void showDialog() {
        if (dialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_progess, null);
            builder.setView(view);
            builder.setTitle("登录中");
            builder.setCancelable(false);
            dialog = builder.create();
        }
        dialog.show();
    }

    private void getUerInfo() {
        OkHttpUtils
                .get()
                .addHeader("Host", "kcxt.cdu.edu.cn")
                .addHeader("Referer", "kcxt.cdu.edu.cn/eol/homepage/common/index.jsp")
                .url("http://kcxt.cdu.edu.cn/eol/welcomepage/student/index.jsp")
                .build().connTimeOut(5000).execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                tvUser.setText("错误响应:" + e.getMessage());
            }

            @Override
            public void onResponse(String response) {
                Document document = Jsoup.parse(response);
                Elements elements = document.select("div.userinfobody").select("ul").first().select("li");
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < elements.size(); i++) {
                    stringBuilder.append(elements.get(i).text()).append("\n");
                }
                tvUser.setText(stringBuilder);
            }
        });
    }

    private void getCourse() {
        OkHttpUtils
                .get()
                .tag(this)
                .addHeader("Host", "kcxt.cdu.edu.cn")
                .addHeader("Referer", "kcxt.cdu.edu.cn/eol/welcomepage/student/index.jsp")
                .url("http://kcxt.cdu.edu.cn/eol/lesson/student.lesson.list.jsp")
                .build()
                .connTimeOut(5000)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Document document = Jsoup.parse(response);
                        Elements elements = document.select("table#table2").select("tbody").select("tr");
                        for (int i = 1; i < elements.size(); i++) {
                            Elements elements1 = elements.get(i).select("td");
                            String name = elements1.get(1).text();
                            String teacher = elements1.get(3).text();
                            String lid = elements1.get(4).getElementsByTag("a").attr("href").replace("student.lesson.list.jsp?ACTION=LESSUP&lid=", "");
                            Course course = new Course(name, teacher, lid);
                            courseList.add(course);
                        }
                        adapterCourse.notifyDataSetChanged();
                    }
                });
    }

    private void getWork(String lid) {
        showDialogContent();
        OkHttpUtils
                .get()
                .tag(this)
                .url(GET_WORK + lid)
                .addHeader("Host", "kcxt.cdu.edu.cn")
                .build()
                .connTimeOut(5000)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        tvWork.setText("错误响应:" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Document document = Jsoup.parse(response);
                        StringBuilder workBuilder = new StringBuilder();
                        Elements homework = document.select("div.homeworklist").select("ul").select("li");
                        for (int i = 0; i < homework.size(); i++) {
                            workBuilder.append(homework.get(i).text()).append("\n");
                        }
                        StringBuilder noticeBuilder = new StringBuilder();
                        Elements notice = document.select("div.noticelist").select("ul").select("li");
                        for (int i = 0; i < notice.size(); i++) {
                            noticeBuilder.append(notice.get(i).text()).append("\n");
                        }
                        if (noticeBuilder.length() == 0) {
                            tvNotice.setText("无");
                        } else
                            tvNotice.setText(noticeBuilder);
                        if (workBuilder.length() == 0) {
                            tvWork.setText("无");
                        } else
                            tvWork.setText(workBuilder);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }

    private void showDialogContent() {
        if (dialogContent == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_course_work_show, null, false);
            progressBar = (ProgressBar) view.findViewById(R.id.progress);
            tvNotice = (TextView) view.findViewById(R.id.tv_notice);
            tvWork = (TextView) view.findViewById(R.id.tv_work);
            button = (Button) view.findViewById(R.id.btn_close);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogContent.dismiss();
                    tvNotice.setText("");
                    tvWork.setText("");
                }
            });
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(view);
            dialogContent = builder.create();
        }
        progressBar.setVisibility(View.VISIBLE);
        dialogContent.show();
        Window window = dialogContent.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
