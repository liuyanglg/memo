package com.galaxy.memov2.utils.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import static android.content.ContentValues.TAG;
import static com.galaxy.memov2.utils.Constant.ALARM_ACTION;
import static com.galaxy.memov2.utils.Constant.ALARM_DETAIL;
import static com.galaxy.memov2.utils.Constant.ALARM_ID;
import static com.galaxy.memov2.utils.Constant.ALARM_RING;

/**
 * Created by liuya on 2017/4/25.
 */

public class AlarmUtils {
    public static String TAG = "AlarmUtils";

    public static void setAlarm(Context context, long millisecond,int id ,String tips,Uri ringUri) {
        Intent intent = new Intent(ALARM_ACTION);
        intent.putExtra(ALARM_ID, id);
        intent.putExtra(ALARM_DETAIL, tips);
        intent.putExtra(ALARM_RING, ringUri.toString());
        Log.d(TAG, ringUri.toString());
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmSender = PendingIntent.getBroadcast(context, id,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        /*重复时间*/
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, millisecond, alarmSender);
            Log.i(TAG, "has set a alarm");
        }
        else {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,millisecond,0,alarmSender);
        }
    }

    public static void cancelAlarm(Context context, int id) {
        Intent intent = new Intent(ALARM_ACTION);
        intent.putExtra(ALARM_ID, id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}
