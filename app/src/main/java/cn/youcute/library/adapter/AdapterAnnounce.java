package cn.youcute.library.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.youcute.library.R;
import cn.youcute.library.bean.Announce;

/**
 * Created by jy on 2016/9/25.
 * 通知公告适配器
 */

public class AdapterAnnounce extends BaseAdapter {
    private List<Announce> data;
    private Context context;

    public AdapterAnnounce(List<Announce> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_announce_list, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_announce_title);
            holder.tvData = (TextView) convertView.findViewById(R.id.tv_announce_data);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvTitle.setText(data.get(position).title);
        holder.tvData.setText(data.get(position).data);
        return convertView;
    }

    private class ViewHolder {
        TextView tvTitle;
        TextView tvData;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        Log.d("TAG", String.valueOf(data.size()));
    }
}
