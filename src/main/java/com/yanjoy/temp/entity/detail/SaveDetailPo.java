package com.yanjoy.temp.entity.detail;

import com.yanjoy.temp.entity.entry.TempEntry;
import com.yanjoy.temp.entity.message.TempMessage;
import lombok.Data;
import java.io.Serializable;

/**
 * 保存实体封装
 */
@Data
public class SaveDetailPo implements Serializable {
    private static final long serialVersionUID = 346584659008426936L;
    private TempMessage message;
    private TempDetail temp;
    /**
     * 外出离开日常工作城市
     */
    private TempEntry route;
    /**
     * 确诊或疑似病例接触史
     */
    private TempEntry contactHistory;

    /**
     * 地区人员接触史
     */
    private TempEntry areaHistory;

    /**
     * 人员接触史
     */
    private TempEntry personHistory;

    /**
     * 个人健康状况
     */
    private TempEntry health;

    /**
     * 血液病毒检测
     */
    private TempEntry bloodTest;

    /**
     * 核酸检测
     */
    private TempEntry nucleaseTest;

    /**
     * 到岗情况
     */
    private TempEntry workStatus;

    /**
     * 定位
     */
    private TempEntry location;

}
