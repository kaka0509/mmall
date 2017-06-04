package com.mmall.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * Created by Skye on 2017/6/4.
 */
public class DateTimeUtil {

    public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 将时间字符串转化为Date格式
     * @param dateTimeStr 时间字符串
     * @param formatStr   格式
     * @return
     */
    public static Date strToDate(String dateTimeStr, String formatStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatStr);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }


    /**
     * 将Date格式时间转化成字符串
     * @param date      时间
     * @param formatStr 格式
     * @return
     */
    public static String dateToStr(Date date, String formatStr) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(formatStr);

    }

    //使用标准日期格式的重载
    public static Date strToDate(String dateTimeStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    //使用标准日期格式的重载
    public static String dateToStr(Date date) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD_FORMAT);

    }


}
