package com.buycolle.aicang.util;

import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by joe on 15/12/30.
 */
public class StringFormatUtil {
    public static final SimpleDateFormat SOCAILFINANCE = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_DAY = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    /**
     * Should look like "9:09 AM".
     */
    public static final SimpleDateFormat DATE_FORMAT_TODAY = new SimpleDateFormat("h:mm a");

    /**
     * Should look like "Sun 1:56 PM".
     */
    public static final SimpleDateFormat DATE_FORMAT_YESTERDAY = new SimpleDateFormat("E h:mm a");

    /**
     * Should look like "Sat Mar 20".
     */
    public static final SimpleDateFormat DATE_FORMAT_OLDER = new SimpleDateFormat("E MMM d");

    /**
     * @param time
     * @return dd/MM/yyyy
     */
    public static String getSimpleDate(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d;
        String date = "";
        try {
            d = format.parse(time);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String dateString = formatter.format(d);
            date = dateString.substring(0, 10);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取时间
     *
     * @param time yyyy-MM-dd
     * @return
     */
    public static String getSimpleDateNew(String time) {
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
     * @param timestr
     * @return HH:mm
     */
    public static String getSimpleTime(String timestr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d;
        String time = "";
        try {
            d = format.parse(timestr);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String dateString = formatter.format(d);
            time = dateString.substring(11, dateString.length());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static String getTimeFromDate(Date date) {
        return DATE_FORMAT_DAY.format(date);
    }

    public static String getTimeFromLong(long time) {
        return DATE_FORMAT_DAY.format(time);
    }

    public static CharSequence getRelativeTimeSpanString(String created) {
        try {
            return DateUtils.getRelativeTimeSpanString(DATE_FORMAT.parse(created).getTime(), new Date().getTime(), DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE);
        } catch (ParseException e) {
            return created;
        }
    }

    public static String getNowTime() {
        return DATE_FORMAT_DAY.format(new Date());
    }

    public static String getNowTimeNew() {
        return DATE_FORMAT.format(new Date());
    }

    /**
     * Returns a format that will look like: "9:09 AM".
     */
    public static String getTodayTimeString(String created) {
        try {
            return DATE_FORMAT_TODAY.format(DATE_FORMAT.parse(created));
        } catch (ParseException e) {
            return created;
        }
    }

    /**
     * Returns a format that will look like: "Sun 1:56 PM".
     */
    public static String getYesterdayTimeString(String created) {
        try {
            return DATE_FORMAT_YESTERDAY.format(DATE_FORMAT.parse(created));
        } catch (ParseException e) {
            return created;
        }
    }

    /**
     * Returns a format that will look like: "Sat Mar 20".
     */
    public static String getOlderTimeString(String created) {
        try {
            return DATE_FORMAT_OLDER.format(DATE_FORMAT.parse(created));
        } catch (ParseException e) {
            return created;
        }
    }

    /**
     * Reads an inputstream into a string.
     */
    public static String inputStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }

    /**
     * 返回 yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String createServerDateFormatV1() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }

    public static String getCurTime(SimpleDateFormat dateFormate) {
        Date date = new Date(System.currentTimeMillis());
        return dateFormate.format(date);

    }

    /**
     * @param begin       开始时间
     * @param end         当前时间
     * @param dateFormate 时间格式
     * @return 两个时间点之间的差，单位小时
     */
    public static long compareTime(String begin, String end, SimpleDateFormat dateFormate) {
        long hours = 0;
        try {
            Date date1 = dateFormate.parse(begin);
            Date date2 = dateFormate.parse(end);
            long diff = date2.getTime() - date1.getTime();
            // int min= diff/(1000 * 60 ); //分钟
            hours = diff / (1000 * 60 * 60);// 小时
        } catch (ParseException e) {

            e.printStackTrace();
        }

        return hours;
    }

    public static String format(int num) {
        String out = String.valueOf(num);
        if (out.length() == 1) {
            out = "0" + out;
        }
        return out;
    }

    public static String getCurrentWeek(String date, boolean head) {// 获得当前星期：true：星期日
        // false：星期六
        int i = date.indexOf("-");
        int j = date.lastIndexOf("-");
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(i + 1, j));
        int day = Integer.parseInt(date.substring(j + 1, date.length()));
        Calendar cal = Calendar.getInstance(Locale.CHINA);
        cal.set(year, month - 1, day);
        int plus;
        if (head)
            plus = 1 - cal.get(Calendar.DAY_OF_WEEK);
        else
            plus = 7 - cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE, plus);
        return cal.get(Calendar.YEAR) + "-" + format(cal.get(Calendar.MONTH) + 1) + "-" + format(cal.get(Calendar.DAY_OF_MONTH));
    }

    public static Calendar getCurrentWeeks(String date, boolean head) {// 获得当前星期：true：星期日
        // false：星期六
        int i = date.indexOf("-");
        int j = date.lastIndexOf("-");
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(i + 1, j));
        int day = Integer.parseInt(date.substring(j + 1, date.length()));
        Calendar cal = Calendar.getInstance(Locale.CHINA);
        cal.set(year, month - 1, day);
        int plus;
        if (head)
            plus = 1 - cal.get(Calendar.DAY_OF_WEEK);
        else
            plus = 7 - cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE, plus);
        return cal;
        // return cal.get(Calendar.YEAR) + "-"
        // + format(cal.get(Calendar.MONTH) + 1) + "-"
        // + format(cal.get(Calendar.DAY_OF_MONTH));
    }

