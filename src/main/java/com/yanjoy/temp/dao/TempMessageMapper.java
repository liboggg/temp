package com.yanjoy.temp.dao;

import com.yanjoy.temp.entity.base.TempParam;
import com.yanjoy.temp.entity.detail.TempDetail;
import com.yanjoy.temp.entity.message.TempMessage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TempMessageMapper {
    int deleteByPrimaryKey(String id);

    int insertSelective(TempMessage record);

    TempMessage selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TempMessage record);

    List<TempMessage> getList(TempParam param);
}