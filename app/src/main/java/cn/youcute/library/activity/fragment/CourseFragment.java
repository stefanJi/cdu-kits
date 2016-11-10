package cn.youcute.library.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

import cn.youcute.library.R;
import cn.youcute.library.adapter.AdapterCourse;
import cn.youcute.library.bean.Course;
import okhttp3.Call;

/**
 * Created by jy on 2016/11/8.
 */

public class CourseFragment extends ListFragment {
    private static final String GET_WORK = "http://kcxt.cdu.edu.cn/eol/welcomepage/course/index.jsp?lid=";
    private List<Course> courseList;
    private AdapterCourse adapterCourse;
    private AlertDialog dialog;
    private ProgressBar progressBar;
    private TextView tvWork, tvNotice;
    private Button button;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        courseList = new ArrayList<>();
        adapterCourse = new AdapterCourse(courseList, getActivity());
        setListAdapter(adapterCourse);
        getCourse();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        getWork(courseList.get(position).lid);
    }


    private void getCourse() {
        OkHttpUtils
                .get()
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
        if (dialog == null) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_course_work_show, null, false);
            progressBar = (ProgressBar) view.findViewById(R.id.progress);
            tvNotice = (TextView) view.findViewById(R.id.tv_notice);
            tvWork = (TextView) view.findViewById(R.id.tv_work);
            button = (Button) view.findViewById(R.id.btn_close);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    tvNotice.setText("");
                    tvWork.setText("");
                }
            });
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(view);
            dialog = builder.create();
        }
        progressBar.setVisibility(View.VISIBLE);
        dialog.show();
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
