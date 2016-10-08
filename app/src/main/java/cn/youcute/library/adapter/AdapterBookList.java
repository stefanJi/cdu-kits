package cn.youcute.library.adapter;

import android.content.Context;
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
            holder.tvBookName = (TextView) convertView.findViewById(R.id.tv_book_name);
            holder.tvBookGetData = (TextView) convertView.findViewById(R.id.tv_book_getData);
            holder.tvBookEndData = (TextView) convertView.findViewById(R.id.tv_book_outData);
            holder.tvBorrowInfo = (TextView) convertView.findViewById(R.id.tv_borrow_info);
            holder.tvOn = (Button) convertView.findViewById(R.id.btn_on);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvBookName.setText(books.get(position).name);
        holder.tvBookGetData.setText(books.get(position).getData);
        holder.tvBookEndData.setText(books.get(position).endData);
        String count = books.get(position).getCount;
        final int countMore = Integer.parseInt(count); //已经续借量
        String info = "已续借" + String.valueOf(countMore);
        if (countMore > 1) {
            holder.tvOn.setText(info);
        }
        holder.tvOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countMore > 1) {
                    AppControl.getInstance().showToast("该书已续借1次，请到图书馆续借");
                } else {
                    //续借
                    AppControl.getInstance().showToast("续借");
                }
            }
        });
        //计算剩余返还天数
        int countDay = 0;
        //
        info = "距离还书还有" + String.valueOf(countDay) + "天";
        holder.tvBorrowInfo.setText(info);

        return convertView;
    }

    private class ViewHolder {
        TextView tvBookName;
        TextView tvBookGetData;
        TextView tvBookEndData;
        TextView tvBorrowInfo;
        Button tvOn;  //续借
    }
}
