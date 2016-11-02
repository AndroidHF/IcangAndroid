package com.example.hufeng.com.example.hufeng.utils;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hufeng on 2016/4/19.
 */
public class StringFormatUtil {
    //设置时间的格式：天/月/年 时：分，例如：19/04/2016 10:03
    public static final SimpleDateFormat SOCAILFINANCE = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    //设置时间的格式：年/月/日 时：分：秒，例如：2016/04/19 10:03：55
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //设置时间的格式：年/月/日 时：分：秒，例如：2016/04/19 10:04
    public static final SimpleDateFormat DATE_FORMAT_DAY = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    //设置今天的时间格式：时：分 am/pm，例如：10：08 AM
    public static final SimpleDateFormat DATE_FORMAT_TODAY = new SimpleDateFormat("h:mm a");
    //设置昨天的时间格式：星期几 时：分 am/pm，例如：SUN 1:10 PM
    public static final SimpleDateFormat DATE_FORMAT_YESTERDAY = new SimpleDateFormat("E h:mm a");
    //设置更早的时间的格式：星期几 月（英文缩写）日，例如：Sat Mar 20
    public static final SimpleDateFormat DATE_FORMAT_OLDER = new SimpleDateFormat("E MMM d");

    /**
     * 获取时间
     * @param time 时间格式：dd/MM/yyyy HH:mm
     * @return
     */
    public static String getSimpleDate(String time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d;
        String date = "";
        try {
            d = format.parse(time);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String dateString = formatter.format(d);
            date = dateString.substring(0,10);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    /**
     * 获取时间
     * @param time 时间的格式：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getSimpleDateNew(String time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d;
        String date = "";
        try {
            d = format.parse(time);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(d);
            date = dateString;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取长时间
     * @param time 时间格式：yyyy-MM-dd HH:mm
     * @return
     */
    public static String getTimeFromLong(long time){
        return DATE_FORMAT_DAY.format(time);
    }

    /**
     * 获取当前的时间
     * @return 时间格式：yyyy-MM-dd HH:mm
     */
    public static String getNowTime(){
        return DATE_FORMAT_DAY.format(new Date());
    }

    /**
     * 获取当前最新的时间
     * @return时间格式：yyyy-MM-dd HH:mm:ss
     */
    public static String getNowTimeNew(){
        return DATE_FORMAT.format(new Date());
    }

    public static CharSequence getRelativeTimeSpanString(String created){
        try {
            return DateUtils.getRelativeTimeSpanString(DATE_FORMAT.parse(created).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return created;
    }

    /**
     * 获取今天的时间
     * @param crated
     * @return  时间格式：h:mm a
     */
    public static String getTodayTimeString(String crated){
        try {
            return DATE_FORMAT_TODAY.format(DATE_FORMAT.parse(crated));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return crated;
    }

    /**
     * 获取昨天的时间
     * @param crated
     * @return  时间格式：E h:mm a
     */
    public static String getYesterdayTimeString(String crated){
        try {
            return DATE_FORMAT_YESTERDAY.format(DATE_FORMAT.parse(crated));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return crated;
    }

    /**
     * 获取更长的时间
     * @param crated
     * @return  时间格式：E MMM d
     */
    public static String getOlderTimeString(String crated){
        try {
            return DATE_FORMAT_OLDER.format(DATE_FORMAT.parse(crated));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return crated;
    }

    /**
     * 获取时间
     * @param crated
     * @return  时间格式：dd/MM/yyyy HH:mm
     */
    public static String getSocailfnanceTimeString(String crated){
        try {
            return SOCAILFINANCE.format(DATE_FORMAT.parse(crated));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return crated;
    }
}
