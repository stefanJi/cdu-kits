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
import io.gitHub.JiYang.library.model.enty.Announce;

public class AnnounceAdapter extends RecyclerView.Adapter<AnnounceAdapter.Holder> {
    private List<Announce> announces;
    private Context mContext;

    public AnnounceAdapter(Context context, List<Announce> announces) {
        this.mContext = context;
        this.announces = announces;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(mContext).inflate(R.layout.item_announce_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Announce announce = announces.get(position);
        holder.title.setText(announce.title);
        holder.data.setText(announce.data);
    }

    @Override
    public int getItemCount() {
        return announces == null ? 0 : announces.size();
    }

    public void setAnnounce(ArrayList<Announce> update) {
        this.announces = update;
        this.notifyDataSetChanged();
    }

    static class Holder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView data;

        Holder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.announceTitle);
            data = itemView.findViewById(R.id.announceData);
        }
    }
}
