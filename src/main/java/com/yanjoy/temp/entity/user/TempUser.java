package com.yanjoy.temp.entity.user;

import com.yanjoy.temp.utils.TempTimeUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author PC
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TempUser implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;

    /**
     * 身份证
     */
    @NotNull(message = "身份证不能为空!")
    private String idCard;

    /**
     * 姓名
     */
    @NotNull(message = "姓名不能为空!")
    private String userName;

    /**
     * 电话
     */
    @NotNull(message = "手机不能为空！")
    private String phone;

    /**
     * 组织机构id
     */
    @NotNull(message = "组织机构不能为空!")
    private String orgId;

    /**
     * 组织机构
     */
    private String orgName;

    /**
     * 所属公司
     */
    @NotNull(message = "所属公司不能为空!")
    private String company;

    /**
     * 项目id
     */
    @NotNull(message = "projectId不能为空!")
    private String projectId;

    /**
     * 0 正常 1 暂离
     */
    @NotNull(message = "0 正常 1 暂离!")
    private Short status;

    /**
     * 人员类型 gl lw
     */
    @NotNull(message = "人员类型不能为空 gl lw!")
    private String personType;

    private Integer age;
    private String sex;

    /**
     * 1 处于14天隔离期
     * 2 已完成14天隔离
     * 3 休假期间一直在穗
     */
    private Short stayStatus;

    /**
     * 来穗时间
     */
    private String comeDate;

    /**
     * 来源地
     */
    private String comeArea;

    /**
     * 应隔离完成日期
     */
    private String finishDate;

    /**
     * 次数
     */
    private Integer amount;

    /**
     * 1 处于14天隔离期
     * 2 已完成14天隔离
     * 3 休假期间一直在穗
     */
    public Short getStayStatus() {
        //来穗日期
        Date date = TempTimeUtils.ymdToDate(comeDate);
        //延后14天
        Date finishDate = TempTimeUtils.getAfterTimeDate(date, 14);
        //实际值yyyy-mm-dd要减一天
        this.finishDate = TempTimeUtils.getAfterTimeYMD(date, 13);
        Date now = new Date();
        if (stayStatus == 3 || stayStatus == 2) {
            return stayStatus;
            //隔离中
        } else if (stayStatus == 1) {
            //还在隔离期
            if (finishDate.before(now)) {
                this.stayStatus = 1;
            } else {
                this.stayStatus = 2;
            }
        }
        return stayStatus;
    }
}