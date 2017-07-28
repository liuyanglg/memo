package com.galaxy.memov2.ui.fragment.second;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.galaxy.memov2.R;
import com.galaxy.memov2.base.BaseBackFragment;
import com.galaxy.memov2.entity.Record;
import com.galaxy.memov2.utils.date.DateUtils;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.Map;

import static com.galaxy.memov2.entity.OperateConstant._ADD;
import static com.galaxy.memov2.entity.OperateConstant._DELETE;
import static com.galaxy.memov2.entity.OperateConstant._INIT;
import static com.galaxy.memov2.entity.OperateConstant._UPDATE;

/**
 * Created by liuya on 2017/4/19.
 */

public class DetailSecondFragment extends BaseBackFragment {
    /*传参识别关键字*/
    private static final String ARG_MSG = "arg_msg";
    /*日志过滤关键字*/
    private static final String TAG = "DetailFirstFragment";
    /*view*/
    private Toolbar mToolbar;
    private TextView tvDetail;

    /*FloatingActionButton*/
    private FloatingActionMenu opMenu;
    private FloatingActionButton fabStar_1;
    private FloatingActionButton fabStar_2;
    private FloatingActionButton fabStar_3;
    private FloatingActionButton fabStar_4;
    private FloatingActionButton fabStar_5;


    /*基础变量*/
    private Record currentRecord;//当前record
    private Record oldRecord;//初始record
    int oldRate;

    /*创建实例*/
    public static DetailSecondFragment newInstance(Record record) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_MSG, record);
        DetailSecondFragment fragment = new DetailSecondFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentRecord = getArguments().getParcelable(ARG_MSG);
        oldRate = currentRecord.rate;
//        oldRecord = getOldRecord(currentRecord);
        initData(currentRecord);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_four, container, false);
        initView(view);
        setEventListener();
        return attachToSwipeBack(view);
    }

    private void initView(View view) {
        initToolbarView(view);
        initFabView(view);
        initOtherView(view);
    }

    public void setEventListener() {
        initFabEvent();
    }

    /* 初始化得到数据*/
    public void initData(Record currentRecord) {
        currentRecord.isSelect = false;
        if (currentRecord.createDate == null)
            currentRecord.createDate = new Date();
        if (currentRecord.detail == null)
            currentRecord.detail = "";
    }

    public void initToolbarView(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.f4_toolbar);
        mToolbar.inflateMenu(R.menu.fm_two_menu);
        initToolbarNav(mToolbar);
        mToolbar.setTitle(DateUtils.DateToStr(currentRecord.createDate));
    }

    public void initFabView(View view) {
        opMenu = (FloatingActionMenu) view.findViewById(R.id.f4_fab_menu);
        fabStar_1 = (FloatingActionButton) view.findViewById(R.id.f4_fab_rate_01);
        fabStar_2 = (FloatingActionButton) view.findViewById(R.id.f4_fab_rate_02);
        fabStar_3 = (FloatingActionButton) view.findViewById(R.id.f4_fab_rate_03);
        fabStar_4 = (FloatingActionButton) view.findViewById(R.id.f4_fab_rate_04);
        fabStar_5 = (FloatingActionButton) view.findViewById(R.id.f4_fab_rate_05);
    }

    public void initOtherView(View view) {
        tvDetail = (TextView) view.findViewById(R.id.f4_tv_detail);
        tvDetail.setText(currentRecord.detail);
    }

    public void initFabEvent() {
        fabStar_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentRecord.rate = 1;
                Toast.makeText(getActivity(),"你打了1分",Toast.LENGTH_SHORT).show();
            }
        });
        fabStar_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentRecord.rate = 2;
                Toast.makeText(getActivity(),"你打了2分",Toast.LENGTH_SHORT).show();

            }
        });
        fabStar_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentRecord.rate = 3;
                Toast.makeText(getActivity(),"你打了3分",Toast.LENGTH_SHORT).show();
            }
        });
        fabStar_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentRecord.rate = 4;
                Toast.makeText(getActivity(),"你打了4分",Toast.LENGTH_SHORT).show();
            }
        });
        fabStar_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentRecord.rate = 5;
                Toast.makeText(getActivity(),"你打了5分",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendRateChange() {
        Log.i(TAG, "old rate is :" + oldRate);
        Log.i(TAG, "new rate is :" + currentRecord.rate);
        if (currentRecord.rate != oldRate) {
            currentRecord.opStatus = _UPDATE;
            EventBus.getDefault().post(currentRecord);
        }
    }


    /*fragment生命周期*/
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sendRateChange();
        _mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        hideSoftInput();
    }
}
