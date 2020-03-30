package com.yanjoy.temp.service;

import com.yanjoy.temp.entity.excel.SumTable;
import com.yanjoy.temp.entity.excel.ChildTable;
import com.yanjoy.temp.entity.excel.UnNormalChildTable;
import com.yanjoy.temp.dao.TempConfigMapper;
import com.yanjoy.temp.dao.TempVoMapper;
import com.yanjoy.temp.entity.detail.TempDetail;
import com.yanjoy.temp.entity.base.TempParam;
import com.yanjoy.temp.entity.excel.*;
import com.yanjoy.temp.entity.org.OrgPo;
import com.yanjoy.temp.entity.org.TempOrgTree;
import com.yanjoy.temp.utils.ForkJoinUitls;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.Collectors;

@Service("tempExcelService")
public class TempExcelServiceImpl implements TempExcelService {

    @Autowired
    private TempVoMapper voMapper;

    @Autowired
    private TempConfigMapper configMapper;

    @Autowired
    private OrgService orgService;

    @Autowired
    private TempUserService userService;


    private TempExcelService tempExcelService;
    @Override
    public List<TempOrgTree> getTempOrgTree(String projectId) {
        if (StringUtils.isEmpty(projectId)) {
            throw new IllegalArgumentException("the projectId is not allowed be null");
        }
        OrgPo topOrg = orgService.getTopOrg(projectId);
        return orgService.getTempOrgTree(topOrg.getId(), topOrg.getType());
    }


    @Override
    public List<TableLineMessage> getTableLineMsg(TempParam param) {
        if (StringUtils.isEmpty(param.getProjectId()) || StringUtils.isEmpty(param.getDateDay())) {
            throw new IllegalArgumentException("the projectId,dateDay is not allowed be null");
        }
        return pullMsgAndSort(voMapper.getTableLineMessage(param).stream().peek(a ->
                a.setUser(userService.pullStatus(a.getUser()))
        ).collect(Collectors.toList()));
    }

    @Override
    public List<TableLineMessage> getSubmitMessage(TempParam param) {
        if (StringUtils.isEmpty(param.getMessageId())) {
            throw new IllegalArgumentException("the messageId is not allowed be null");
        }
        return pullMsgAndSort(voMapper.getSubmitMessage(param).stream().peek(a ->
                a.setUser(userService.pullStatus(a.getUser()))
        ).collect(Collectors.toList()));
    }

    @Override
    public List<TableLineMessage> pushMsg(String dateDay) {
        if ( StringUtils.isEmpty(dateDay)) {
            throw new IllegalArgumentException("the dateDay is not allowed be null");
        }
        TempParam param = new TempParam();
        param.setDateDay(dateDay);
        return pullMsgAndSort(voMapper.getTableLineMessage(param).stream()
                .filter(a -> StringUtils.isNotEmpty(a.getId()))
                .peek(a -> a.setUser(userService.pullStatus(a.getUser())))
                .collect(Collectors.toList()));
    }

    @Override
    public ChildTable childTable(TempParam param) {
        if (StringUtils.isEmpty(param.getProjectId()) || StringUtils.isEmpty(param.getDateDay())) {
            throw new IllegalArgumentException("the projectId,dateDay is not allowed be null");
        }

        String projectName = chooseProjectName(param.getProjectId());
        List<TableLineMessage> tableLineMsg = getTableLineMsg(param);
        AlarmCalculate calculate = calculate(tableLineMsg);
        ChildTable data = new ChildTable();
        data.setProjectId(param.getProjectId());
        data.setProjectName(projectName);
        data.setStatistics(calculate);
        data.setDateDay(param.getDateDay());
        data.setList(tableLineMsg);
        return data;
    }


    @Override
    public UnNormalChildTable unNormalChild(TempParam param) {
        if (StringUtils.isEmpty(param.getProjectId()) || StringUtils.isEmpty(param.getDateDay())) {
            throw new IllegalArgumentException("the projectId,dateDay is not allowed be null");
        }

        String projectName = chooseProjectName(param.getProjectId());
        //正常
        List<TableLineMessage> total = new ArrayList<>();
        //异常列表
        List<TableLineMessage> unNormal = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<TempOrgTree> tempOrgTree = getTempOrgTree(param.getProjectId());
        for (TempOrgTree organizationSimple : tempOrgTree) {
            List<TempOrgTree> nextChild = organizationSimple.getNextChild();
            //实际数据存放
            for (TempOrgTree simple : nextChild) {
                TempParam newParam = new TempParam();
                BeanUtils.copyProperties(param, newParam);
                newParam.setOrgId(simple.getOrgId());
                executorService.execute(() -> {
                    //全部
                    List<TableLineMessage> tableLineMsg = getTableLineMsg(newParam);
                    AlarmCalculate calculate = calculate(tableLineMsg);
                    simple.setTempStatistics(calculate);
                    total.addAll(tableLineMsg);

                    //异常
                    List<TableLineMessage> no = new ArrayList<>();
                    no = alarmFilter(tableLineMsg);
                    unNormal.addAll(no);
                    simple.setLineMessages(no);
                });
            }

        }
        executorService.shutdown();
        while (true) {
            if (executorService.isTerminated()) {
                break;
            }
        }

        UnNormalChildTable data = new UnNormalChildTable();
        data.setProjectId(param.getProjectId());
        data.setProjectName(projectName);
        data.setStatistics(calculate(total));
        data.setDateDay(param.getDateDay());
        data.setList(tempOrgTree);
        return data;
    }

