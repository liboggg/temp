package com.yanjoy.temp.entity.excel;

import lombok.Data;

import java.io.Serializable;

/**
 * Excel 数据
 */
@Data
public class TempExcelVo implements Serializable {
    private static final long serialVersionUID = -7825003822634409552L;
    /**
     * 主表
     */
    private SumTable sumTable;
    /**
     * 劳务子表
     */
    private ChildTable labourChild;
    /**
     * 管理子表
     */
    private ChildTable managerChild;
    /**
     * 异常子表
     */
    private UnNormalChildTable unNormalChild;

}
