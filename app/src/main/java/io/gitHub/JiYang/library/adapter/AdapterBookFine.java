package io.gitHub.JiYang.library.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import io.gitHub.JiYang.library.R;
import io.gitHub.JiYang.library.bean.BookFine;

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
            viewHolder.tvCode = (TextView) convertView.findViewById(R.id.tv_fine_code);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_fine_name);
            viewHolder.tvEMoney = (TextView) convertView.findViewById(R.id.tv_fine_money_e);
            viewHolder.tvSMoney = (TextView) convertView.findViewById(R.id.tv_fine_money_s);
            viewHolder.tvGetData = (TextView) convertView.findViewById(R.id.tv_fine_get);
            viewHolder.tvEndData = (TextView) convertView.findViewById(R.id.tv_fine_end);
            viewHolder.tvStatus = (TextView) convertView.findViewById(R.id.tv_fine_status);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvCode.setText("编码:" + bookFines.get(position).code);
        viewHolder.tvName.setText(bookFines.get(position).name);
        viewHolder.tvGetData.setText("借书日期:" + bookFines.get(position).getData);
        viewHolder.tvEndData.setText("还书日期:" + bookFines.get(position).endData);
        viewHolder.tvSMoney.setText("欠款:" + bookFines.get(position).shouldMoney);
        viewHolder.tvEMoney.setText("已还:" + bookFines.get(position).money);
        float money1, money2;
        money1 = Float.parseFloat(bookFines.get(position).shouldMoney);
        money2 = Float.parseFloat(bookFines.get(position).money);
        if (money1 > money2) {
            viewHolder.tvEMoney.setTextColor(Color.RED);
        }
        viewHolder.tvStatus.setText("状态:" + bookFines.get(position).status);
        return convertView;
    }

    private class ViewHolder {
        TextView tvCode;
        TextView tvName;
        TextView tvGetData;
        TextView tvEndData;
        TextView tvSMoney;
        TextView tvEMoney;
        TextView tvStatus;
    }
}
