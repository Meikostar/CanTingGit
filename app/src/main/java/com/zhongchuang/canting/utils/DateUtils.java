package com.zhongchuang.canting.utils;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;




import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2016/7/20 0020.
 */
public class DateUtils {

    public static long oneHourMillis = 60 * 60 * 1000; // 一小时的毫秒数
    public static long oneDayMillis = 24 * oneHourMillis; // 一天的毫秒数
    public static long oneYearMillis = 365 * oneDayMillis; // 一年的毫秒数


    public static String getCurrentMonthYear(Calendar c){
        int year = c.get(Calendar.YEAR);
        String mon = "";
        int month = c.get(Calendar.MONTH);
        switch (month){
            case 0:
                mon = "一月";
                break;
            case 1:
                mon = "二月";
                break;
            case 2:
                mon = "三月";
                break;
            case 3:
                mon = "四月";
                break;
            case 4:
                mon = "五月";
                break;
            case 5:
                mon = "六月";
                break;
            case 6:
                mon = "七月";
                break;
            case 7:
                mon = "八月";
                break;
            case 8:
                mon = "九月";
                break;
            case 9:
                mon = "十月";
                break;
            case 10:
                mon = "十一月";
                break;
            case 11:
                mon = "十二月";
                break;
        }
        return mon+"  "+year;
    }



    /**
     * 获取当天是几号
     * @return
     */
    public static int getCurrDayNum(){
        Calendar cal = Calendar.getInstance();
       return cal.get(Calendar.DAY_OF_MONTH)+1;
    }
    public static String formatToYear(long time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return format.format(new Date(time));
    }
    public static String formatToMonth(long time){
        SimpleDateFormat format = new SimpleDateFormat("MM");
        return format.format(new Date(time));
    }
    public static String formatToDay(long time){
        SimpleDateFormat format = new SimpleDateFormat("dd");
        return format.format(new Date(time));
    }
    /**
     *  根据传入日期得到天
     * @param str
     * @return
     */
    public static int getDayNum(String str){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(str);
            Calendar cal = format.getCalendar();
            Calendar current = Calendar.getInstance();
            if (current.get(Calendar.MONTH)==cal.get(Calendar.MONTH)){
                return cal.get(Calendar.DAY_OF_MONTH);
            }else {
                return -1;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     *  返回一个月有多少天
     *
     * @return
     */
    public static int sumDaysofMonth(){
        Calendar calendar =Calendar.getInstance();

        Calendar time=Calendar.getInstance();
        time.clear();
        time.set(Calendar.YEAR,calendar.get(Calendar.YEAR));
        time.set(Calendar.MONTH,calendar.get(Calendar.MONTH));//注意,Calendar对象默认一月为0

        return time.getActualMaximum(Calendar.DAY_OF_MONTH);//本月份的天数
    }

    public static int firstDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        int i = cal.get(Calendar.DAY_OF_WEEK);

        return i;
    }


    public static int getWeekDay(Calendar calendar) {

        int i = calendar.get(Calendar.DAY_OF_WEEK);

        return i;
    }

    /**
     * 字符串解析成毫秒数
     * @param str
     * @param pattern
     * @return
     */
    public static long string2Millis(String str, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern,
                Locale.getDefault());
        long millis = 0;
        try {
            millis = format.parse(str).getTime();
        } catch (ParseException e) {

        }
        return millis;
    }

    public static String millisToLifeString(long millis) {
        long now = System.currentTimeMillis();
        long todayStart = string2Millis(millisToStringDate(now, "yyyy-MM-dd"),
                "yyyy-MM-dd");

        // 一小时内
        if (now - millis <= oneHourMillis && now - millis > 0l) {
            String m = millisToStringShort(now - millis, false, false);
            return "".equals(m) ? "1分钟内" : m + "前";
        }

        // 大于今天开始开始值，小于今天开始值加一天（即今天结束值）
        if (millis >= todayStart && millis <= oneDayMillis + todayStart) {
            return "今天 " + millisToStringDate(millis, "HH:mm");
        }

        // 大于（今天开始值减一天，即昨天开始值）
        if (millis > todayStart - oneDayMillis) {
            return "昨天 " + millisToStringDate(millis, "HH:mm");
        }

        long thisYearStart = string2Millis(millisToStringDate(now, "yyyy"),
                "yyyy");
        // 大于今天小于今年
        if (millis > thisYearStart) {
            return millisToStringDate(millis, "MM月dd日 HH:mm");
        }

        return millisToStringDate(millis, "yyyy年MM月dd日 HH:mm");
    }

