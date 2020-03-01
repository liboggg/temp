package com.yanjoy.temp.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TempTimeUtils {

    public static String dateToYMD(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    /**
     * 字符串转换完整日期
     *
     * @param str
     * @return date
     */
    public static Date strToFullDate(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String dateToFullStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return format.format(date);

    }


    /**
     * 根据当前时间返回体温阶段
     * 1 00:00:00 - 08:30:00
     * 2 08:30:01 - 13:30:00
     * 3 13:30:01 - 19:30:00
     * 4 不允许修改添加
     */
    public static int getTempStage(Date date) {
        String ymd = dateToYMD(date);
        Date group1 = strToFullDate(ymd + " 00:00:00");
        Date group12 = strToFullDate(ymd + " 08:30:00");
        Date group2 = strToFullDate(ymd + " 08:30:01");
        Date group22 = strToFullDate(ymd + " 13:30:00");
        Date group3 = strToFullDate(ymd + " 13:30:01");
        Date group32 = strToFullDate(ymd + " 19:30:00");
        if (group1.getTime() <= date.getTime() && date.getTime() <= group12.getTime()) {
            return 1;
        }
        if (group2.getTime() <= date.getTime() && date.getTime() <= group22.getTime()) {
            return 2;
        }
        if (group3.getTime() <= date.getTime() && date.getTime() <= group32.getTime()) {
            return 3;
        }
        return 4;
    }

    /**
     * 几天后年月日
     *
     * @param date      时间
     * @param afterDays 几天后
     * @return yyyy-MM-dd
     */
    public static String getAfterTimeYMD(Date date, int afterDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, afterDays);
        date = calendar.getTime();
        return dateToYMD(date);
    }

    /**
     * 几天后时间
     *
     * @param date      时间
     * @param afterDays 几天后
     * @return calendar.setTime(date);
     */
    public static Date getAfterTimeDate(Date date, int afterDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, afterDays);
        date = calendar.getTime();
        return date;
    }

    public static Date ymdToDate(String day) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;

    }
}
