package com.yanjoy.temp.entity.vo;

import com.yanjoy.temp.entity.detail.TempDetail;
import com.yanjoy.temp.entity.entry.TempEntry;
import com.yanjoy.temp.entity.message.TempMessage;
import com.yanjoy.temp.entity.user.TempUser;
import com.yanjoy.temp.utils.TempTimeUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author PC
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppTempVo implements Serializable {
    private static final long serialVersionUID = -6029575686500123127L;
    private String id;
    /**
     * 标替
     */
    private String title;
    /**
     * 时间
     */
    private Date date;

    /**
     * 是否有编辑权限
     * false没有
     * true 有
     */
    private boolean editPower = false;


    /**
     * 当前时间阶段
     */
    private Short tempType;

    /**
     * 用户信息
     */
    private TempUser user;

    /**
     * 其它数据
     */
    private TempMessage message;
    /**
     * 体温数据
     */
    private TempDetail temp;

    /**
     * 外出离开日常工作城市
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
     * 定位
     */
    private TempEntry location;

    public String getId() {
        return this.message.getId();
    }

    public void setTitle(String projectName) {
        this.title = projectName + "疫情防控信息采集";
    }

    public Date getDate() {
        return this.message.getCreateDate();
    }

    public boolean isEditPower() {
        Date date = new Date();
        //今日
        if (TempTimeUtils.dateToYMD(date).equals(this.message.getCreateDay())) {
            //是否当前阶段
            int tempStage = TempTimeUtils.getTempStage(date);
            return this.tempType == tempStage;
        }
        return editPower;
    }
}
