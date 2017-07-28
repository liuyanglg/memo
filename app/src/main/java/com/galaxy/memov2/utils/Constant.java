package com.galaxy.memov2.utils;

/**
 * Created by liuya on 2017/4/7.
 */

public class Constant {
    /*Memo数据库表字段*/
    public final static String DATABASE_NAME = "memo.db";
    public final static int DATABASE_VERSION = 1;
    public final static String TABLE_NAME = "memo_table";
    public final static String ID = "id";//    提醒事件id
    public final static String DETAIL = "detail";//提醒内容
    public final static String CREATE_DATETIME = "create_datetime";//起始时间
    public final static String ALARM_DATETIME = "alarm_datetime";//提醒时间
    public final static String RATE = "rate";//完成率
    public final static String PROGRESS = "progress";//进度
    public final static String FINISH = "finish";//事件是否失效
    /*标志字段*/
    public final static int IS_FINISH = 1;

    /*数据库操作返回码*/
    public final static int _SUCCESS = 1;
    public final static int _FAIL = -1;
    /*日期格式化*/
    public final static String DATE_FORMAT = "yyyy-MM-dd HH:mm";
    /*闹钟提示*/
    public static final String ALARM_ACTION = "com.galaxy.memov2.alarm.clock";
    public static final String ALARM_ID = "alarm_id";
    public static final String ALARM_DETAIL = "alarm_detail";
    public static final String ALARM_RING = "alarm_ring";
    public static final long ALARM_AGAIN_MILLIS = 10*60*1000;//10分钟

    public static final String ALARM_TAG = "Broadcast";

    /*进度相关*/
    public static final int AFFAIR_PROGRESS_00 = 0;
    public static final int AFFAIR_PROGRESS_01 = 1;
    public static final int AFFAIR_PROGRESS_02 = 2;
    public static final int AFFAIR_PROGRESS_03 = 3;
    public static final int AFFAIR_PROGRESS_04 = 4;
    public static final int PROGRESS_FINISH = 5;

    /*Memo数据库表字段*/
    public final static String ALARM_CLOCK_DATABASE_NAME = "alarm.db";
    public final static int ALARM_CLOCK_DATABASE_VERSION = 1;
    public final static String ALARM_CLOCK_TABLE_NAME = "alarm_table";
    public final static String ALARM_CLOCK_TABLE_ID = "id";//    id
    public final static String ALARM_CLOCK_ID = "alarm_id";//    闹钟id
    public final static String ALARM_CLOCK_DETAIL = "detail";//提醒内容
    public final static String ALARM_CLOCK_DATETIME = "alarm_datetime";//提醒时间

}
