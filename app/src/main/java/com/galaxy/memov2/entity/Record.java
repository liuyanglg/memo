package com.galaxy.memov2.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by liuya on 2017/4/15.
 */

public class Record implements Parcelable {
    public String detail;
    public Date createDate;
    public boolean isSelect;
    public int opStatus;//操作状态
    public int id;
    public Date alarmDate;
    public int rate;
    public int progress;
    public int isFinish;

    public Record() {
    }

    protected Record(Parcel in) {
        detail = in.readString();
        createDate = (Date) in.readSerializable();
        isSelect = in.readByte() != 0;
        opStatus = in.readInt();
        id = in.readInt();
        alarmDate = (Date) in.readSerializable();
        rate = in.readInt();
        progress = in.readInt();
        isFinish = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(detail);
        dest.writeSerializable(createDate);
        dest.writeByte((byte) (isSelect ? 1 : 0));
        dest.writeInt(opStatus);
        dest.writeInt(id);
        dest.writeSerializable(alarmDate);
        dest.writeInt(rate);
        dest.writeInt(progress);
        dest.writeInt(isFinish);

    }

    public static final Creator<Record> CREATOR = new Creator<Record>() {
        @Override
        public Record createFromParcel(Parcel source) {
            return new Record(source);
        }

        @Override
        public Record[] newArray(int size) {
            return new Record[size];
        }
    };

    @Override
    public String toString() {
        return "id:" + id + " ,detail:" + detail + " ,createDate:"
                + createDate + " ,alarmDate:" + alarmDate
                + " ,rate:" + rate + " ,isFinish:" + isFinish
                + " ,progress:" + progress;
    }
}
