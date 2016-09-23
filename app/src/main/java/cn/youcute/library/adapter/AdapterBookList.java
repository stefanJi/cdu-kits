package cn.youcute.library.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import cn.youcute.library.AppControl;
import cn.youcute.library.R;
import cn.youcute.library.bean.Book;

/**
 * Created by jy on 2016/9/23.
 * 借书
 */
public class AdapterBookList extends BaseAdapter {
    private Context context;
    private List<Book> books;

    public AdapterBookList(Context context, List<Book> books) {
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
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_booklist, null);
            convertView.setTag(holder);
            holder.tvBookCode = (TextView) convertView.findViewById(R.id.tv_book_code);
            holder.tvBookName = (TextView) convertView.findViewById(R.id.tv_book_name);
            holder.tvBookGetData = (TextView) convertView.findViewById(R.id.tv_book_getData);
            holder.tvBookEndData = (TextView) convertView.findViewById(R.id.tv_book_endData);
            holder.button = (Button) convertView.findViewById(R.id.btn_get_more);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvBookCode.setText(books.get(position).code);
        holder.tvBookName.setText(books.get(position).name);
        holder.tvBookGetData.setText(books.get(position).getData);
        holder.tvBookEndData.setText(books.get(position).endData);
        String count = books.get(position).getCount;
        final int countMore = Integer.parseInt(count); //已经续借量
        holder.button.setText("已续借:".concat(count));
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countMore >= 1) {
                    AppControl.getInstance().showToast("线上最多续借1次，请到图书馆续借");
                } else {
                    //续借
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tvBookCode;
        TextView tvBookName;
        TextView tvBookGetData;
        TextView tvBookEndData;
        Button button;
    }
}
