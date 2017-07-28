package com.galaxy.memov2.utils.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.galaxy.memov2.utils.Constant.ALARM_CLOCK_DATABASE_NAME;
import static com.galaxy.memov2.utils.Constant.ALARM_CLOCK_DATABASE_VERSION;
import static com.galaxy.memov2.utils.Constant.ALARM_CLOCK_DATETIME;
import static com.galaxy.memov2.utils.Constant.ALARM_CLOCK_DETAIL;
import static com.galaxy.memov2.utils.Constant.ALARM_CLOCK_ID;
import static com.galaxy.memov2.utils.Constant.ALARM_CLOCK_TABLE_ID;
import static com.galaxy.memov2.utils.Constant.ALARM_CLOCK_TABLE_NAME;
import static com.galaxy.memov2.utils.Constant.ALARM_DATETIME;
import static com.galaxy.memov2.utils.Constant.CREATE_DATETIME;
import static com.galaxy.memov2.utils.Constant.DATABASE_NAME;
import static com.galaxy.memov2.utils.Constant.DATABASE_VERSION;
import static com.galaxy.memov2.utils.Constant.DETAIL;
import static com.galaxy.memov2.utils.Constant.FINISH;
import static com.galaxy.memov2.utils.Constant.ID;
import static com.galaxy.memov2.utils.Constant.PROGRESS;
import static com.galaxy.memov2.utils.Constant.RATE;
import static com.galaxy.memov2.utils.Constant.TABLE_NAME;

/**
 * Created by Galaxy on 2017/3/22.
 */

public class AlarmDBHelper extends SQLiteOpenHelper {
    private static final String TAG = "AlarmDB";

    public AlarmDBHelper(Context context) {
        super(context, ALARM_CLOCK_DATABASE_NAME, null, ALARM_CLOCK_DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="CREATE TABLE  "+ALARM_CLOCK_TABLE_NAME+" ("
                + ALARM_CLOCK_TABLE_ID +" INTEGER PRIMARY KEY AUTOINCREMENT "+" , "
                + ALARM_CLOCK_ID +" INTEGER "+" , "
                + ALARM_CLOCK_DETAIL +" TEXT "+" , "
                + ALARM_CLOCK_DATETIME +" TEXT "
                +" ); ";
        Log.i(TAG,"create database......");
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        Log.i(TAG,"create database......");
//        db.execSQL(sql);
        onCreate(db);
    }
}
