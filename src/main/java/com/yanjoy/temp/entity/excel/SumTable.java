package com.yanjoy.temp.entity.excel;

import com.yanjoy.temp.entity.org.TempOrgTree;
import lombok.Data;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Data
public class SumTable implements Serializable {
    private static final long serialVersionUID = -2818577455250641367L;
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
     * 在橞人员
     */
    private int nowInArea;

    /**
     * 管理统计
     */
    private AlarmCalculate labourStatistics;
    /**
     * 劳务统计
     */
    private AlarmCalculate managerStatistics;

    /**
     * 日期年月日 yyyy-mm-dd
     */
    private String dateDay;

    /**
     * 详情
     */
    List<TempOrgTree> list = new ArrayList<>();


    public String getTitle() {
        StringBuilder builder = new StringBuilder();
        builder.append(projectName)
                .append("体温日报总表");
        return builder.toString();
    }

    public int getNowInArea() {
        int a = 0;
        if (labourStatistics != null) {
            a += labourStatistics.getNowInArea();
        }
        if (managerStatistics != null) {
            a += managerStatistics.getNowInArea();
        }
        return a;
    }
}
