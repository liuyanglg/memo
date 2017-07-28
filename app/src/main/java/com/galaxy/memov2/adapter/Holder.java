package com.galaxy.memov2.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.galaxy.memov2.R;

/**
 * Created by liuya on 2017/4/24.
 */

public class Holder extends RecyclerView.ViewHolder {
    public TextView tvDetail;
    public TextView tvCreateDate;
    public TextView tvAlarmDate;
    public CheckBox checkBox;
    public LinearLayout layoutAlarm;
    public ImageView imgAlarm;

    public Holder(View view) {
        super(view);
        tvDetail = (TextView) view.findViewById(R.id.i1_tv_detail);
        tvCreateDate = (TextView) view.findViewById(R.id.i1_tv_create_date);
        tvAlarmDate = (TextView) view.findViewById(R.id.i1_tv_alarm_date);
        checkBox = (CheckBox) view.findViewById(R.id.i1_checkbox_select);
        layoutAlarm = (LinearLayout) view.findViewById(R.id.i1_layout_alarm);
        imgAlarm = (ImageView) view.findViewById(R.id.i1_img_alarm);
    }
}
