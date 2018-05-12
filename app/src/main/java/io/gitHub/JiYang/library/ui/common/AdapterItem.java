package io.gitHub.JiYang.library.ui.common;

import android.view.View;

public interface AdapterItem<T> {
    void handleData(int position);

    int getResId();

    void bindViews(View itemView);
}
