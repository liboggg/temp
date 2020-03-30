package com.yanjoy.temp.service;

import com.yanjoy.temp.entity.base.TempParam;
import com.yanjoy.temp.entity.excel.*;
import com.yanjoy.temp.entity.org.TempOrgTree;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface TempExcelService {

    /**
     * 项目id获取体温组织机构树
     */
    List<TempOrgTree> getTempOrgTree(String projectId);
    /**
     * 劳务或管理子表列数据封装排序 user left msg
     */
    List<TableLineMessage> getTableLineMsg(TempParam param);

    /**
     * user inner msg
     */
    List<TableLineMessage> getSubmitMessage(TempParam param);

    /**
     * 推送
     */
    List<TableLineMessage> pushMsg(String dateDay);


    /**
     *劳务或管理子表数据
     */
    ChildTable childTable(TempParam param);

    /**
     * 不正常子表
     */
    UnNormalChildTable unNormalChild(TempParam param);

    /**
     * 总表
     */
    SumTable sumTable(TempParam param);

    /**
     *Excel返回
     */
    TempExcelVo excelVo(TempParam param) throws ExecutionException, InterruptedException;



    /**
     * 选择导出项目名
     * @param projectId 项目id
     * @return 项目名
     */
    String chooseProjectName(String projectId);

    /**
     * 统计结果计算
     */
    AlarmCalculate calculate(List<TableLineMessage> list);

    /**
     * 异常数据筛选并排序
     */
    List<TableLineMessage> alarmFilter(List<TableLineMessage> list);


    /**
     * 填充阈值后综合多种排序
     * @param list  List<TableLineMessage>
     * @return  List<TableLineMessage>
     */
    List<TableLineMessage> pullMsgAndSort(List<TableLineMessage> list);

}
