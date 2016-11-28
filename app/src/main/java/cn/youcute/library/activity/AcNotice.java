package cn.youcute.library.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

import cn.youcute.library.AppControl;
import cn.youcute.library.R;
import cn.youcute.library.adapter.AdapterAnnounce;
import cn.youcute.library.bean.Announce;
import cn.youcute.library.util.NetRequest;
import cn.youcute.library.util.ToastUtil;
import okhttp3.Call;

/**
 * Created by jy on 2016/11/6.
 * notice of school
 */

public class AcNotice extends AcBase implements NetRequest.GetAnnounceCallBack {
    private ListView listView;
    private List<Announce> announceList;
    private ProgressBar progressBar, progressBarFooter;
    private TextView tvInfo;
    private AdapterAnnounce adapterAnnounce;
    private int page = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_notice);
        initView();
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.list_notice);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        tvInfo = (TextView) findViewById(R.id.tv_info);
        AppControl.getInstance().getNetRequest().getAnnounce(page, this);
    }

    @Override
    public void getAnnounceSuccess(List<Announce> announces) {
        progressBar.setVisibility(View.INVISIBLE);
        if (progressBarFooter != null) {
            progressBarFooter.setVisibility(View.INVISIBLE);
        }
        if (announces.size() == 0) {
            return;
        }
        if (adapterAnnounce == null) {
            announceList = new ArrayList<>();
            adapterAnnounce = new AdapterAnnounce(this.announceList, this);
            View view = LayoutInflater.from(this).inflate(R.layout.layout_list_footer, listView, false);
            progressBarFooter = (ProgressBar) view.findViewById(R.id.progress_footer);
            progressBarFooter.setVisibility(View.INVISIBLE);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressBarFooter.setVisibility(View.VISIBLE);
                    page++;
                    AppControl.getInstance().getNetRequest().getAnnounce(page, AcNotice.this);
                }
            });
            listView.addFooterView(view);
            listView.setAdapter(adapterAnnounce);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    showContent(announceList.get(i).title);
                    getContent(announceList.get(i).url);
                }
            });
        }
        this.announceList.addAll(announces);
        adapterAnnounce.notifyDataSetChanged();
    }

    @Override
    public void getAnnounceFailed(String info) {
        progressBar.setVisibility(View.INVISIBLE);
        tvInfo.setText(info);
    }

    private void getContent(String url) {
        if(!isNetworkConnected()){
            ToastUtil.showToast("网络未连接，请检查网络");
            return;
        }
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .connTimeOut(5000)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        progressBarGet.setVisibility(View.INVISIBLE);
                        tvContent.setText("错误响应:" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        progressBarGet.setVisibility(View.INVISIBLE);
                        Document document = Jsoup.parse(response);
                        String content = document.select("div#news_content").text();
                        if (content != null)
                            tvContent.setText(content);
                    }
                });
    }

    private AlertDialog dialogGet;
    private ProgressBar progressBarGet;
    private TextView tvTitle, tvContent;
    private Button btnClose;

    private void showContent(String title) {
        if (dialogGet == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_notice_show, null, false);
            progressBarGet = (ProgressBar) view.findViewById(R.id.progress);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvContent = (TextView) view.findViewById(R.id.tv_content);
            btnClose = (Button) view.findViewById(R.id.btn_close);
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogGet.dismiss();
                    tvContent.setText("");
                    tvTitle.setText("");
                }
            });
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(view);
            dialogGet = builder.create();
        }
        progressBarGet.setVisibility(View.VISIBLE);
        tvTitle.setText(title);
        dialogGet.show();
    }
}
