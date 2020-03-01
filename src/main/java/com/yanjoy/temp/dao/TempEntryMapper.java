package com.yanjoy.temp.dao;

import com.yanjoy.temp.entity.entry.TempEntry;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TempEntryMapper {

    int insertSelective(TempEntry record);

    TempEntry selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TempEntry record);


    TempEntry getLastEntryByIdCardAndType(@Param("idCard") String idCard, @Param("typeCode") String typeCode);
}