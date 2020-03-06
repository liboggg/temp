package com.yanjoy.temp.utils;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Ftp定时任务
 */
public class TimerUtil {
    /**
     * 每日任务
     * @param runnable runnable
     * @param startTime startTime  每日执行时间 "06:00:00"
     */
    public static void dayJobStart(Runnable runnable,String startTime){
        ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder()
                        .namingPattern(UUID.randomUUID().toString())
                        .daemon(true)
                        .build());
        long oneDay = 24 * 60 * 60 * 1000;
        long initDelay  = getTimeMillis(startTime) - System.currentTimeMillis();
        initDelay = initDelay > 0 ? initDelay : oneDay + initDelay;
        executor.scheduleAtFixedRate(
                runnable,
                initDelay,
                oneDay,
                TimeUnit.MILLISECONDS);
    }

    /**
     * 获取指定时间对应的毫秒数
     * @param time “HH:mm:ss”
     * @return
     */
    private static long getTimeMillis(String time) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
            DateFormat dayFormat = new SimpleDateFormat("yy-MM-dd");
            Date curDate = dateFormat.parse(dayFormat.format(new Date()) + " " + time);
            return curDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
