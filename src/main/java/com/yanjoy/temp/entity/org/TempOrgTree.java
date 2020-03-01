package com.yanjoy.temp.entity.org;

import com.yanjoy.temp.entity.excel.AlarmCalculate;
import com.yanjoy.temp.entity.excel.TableLineMessage;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TempOrgTree implements Serializable {
    private static final long serialVersionUID = 1606037113626147118L;
    /**
     * 组织机构id
     */
    private String orgId;
    /**
     * 组织机构
     */
    private String orgName;
    /**
     * 父级id
     */
    private String parentId;
    /**
     * 组织机构type
     */
    private Integer orgType;
    /**
     * 项目id
     */
    private String projectId;
    /**
     * 排序编码
     */
    private Integer sort;

    /**
     * 二级公司
     */
    private String company;

    /**
     * 下级
     */
    private List<TempOrgTree> nextChild = new ArrayList<>();

    /**
     * 子表使用合计统计
     */
    private AlarmCalculate tempStatistics;

    /**
     * 总表-管理统计
     */
    private AlarmCalculate labourStatistics;
    /**
     * 总表-劳务统计
     */
    private AlarmCalculate managerStatistics;

    /**
     * 列表
     */
    private List<TableLineMessage> lineMessages = new ArrayList<>();

    public List<TempOrgTree> getNextChild() {
        return nextChild.stream()
                .sorted(Comparator.comparing(TempOrgTree::getSort))
                .collect(Collectors.toList());
    }
}
