package cn.youcute.library.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.youcute.library.R;
import cn.youcute.library.bean.BookFine;

/**
 * Created by jy on 2016/9/23.
 * 违章缴款
 */
public class AdapterBookFine extends BaseAdapter {
    private Context context;
    private List<BookFine> bookFines;

    public AdapterBookFine(Context context, List<BookFine> bookFines) {
        this.context = context;
        this.bookFines = bookFines;
    }

    @Override
    public int getCount() {
        return bookFines.size();
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
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_bookfine, null);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
            viewHolder.tvCode = (TextView) convertView.findViewById(R.id.tv_book_code_fine);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_book_name_fine);
            viewHolder.tvGetData = (TextView) convertView.findViewById(R.id.tv_getData_fine);
            viewHolder.tvEndData = (TextView) convertView.findViewById(R.id.tv_book_endData_fine);
            viewHolder.tvSMoney = (TextView) convertView.findViewById(R.id.tv_book_shouldMoney);
            viewHolder.tvEMoney = (TextView) convertView.findViewById(R.id.tv_endMoney);
            viewHolder.tvStatus = (TextView) convertView.findViewById(R.id.tv_book_status);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvCode.setText(bookFines.get(position).code);
        viewHolder.tvName.setText(bookFines.get(position).name);
        viewHolder.tvGetData.setText(bookFines.get(position).getData);
        viewHolder.tvEndData.setText(bookFines.get(position).endData);
        viewHolder.tvSMoney.setText(bookFines.get(position).shouldMoney);
        viewHolder.tvEMoney.setText(bookFines.get(position).money);
        float money1, money2;
        money1 = Float.parseFloat(bookFines.get(position).shouldMoney);
        money2 = Float.parseFloat(bookFines.get(position).money);
        if (money1 > money2) {
            viewHolder.tvEMoney.setTextColor(Color.RED);
        }
        viewHolder.tvStatus.setText(bookFines.get(position).status);
        return convertView;
    }

    class ViewHolder {
        TextView tvCode;
        TextView tvName;
        TextView tvGetData;
        TextView tvEndData;
        TextView tvSMoney;
        TextView tvEMoney;
        TextView tvStatus;
    }
}
