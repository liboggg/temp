package com.yanjoy.temp.dao;

import com.yanjoy.temp.entity.config.TempConfig;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;

@Mapper
public interface TempConfigMapper {
    int delete();

    int insert(TempConfig record);

    int insertSelective(TempConfig record);

    TempConfig selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TempConfig record);

    int updateByPrimaryKey(TempConfig record);

    BigDecimal getTempConfig();
}