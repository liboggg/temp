package com.yanjoy.temp.entity.excel;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 报警计算
 */
@Data
public class AlarmCalculate implements Serializable {
    private static final long serialVersionUID = -2827794994883726328L;
    /**
     * 处于14天隔离期 数
     */
    private int inIso;

    /**
     * 已完成14天隔离 数
     */
    private int overIso;

    /**
     * 休假期间一直在穗 数
     */
    private int inArea;

    /**
     * 目前在穗人员
     */
    private int nowInArea;

    /**
     * 体温异常数
     */
    private int tempAbnormal;

    /**
     * 未填报
     */
    private int noReport;

    /**
     * 核酸检测报警
     */
    private int nucleaseAlarm;

    /**
     * 血液病毒检测报警
     */
    private int bloodAlarm;

    /**
     * 计算总统计数据
     *
     * @param list 结果
     * @return 统计结果
     */
    public AlarmCalculate calculate(List<TableLineMessage> list) {
        AlarmCalculate obj = new AlarmCalculate();
        if (list == null || list.isEmpty()) {
            return obj;
        }
        obj.setNowInArea(list.size());
        obj.setInIso((int) list.stream().filter(a -> a.getUser().getStayStatus() == 1).count());
        obj.setOverIso((int) list.stream().filter(a -> a.getUser().getStayStatus() == 2).count());
        obj.setInArea((int) list.stream().filter(a -> a.getUser().getStayStatus() == 3).count());
        obj.setTempAbnormal((int) list.stream().filter(a -> a.getDetailAlarm() > 0).count());
        obj.setNoReport((int) list.stream().filter(a -> a.getDetailNoReport() > 0).count());
        obj.setNucleaseAlarm((int) list.stream().filter(a -> a.getNucleaseAlarm() == 1).count());
        obj.setBloodAlarm((int) list.stream().filter(a -> a.getBloodAlarm() == 1).count());
        return obj;
    }

}
