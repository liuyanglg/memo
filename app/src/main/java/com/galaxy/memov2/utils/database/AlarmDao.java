package com.galaxy.memov2.utils.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.galaxy.memov2.entity.Alarm;
import com.galaxy.memov2.entity.Record;
import com.galaxy.memov2.utils.Constant;
import com.galaxy.memov2.utils.date.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuya on 2017/4/7.
 */

public class AlarmDao {
    private static AlarmDBHelper alarmDBHelper = null;
    private static String TAG = "AlarmDao";

    public static int daoAddAlarm(Context context, Alarm alarm) {
        if (alarmDBHelper == null) {
            alarmDBHelper = AlarmDBFactory.getInstance(context);
        }
        SQLiteDatabase db = alarmDBHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(Constant.ALARM_CLOCK_ID, alarm.alarmId);
        cv.put(Constant.ALARM_CLOCK_DETAIL, alarm.detail);
        cv.put(Constant.ALARM_CLOCK_DATETIME, DateUtils.DateToStr(alarm.alarmDate) + "");
        Log.i(TAG, "add alarm");
        Log.i(TAG, alarm.detail);
        long result = db.insert(Constant.ALARM_CLOCK_TABLE_NAME, null, cv);
        db.close();
        if (result > 0)
            return Constant._SUCCESS;
        else
            return Constant._FAIL;
    }

    public static int daoUpdateAlarm(Context context, Alarm alarm) {
        if (alarmDBHelper == null) {
            alarmDBHelper = AlarmDBFactory.getInstance(context);
        }
        SQLiteDatabase db = alarmDBHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(Constant.ALARM_CLOCK_ID, alarm.alarmId);
        cv.put(Constant.ALARM_CLOCK_DETAIL, alarm.detail);
        cv.put(Constant.ALARM_CLOCK_DATETIME, DateUtils.DateToStr(alarm.alarmDate) + "");

        String whereClause = Constant.ALARM_CLOCK_TABLE_ID + "=?";
        String[] whereArgs = {String.valueOf(alarm.id)};
        int result = db.update(Constant.ALARM_CLOCK_TABLE_NAME, cv, whereClause, whereArgs);
        Log.i(TAG, "update : " + alarm.toString());
        db.close();
        if (result > 0)
            return Constant._SUCCESS;
        else
            return Constant._FAIL;
    }

    public static int daoDeleteAlarm(Context context, Alarm alarm) {
        if (alarmDBHelper == null) {
            alarmDBHelper = AlarmDBFactory.getInstance(context);
        }
        SQLiteDatabase db = alarmDBHelper.getWritableDatabase();
        String whereClause = Constant.ALARM_CLOCK_TABLE_ID + "=?";
        String[] whereArgs = {String.valueOf(alarm.id)};
        int result = db.delete(Constant.ALARM_CLOCK_TABLE_NAME, whereClause, whereArgs);
        Log.i(TAG, "delete : " + alarm.toString());
        db.close();
        if (result > 0)
            return Constant._SUCCESS;
        else
            return Constant._FAIL;
    }

    public static ArrayList<Alarm> daoFindAllAlarm(Context context) {
        ArrayList<Alarm> alarms = new ArrayList<Alarm>();
        if (alarmDBHelper == null) {
            alarmDBHelper = AlarmDBFactory.getInstance(context);
        }
        SQLiteDatabase db = alarmDBHelper.getWritableDatabase();
        Cursor cursor = db.query(Constant.ALARM_CLOCK_TABLE_NAME, null, null, null, null, null, Constant.ALARM_CLOCK_TABLE_ID + " desc ");
        while (cursor.moveToNext()) {
            Alarm alarm = new Alarm();
            alarm.id = cursor.getInt(cursor.getColumnIndex(Constant.ALARM_CLOCK_TABLE_ID));
            alarm.alarmId = cursor.getInt(cursor.getColumnIndex(Constant.ALARM_CLOCK_ID));
            alarm.detail = cursor.getString(cursor.getColumnIndex(Constant.ALARM_CLOCK_DETAIL));
            alarm.alarmDate = DateUtils.StrToDate(cursor.getString(cursor.getColumnIndex(Constant.ALARM_CLOCK_DATETIME)));

            alarms.add(alarm);
        }
        db.close();
        for (int i = 0; i < alarms.size(); i++) {
            Log.i(TAG, "query data is :" + alarms.get(i).toString());
        }
        Log.i(TAG, "find all...");
        return alarms;
    }


    public static void saveOrUpdate(Context context, Alarm alarm) {
        daoDeleteByID(context, alarm);
        daoAddAlarm(context, alarm);
    }

    public static int daoDeleteByID(Context context, Alarm alarm) {
        Log.i(TAG, "alarm_id: " + alarm.alarmId);
        if (alarmDBHelper == null) {
            alarmDBHelper = AlarmDBFactory.getInstance(context);
        }
        SQLiteDatabase db = alarmDBHelper.getWritableDatabase();
        String whereClause = Constant.ALARM_CLOCK_ID + " =? ";
        String[] whereArgs = {String.valueOf(alarm.alarmId)};
        int result = db.delete(Constant.ALARM_CLOCK_TABLE_NAME, whereClause, whereArgs);
        Log.i(TAG, "result has found: " + result);
        Log.i(TAG, "delete : " + alarm.toString());
        db.close();
        if (result > 0)
            return Constant._SUCCESS;
        else
            return Constant._FAIL;
    }

    public static int daoDeleteByID(Context context, int alarmId) {
        Log.i(TAG, "alarm_id: " + alarmId);
        if (alarmDBHelper == null) {
            alarmDBHelper = AlarmDBFactory.getInstance(context);
        }
        SQLiteDatabase db = alarmDBHelper.getWritableDatabase();
        String whereClause = Constant.ALARM_CLOCK_ID + " =? ";
        String[] whereArgs = {String.valueOf(alarmId)};
        int result = db.delete(Constant.ALARM_CLOCK_TABLE_NAME, whereClause, whereArgs);
        Log.i(TAG, "result has found: " + result);
        db.close();
        if (result > 0)
            return Constant._SUCCESS;
        else
            return Constant._FAIL;
    }
}
