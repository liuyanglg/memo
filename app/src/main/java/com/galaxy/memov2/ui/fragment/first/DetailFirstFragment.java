package com.galaxy.memov2.ui.fragment.first;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.galaxy.memov2.R;
import com.galaxy.memov2.activity.MainActivity;
import com.galaxy.memov2.asynctask.AlarmDaoAsyncTask;
import com.galaxy.memov2.base.BaseBackFragment;
import com.galaxy.memov2.entity.Alarm;
import com.galaxy.memov2.entity.Record;
import com.galaxy.memov2.listener.BackPressListener;
import com.galaxy.memov2.listener.ProgressPopupClickListener;
import com.galaxy.memov2.ui.view.ProgressPopup;
import com.galaxy.memov2.utils.alarm.AlarmUtils;
import com.galaxy.memov2.utils.date.DateUtils;
import com.github.clans.fab.FloatingActionMenu;
import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.galaxy.memov2.entity.OperateConstant._ADD;
import static com.galaxy.memov2.entity.OperateConstant._DELETE;
import static com.galaxy.memov2.entity.OperateConstant._DELETE_BY_ID;
import static com.galaxy.memov2.entity.OperateConstant._INIT;
import static com.galaxy.memov2.entity.OperateConstant._MOVE;
import static com.galaxy.memov2.entity.OperateConstant._SAVE_OR_UPDATE;
import static com.galaxy.memov2.entity.OperateConstant._UPDATE;
import static com.galaxy.memov2.utils.Constant.ALARM_TAG;
import static com.galaxy.memov2.utils.Constant.DATE_FORMAT;
import static com.galaxy.memov2.utils.Constant.IS_FINISH;
import static com.galaxy.memov2.utils.Constant.PROGRESS_FINISH;

/**
 * Created by liuya on 2017/4/19.
 */

public class DetailFirstFragment extends BaseBackFragment {
    /*传参识别关键字*/
    private static final String ARG_MSG = "arg_msg";
    /*日志过滤关键字*/
    private static final String TAG = "DetailFirstFragment";
    /*view*/
    private ProgressPopup progressPopup;
    private FrameLayout frameLayout;//popupWindow容器
    private Toolbar mToolbar;
    private ScrollView scrollView;
    private MultiAutoCompleteTextView edtDetail;

    /*FloatingActionButton*/
    private FloatingActionMenu opMenu;
    private com.github.clans.fab.FloatingActionButton fabSetAlarm;
    private com.github.clans.fab.FloatingActionButton fabCancelAlarm;
    private com.github.clans.fab.FloatingActionButton fabSetProgress;

    /*时间日期选择器*/
    private SlideDateTimePicker dateTimePicker = null;
    private SimpleDateFormat mFormatter;
    /*闹钟Uri*/
    private Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
    Alarm alarm;

    /*基础变量*/
    private Record currentRecord;//当前record
    private Record oldRecord;//初始record

    /*创建实例*/
    public static DetailFirstFragment newInstance(Record record) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_MSG, record);
        DetailFirstFragment fragment = new DetailFirstFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentRecord = getArguments().getParcelable(ARG_MSG);
        oldRecord = getOldRecord(currentRecord);
        initData(currentRecord);
