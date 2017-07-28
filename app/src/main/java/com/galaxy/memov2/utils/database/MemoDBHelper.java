package com.galaxy.memov2.utils.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.galaxy.memov2.utils.Constant.DATABASE_NAME;
import static com.galaxy.memov2.utils.Constant.DATABASE_VERSION;
import static com.galaxy.memov2.utils.Constant.ALARM_DATETIME;
import static com.galaxy.memov2.utils.Constant.CREATE_DATETIME;
import static com.galaxy.memov2.utils.Constant.DETAIL;
import static com.galaxy.memov2.utils.Constant.ID;
import static com.galaxy.memov2.utils.Constant.PROGRESS;
import static com.galaxy.memov2.utils.Constant.RATE;
import static com.galaxy.memov2.utils.Constant.FINISH;
import static com.galaxy.memov2.utils.Constant.TABLE_NAME;

/**
 * Created by Galaxy on 2017/3/22.
 */

public class MemoDBHelper extends SQLiteOpenHelper {
    private static final String TAG = "memoDB";

    public MemoDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="CREATE TABLE  "+TABLE_NAME+" ("
                + ID +" INTEGER PRIMARY KEY AUTOINCREMENT "+" , "
                + DETAIL +" TEXT "+" , "
                + CREATE_DATETIME +" TEXT "+" , "
                + ALARM_DATETIME +" TEXT "+" , "
                + RATE +" INTEGER "+" , "
                + PROGRESS +" INTEGER "+" , "
                + FINISH +" INTEGER "
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
