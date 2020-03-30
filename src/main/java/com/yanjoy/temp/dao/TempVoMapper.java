package com.yanjoy.temp.dao;

import com.yanjoy.temp.entity.base.TempParam;
import com.yanjoy.temp.entity.excel.TableLineMessage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TempVoMapper {

    /**
     *注册人员 left msg
     */
    List<TableLineMessage> getTableLineMessage(TempParam param);
    /**
     *注册人员 inner msg
     */
    List<TableLineMessage> getSubmitMessage(TempParam param);
}