//        Log.i(TAG, "detail: " + oldRecord.detail);
//        Log.i(TAG, "(oldRecord.detail==\"\")" + (oldRecord.detail == ""));
//        Log.i(TAG, "(oldRecord.detail==null)" + (oldRecord.detail == null));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_two, container, false);
        initView(view);
        initDateTimePicker();
        setEventListener();
        return attachToSwipeBack(view);
    }

    private void initView(View view) {
        initToolbarView(view);
        initFabView(view);
        initOtherView(view);
        initProgressEvent();
    }

    public void setEventListener() {
        initFabEvent();
        initDateTimePickerEvent();
        initEditViewEvent();
        initMenuItemEvent();
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
        mToolbar = (Toolbar) view.findViewById(R.id.f2_toolbar);
        mToolbar.inflateMenu(R.menu.fm_two_menu);
        initToolbarNav(mToolbar);
        mToolbar.setTitle(DateUtils.DateToStr(currentRecord.createDate));
    }

    public void initFabView(View view) {
        opMenu = (FloatingActionMenu) view.findViewById(R.id.f2_fab_menu);
        fabSetAlarm = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.f2_fab_alarm);
        fabCancelAlarm = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.f2_fab_cancel_alarm);
        fabSetProgress = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.f2_fab_set_progress);
    }

    public void initOtherView(View view) {
        frameLayout = (FrameLayout) view.findViewById(R.id.f2_container);

        edtDetail = (MultiAutoCompleteTextView) view.findViewById(R.id.f2_edt_detail);
        edtDetail.setText(currentRecord.detail);
        edtDetail.setSelection(edtDetail.getText().length());
        edtDetail.clearFocus();
        edtDetail.setCursorVisible(false);
    }

    private void initDateTimePicker() {
        mFormatter = new SimpleDateFormat(DATE_FORMAT);
        dateTimePicker = new SlideDateTimePicker.Builder(_mActivity.getSupportFragmentManager()).build();
    }


    public void initFabEvent() {
        fabSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popTimeSelector(new Date());
            }
        });

        fabCancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentRecord.alarmDate = null;
                if(alarm!=null){
                    new AlarmDaoAsyncTask(getActivity(), alarm).execute(_DELETE_BY_ID);
                }
                AlarmUtils.cancelAlarm(getActivity(), currentRecord.id);
                Toast.makeText(getActivity(),
                        "闹钟已取消", Toast.LENGTH_SHORT).show();
            }
        });
        fabSetProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressPopup.showPopup(frameLayout);
                Log.i("popup", "fab show is click");
            }
        });
    }

    public void initDateTimePickerEvent() {
        dateTimePicker.setListener(new SlideDateTimeListener() {
            @Override
            public void onDateTimeSet(Date date) {
                currentRecord.alarmDate = date;
                long millis = date.getTime();
                millis=millis/60000*60000;
                if(alarm==null){
                    alarm = new Alarm();
                }
                alarm.alarmId = currentRecord.id;
                alarm.alarmDate = currentRecord.alarmDate;
                alarm.detail = currentRecord.detail;
                new AlarmDaoAsyncTask(getActivity(), alarm).execute(_SAVE_OR_UPDATE);
                AlarmUtils.setAlarm(getActivity(),millis,currentRecord.id,currentRecord.detail,ringUri);
//                AlarmUtils.setAlarm(getActivity(), System.currentTimeMillis() + 15 * 1000, currentRecord.id, currentRecord.detail, ringUri);
                Log.i(ALARM_TAG, "set a alarm. . .");

                Toast.makeText(getActivity(),
                        "闹钟将在" + mFormatter.format(date) + "提醒", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDateTimeCancel() {
                Toast.makeText(getActivity(),
                        "取消", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initProgressEvent() {
        progressPopup = new ProgressPopup(getActivity(), currentRecord.progress);
        progressPopup.setProgressPopupClickListener(new ProgressPopupClickListener() {
            @Override
            public void progressOnClick(int pos) {
                currentRecord.progress = pos;
                if (pos == PROGRESS_FINISH) {
                    currentRecord.isFinish = IS_FINISH;
                }
            }
        });
    }


    public void initEditViewEvent() {
        edtDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtDetail.setCursorVisible(true);
            }
        });
    }

    public void initMenuItemEvent() {
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.i2_set_ring:
                        setRingtone();
                        break;
                }
                return false;
            }
        });
    }


    /*保存当前record状态并下一步的操作状态，为增删改查准备*/
    public void saveCurrentRecord(Record currentRecord) {
        currentRecord.detail = edtDetail.getText().toString();
        if (oldRecord.detail == null && currentRecord.detail.equals("")) {
            currentRecord.opStatus = _INIT;
        } else if (oldRecord.detail == null && !currentRecord.detail.equals("")) {
            currentRecord.opStatus = _ADD;
        } else if (oldRecord.detail != null && currentRecord.detail.equals("")) {
            currentRecord.opStatus = _DELETE;
        } else if (oldRecord.detail != null && !currentRecord.detail.equals("")) {
            if (!isRecordChange()) {
                currentRecord.opStatus = _INIT;
            } else {
                if (currentRecord.isFinish == IS_FINISH) {
                    currentRecord.opStatus = _MOVE;
                } else {
                    currentRecord.opStatus = _UPDATE;
                }
            }
        }
    }

    /*弹出时间日期选择器*/
    private void popTimeSelector(Date minDate) {
        dateTimePicker.setInitialDate(minDate);
        dateTimePicker.setMinDate(minDate);
        dateTimePicker.setIs24HourTime(true);
        dateTimePicker.setIndicatorColor(ContextCompat.getColor(getActivity(), R.color.wheat));
        dateTimePicker.show();
    }


    /*保存传入的oldRecord*/
    public Record getOldRecord(Record r) {
        Record old = new Record();
        old.id = r.id;
        old.detail = r.detail;
        old.createDate = r.createDate;
        old.alarmDate = r.alarmDate;
        old.rate = r.rate;
        old.progress = r.progress;
        old.isFinish = r.isFinish;
        old.isSelect = r.isSelect;
        old.opStatus = r.opStatus;
        return old;
    }

    public boolean isRecordChange() {
        boolean isDetailChange = false;
        currentRecord.detail = edtDetail.getText().toString();
        isDetailChange = !(currentRecord.detail.equals(oldRecord.detail));
        boolean isAlarmDateChange = false;
        String currentAlarmDate = DateUtils.DateToStr(currentRecord.alarmDate);
        String oldAlarmDate = DateUtils.DateToStr(oldRecord.alarmDate);
        Log.i(TAG, "old_date" + oldAlarmDate);
        Log.i(TAG, "new_date" + currentAlarmDate);
        if (currentAlarmDate != null && oldAlarmDate != null)
            isAlarmDateChange = !(currentAlarmDate.equals(oldAlarmDate));
        else {
            if (currentAlarmDate == null && oldAlarmDate == null) {
                isAlarmDateChange = false;
            } else {
                isAlarmDateChange = true;
            }
        }
        boolean isProgressChange = false;
        isProgressChange = !(currentRecord.progress == oldRecord.progress);
        boolean isFinishChange = false;
        isFinishChange = !(currentRecord.isFinish == oldRecord.isFinish);
        Log.i(ALARM_TAG, "isDetailChange: " + isDetailChange);
        Log.i(ALARM_TAG, "isAlarmDateChange: " + isAlarmDateChange);
        Log.i(ALARM_TAG, "isProgressChange: " + isProgressChange);
        Log.i(ALARM_TAG, "isFinishChange: " + isFinishChange);
        if (isDetailChange || isAlarmDateChange || isProgressChange || isFinishChange) {
            Log.i(ALARM_TAG, "isChange: " + true);
            return true;
        }
        Log.i(ALARM_TAG, "isChange: " + false);
        return false;
    }

    /*fragment生命周期*/
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "destroy fragment");
        saveCurrentRecord(currentRecord);
        EventBus.getDefault().post(currentRecord);
        _mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        hideSoftInput();
    }

    /*设置铃声*/
    private void setRingtone() {
        Intent intent = new Intent();
        intent.setAction(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "设置闹玲铃声");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALL);
        Uri pickedUri = RingtoneManager.getActualDefaultRingtoneUri(getActivity(), RingtoneManager.TYPE_ALARM);
        if (pickedUri != null) {
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, pickedUri);
            ringUri = pickedUri;
        }
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 1:
                Uri pickedURI = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                Log.i("alarm", pickedURI.toString());
                if (pickedURI.toString() != null) {
                    ringUri = pickedURI;
                }
                break;
            default:
                break;
        }
    }
}
