package cn.youcute.library.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.youcute.library.R;
import cn.youcute.library.activity.AcWebView;
import cn.youcute.library.bean.History;

/**
 * Created by jy on 2016/9/23.
 * 历史借阅数据适配器
 */
public class AdapterBookHistory extends BaseAdapter {
    private List<History> historyList;
    private Context context;

    public AdapterBookHistory(List<History> historyList, Context context) {
        this.historyList = historyList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return historyList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_bookhistory, null);
            holder = new ViewHolder();
            holder.tvHistoryBookName = (TextView) convertView.findViewById(R.id.tv_history_book_name);
            holder.tvGetData = (TextView) convertView.findViewById(R.id.tv_getData);
            holder.tvEndData = (TextView) convertView.findViewById(R.id.tv_endData);
            holder.tvHistoryBookId = (TextView) convertView.findViewById(R.id.tv_history_id);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AcWebView.class);
                    intent.putExtra("url", historyList.get(position).url);
                    intent.putExtra("title", historyList.get(position).name);
                    context.startActivity(intent);
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvHistoryBookId.setText(historyList.get(position).historyId);
        holder.tvHistoryBookName.setText(historyList.get(position).name);
        holder.tvGetData.setText(historyList.get(position).getData);
        holder.tvEndData.setText(historyList.get(position).endData);
        return convertView;
    }

    class ViewHolder {
        TextView tvHistoryBookId;
        TextView tvHistoryBookName;
        TextView tvGetData;
        TextView tvEndData;
    }
}
