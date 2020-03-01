package com.yanjoy.temp.entity.message;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TempMessage {
    private static final long serialVersionUID = 1L;
    private String id;

    /**
     * 身份证
     */
    private String idCard;

    /**
     * 居住的省市区
     */
    private String province;

    /**
     * 居住详细地址
     */
    private String address;

    /**
     *是否离开广州
     * 修改为条目id
     */
    private String route;

    /**
     * 疑似病人接触史
     * 修改为条目id
     */
    private String contactHistory;

    /**
     * 有无经过湖北或其它疫情严重区
     * 修改为条目id
     */
    private String areaHistory;

    /**
     * 有无与湖北等地区人员接触史
     * 修改为条目id
     */
    private String personHistory;

    /**
     * 个人健康状况（如有问题请填写时间、隔离地点）
     * 修改为条目id
     */
    private String health;

    /**
     * 血液病毒检测
     */
    private String bloodTest;

    /**
     * 核酸检测
     */
    private String nucleaseTest;

    /**
     * 到岗情况
     */
    private String workStatus;



    /**
     * 定位
     */
    private String location;


    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 创建年月日
     */
    private String createDay;


    /**
     * 备注
     */
    private String note;


    private String detailId;




}