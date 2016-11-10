package cn.youcute.library.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_booklist, null);
            holder.tvBookName = (TextView) convertView.findViewById(R.id.tv_book_name);
            holder.tvBookGetData = (TextView) convertView.findViewById(R.id.tv_book_getData);
            holder.tvBookEndData = (TextView) convertView.findViewById(R.id.tv_book_outData);
            holder.tvBorrowInfo = (TextView) convertView.findViewById(R.id.tv_borrow_info);
            holder.tvRenewCount = (TextView) convertView.findViewById(R.id.tv_renew_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvBookName.setText(books.get(position).name);
        holder.tvBookGetData.setText("借书日期:" + books.get(position).getData);
        holder.tvBookEndData.setText("还书日期:" + books.get(position).endData);
        String count = books.get(position).getCount;
        String info = "续借量:" + String.valueOf(count);
        holder.tvRenewCount.setText(info);
        //计算剩余返还天数
        int countDay;
        try {
            countDay = daysBetween(books.get(position).getData, books.get(position).endData);
            if (countDay > 0) {
                info = "距离还书还有" + String.valueOf(countDay) + "天";
                if (countDay < 5) {
                    holder.tvBorrowInfo.setTextColor(Color.BLUE);
                }
            } else {
                info = "已经超期借阅" + String.valueOf(-countDay) + "天";
                holder.tvBorrowInfo.setTextColor(Color.RED);
            }
            holder.tvBorrowInfo.setText(info);
        } catch (ParseException e) {
            e.printStackTrace();
            holder.tvBorrowInfo.setText("还书日期,计算出错:" + e.getMessage());
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tvBookName;
        TextView tvBookGetData;
        TextView tvBookEndData;
        TextView tvBorrowInfo;
        TextView tvRenewCount;
    }

    /**
     * 字符串的日期格式的计算
     */
    private static int daysBetween(String smdate, String bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }
}