    /**
     * 把日期毫秒转化为字符串（文件名）
     * @param millis
     *            要转化的日期毫秒数。
     * @param pattern
     *            要转化为的字符串格式（如：yyyy-MM-dd HH:mm:ss）。
     * @return 返回日期字符串（yyyy_MM_dd_HH_mm_ss）。
     */
    public static String millisToStringFilename(long millis, String pattern) {
        String dateStr = millisToStringDate(millis, pattern);
        return dateStr.replaceAll("[- :]", "_");
    }

    /**
     *把日期毫秒转化为字符串
     * @param millis
     *            要转化的日期毫秒数。
     * @param pattern
     *            要转化为的字符串格式（如：yyyy-MM-dd HH:mm:ss）。
     * @return 返回日期字符串。
     */
    public static String millisToStringDate(long millis, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern,
                Locale.getDefault());

        return format.format(new Date(millis));
    }


    /**
     *把日期毫秒转化为字符串
     * @param millis
     *            要转化的日期毫秒数。
     * @param pattern
     *            要转化为的字符串格式（如：yyyy-MM-dd HH:mm:ss）。
     * @return 返回日期字符串。
     */
    public static String dateToStringDate(Date millis, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern,
                Locale.getDefault());

        return format.format(millis);
    }




    /**
     *
     * 把一个毫秒数转化成时间字符串
     格式为小时/分/秒/毫秒（如：24903600 –> 06小时55分03秒600毫秒）
     * @param millis
     *            要转化的毫秒数。
     * @param isWhole
     *            是否强制全部显示小时/分/秒/毫秒。
     * @param isFormat
     *            时间数字是否要格式化，如果true：少位数前面补全；如果false：少位数前面不补全。
     * @return 返回时间字符串：小时/分/秒/毫秒的格式（如：24903600 --> 06小时55分03秒600毫秒）。
     */
    public static String millisToString(long millis, boolean isWhole,
                                        boolean isFormat) {
        String h = "";
        String m = "";
        String s = "";
        String mi = "";
        if (isWhole) {
            h = isFormat ? "00小时" : "0小时";
            m = isFormat ? "00分" : "0分";
            s = isFormat ? "00秒" : "0秒";
            mi = isFormat ? "00毫秒" : "0毫秒";
        }

        long temp = millis;

        long hper = 60 * 60 * 1000;
        long mper = 60 * 1000;
        long sper = 1000;

        if (temp / hper > 0) {
            if (isFormat) {
                h = temp / hper < 10 ? "0" + temp / hper : temp / hper + "";
            } else {
                h = temp / hper + "";
            }
            h += "小时";
        }
        temp = temp % hper;

        if (temp / mper > 0) {
            if (isFormat) {
                m = temp / mper < 10 ? "0" + temp / mper : temp / mper + "";
            } else {
                m = temp / mper + "";
            }
            m += "分";
        }
        temp = temp % mper;

        if (temp / sper > 0) {
            if (isFormat) {
                s = temp / sper < 10 ? "0" + temp / sper : temp / sper + "";
            } else {
                s = temp / sper + "";
            }
            s += "秒";
        }
        temp = temp % sper;
        mi = temp + "";

        if (isFormat) {
            if (temp < 100 && temp >= 10) {
                mi = "0" + temp;
            }
            if (temp < 10) {
                mi = "00" + temp;
            }
        }

        mi += "毫秒";
        return h + m + s + mi;
    }

    /**
     *格式为小时/分/秒/毫秒（如：24903600 –> 06小时55分03秒）。
     * @param millis
     *            要转化的毫秒数。
     * @param isWhole
     *            是否强制全部显示小时/分/秒/毫秒。
     * @param isFormat
     *            时间数字是否要格式化，如果true：少位数前面补全；如果false：少位数前面不补全。
     * @return 返回时间字符串：小时/分/秒/毫秒的格式（如：24903600 --> 06小时55分03秒）。
     */
    public static String millisToStringMiddle(long millis, boolean isWhole,
                                              boolean isFormat) {
        return millisToStringMiddle(millis, isWhole, isFormat, "小时", "分钟", "秒");
    }

    public static String millisToStringMiddle(long millis, boolean isWhole,
                                              boolean isFormat, String hUnit, String mUnit, String sUnit) {
        String h = "";
        String m = "";
        String s = "";
        if (isWhole) {
            h = isFormat ? "00" + hUnit : "0" + hUnit;
            m = isFormat ? "00" + mUnit : "0" + mUnit;
            s = isFormat ? "00" + sUnit : "0" + sUnit;
        }

        long temp = millis;

        long hper = 60 * 60 * 1000;
        long mper = 60 * 1000;
        long sper = 1000;

        if (temp / hper > 0) {
            if (isFormat) {
                h = temp / hper < 10 ? "0" + temp / hper : temp / hper + "";
            } else {
                h = temp / hper + "";
            }
            h += hUnit;
        }
        temp = temp % hper;

        if (temp / mper > 0) {
            if (isFormat) {
                m = temp / mper < 10 ? "0" + temp / mper : temp / mper + "";
            } else {
                m = temp / mper + "";
            }
            m += mUnit;
        }
        temp = temp % mper;

        if (temp / sper > 0) {
            if (isFormat) {
                s = temp / sper < 10 ? "0" + temp / sper : temp / sper + "";
            } else {
                s = temp / sper + "";
            }
            s += sUnit;
        }
        return h + m + s;
    }


    /**
     *把一个毫秒数转化成时间字符串。格式为小时/分/秒/毫秒（如：24903600 –> 06小时55分钟）
     * @param millis
     *            要转化的毫秒数。
     * @param isWhole
     *            是否强制全部显示小时/分。
     * @param isFormat
     *            时间数字是否要格式化，如果true：少位数前面补全；如果false：少位数前面不补全。
     * @return 返回时间字符串：小时/分/秒/毫秒的格式（如：24903600 --> 06小时55分钟）。
     */
    public static String millisToStringShort(long millis, boolean isWhole,
                                             boolean isFormat) {
        String h = "";
        String m = "";
        if (isWhole) {
            h = isFormat ? "00小时" : "0小时";
            m = isFormat ? "00分钟" : "0分钟";
        }

        long temp = millis;

        long hper = 60 * 60 * 1000;
        long mper = 60 * 1000;
        long sper = 1000;

        if (temp / hper > 0) {
            if (isFormat) {
                h = temp / hper < 10 ? "0" + temp / hper : temp / hper + "";
            } else {
                h = temp / hper + "";
            }
            h += "小时";
        }
        temp = temp % hper;

        if (temp / mper > 0) {
            if (isFormat) {
                m = temp / mper < 10 ? "0" + temp / mper : temp / mper + "";
            } else {
                m = temp / mper + "";
            }
            m += "分钟";
        }

        return h + m;
    }


    private static final int seconds_of_1minute = 60;

    private static final int seconds_of_30minutes = 30 * 60;

    private static final int seconds_of_1hour = 60 * 60;

    private static final int seconds_of_1day = 24 * 60 * 60;

    private static final int seconds_of_15days = seconds_of_1day * 15;

    private static final int seconds_of_30days = seconds_of_1day * 30;

    private static final int seconds_of_6months = seconds_of_30days * 6;

    private static final int seconds_of_1year = seconds_of_30days * 12;



    /**
     * @return timtPoint距离现在经过的时间，分为
     *         刚刚，1-29分钟前，半小时前，1-23小时前，1-14天前，半个月前，1-5个月前，半年前，1-xxx年前
     */
    public static String getTimeElapse(long createTime) {

        long nowTime = new Date().getTime() / 1000;


        //elapsedTime是发表和现在的间隔时间

        long elapsedTime = nowTime - createTime;

        if (elapsedTime < seconds_of_1minute) {

            return "刚刚";
        }
        if (elapsedTime < seconds_of_30minutes) {

            return elapsedTime / seconds_of_1minute + "分钟前";
        }
        if (elapsedTime < seconds_of_1hour) {

            return "半小时前";
        }
        if (elapsedTime < seconds_of_1day) {

            return elapsedTime / seconds_of_1hour + "小时前";
        }
        if (elapsedTime < seconds_of_15days) {

            return elapsedTime / seconds_of_1day + "天前";
        }
        if (elapsedTime < seconds_of_30days) {

            return "半个月前";
        }
        if (elapsedTime < seconds_of_6months) {

            return elapsedTime / seconds_of_30days + "月前";
        }
        if (elapsedTime < seconds_of_1year) {

            return "半年前";
        }
        if (elapsedTime >= seconds_of_1year) {

            return elapsedTime / seconds_of_1year + "年前";
        }

        return "";
    }


    /**
     * 月日时分秒，0-9前补0
     *
     * @param number the number
     * @return the string
     */
