package io.gitHub.JiYang.library.ui.common;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class CommHolder<T> extends RecyclerView.ViewHolder {

    public AdapterItem<T> item;

    public CommHolder(Context context, ViewGroup parent, @NonNull AdapterItem<T> item) {
        super(LayoutInflater.from(context).inflate(item.getResId(), parent, false));
        this.item = item;
        this.item.bindViews(this.itemView);
    }
}
