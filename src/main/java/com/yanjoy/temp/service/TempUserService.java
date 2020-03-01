package com.yanjoy.temp.service;


import com.yanjoy.temp.entity.user.TempUser;
import com.yanjoy.temp.entity.user.UserInfo;

public interface TempUserService {

    /**
     * 注册/修改
     * @return 0 失败 1 成功
     */
    int saveOrUpdate(TempUser tempUser);

    /**
     * 身份证获取用户基本信息
     * @param idCard 身份证
     * @return TempUser
     */
    TempUser getTempUserByIdCardOrPhone(String idCardOrPhone);


    /**
     * 身份证获取用户统计信息
     * @param idCard 身份证
     * @return TempUser
     */
    TempUser getUser(String idCardOrPhone);

    /**
     *人员暂离
     * @param idCard 身份证
     */
    int leave(String idCard);
    int comeBack(String idCard);

    /**
     * 获取平台用户基本信息
     * @param id 身份证/电话
     * @return UserInfo
     */
    UserInfo geuPlatUserInfo(String id);

    /**
     * 校验用户是否存在
     * 存在返回false
     * 不存在返回true
     */
    boolean checkUser(String id);
}
