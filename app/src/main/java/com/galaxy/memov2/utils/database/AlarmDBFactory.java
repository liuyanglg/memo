package com.galaxy.memov2.utils.database;

import android.content.Context;
import android.util.Log;

/**
 * Created by liuya on 2017/4/7.
 */

public class AlarmDBFactory {
    public static AlarmDBHelper alarmDBHelper =null;
    public static AlarmDBHelper getInstance(Context context){
        if(alarmDBHelper ==null){
            Log.i("MemoDBFactory","create memoDb by dbFactory");
            alarmDBHelper = new AlarmDBHelper(context);
        }
        return alarmDBHelper;
    }

}
