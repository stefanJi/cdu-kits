package io.gitHub.JiYang.library.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import io.gitHub.JiYang.library.R;
import io.gitHub.JiYang.library.bean.Course;

/**
 * Created by jy on 2016/9/23.
 * 历史借阅数据适配器
 */
public class AdapterCourse extends BaseAdapter {
    private List<Course> courseList;
    private Context context;

    public AdapterCourse(List<Course> courseList, Context context) {
        this.courseList = courseList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return courseList.size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_course, null);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_course_name);
            holder.tvTeacher = (TextView) convertView.findViewById(R.id.tv_teacher_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(courseList.get(position).name);
        holder.tvTeacher.setText(courseList.get(position).teacher);
        return convertView;
    }

    class ViewHolder {
        TextView tvName;
        TextView tvTeacher;
    }
}