    public static String[] getAllWeeksSet() {// 获得本月的所有星期
        String[] weeks = new String[5];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, lastDate.get(Calendar.DATE));// 设为当前月的1号
        lastDate.add(Calendar.DATE, -14);
        String firstday = "";
        String date = "";
        String head = "";
        int numWeek = 0;
        for (int i = 0; i < 5; i++) {
            firstday = sdf.format(lastDate.getTime());
            Calendar cal = getCurrentWeeks(firstday, true);
            numWeek = cal.get(Calendar.DAY_OF_WEEK_IN_MONTH);
            date = sdf.format(cal.getTime()) + "至" + sdf.format(getCurrentWeeks(firstday, false).getTime());
            head = cal.get(Calendar.YEAR) + "年" + format(cal.get(Calendar.MONTH) + 1) + "月第" + numWeek + "周";
            weeks[i] = head + " " + date;
            lastDate.add(Calendar.DATE, 7);
        }
        return weeks;
    }

    public static String[] getAllMonthSet(int m_year) {
        String[] months = new String[14];
        months[0] = (m_year - 1) + "年12月";
        for (int i = 1; i < 13; i++) {
            months[i] = m_year + "年" + i + "月";
        }
        months[13] = (m_year + 1) + "年1月";
        return months;
    }

    public static int[] getDate() {
        int[] res = new int[3];
        Calendar cal = Calendar.getInstance(Locale.CHINA);// 获得当前日期
        res[0] = cal.get(Calendar.YEAR);
        res[1] = cal.get(Calendar.MONTH);
        res[2] = cal.get(Calendar.DAY_OF_MONTH);
        return res;
    }

    public static String[] getAllQuarter(int m_year) {
        String[] quarter = new String[5];
        quarter[0] = (m_year - 1) + "年第4季度 " + (m_year - 1) + "-10-1至" + (m_year - 1) + "-12-31";
        quarter[1] = m_year + "年第1季度 " + m_year + "-1-1至" + m_year + "-3-31";
        quarter[2] = m_year + "年第2季度 " + m_year + "-4-1至" + m_year + "-6-30";
        quarter[3] = m_year + "年第3季度 " + m_year + "-7-1至" + m_year + "-9-30";
        quarter[4] = m_year + "年第4季度 " + m_year + "-10-1至" + m_year + "-12-31";
        return quarter;
    }

    public static String[] getAllyear(int m_year) {
        String[] yearset = new String[3];
        yearset[0] = (m_year - 1) + "年";
        yearset[1] = m_year + "年";
        yearset[2] = (m_year + 1) + "年";
        return yearset;
    }

    /**
     * 比较时间
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean comparisonDate(String date1, String date2) {
        try {
            Date start = DATE_FORMAT_DAY.parse(date1);
            Date end = DATE_FORMAT_DAY.parse(date2);
            long between = (end.getTime() - start.getTime()) / 1000;
            if (between <= 0) {
                return false;
            }
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean comparisonDateNew(String date1, String date2) {
        try {
            Date start = DATE_FORMAT.parse(date1);
            Date end = DATE_FORMAT.parse(date2);
            long between = (end.getTime() - start.getTime()) / 1000;
            if (between <= 0) {
                return false;
            }
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static ArrayList<String> getWorkTimeList(int time1, int time2, int time3, int time4, int time5, int time6, int time7) {
        ArrayList<String> data = new ArrayList<>();
        if (time1 == 1) {
            data.add("周一：上午");
        } else if (time1 == 2) {
            data.add("周一：下午");
        } else if (time1 == 5) {
            data.add("周一：上午 下午");
        } else if (time1 == 7) {

        }
        if (time2 == 1) {
            data.add("周二：上午");
        } else if (time2 == 2) {
            data.add("周二：下午");
        } else if (time2 == 5) {
            data.add("周二：上午 下午");
        } else if (time2 == 7) {

        }
        if (time3 == 1) {
            data.add("周三：上午");
        } else if (time3 == 2) {
            data.add("周三：下午");
        } else if (time3 == 5) {
            data.add("周三：上午 下午");
        } else if (time3 == 7) {

        }
        if (time4 == 1) {
            data.add("周四：上午");
        } else if (time4 == 2) {
            data.add("周四：下午");
        } else if (time4 == 5) {
            data.add("周四：上午 下午");
        } else if (time4 == 7) {

        }
        if (time5 == 1) {
            data.add("周五：上午");
        } else if (time5 == 2) {
            data.add("周五：下午");
        } else if (time5 == 5) {
            data.add("周五：上午 下午");
        } else if (time5 == 7) {

        }
        if (time6 == 1) {
            data.add("周六：上午");
        } else if (time6 == 2) {
            data.add("周六：下午");
        } else if (time6 == 5) {
            data.add("周六：上午 下午");
        } else if (time6 == 7) {

        }
        if (time7 == 1) {
            data.add("周日：上午");
        } else if (time7 == 2) {
            data.add("周日：下午");
        } else if (time7 == 5) {
            data.add("周日：上午 下午");
        } else if (time7 == 7) {

        }
        return data;
    }


    /**
     * 获取订单的规格
     *
     * @param value
     * @return
     */
    public static String getSpecifications(int value) {
        if (value == 1) {
            return "小份";
        } else if (value == 2) {
            return "中份";
        } else {
            return "大份";
        }
    }

    /**
     * 获取订单的口味
     *
     * @param value
     * @return
     */
    public static String getTast(int value) {
        if (value == 1) {
            return "不辣";
        } else if (value == 2) {
            return "微辣";
        } else if (value == 3) {
            return "中辣";
        } else {
            return "特辣";
        }
    }

    public static String getTimeBySecond(int second) {
        int hour = second / 60;
        int se = second % 60;
        String hourStr;
        if (hour < 10) {
            hourStr = "0" + hour;
        } else {
            hourStr = "" + hour;
        }
        String seStr;
        if (se < 10) {
            seStr = "0" + se;
        } else {
            seStr = "" + se;
        }
        return hourStr + ":" + seStr;
    }

    public static boolean isRightTimeStr(EditText... editTexts) {
        for (EditText editText : editTexts) {
            if (editText.getText().toString().indexOf(":") == -1 && editText.getText().toString().indexOf("：") == -1) {
                return false;
            }
            String time = editText.getText().toString();
            if (time.indexOf(":") != -1) {
                String hour = time.substring(0, time.indexOf(":"));
                Log.e("获取到是小时----", "获取到是小时----" + hour);
                int hourInt = Integer.valueOf(hour);
                if (hourInt > 24) {
                    return false;
                }
                int secondInt = Integer.valueOf(time.substring(time.indexOf(":") + 1,
                        time.length()));
                if (secondInt > 60) {
                    return false;
                }
            } else {
                String hour = time.substring(0, time.indexOf("："));
                Log.e("获取到是小时----", "获取到是小时----" + hour);
                int hourInt = Integer.valueOf(hour);
                if (hourInt > 24) {
                    return false;
                }
                int secondInt = Integer.valueOf(time.substring(time.indexOf("：") + 1,
                        time.length()));
                if (secondInt > 60) {
                    return false;
                }
            }
        }
        return true;
    }

    public static int getSencondByTime(String time) {
        if (time.indexOf(":") != -1) {
            String hour = time.substring(0, time.indexOf(":"));
            Log.e("获取到是小时----", "获取到是小时----" + hour);
            int hourInt = Integer.valueOf(hour);
            int secondInt = Integer.valueOf(time.substring(time.indexOf(":") + 1,
                    time.length()));
            Log.e("获取到是secondInt----", "获取到是secondInt----" + secondInt);
            Log.e("获取到是结果----", "获取到是结果----" + (hourInt * 60 + secondInt));
            return hourInt * 60 + secondInt;
        } else {
            String hour = time.substring(0, time.indexOf("："));
            Log.e("获取到是小时----", "获取到是小时----" + hour);
            int hourInt = Integer.valueOf(hour);
            int secondInt = Integer.valueOf(time.substring(time.indexOf("：") + 1,
                    time.length()));
            Log.e("获取到是secondInt----", "获取到是secondInt----" + secondInt);
            Log.e("获取到是结果----", "获取到是结果----" + (hourInt * 60 + secondInt));
            return hourInt * 60 + secondInt;
        }
    }


    /**
     * 传 double 类型的 string
     *
     * @param value
     * @return
     */
    public static String getDoubleFormat(double value) {
        DecimalFormat df = new DecimalFormat("0.00");
        if (value > (int) value) {
            double fee = Double.parseDouble(value + "");
            return df.format(fee) + "";
        } else {
            return (int) value + "";
        }
    }

    public static String getDoubleFormatNew(String value) {
        if (TextUtils.isEmpty(value)) {
            return "0";
        }
        double valueee = Double.valueOf(value);
        DecimalFormat df = new DecimalFormat("0.00");
        if (valueee > (int) valueee) {
            double fee = Double.parseDouble(valueee + "");
            return df.format(fee) + "";
        } else {
            return (int) valueee + "";
        }
    }

    /**
     * 总计回血和消费的显示为字符串
     * @param value
     * @return
     */
    public static String getDoubleFormatNew2(String value) {
        if (TextUtils.isEmpty(value)) {
            return "0.00";
        }
        double valueee = Double.valueOf(value);
        DecimalFormat df = new DecimalFormat("0.00");
        if (valueee > (int) valueee) {
            double fee = Double.parseDouble(valueee + "");
            return df.format(fee) + "";
        } else {
            return valueee + "";
        }
    }


    /**
     * 两个数据对比操作
     *
     * @param value1
     * @param value2
     * @return
     */
    public static boolean isBigger(String value1, String value2) {
        if (TextUtils.isEmpty(value1) || "null".equals(value1)) {
            value1 = "0";
        }
        if (TextUtils.isEmpty(value2) || "null".equals(value2)) {
            value2 = "0";
        }

        double val_1 = Double.valueOf(value1);
        double val_2 = Double.valueOf(value2);

        return val_1 > val_2;
    }

    /**
     * 两个数据对比操作
     *
     * @param value1
     * @param value2
     * @return
     */
    public static boolean isBiggerAndEqual(String value1, String value2) {
        if (TextUtils.isEmpty(value1) || "null".equals(value1)) {
            value1 = "0";
        }
        if (TextUtils.isEmpty(value2) || "null".equals(value2)) {
            value2 = "0";
        }

        double val_1 = Double.valueOf(value1);
        double val_2 = Double.valueOf(value2);

        return val_1 >= val_2;
    }

    public static String getPlusValue(String value, String currentValue, int plus) {
        if (".".equals(value)) {
            return getDoubleFormatNew(Double.valueOf(currentValue) + plus + "");
        } else {
            if (value.startsWith(".")) {
                value = "0" + value;
                return getDoubleFormatNew((Double.valueOf(value) + plus) + "");
            } else if (value.endsWith(".")) {
                value = value.substring(value.length() - 1, value.length());
                return getDoubleFormatNew((Double.valueOf(value) + plus) + "");
            } else {
                return getDoubleFormatNew((Double.valueOf(value) + plus) + "");
            }
        }
    }


    public static String[] getTimeFromInt(long time) {
        long day = time / (1 * 60 * 60 * 24);
        long hour = time / (1 * 60 * 60) % 24;
        long minute = time / (1 * 60) % 60;
        long second = time / (1) % 60;

        String[] data = new String[4];
        String dayStr = "";
        if (day < 10) {
            dayStr = "0" + day;
        } else {
            dayStr = "" + day;
        }
        data[0] = dayStr;

        String hourStr = "";
        if (hour < 10) {
            hourStr = "0" + hour;
        } else {
            hourStr = "" + hour;
        }
        data[1] = hourStr;

        String minStr = "";
        if (minute < 10) {
            minStr = "0" + minute;
        } else {
            minStr = "" + minute;
        }
        data[2] = minStr;

        String SecsStr = "";
        if (second < 10) {
            SecsStr = "0" + second;
        } else {
            SecsStr = "" + second;
        }
        data[3] = SecsStr;
        return data;
    }

    public static String[] getTimeFromIntNew(long time) {
        long day = time / (1 * 60 * 60 * 24);
        long hour = time / (1 * 60 * 60) % 24;
        long minute = time / (1 * 60) % 60;
        long second = time / (1) % 60;

        String[] data = new String[4];
        String dayStr = "";
        dayStr = "" + day;
        data[0] = dayStr;

        String hourStr = "";
        hourStr = "" + hour;
        data[1] = hourStr;

        String minStr = "";
        minStr = "" + minute;
        data[2] = minStr;

        String SecsStr = "";
        SecsStr = "" + second;
        data[3] = SecsStr;
        return data;
    }

    public static Date StringToDate(String dateStr) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static long getDaoJiShiTime(String endTime) {
        String now = createServerDateFormatV1();
        Date nowDate = StringToDate(now);
        String server = getSimpleDateNew(endTime);
        Date dtServer = StringToDate(server);
        return dtServer.getTime() - nowDate.getTime();
    }

    public static String getHomeDaoJiShiTime(long time) {
        long day = time / (1 * 60 * 60 * 24);
        long hour = time / (1 * 60 * 60) % 24;
        long minute = time / (1 * 60) % 60;
        long second = time / (1) % 60;
        if (day > 0 && hour >= 0) {
            return (day + 1) + "天";
        }
        if (day == 0 && hour >= 1 && minute >= 0) {
            return (hour + 1) + "时";
        }
        if (day == 0 && hour < 1 && minute >= 1 && second >= 0) {
            return (minute + 1) + "分";
        }
        if (day == 0 && hour < 1 && minute < 1) {
            return second + "秒";
        }
        return "";
    }

}
