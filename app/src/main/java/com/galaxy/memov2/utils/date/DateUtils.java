package com.galaxy.memov2.utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.galaxy.memov2.utils.Constant.DATE_FORMAT;

/**
 * Created by liuya on 2017/4/7.
 */

public class DateUtils {
    /**
     * 日期转换成字符串
     *
     * @param date
     * @return str
     */
    public static String DateToStr(Date date) {
        if(date==null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        String str = format.format(date);
        return str;
    }

    /**
     * 字符串转换成日期
     *
     * @param str
     * @return date
     */
    public static Date StrToDate(String str) {
        if(str==""||str==null)
            return null;
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}

