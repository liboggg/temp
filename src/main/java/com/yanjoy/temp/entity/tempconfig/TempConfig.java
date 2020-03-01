package com.yanjoy.temp.entity.tempconfig;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TempConfig {
    private static final long serialVersionUID = 1L;
    private String id;

    /**
     * 上限
     */
    private BigDecimal upperLimit;

}