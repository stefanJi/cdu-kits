package cn.youcute.library.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.youcute.library.AppControl;
import cn.youcute.library.R;
import cn.youcute.library.adapter.AdapterAnnounce;
import cn.youcute.library.bean.Announce;
import cn.youcute.library.util.NetRequest;

/**
 * Created by jy on 2016/11/6.
 */

public class AcNotice extends AcBase implements NetRequest.GetAnnounceCallBack {
    private ListView listView;
    private List<Announce> announceList;
    private ProgressBar progressBar, progressBarFooter;
    private TextView tvInfo;
    private AdapterAnnounce adapterAnnounce;
    private int page = 0;

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
        }
        this.announceList.addAll(announces);
        adapterAnnounce.notifyDataSetChanged();
    }

    @Override
    public void getAnnounceFailed() {
        progressBar.setVisibility(View.INVISIBLE);
        tvInfo.setText("获取失败,请重试");
    }
}
