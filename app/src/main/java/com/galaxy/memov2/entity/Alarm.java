package com.galaxy.memov2.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by liuya on 2017/4/15.
 */

public class Alarm{
    public String detail;
    public int id;
    public int alarmId;
    public Date alarmDate;

    @Override
    public String toString() {
        return "id:" + id + "alarm_id:" + alarmId +" ,detail:" + detail
                + " ,alarmDate:" + alarmDate;
    }
}
