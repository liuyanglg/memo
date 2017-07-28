package com.galaxy.memov2.utils.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.galaxy.memov2.entity.Record;
import com.galaxy.memov2.utils.Constant;
import com.galaxy.memov2.utils.date.DateUtils;

import java.util.ArrayList;

/**
 * Created by liuya on 2017/4/7.
 */

public class MemoDao {
    private static MemoDBHelper memoDBHelper = null;
    private static String TAG="MemoDao";
    public static int daoAddRecord(Context context, Record record) {
        if (memoDBHelper == null) {
            memoDBHelper = MemoDBFactory.getInstance(context);
        }
        SQLiteDatabase db = memoDBHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(Constant.DETAIL, record.detail);
        cv.put(Constant.CREATE_DATETIME, DateUtils.DateToStr(record.createDate)+ "");
        cv.put(Constant.ALARM_DATETIME, DateUtils.DateToStr(record.alarmDate) + "");
        cv.put(Constant.RATE, record.rate);
        cv.put(Constant.FINISH, record.isFinish);
        Log.i(TAG, "add memo");
        Log.i(TAG, record.detail);
        long result = db.insert(Constant.TABLE_NAME, null, cv);
        db.close();
        if (result > 0)
            return Constant._SUCCESS;
        else
            return Constant._FAIL;
    }

    public static int daoUpdateRecord(Context context, Record record) {
        if (memoDBHelper == null) {
            memoDBHelper = MemoDBFactory.getInstance(context);
        }
        SQLiteDatabase db = memoDBHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(Constant.DETAIL, record.detail);
        cv.put(Constant.CREATE_DATETIME, DateUtils.DateToStr(record.createDate) + "");
        cv.put(Constant.ALARM_DATETIME, DateUtils.DateToStr(record.alarmDate) + "");
        cv.put(Constant.RATE, record.rate);
        cv.put(Constant.FINISH, record.isFinish);

        String whereClause = Constant.ID + "=?";
        String[] whereArgs = {String.valueOf(record.id)};
        int result = db.update(Constant.TABLE_NAME, cv, whereClause, whereArgs);
        Log.i(TAG, "update : " + record.toString());
        db.close();
        if (result > 0)
            return Constant._SUCCESS;
        else
            return Constant._FAIL;
    }

    public static int daoDeleteRecord(Context context, Record record) {
        if (memoDBHelper == null) {
            memoDBHelper = MemoDBFactory.getInstance(context);
        }
        SQLiteDatabase db = memoDBHelper.getWritableDatabase();
        String whereClause = Constant.ID + "=?";
        String[] whereArgs = {String.valueOf(record.id)};
        int result = db.delete(Constant.TABLE_NAME, whereClause, whereArgs);
        Log.i(TAG, "delete : " + record.toString());
        db.close();
        if (result > 0)
            return Constant._SUCCESS;
        else
            return Constant._FAIL;
    }

    public static ArrayList<Record> daoFindAllRecord(Context context) {
        ArrayList<Record> records = new ArrayList<Record>();
        if (memoDBHelper == null) {
            memoDBHelper = MemoDBFactory.getInstance(context);
        }
        SQLiteDatabase db = memoDBHelper.getWritableDatabase();
        Cursor cursor = db.query(Constant.TABLE_NAME, null, null, null, null, null, Constant.ID +" desc ");
        while (cursor.moveToNext()) {
            Record record = new Record();
            record.id = cursor.getInt(cursor.getColumnIndex(Constant.ID));
            record.detail = cursor.getString(cursor.getColumnIndex(Constant.DETAIL));
            record.createDate = DateUtils.StrToDate(cursor.getString(cursor.getColumnIndex(Constant.CREATE_DATETIME)));
            record.alarmDate = DateUtils.StrToDate(cursor.getString(cursor.getColumnIndex(Constant.ALARM_DATETIME)));
            record.rate = cursor.getInt(cursor.getColumnIndex(Constant.RATE));
            record.isFinish = cursor.getInt(cursor.getColumnIndex(Constant.FINISH));
            
            records.add(record);
        }
        db.close();
        for(int i=0;i<records.size();i++){
            Log.i(TAG, records.get(i).toString());
        }
        Log.i(TAG, "find all...");
        return records;
    }
}
