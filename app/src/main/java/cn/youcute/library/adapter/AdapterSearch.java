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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_search, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_book_name);
            holder.tvCount = (TextView) convertView.findViewById(R.id.tv_book_count);
            holder.tvCode = (TextView) convertView.findViewById(R.id.tv_book_code);
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
