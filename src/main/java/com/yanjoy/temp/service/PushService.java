package com.yanjoy.temp.service;

import com.yanjoy.temp.entity.push.TableLineMessageVo;

import java.util.List;

public interface PushService {
    /**
     * 获取推送数据
     */
    List<TableLineMessageVo> getPushMsg(String dateDay);
}
