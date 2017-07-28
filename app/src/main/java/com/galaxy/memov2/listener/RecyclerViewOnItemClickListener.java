package com.galaxy.memov2.listener;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public interface RecyclerViewOnItemClickListener {
    void onItemClick(int position, View view, RecyclerView.ViewHolder vh);

    void checkBoxClick(int position);
}