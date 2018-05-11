package io.gitHub.JiYang.library.ui.fragment.feeds;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.gitHub.JiYang.library.R;
import io.gitHub.JiYang.library.model.enty.Feed;

public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.Holder> implements View.OnClickListener {
    private List<Feed> feeds;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener = null;

    public FeedsAdapter(Context context, List<Feed> feeds) {
        this.mContext = context;
        this.feeds = feeds;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_feeds_list, parent, false);
        Holder holder = new Holder(view);
        holder.itemView.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Feed feed = feeds.get(position);
        holder.title.setText(feed.title);
        holder.data.setText(feed.date);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return feeds == null ? 0 : feeds.size();
    }

    public void setAnnounce(ArrayList<Feed> update) {
        this.feeds = update;
        this.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (int) (v.getTag()));
        }
    }

    static class Holder extends RecyclerView.ViewHolder {
        private View itemView;
        private TextView title;
        private TextView data;

        Holder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            title = itemView.findViewById(R.id.announceTitle);
            data = itemView.findViewById(R.id.announceDate);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
