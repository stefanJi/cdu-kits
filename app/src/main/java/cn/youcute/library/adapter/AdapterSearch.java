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
import cn.youcute.library.bean.Book;

/**
 * Created by jy on 2016/9/26.
 * 搜索数据适配器
 */

public class AdapterSearch extends BaseAdapter {
    private Context context;
    private List<Book> books;

    public AdapterSearch(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
    }

    @Override
    public int getCount() {
        return books.size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_search, null);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_search_name);
            holder.tvCode = (TextView) convertView.findViewById(R.id.tv_search_code);
            holder.tvCount = (TextView) convertView.findViewById(R.id.tv_search_count);
            convertView.setTag(holder);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AcWebView.class);
                    intent.putExtra("url", books.get(position).url);
                    intent.putExtra("title", books.get(position).name);
                    context.startActivity(intent);
                }
            });
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(books.get(position).name);
        holder.tvCount.setText(books.get(position).count);
        holder.tvCode.setText(books.get(position).code);
        return convertView;
    }

    private class ViewHolder {
        TextView tvName, tvCount, tvCode;
    }
}
