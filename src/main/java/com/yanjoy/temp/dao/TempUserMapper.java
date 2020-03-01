package com.yanjoy.temp.dao;

import com.yanjoy.temp.entity.user.TempUser;
import com.yanjoy.temp.entity.user.UserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TempUserMapper {


    int insertSelective(TempUser record);

    TempUser selectByIdCard(String idCardOrPhone);

    int updateByPrimaryKeySelective(TempUser record);

    int updateLeave(String idCard);
    int updateComeBack(String idCard);

    /**
     * 获取平台基本信息
     *
     * @param id 身份证/电话
     * @return List<UserInfo>
     */
    List<UserInfo> getPlatUserInfo(String id);
}