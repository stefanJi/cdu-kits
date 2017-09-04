package io.gitHub.JiYang.library.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import io.gitHub.JiYang.library.R;
import io.gitHub.JiYang.library.bean.Book;
import io.gitHub.JiYang.library.util.TextUtil;

/**
 * Created by jy on 2016/9/26.
 * 搜索数据适配器
 */

public class AdapterSearch extends BaseAdapter {
    private Context context;
    private List<Book> books;
    private String searchKey;

    public AdapterSearch(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_search, null);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_search_name);
            holder.tvCode = (TextView) convertView.findViewById(R.id.tv_search_code);
            holder.tvCount = (TextView) convertView.findViewById(R.id.tv_search_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(TextUtil.highlight(books.get(position).name.toLowerCase(), searchKey.toLowerCase()));
        holder.tvCount.setText(books.get(position).count);
        holder.tvCode.setText(books.get(position).code);
        return convertView;
    }

    private class ViewHolder {
        TextView tvName, tvCount, tvCode;
    }
}
