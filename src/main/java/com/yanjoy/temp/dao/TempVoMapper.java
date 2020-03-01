package com.yanjoy.temp.dao;

import com.yanjoy.temp.entity.base.TempParam;
import com.yanjoy.temp.entity.excel.TableLineMessage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TempVoMapper {

    List<TableLineMessage> getTableLineMessage(TempParam param);

}
