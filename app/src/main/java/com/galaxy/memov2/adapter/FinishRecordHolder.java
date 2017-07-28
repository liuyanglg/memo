package com.galaxy.memov2.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.galaxy.memov2.R;

/**
 * Created by liuya on 2017/4/24.
 */

public class FinishRecordHolder extends RecyclerView.ViewHolder {
    public TextView tvDetail;
    public TextView tvCreateDate;
    //    public TextView tvRateStar;
    public CheckBox checkBox;
    //    public ImageView imgStar;
    public RatingBar ratingBar;

    public FinishRecordHolder(View view) {
        super(view);
        tvDetail = (TextView) view.findViewById(R.id.i3_tv_detail);
        tvCreateDate = (TextView) view.findViewById(R.id.i3_tv_create_date);
//        tvRateStar = (TextView) view.findViewById(R.id.i3_tv_rate_star);
        checkBox = (CheckBox) view.findViewById(R.id.i3_checkbox_select);
//        imgStar = (ImageView) view.findViewById(R.id.i3_img_rate_star);
        ratingBar = (RatingBar) view.findViewById(R.id.i3_rating_bar);
    }
}
