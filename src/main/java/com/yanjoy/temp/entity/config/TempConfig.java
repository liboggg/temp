package com.yanjoy.temp.entity.config;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TempConfig {
    private String id;

    /**
    * 上限
    */
    private BigDecimal upperLimit;

}