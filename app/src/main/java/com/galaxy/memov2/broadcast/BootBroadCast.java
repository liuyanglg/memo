package com.galaxy.memov2.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.galaxy.memov2.activity.MainActivity;
import com.galaxy.memov2.service.AlarmService;

/**
 * Created by liuya on 2017/4/28.
 */

public class BootBroadCast extends BroadcastReceiver {
    public static String TAG = "BootBroadCast";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.i(TAG, "BootBroadCast receive a reboot");

            Intent service = new Intent(context, AlarmService.class);
            context.startService(service);
            Log.i(TAG, "start alarm service ....");
                        /* 应用开机自启动 */
//            Intent mainIntent = new Intent(context,
//                    MainActivity.class);
//            mainIntent.setAction("android.intent.action.MAIN");
//            mainIntent.addCategory("android.intent.category.LAUNCHER");
//            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(mainIntent);
        }
    }
}
