package com.yanjoy.temp.service;


import com.github.pagehelper.PageInfo;
import com.yanjoy.temp.entity.base.TempParam;
import com.yanjoy.temp.entity.vo.AppTempVo;

import java.util.List;


public interface TempVoService {

    /**
     * app列表
     * 项目id
     * 身份证
     */
    PageInfo<AppTempVo> appListPage(TempParam param);

    /**
     * app单条
     * 项目id
     * id
     */
    AppTempVo appSingle(TempParam param);

}
