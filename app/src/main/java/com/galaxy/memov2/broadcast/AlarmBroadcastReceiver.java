package com.galaxy.memov2.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.galaxy.memov2.activity.AlarmActivity;

import static com.galaxy.memov2.utils.Constant.ALARM_ACTION;
import static com.galaxy.memov2.utils.Constant.ALARM_DETAIL;
import static com.galaxy.memov2.utils.Constant.ALARM_ID;
import static com.galaxy.memov2.utils.Constant.ALARM_RING;
import static com.galaxy.memov2.utils.Constant.ALARM_TAG;


/**
 * Created by liuya on 2017/4/25.
 */

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ALARM_ACTION)) {
            int id = intent.getIntExtra(ALARM_ID,0);
            String detail = intent.getStringExtra(ALARM_DETAIL);
            String uri = intent.getStringExtra(ALARM_RING);
            Log.i(ALARM_TAG, "receive a alarm:" + id + ", " + detail);
            Intent clockIntent = new Intent(context, AlarmActivity.class);
            clockIntent.setAction(ALARM_ACTION);
            clockIntent.putExtra(ALARM_ID, id);
            clockIntent.putExtra(ALARM_DETAIL, detail);
            clockIntent.putExtra(ALARM_RING, uri);
            clockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(clockIntent);
        }
    }
}
