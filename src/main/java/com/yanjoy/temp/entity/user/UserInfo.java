package com.yanjoy.temp.entity.user;


import lombok.Data;
import java.io.Serializable;
import java.util.Calendar;
@Data
public class UserInfo implements Serializable {
    private static final long serialVersionUID = -1510013214033711350L;
    /**
     * 身份证
     */
    private String idCard;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 电话
     */
    private Long phone;

    /**
     * 性别
     */
    private String sex;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 人员类型
     */
    private String personType;

    public Integer getAge() {
        //截取身份证中出行人出生日期中的年、月、日
        Integer personYear = Integer.parseInt(this.idCard.substring(6, 10));
        Integer personMonth = Integer.parseInt(this.idCard.substring(10, 12));
        Integer personDay = Integer.parseInt(this.idCard.substring(12, 14));
        Calendar cal = Calendar.getInstance();
        // 得到当前时间的年、月、日
        Integer yearNow = cal.get(Calendar.YEAR);
        Integer monthNow = cal.get(Calendar.MONTH) + 1;
        Integer dayNow = cal.get(Calendar.DATE);
        // 用当前年月日减去生日年月日
        Integer yearMinus = yearNow - personYear;
        Integer monthMinus = monthNow - personMonth;
        Integer dayMinus = dayNow - personDay;
        Integer age = yearMinus;
        //先大致赋值
        //出生年份为当前年份
        if (yearMinus == 0) {
            age = 0;
        } else {
            //出生年份大于当前年份
            //出生月份小于当前月份时，还没满周岁
            if (monthMinus < 0) {
                age = age - 1;
            }
            //当前月份为出生月份时，判断日期
            if (monthMinus == 0) {
                //出生日期小于当前月份时，没满周岁
                if (dayMinus < 0) {
                    age = age - 1;
                }
            }
        }
        return age;
    }
}
