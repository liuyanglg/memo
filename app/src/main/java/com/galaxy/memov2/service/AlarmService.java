package com.galaxy.memov2.service;

import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.galaxy.memov2.asynctask.AlarmDaoAsyncTask;
import com.galaxy.memov2.entity.Alarm;
import com.galaxy.memov2.utils.alarm.AlarmUtils;
import com.galaxy.memov2.utils.database.AlarmDao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.galaxy.memov2.entity.OperateConstant._DELETE;
import static com.galaxy.memov2.entity.OperateConstant._FIND_ALL;
import static org.greenrobot.eventbus.EventBus.TAG;

/**
 * Created by liuya on 2017/4/28.
 */

public class AlarmService extends Service {
    public static String TAG = "BootBroadCast";
    private Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
    private List<Alarm> alarms = new ArrayList<>();
    private HashSet<Alarm> alarmsTemp = new HashSet<>();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "alarm service has started ...");

        setAlarm();
        clearTimeoutAlarm();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void setAlarm() {
        new AlarmDaoAsyncTask(this, alarms).execute(_FIND_ALL);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "Handler is run");
                Log.i(TAG, "alarm size is: " + alarms.size());
                long currentMills = System.currentTimeMillis();
                int size = alarms.size();
                Alarm alarm;
                for (int i = 0; i < size; i++) {
                    alarm = alarms.get(i);
                    alarmsTemp.add(alarm);
                    if (alarm.alarmDate.getTime() > currentMills) {
                        AlarmUtils.setAlarm(AlarmService.this, alarm.alarmDate.getTime(), alarm.alarmId, alarm.detail, ringUri);
                    }
                }

            }
        }, 3 * 1000);
    }

    private void clearTimeoutAlarm() {
        alarmsTemp.clear();
        alarms.addAll(alarms);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (Alarm a : alarmsTemp) {
                    long currentMills = System.currentTimeMillis();
                    if (a.alarmDate.getTime() <= currentMills) {
                        if (alarms.contains(a)) {
                            alarms.remove(a);
                            new AlarmDaoAsyncTask(AlarmService.this, a).execute(_DELETE);
                        }
                    }
                }
                alarmsTemp.clear();
            }
        }, 5 * 1000);

    }

}
