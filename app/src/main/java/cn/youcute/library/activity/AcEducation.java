package cn.youcute.library.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import cn.youcute.library.R;
import cn.youcute.library.util.ToastUtil;
import okhttp3.Call;

/**
 * Created by jy on 2016/11/9.
 */

public class AcEducation extends AcBase {
    private ProgressBar progressBar;
    private ListView listView;
    private Button btnRe;
    private List<String> stringList;
    private List<String> serverUrlList;
    private ArrayAdapter<String> adapter;
    private InterstitialAd interstitial;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_education);
        listView = (ListView) findViewById(R.id.list_education);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        btnRe = (Button) findViewById(R.id.btn_re);
        btnRe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getServer();
            }
        });
        stringList = new ArrayList<>();
        serverUrlList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, R.layout.item_textview, stringList);
        getServer();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(AcEducation.this, AcWebView.class);
                intent.putExtra("title", "教务管理系统");
                intent.putExtra("url", serverUrlList.get(i));
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
        listView.setAdapter(adapter);
        // Create the interstitial.
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(getResources().getString(R.string.banner_ad_unit_id));
        // Create ad request.
        AdRequest adRequest = new AdRequest.Builder().build();
        // Begin loading your interstitial.
        interstitial.loadAd(adRequest);
        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (interstitial.isLoaded()) {
                    interstitial.show();
                }
            }
        });
    }


    private void getServer() {
        btnRe.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        OkHttpUtils
                .get()
                .url("http://jw.cdu.edu.cn/Others/jwglxt.aspx")
                .tag(this)
                .build().connTimeOut(5000).execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.showToast("获取服务器列表失败,请重试" + e.getMessage());
                call.cancel();
                btnRe.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onResponse(String response) {
                Document document = Jsoup.parse(response);
                Elements elements = document.select("div.indexLink").select("ul").first().select("li").select("a");
                for (int i = 0; i < elements.size(); i++) {
                    String name = elements.get(i).text();
                    stringList.add(name);
                    String url = elements.get(i).attr("href");
                    serverUrlList.add(url);
                }
                progressBar.setVisibility(View.INVISIBLE);
                if (stringList.size() < 1) {
                    ToastUtil.showToast("获取服务器列表失败,请重试");
                    btnRe.setVisibility(View.VISIBLE);
                    return;
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
