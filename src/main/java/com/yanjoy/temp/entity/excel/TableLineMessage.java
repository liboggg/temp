package com.yanjoy.temp.entity.excel;

import com.yanjoy.temp.entity.detail.TempDetail;
import com.yanjoy.temp.entity.entry.TempEntry;
import com.yanjoy.temp.entity.message.TempMessage;
import com.yanjoy.temp.entity.user.TempUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Pc端子table
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableLineMessage implements Serializable {
    private static final long serialVersionUID = -6029575686500123127L;
    private String id;
    private String idCard;

    /**
     * 人员
     */
    private TempUser user;

    /**
     * 基本数据
     */
    private TempMessage message;
    /**
     * 体温数据
     */
    private List<TempDetail> temp = new ArrayList<>();

    /**
     * 是否离开广州
     */
    private TempEntry route;
    /**
     * 确诊或疑似病例接触史
     */
    private TempEntry contactHistory;

    /**
     * 地区人员接触史
     */
    private TempEntry areaHistory;

    /**
     * 人员接触史
     */
    private TempEntry personHistory;

    /**
     * 个人健康状况
     */
    private TempEntry health;

    /**
     * 血液病毒检测
     */
    private TempEntry bloodTest;

    /**
     * 核酸检测
     */
    private TempEntry nucleaseTest;

    /**
     * 到岗情况
     */
    private TempEntry workStatus;

    /**
     * 该天未填报
     * > 0 报警
     */
    private int msgNoReport;

    /**
     * 有体温未填报（共三次 这里是未填报的次数）
     * > 0 未填报数量
     */
    private int detailNoReport;

    /**
     * 体温报警数量（一天中的次数）
     */
    private int detailAlarm;

    /**
     * 核酸检测报警
     */
    private int nucleaseAlarm;
    private int nucleaseNoReport;

    /**
     * 血液病毒检测报警
     */
    private int bloodAlarm;
    private int bloodNoReport;

    /**
     * 到岗情况变化
     */
    private int workChange;


    /**
     * 基本情况是否变化
     * 0 未变化 1 变化
     */
    private int routeAlarm;

    private int contAlarm;

    private int areaAlarm;

    private int personAlarm;

    private int healthAlarm;

    /**
     * 基本情况变化项数
     */
    private int msgTotalAlarm;


    public int getMsgNoReport() {
        if (StringUtils.isEmpty(id)) {
            this.msgNoReport = 1;
            return 1;
        }
        return msgNoReport;
    }

    public int getDetailNoReport() {
        //有一次上报则算上报
        int i = temp.size() > 0 ? 0 : 1;
        this.detailNoReport = i;
        return i;
    }

    public int getDetailAlarm() {
        if (!temp.isEmpty()) {
            return (int) temp.stream()
                    .filter(this::detailsAlarm)
                    .count();
        }
        return detailAlarm;
    }

    /**
     * 单项体温是否报警
     */
    private boolean detailsAlarm(TempDetail a) {
        if (null != a && null != a.getTemperature() && null != a.getUpperLimit()) {
            return a.getTemperature().doubleValue() > a.getUpperLimit().doubleValue();
        }
        return false;
    }

    public int getRouteAlarm() {
        if (route != null) {
            if (route.getWhetherChange() > 0) {
                this.routeAlarm = 1;
                return 1;
            }
        }
        return routeAlarm;
    }

    public int getContAlarm() {
        if (contactHistory != null) {
            if (contactHistory.getWhetherChange() > 0) {
                this.contAlarm = 1;
                return 1;
            }
        }
        return contAlarm;
    }

    public int getAreaAlarm() {
        if (areaHistory != null) {
            if (areaHistory.getWhetherChange() > 0) {
                this.areaAlarm = 1;
                return 1;
            }
        }
        return areaAlarm;
    }

    public int getPersonAlarm() {
        if (personHistory != null) {
            if (personHistory.getWhetherChange() > 0) {
                this.personAlarm = 1;
                return 1;
            }
        }
        return personAlarm;
    }

    public int getHealthAlarm() {
        if (health != null) {
            if (health.getWhetherChange() > 0) {
                this.healthAlarm = 1;
                return 1;
            }
        }

        return healthAlarm;
    }

    public int getMsgTotalAlarm() {
        this.msgTotalAlarm = routeAlarm + contAlarm + areaAlarm + personAlarm + healthAlarm + workChange;
        return routeAlarm + contAlarm + areaAlarm + personAlarm + healthAlarm + workChange;
    }

    public int getNucleaseAlarm() {
        if (nucleaseTest != null) {
            if (nucleaseTest.getTestResult() != null && nucleaseTest.getTestResult().intValue() == 1) {
                this.nucleaseAlarm = 1;
                return 1;
            }
        }
        return 0;
    }

    public int getNucleaseNoReport() {
        if (nucleaseTest == null) {
            this.nucleaseNoReport = 1;
            return 1;
        }
        return 0;
    }

    public int getBloodAlarm() {
        if (bloodTest != null) {
            if (bloodTest.getTestResult() != null && bloodTest.getTestResult().intValue() == 1) {
                this.bloodAlarm = 1;
                return 1;
            }
        }
        return 0;
    }

    public int getBloodNoReport() {
        if (bloodTest == null) {
            this.bloodNoReport = 1;
            return 1;
        }
        return 0;
    }

    public int getWorkChange() {
        if (workStatus != null) {
            if (workStatus.getWhetherChange() > 0) {
                this.workChange = 1;
                return 1;
            }
        }

        return workChange;
    }
}
