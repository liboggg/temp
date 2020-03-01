package com.yanjoy.temp.service;

import com.yanjoy.temp.entity.config.TempConfig;
import com.yanjoy.temp.entity.detail.SaveDetailPo;

import java.math.BigDecimal;

/**
 * 体温监测
 */

public interface TempMonitorService {
    /**
     * 保存或修改
     * 根据日阶段判断
     */
    int save(SaveDetailPo saveDetailPo);

    /**
     * 保存配置
     */
    int saveConfig(TempConfig config);

    /**
     * 获取配置
     * @return  获取体温阈值
     */
    BigDecimal getConfig();


}
