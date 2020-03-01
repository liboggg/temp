package com.yanjoy.temp.entity.entry;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TempEntry implements Serializable {
    private static final long serialVersionUID = -518241029824870274L;
    private String id;

    /**
     * 身份证
     */
    private String idCard;

    /**
     * messageId
     */
    private String messageId;

    /**
     * 类型状态码
     */
    private Short status;

    /**
     * 类型状态
     */
    private String name;

    /**
     * 类型编码
     */
    private String typeCode;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 创建年月日
     */
    private String createDay;

    /**
     * 检测时间
     */
    private Date testDate;

    /**
     * 检测结果
     * 0 阴性
     * 1 阳性
     */
    private Short testResult;

    /**
     * 相同TYPE_CODE相对于上一条数据是否改变，
     * 0未改变1改变
     */
    private Short whetherChange;
}