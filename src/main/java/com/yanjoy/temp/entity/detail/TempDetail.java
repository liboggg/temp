package com.yanjoy.temp.entity.detail;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TempDetail {
    private static final long serialVersionUID = 1L;
    private String id;

    /**
     * 身份证
     */
    private String idCard;

    /**
     * 信息id
     */
    private String messageId;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 创建年月日
     */
    private String createDay;

    /**
     * 上午7时
     * 1 上午7时 2 中午12时 3 下午5时
     */
    private Short tempType;

    /**
     * 体温
     */
    private BigDecimal temperature;

    /**
     * 阈值
     */
    private BigDecimal upperLimit;

    /**
     * 是否报警
     * 0 未报警
     * 1 报警
     */
    private int alarm;



    public int getAlarm() {
        if (StringUtils.isEmpty(id)) {
            return alarm;
        }
        if (temperature != null && upperLimit != null) {
            if (temperature.doubleValue() <= upperLimit.doubleValue()) {
                this.alarm = 0;
                return 0;
            } else {
                this.alarm = 1;
                return 1;
            }
        }
        return alarm;
    }
}