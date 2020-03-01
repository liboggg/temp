package com.yanjoy.temp.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

public class
ForkJoinUitls {

    private static final ForkJoinPool joinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors() * 2);

    public static ForkJoinPool getJoinPool() {
        return ForkJoinUitls.joinPool;
    }

    /**
     * 提交任务
     * @param task 执行任务
     * @param <T> 任务返回值类型
     * @return 任务执行情况
     */
    public static <T> ForkJoinTask<T> submit(Callable<T> task) {
        return joinPool.submit(task);
    }

}
