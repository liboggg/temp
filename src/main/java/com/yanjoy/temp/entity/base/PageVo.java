package com.yanjoy.temp.entity.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 内存分页
 */
public class PageVo<T> implements Serializable {
    private static final long serialVersionUID = 6599279440400283927L;
    /**
     * 当前页码
     */
    private int currentPage;
    /**
     * 总页数
     */
    private int totalPage;
    /**
     * 总行数
     */
    private int totalResult;
    /**
     * 每页显示条数
     */
    private int showCount = 10;

    /**
     * 从第几条开始查询
     */
    private int currentResult;

    /**
     * 需要分页的数据
     */
    private List<T> list;

    public PageVo() {
    }

    /**
     * 获取分页后的分页详情
     *
     * @param currentPage 当前页码
     * @param showCount   每页展示数量
     * @param list        源数据
     */
    public PageVo(int currentPage, int showCount, List<T> list) {
        this.currentPage = currentPage;
        this.showCount = showCount;
        this.list = list;
        this.totalResult = list.size();
        this.currentResult = (currentPage - 1) * showCount;
        this.totalPage = (list.size() - 1) / showCount + 1;
    }

    /**
     * 获取分页后的数据
     */
    public List<T> pagedList() {
        List<T> newList = new ArrayList();
        for (int i = (currentPage - 1) * showCount; i < totalResult && i < currentPage * showCount; i++) {
            newList.add(list.get(i));
        }
        //源数据置空
        this.list = new ArrayList<>();
        return newList;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(int totalResult) {
        this.totalResult = totalResult;
    }

    public int getShowCount() {
        return showCount;
    }

    public void setShowCount(int showCount) {
        this.showCount = showCount;
    }

    public int getCurrentResult() {
        return currentResult;
    }

    public void setCurrentResult(int currentResult) {
        this.currentResult = currentResult;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}