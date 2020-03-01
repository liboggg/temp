package com.yanjoy.temp.entity.base;

import lombok.Data;

import java.io.Serializable;


@Data
public class PageInfo implements Serializable {
    private static final long serialVersionUID = 5151720616372070084L;
    private Integer pageNum;
    private Integer pageSize;

    public Integer getPageNum() {
        if(null == pageNum){
            pageNum = 1;
        }
        return pageNum;
    }

    public Integer getPageSize() {
        if(null == pageSize){
            pageSize = 10;
        }
        return pageSize;
    }
}
