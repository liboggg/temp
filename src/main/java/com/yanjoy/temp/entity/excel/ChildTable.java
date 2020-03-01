package com.yanjoy.temp.entity.excel;

import lombok.Data;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 子表
 */
@Data
public class ChildTable implements Serializable {
    private static final long serialVersionUID = 2046359861646931363L;
    /**
     * 表头
     */
    private String title;

    /**
     * 项目id
     */
    private String projectId;
    /**
     * 项目名
     */
    private String projectName;

    /**
     * 统计数据
     */
    private AlarmCalculate statistics;

    /**
     * 日期年月日 yyyy-mm-dd
     */
    private String dateDay;

    /**
     * 详情
     */
    private List<TableLineMessage> list = new ArrayList<>();


    public String getTitle() {
        StringBuilder builder = new StringBuilder();
        builder.append(projectName)
                .append("目前在穗人员")
                .append(statistics.getNowInArea())
                .append("人，处于隔离期")
                .append(statistics.getInIso())
                .append("人，完成隔离期")
                .append(statistics.getOverIso())
                .append("人，一直在穗人员")
                .append(statistics.getInArea())
                .append("人,体温异常")
                .append(statistics.getTempAbnormal()).append("人");
        return builder.toString();
    }
}
