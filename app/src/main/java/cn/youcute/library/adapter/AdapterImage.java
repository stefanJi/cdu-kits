package cn.youcute.library.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import cn.youcute.library.AppControl;
import cn.youcute.library.R;
import cn.youcute.library.bean.Album;

/**
 * Created by jy on 2016/11/6.
 */

public class AdapterImage extends BaseAdapter {
    private List<Album> albumList;
    private Context context;

    public AdapterImage(List<Album> albumList, Context context) {
        this.albumList = albumList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return albumList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_list_image, null, false);
            viewHolder = new ViewHolder();
            viewHolder.iv = (ImageView) view.findViewById(R.id.iv);
            viewHolder.tv = (TextView) view.findViewById(R.id.tv_author);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv.setText(albumList.get(i).author);
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(viewHolder.iv, R.mipmap.ic_good, R.mipmap.ic_good);
        ImageLoader imageLoader = new ImageLoader(AppControl.getInstance().getRequestQueue(), AppControl.getInstance().getBitmapCache());
        imageLoader.get(albumList.get(i).url, listener);
        return view;
    }

    private class ViewHolder {
        ImageView iv;
        TextView tv;
    }
}
