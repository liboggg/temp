package com.yanjoy.temp.service;

import com.yanjoy.temp.entity.push.TableLineMessageVo;

import java.util.List;

public interface PushService {
    /**
     * 获取推送数据
     */
    List<TableLineMessageVo> getPushMsg(String dateDay);

    /**
     * 推送消息
     * @param dayTime  yyyy-mm-dd
     */
    void doJob(String dayTime);
}
