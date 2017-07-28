package com.galaxy.memov2.activity;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import com.galaxy.memov2.R;
import com.galaxy.memov2.utils.alarm.AlarmDialogOnClickListener;
import com.galaxy.memov2.utils.alarm.AlarmPopupDialog;
import com.galaxy.memov2.utils.alarm.AlarmUtils;

import java.io.IOException;

import static com.galaxy.memov2.utils.Constant.ALARM_AGAIN_MILLIS;
import static com.galaxy.memov2.utils.Constant.ALARM_DETAIL;
import static com.galaxy.memov2.utils.Constant.ALARM_ID;
import static com.galaxy.memov2.utils.Constant.ALARM_RING;
import static com.galaxy.memov2.utils.Constant.ALARM_TAG;


/**
 * Created by liuya on 2017/4/25.
 */

public class AlarmActivity extends Activity {
    private AlarmPopupDialog alarmDialog;
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    private String uri;
    private String alarmDetail;
    private int id;
    private Intent intent;
    private Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

    PowerManager pm;
    PowerManager.WakeLock wl;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE); // hide title
//        final Window win = getWindow();
//        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
//                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
//                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
//                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_clock_alarm);
        intent = this.getIntent();
        alarmDetail = intent.getStringExtra(ALARM_DETAIL);
        id = intent.getIntExtra(ALARM_ID, 0);
        uri = intent.getStringExtra(ALARM_RING);
        Log.i(ALARM_TAG, "AlarmActivity receive: " + uri);
        lightScreen();
        popupAlarmDialog();
        initEvent();
    }

    public void lightScreen() {
        if (wl == null) {
            pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            //获取电源管理器对象
            wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        }
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        wl.acquire();
        Log.i("screen", "light");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                closeScreen();
                Log.i("screen", "close");
            }
        }, 5 * 1000);
    }

    public void closeScreen() {
        if (wl != null)
            wl.release();
    }


    private void popupAlarmDialog() {
        alarmDialog = new AlarmPopupDialog(this);
        alarmDialog.show();
        alarmDialog.setTitle("待办事件提醒");
        alarmDialog.setMessage(alarmDetail);
        OpenMedia();
        OpenVibrator();
    }

    private void initEvent() {
        alarmDialog.setAlarmDialogOnClickListener(new AlarmDialogOnClickListener() {
            @Override
            public void ButtonConfirmOnClick(View v) {
                closeDialog();
            }

            @Override
            public void ButtonAgainOnClick(View v) {
                setRingUri();
                AlarmUtils.setAlarm(AlarmActivity.this, System.currentTimeMillis() + ALARM_AGAIN_MILLIS, id, alarmDetail, ringUri);
                closeDialog();
            }
        });
    }

    private void OpenMedia() {
        mediaPlayer = new MediaPlayer();
        if (uri != null && uri != "") {
            ringUri = Uri.parse(uri);
            Log.i(ALARM_TAG, ringUri.toString());
            try {
                mediaPlayer.setDataSource(this, ringUri);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        } else {
            mediaPlayer = MediaPlayer.create(this, R.raw.in_call_alarm);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    private void OpenVibrator() {
        vibrator = (Vibrator) this.getSystemService(Service.VIBRATOR_SERVICE);
        //数组参数意义：第一个参数为等待指定时间后开始震动，震动时间为第二个参数。后边的参数依次为等待震动和震动的时间
        //第二个参数为重复次数，-1为不重复，0为一直震动
        vibrator.vibrate(new long[]{100, 10, 100, 600}, 0);
    }

    private void setRingUri() {
        if (uri != null && uri != "") {
            ringUri = Uri.parse(uri);
        }
    }

    private void closeDialog() {
        mediaPlayer.stop();
        mediaPlayer.release();
        vibrator.cancel();
        alarmDialog.dismiss();
        finish();
    }

}


