package com.galaxy.memov2.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.galaxy.memov2.R;

/**
 * Created by liuya on 2017/4/27.
 */

public class ProgressHolder extends RecyclerView.ViewHolder {
//    private FrameLayout frameLayout;
    public ImageView imgFlag;
    public TextView tvProgress;

    public ProgressHolder(View itemView) {
        super(itemView);
//        frameLayout = (FrameLayout) itemView.findViewById(R.id.dig_layout_fm);
        imgFlag = (ImageView) itemView.findViewById(R.id.dig_img_progress);
        tvProgress = (TextView) itemView.findViewById(R.id.dig_tv_progress);
    }
}
