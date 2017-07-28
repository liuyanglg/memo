package com.galaxy.memov2.ui.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.galaxy.memov2.R;
import com.galaxy.memov2.listener.ProgressPopupClickListener;

import static com.galaxy.memov2.utils.Constant.AFFAIR_PROGRESS_00;
import static com.galaxy.memov2.utils.Constant.AFFAIR_PROGRESS_01;
import static com.galaxy.memov2.utils.Constant.AFFAIR_PROGRESS_02;
import static com.galaxy.memov2.utils.Constant.AFFAIR_PROGRESS_03;
import static com.galaxy.memov2.utils.Constant.AFFAIR_PROGRESS_04;
import static com.galaxy.memov2.utils.Constant.PROGRESS_FINISH;

/**
 * Created by liuya on 2017/4/25.
 */

public class ProgressPopup implements View.OnClickListener {
    /*view*/
    private TextView[] tvProgressArray = new TextView[6];
    private ImageView[] img = new ImageView[6];
    private FrameLayout[] frameLayouts = new FrameLayout[6];

    private int progress;
    private Context context;
    private PopupWindow popupWindow;
    private ProgressPopupClickListener progressPopupClickListener;

    public ProgressPopup(Context context, int progress) {
        this.progress = progress;
        this.context = context;
        popupWindow = new PopupWindow(context);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.AnimBottom);
        popupWindow.setContentView(initView());
//        initEvent();
        popupWindow.update();
    }

    private View initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_progerss_popup, null);
        tvProgressArray[0] = (TextView) view.findViewById(R.id.tv_progress_00);
        tvProgressArray[1] = (TextView) view.findViewById(R.id.tv_progress_01);
        tvProgressArray[2] = (TextView) view.findViewById(R.id.tv_progress_02);
        tvProgressArray[3] = (TextView) view.findViewById(R.id.tv_progress_03);
        tvProgressArray[4] = (TextView) view.findViewById(R.id.tv_progress_04);
        tvProgressArray[5] = (TextView) view.findViewById(R.id.tv_progress_05);

        img[0] = (ImageView) view.findViewById(R.id.img_progress_00);
        img[1] = (ImageView) view.findViewById(R.id.img_progress_01);
        img[2] = (ImageView) view.findViewById(R.id.img_progress_02);
        img[3] = (ImageView) view.findViewById(R.id.img_progress_03);
        img[4] = (ImageView) view.findViewById(R.id.img_progress_04);
        img[5] = (ImageView) view.findViewById(R.id.img_progress_05);

        frameLayouts[0] = (FrameLayout) view.findViewById(R.id.dig_layout_fm_00);
        frameLayouts[1] = (FrameLayout) view.findViewById(R.id.dig_layout_fm_01);
        frameLayouts[2] = (FrameLayout) view.findViewById(R.id.dig_layout_fm_02);
        frameLayouts[3] = (FrameLayout) view.findViewById(R.id.dig_layout_fm_03);
        frameLayouts[4] = (FrameLayout) view.findViewById(R.id.dig_layout_fm_04);
        frameLayouts[5] = (FrameLayout) view.findViewById(R.id.dig_layout_fm_05);

        initData();
        for(int i=0;i<img.length;i++){
            frameLayouts[i].setOnClickListener(this);
        }
        return view;
    }

    public void showPopup(View rootView) {
        // 第一个参数是要将PopupWindow放到的View，第二个参数是位置，第三第四是偏移值
        popupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
        Log.i("popup", "fab show is click2");
    }

    private void initData(){
            img[progress].setVisibility(View.VISIBLE);
            tvProgressArray[progress].setTextColor(ContextCompat.getColor(context, R.color.green_dark_01));
    }

    private void changeView(int pos){
        for(int i=0;i<img.length;i++){
            img[i].setVisibility(View.INVISIBLE);
            tvProgressArray[i].setTextColor(ContextCompat.getColor(context, R.color.gray_dark));
        }
        img[pos].setVisibility(View.VISIBLE);
        tvProgressArray[pos].setTextColor(ContextCompat.getColor(context, R.color.green_dark_01));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dig_layout_fm_00:
                progressPopupClickListener.progressOnClick(AFFAIR_PROGRESS_00);
                changeView(0);
                break;
            case R.id.dig_layout_fm_01:
                progressPopupClickListener.progressOnClick(AFFAIR_PROGRESS_01);
                changeView(1);
                break;
            case R.id.dig_layout_fm_02:
                progressPopupClickListener.progressOnClick(AFFAIR_PROGRESS_02);
                changeView(2);
                break;
            case R.id.dig_layout_fm_03:
                progressPopupClickListener.progressOnClick(AFFAIR_PROGRESS_03);
                changeView(3);
                break;
            case R.id.dig_layout_fm_04:
                progressPopupClickListener.progressOnClick(AFFAIR_PROGRESS_04);
                changeView(4);
                break;
            case R.id.dig_layout_fm_05:
                progressPopupClickListener.progressOnClick(PROGRESS_FINISH);
                changeView(5);
                break;
        }
    }

    public void setProgressPopupClickListener(ProgressPopupClickListener progressPopupClickListener) {
        this.progressPopupClickListener = progressPopupClickListener;
    }
}