    @Override
    public TempExcelVo excelVo(TempParam param) throws ExecutionException, InterruptedException {
        TempParam managerParam = new TempParam();
        TempParam labourParam = new TempParam();
        BeanUtils.copyProperties(param, managerParam);
        BeanUtils.copyProperties(param, labourParam);
        ForkJoinTask<SumTable> total = ForkJoinUitls.submit(() -> sumTable(param));
        labourParam.setPersonType("lw");
        ForkJoinTask<ChildTable> labour = ForkJoinUitls.submit(() -> childTable(labourParam));
        managerParam.setPersonType("gl");
        ForkJoinTask<ChildTable> manager = ForkJoinUitls.submit(() -> childTable(managerParam));
        ForkJoinTask<UnNormalChildTable> unNormal = ForkJoinUitls.submit(() -> unNormalChild(param));
        TempExcelVo data = new TempExcelVo();
        data.setSumTable(total.get());
        data.setLabourChild(labour.get());
        data.setManagerChild(manager.get());
        data.setUnNormalChild(unNormal.get());
        return data;
    }


    @Override
    public SumTable sumTable(TempParam param) {
        if (StringUtils.isEmpty(param.getProjectId()) || StringUtils.isEmpty(param.getDateDay())) {
            throw new IllegalArgumentException("the projectId,dateDay is not allowed be null");
        }
        String projectName = chooseProjectName(param.getProjectId());
        //管理
        List<TableLineMessage> mTotal = new ArrayList<>();
        //劳务
        List<TableLineMessage> lTotal = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<TempOrgTree> tempOrgTree = getTempOrgTree(param.getProjectId());
        for (TempOrgTree organizationSimple : tempOrgTree) {
            List<TempOrgTree> nextChild = organizationSimple.getNextChild();
            //实际数据存放
            for (TempOrgTree simple : nextChild) {
                TempParam newParam = new TempParam();
                BeanUtils.copyProperties(param, newParam);
                newParam.setOrgId(simple.getOrgId());
                executorService.execute(() -> {
                    //劳务
                    newParam.setPersonType("lw");
                    List<TableLineMessage> labour = getTableLineMsg(newParam);
                    simple.setLabourStatistics(calculate(labour));
                    //管理
                    newParam.setPersonType("gl");
                    List<TableLineMessage> manager = getTableLineMsg(newParam);
                    simple.setManagerStatistics(calculate(manager));
                    lTotal.addAll(labour);
                    mTotal.addAll(manager);

                });
            }
        }
        executorService.shutdown();
        while (true) {
            if (executorService.isTerminated()) {
                break;
            }
        }

        SumTable data = new SumTable();
        data.setProjectId(param.getProjectId());
        data.setProjectName(projectName);
        data.setLabourStatistics(calculate(lTotal));
        data.setManagerStatistics(calculate(mTotal));
        data.setDateDay(param.getDateDay());
        data.setList(tempOrgTree);
        return data;
    }


    @Override
    public String chooseProjectName(String projectId) {
//        6245721945602523139	七号线二期
//        6245721945602523137	十三号线二期
//        6422195692059623424	地下综合管廊
//        6245721945602523136	十一号线
        switch (projectId) {
            case "6245721945602523136":
                return "十一号线";
            case "6422195692059623424":
                return "地下综合管廊";
            case "6245721945602523137":
                return "十三号线二期";
            case "6245721945602523139":
                return "七号线二期";
            default:
                return "";
        }
    }

    @Override
    public AlarmCalculate calculate(List<TableLineMessage> list) {
        return new AlarmCalculate().calculate(list);
    }

    @Override
    public List<TableLineMessage> alarmFilter(List<TableLineMessage> list) {
        List<TableLineMessage> data = new ArrayList<>();
        if (list == null) {
            return new ArrayList<>();
        }
        data = baseSort(list.stream()
                .filter(a -> (a.getDetailAlarm() > 0 || a.getMsgTotalAlarm() > 0
                        || a.getNucleaseAlarm() > 0 || a.getBloodAlarm() > 0))
                .collect(Collectors.toList()));
        return data;
    }

    @Override
    public List<TableLineMessage> pullMsgAndSort(List<TableLineMessage> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        return baseSort(putConfig(list));
    }


    /**
     * 公共排序
     */
    private List<TableLineMessage> baseSort(List<TableLineMessage> lineMessages) {
        return lineMessages.stream()
                .sorted(Comparator.comparing(TableLineMessage::getNucleaseAlarm, Comparator.reverseOrder())
                        .thenComparing(TableLineMessage::getBloodAlarm, Comparator.reverseOrder())
                        .thenComparing(TableLineMessage::getDetailAlarm,Comparator.reverseOrder())
                        .thenComparing(TableLineMessage::getMsgNoReport, Comparator.reverseOrder())
                        .thenComparing(TableLineMessage::getMsgTotalAlarm, Comparator.reverseOrder())
                        .thenComparing(a -> maxTemp(a.getTemp()),Comparator.reverseOrder())
                        .thenComparing(TableLineMessage::getIdCard)).collect(Collectors.toList());
    }

    double maxTemp(List<TempDetail> details) {
        if (details.isEmpty()) {
            return 0;
        }
        return details.stream().max(Comparator.comparing(TempDetail::getTemperature)).get().getTemperature().doubleValue();
    }


    /**
     * 填充报警配置并针对体温结果排序
     *
     * @param lineMessages List<TableLineMessage>
     * @return List<TableLineMessage>
     */
    private List<TableLineMessage> putConfig(List<TableLineMessage> lineMessages) {
        BigDecimal tempConfig = configMapper.getTempConfig();
        return lineMessages.stream().peek(a -> {
            if (!a.getTemp().isEmpty()) {
                a.setTemp(a.getTemp().stream()
                        .peek(b -> {
                            if (null != b) {
                                b.setUpperLimit(tempConfig);
                            }
                        }).sorted(Comparator.comparing(TempDetail::getTempType)).collect(Collectors.toList()));
            }
        }).collect(Collectors.toList());
    }
}