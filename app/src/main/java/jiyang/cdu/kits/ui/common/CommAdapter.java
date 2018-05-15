package jiyang.cdu.kits.ui.common;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class CommAdapter<T> extends RecyclerView.Adapter<CommHolder> implements View.OnClickListener {
    private OnItemClickListener itemClickListener;
    private List<T> data;


    public CommAdapter(List<T> data) {
        this.data = data;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public abstract AdapterItem<T> createItem();

    public long getItemId(int position) {
        return position;
    }

    public List<T> getData() {
        return data;
    }

    @NonNull
    public CommHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommHolder<>(parent.getContext(), parent, createItem());
    }

    @Override
    public void onBindViewHolder(@NonNull CommHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(this);
        holder.item.handleData(position);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public void onClick(View v) {
        if (itemClickListener != null) {
            int position = (int) v.getTag();
            itemClickListener.OnItemClick(position);
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }
}
