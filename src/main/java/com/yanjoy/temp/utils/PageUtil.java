package com.yanjoy.temp.utils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;


public class PageUtil {
    /**
     * 分页工具
     * @param pageNum 页码
     * @param pageSize 数量
     * @return 适用直接返回数据
     */
    public static Page pageHelper(Integer pageNum, Integer pageSize) {
        if (pageSize == null || pageNum == null) {
            pageSize = 10;
            pageNum = 1;
        }
        return PageHelper.startPage(pageNum, pageSize);
    }
}
