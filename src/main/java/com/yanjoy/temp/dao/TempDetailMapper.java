package com.yanjoy.temp.dao;

import com.yanjoy.temp.entity.detail.TempDetail;
import com.yanjoy.temp.entity.base.TempParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TempDetailMapper {
    int deleteByPrimaryKey(String id);

    int insertSelective(TempDetail record);

    TempDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TempDetail record);


    List<TempDetail> getList(TempParam param);

    /**
     * 当前阶段是否存在数据
     * @param param  idCard YMD tempTYPE
     * @return TempDetail
     */
    TempDetail getLast(TempParam param);
}