//    @NonNull
//    public static String fillZero(int number) {
//        return number < 10 ? "0" + number : "" + number;
//    }


    /**
     * Calculate days in month int.
     *
     * @param year  the year
     * @param month the month
     * @return the int
     */

//    public static int calculateDaysInMonth(int year, int month) {
//        // 添加大小月月份并将其转换为list,方便之后的判断
//        String[] bigMonths = {"1", "3", "5", "7", "8", "10", "12"};
//        String[] littleMonths = {"4", "6", "9", "11"};
//        List<String> bigList = Arrays.asList(bigMonths);
//        List<String> littleList = Arrays.asList(littleMonths);
//        // 判断大小月及是否闰年,用来确定"日"的数据
//        if (bigList.contains(String.valueOf(month))) {
//            return 31;
//        } else if (littleList.contains(String.valueOf(month))) {
//            return 30;
//        } else {
//            if (year <= 0) {
//                return 29;
//            }
//            // 是否闰年
//            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
//                return 29;
//            } else {
//                return 28;
//            }
//        }
//    }


    public static final int Second = 0;
    public static final int Minute = 1;
    public static final int Hour = 2;
    public static final int Day = 3;

    @IntDef(value = {Second, Minute, Hour, Day})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DifferenceMode {
    }

    public static long calculateDifferentSecond(Date startDate, Date endDate) {
        return calculateDifference(startDate, endDate, Second);
    }

    public static long calculateDifferentMinute(Date startDate, Date endDate) {
        return calculateDifference(startDate, endDate, Minute);
    }

    public static long calculateDifferentHour(Date startDate, Date endDate) {
        return calculateDifference(startDate, endDate, Hour);
    }

    public static long calculateDifferentDay(Date startDate, Date endDate) {
        return calculateDifference(startDate, endDate, Day);
    }

    public static long calculateDifferentDayOrder(Date startDate, Date endDate) {
        return calculateDifferenceOrder(startDate, endDate, Day);
    }

    public static long calculateDifferentSecond(long startTimeMillis, long endTimeMillis) {
        return calculateDifference(startTimeMillis, endTimeMillis, Second);
    }

    public static long calculateDifferentMinute(long startTimeMillis, long endTimeMillis) {
        return calculateDifference(startTimeMillis, endTimeMillis, Minute);
    }

    public static long calculateDifferentHour(long startTimeMillis, long endTimeMillis) {
        return calculateDifference(startTimeMillis, endTimeMillis, Hour);
    }

    public static long calculateDifferentDay(long startTimeMillis, long endTimeMillis) {
        return calculateDifference(startTimeMillis, endTimeMillis, Day);
    }

    /**
     * 计算两个时间戳之间相差的时间戳数
     */
    public static long calculateDifference(long startTimeMillis, long endTimeMillis, @DifferenceMode int mode) {
        return calculateDifference(new Date(startTimeMillis), new Date(endTimeMillis), mode);
    }

    /**
     * 计算两个日期之间相差的时间戳数
     */
    public static long calculateDifference(Date startDate, Date endDate, @DifferenceMode int mode) {
        long[] different = calculateDifference(startDate, endDate);
        if (mode == Minute) {
            return different[2];
        } else if (mode == Hour) {
            return different[1];
        } else if (mode == Day) {
            return different[0];
        } else {
            return different[3];
        }
    }

    /**
     * 计算两个日期之间相差的时间戳数   向上取证
     */
    public static long calculateDifferenceOrder(Date startDate, Date endDate, @DifferenceMode int mode) {
        long[] different = calculateDifference(startDate, endDate);
        if (mode == Minute) {
            return different[2];
        } else if (mode == Hour) {
            return different[1];
        } else if (mode == Day) {
            if ((different[3]!=0)||(different[1]!=0)||(different[2]!=0)){
                return different[0]+1;
            }else {
                return different[0];
            }

        } else {
            return different[3];
        }
    }

    private static long[] calculateDifference(Date startDate, Date endDate) {
        return calculateDifference(endDate.getTime() - startDate.getTime());
    }

    private static long[] calculateDifference(long differentMilliSeconds) {
        long secondsInMilli = 1000;//1s==1000ms
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long elapsedDays = differentMilliSeconds / daysInMilli;
        differentMilliSeconds = differentMilliSeconds % daysInMilli;
        long elapsedHours = differentMilliSeconds / hoursInMilli;
        differentMilliSeconds = differentMilliSeconds % hoursInMilli;
        long elapsedMinutes = differentMilliSeconds / minutesInMilli;
        differentMilliSeconds = differentMilliSeconds % minutesInMilli;
        long elapsedSeconds = differentMilliSeconds / secondsInMilli;
        LogUtil.d(String.format(Locale.CHINA, "different: %d ms, %d days, %d hours, %d minutes, %d seconds",
                differentMilliSeconds, elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds));
        return new long[]{elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds};
    }



    /**
     * 计算每月的天数
     */
    public static int calculateDaysInMonth(int month) {
        return calculateDaysInMonth(0, month);
    }

    /**
     * 根据年份及月份计算每月的天数
     */
    public static int calculateDaysInMonth(int year, int month) {
        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] bigMonths = {"1", "3", "5", "7", "8", "10", "12"};
        String[] littleMonths = {"4", "6", "9", "11"};
        List<String> bigList = Arrays.asList(bigMonths);
        List<String> littleList = Arrays.asList(littleMonths);
        // 判断大小月及是否闰年,用来确定"日"的数据
        if (bigList.contains(String.valueOf(month))) {
            return 31;
        } else if (littleList.contains(String.valueOf(month))) {
            return 30;
        } else {
            if (year <= 0) {
                return 29;
            }
            // 是否闰年
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                return 29;
            } else {
                return 28;
            }
        }
    }

    /**
     * 月日时分秒，0-9前补0
     */
    @NonNull
    public static String fillZero(int number) {
        return number < 10 ? "0" + number : "" + number;
    }

    /**
     * 月日时分秒，0-9前补0
     */
    @NonNull
    public static int fillZeroInt(int number) {
        return number < 10 ? number : number;
    }

    /**
     * 截取掉前缀0以便转换为整数
     *
     * @see #fillZero(int)
     */
    public static int trimZero(@NonNull String text) {
        try {
            if (text.startsWith("0")) {
                text = text.substring(1);
            }
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {

            return 0;
        }
    }

    /**
     * 功能：判断日期是否和当前date对象在同一天。
     * 参见：http://www.cnblogs.com/myzhijie/p/3330970.html
     *
     * @param date 比较的日期
     * @return boolean 如果在返回true，否则返回false。
     */
    public static boolean isSameDay(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("date is null");
        }
        Calendar nowCalendar = Calendar.getInstance();
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.setTime(date);
        return (nowCalendar.get(Calendar.ERA) == newCalendar.get(Calendar.ERA) &&
                nowCalendar.get(Calendar.YEAR) == newCalendar.get(Calendar.YEAR) &&
                nowCalendar.get(Calendar.DAY_OF_YEAR) == newCalendar.get(Calendar.DAY_OF_YEAR));
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss字符串转换成日期<br/>
     *
     * @param dateStr    时间字符串
     * @param dataFormat 当前时间字符串的格式。
     * @return Date 日期 ,转换异常时返回null。
     */
    public static Date parseDate(String dateStr, String dataFormat) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(dataFormat, Locale.PRC);
            Date date = dateFormat.parse(dateStr);
            return new Date(date.getTime());
        } catch (ParseException e) {

            return null;
        }
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss字符串转换成日期<br/>
     *
     * @param dateStr yyyy-MM-dd HH:mm:ss字符串
     * @return Date 日期 ,转换异常时返回null。
     */
    public static Date parseDate(String dateStr) {
        return parseDate(dateStr, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 将指定的日期转换为一定格式的字符串
     */
    public static String formatDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.PRC);
        return sdf.format(date);
    }

    /**
     * 将当前日期转换为一定格式的字符串
     */
    public static String formatDate(String format) {
        return formatDate(Calendar.getInstance(Locale.CHINA).getTime(), format);
    }

    public static   String calendarToString(Calendar date){

        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);
        int day = date.get(Calendar.DAY_OF_MONTH);
        int hours = date.get(Calendar.HOUR_OF_DAY);
        int minute = date.get(Calendar.MINUTE);
        int seconds = date.get(Calendar.SECOND);
        int weekInt = date.get(Calendar.DAY_OF_WEEK);
        String week = "";
        switch (weekInt){
            case 1:
                week = "周日";
                break;
            case 2:
                week = "周一";
                break;
            case 3:
                week = "周二";
                break;
            case 4:
                week = "周三";
                break;
            case 5:
                week = "周四";
                break;
            case 6:
                week = "周五";
                break;
            case 7:
                week = "周六";
                break;
        }
        return year+"年"+(month+1)+"月"+day+"日  " + week+"    "+hours+"时"+minute+"分"+seconds+"秒";
    }

    public static String getWeek(Calendar date){

//        int year = date.get(Calendar.YEAR);
//        int month = date.get(Calendar.MONTH);
//        int day = date.get(Calendar.DAY_OF_MONTH);
//        int hours = date.get(Calendar.HOUR_OF_DAY);
//        int minute = date.get(Calendar.MINUTE);
//        int seconds = date.get(Calendar.SECOND);
        int weekInt = date.get(Calendar.DAY_OF_WEEK);
        String week = "";
        switch (weekInt){
            case 1:
                week = "周日";
                break;
            case 2:
                week = "周一";
                break;
            case 3:
                week = "周二";
                break;
            case 4:
                week = "周三";
                break;
            case 5:
                week = "周四";
                break;
            case 6:
                week = "周五";
                break;
            case 7:
                week = "周六";
                break;
        }
        return  week ;
    }

    public static String getHoursMin(Calendar date){

//        int year = date.get(Calendar.YEAR);
//        int month = date.get(Calendar.MONTH);
//        int day = date.get(Calendar.DAY_OF_MONTH);
        int hours = date.get(Calendar.HOUR_OF_DAY);
        int minute = date.get(Calendar.MINUTE);
//        int seconds = date.get(Calendar.SECOND);

        return   hours+":"+fillZero(minute) ;
    }

    public static List<Integer> getHoursMinInt(Calendar date){

//        int year = date.get(Calendar.YEAR);
//        int month = date.get(Calendar.MONTH);
//        int day = date.get(Calendar.DAY_OF_MONTH);
        int hours = date.get(Calendar.HOUR_OF_DAY);
        int minute = date.get(Calendar.MINUTE);
//        int seconds = date.get(Calendar.SECOND);
        List<Integer> integers = new ArrayList<>();
        integers.add(hours);
        integers.add(minute);
        return integers;
    }

    public static String getWeekHoursMin(Calendar date){

//        int year = date.get(Calendar.YEAR);
//        int month = date.get(Calendar.MONTH);
//        int day = date.get(Calendar.DAY_OF_MONTH);
        int hours = date.get(Calendar.HOUR_OF_DAY);
        int minute = date.get(Calendar.MINUTE);
//        int seconds = date.get(Calendar.SECOND);
        int weekInt = date.get(Calendar.DAY_OF_WEEK);
        String week = "";
        switch (weekInt){
            case 1:
                week = "周日";
                break;
            case 2:
                week = "周一";
                break;
            case 3:
                week = "周二";
                break;
            case 4:
                week = "周三";
                break;
            case 5:
                week = "周四";
                break;
            case 6:
                week = "周五";
                break;
            case 7:
                week = "周六";
                break;
        }
        return  week +" "+hours+":"+fillZero(minute);
    }

    public static Calendar dateStrToCalendar(Date date,String str){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        try {
            Date date1 = dateFormat.parse(str);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY,date1.getHours());
            calendar.set(Calendar.MINUTE,date1.getMinutes());
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


}
