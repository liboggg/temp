package com.yanjoy.temp.entity.base;

import lombok.Data;
import java.util.Date;

@Data
public class TempParam extends PageInfo {
    private String id;
    /**
     * 身份证
     */
    private String idCard;
    /**
     * 组织机构id
     */
    private String orgId;
    /**
     * 项目id
     */
    private String projectId;

    /**
     * 上午7时
     * 1 上午7时
     * 2 中午12时
     * 3 下午5时
     */
    private Integer tempType;

    /**
     * 信息id
     */
    private String messageId;

    /**
     * 年月日 2019-01-02
     */
    private String dateDay;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 人员类型
     * gl lw
     */
    private String personType;

}
