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
public class OrganizationSimple implements Serializable {
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
     * 坐标
     */
    private String coordinate;

    /**
     * 二级公司
     */
    private String company;

}
