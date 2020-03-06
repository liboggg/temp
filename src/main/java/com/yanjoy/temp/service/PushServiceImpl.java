package com.yanjoy.temp.service;

import com.alibaba.fastjson.JSONObject;
import com.yanjoy.temp.entity.excel.TableLineMessage;
import com.yanjoy.temp.entity.push.TableLineMessageVo;
import com.yanjoy.temp.utils.HttpUtil;
import com.yanjoy.temp.utils.TempTimeUtils;
import com.yanjoy.temp.utils.TimerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service("pushService")
public class PushServiceImpl implements PushService, Runnable {

    private static final Logger logger = LoggerFactory.getLogger(PushServiceImpl.class);
    @Autowired
    private TempExcelService tempExcelService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String POST_URL = "http://yqfk7.crungoo.com:8008/WJX/GetRawDataApi";

    private static final String LOCK = "temp_push_lock";

    /**
     * 每日执行时间
     */
    private static final String PUSH_TIME = "10:00:00";

    @Override
    public List<TableLineMessageVo> getPushMsg(String dateDay) {
        List<TableLineMessage> list = tempExcelService.pushMsg(dateDay);
        return getPushMessage(list);
    }

    public List<TableLineMessageVo> getPushMessage(List<TableLineMessage> tableLineMsg) {
        List<TableLineMessageVo> result = new ArrayList<>();
        int index = 1;
        for (TableLineMessage tableLineMessage : tableLineMsg) {
            TableLineMessageVo tableLineMessageVo = new TableLineMessageVo();
            tableLineMessageVo.setActivity(tableLineMessage.getId());
            tableLineMessageVo.setIpaddress("124.42.243.98");
            if ("6245721945602523136".equals(tableLineMessage.getUser().getProjectId())) {
                tableLineMessageVo.setName("广州市轨道交通十一号线疫情防控信息采集");
            } else if ("6245721945602523137".equals(tableLineMessage.getUser().getProjectId())) {
                tableLineMessageVo.setName("广州市轨道交通十三号线二期疫情防控信息采集");
            } else if ("6245721945602523139".equals(tableLineMessage.getUser().getProjectId())) {
                tableLineMessageVo.setName("广州市轨道交通七号线二期疫情防控信息采集");
            } else if ("6422195692059623424".equals(tableLineMessage.getUser().getProjectId())) {
                tableLineMessageVo.setName("广州市中心城区地下综合管廊疫情防控信息采集");
            } else {
                tableLineMessageVo.setName(" ");
            }
            tableLineMessageVo.setQ1(tableLineMessage.getUser().getUserName());
            tableLineMessageVo.setQ2(tableLineMessage.getUser().getPhone());
            tableLineMessageVo.setQ3(tableLineMessage.getUser().getOrgName());
            tableLineMessageVo.setQ4("广东省广州市[113.27,23.13]");

            if (tableLineMessage.getWorkStatus().getStatus().equals((short) 0)) {
                tableLineMessageVo.setQ5("1");
            } else if (tableLineMessage.getWorkStatus().getStatus().equals((short) 4)) {
                tableLineMessageVo.setQ5("2");
            } else if (tableLineMessage.getWorkStatus().getStatus().equals((short) 2)) {
                tableLineMessageVo.setQ5("3");
            } else if (tableLineMessage.getWorkStatus().getStatus().equals((short) 5)) {
                tableLineMessageVo.setQ5("4");
            }


            if (tableLineMessage.getContactHistory().getStatus().equals((short) 0)) {
                tableLineMessageVo.setQ6("1");
            } else {
                tableLineMessageVo.setQ6("2");
            }

            if (tableLineMessage.getPersonHistory().getStatus().equals((short) 0)) {
                tableLineMessageVo.setQ7("1");
            } else {
                tableLineMessageVo.setQ7("2");
            }

            if (tableLineMessage.getHealth().getStatus().equals((short) 0)) {
                tableLineMessageVo.setQ8("1");
            } else if (tableLineMessage.getHealth().getStatus().equals((short) 1)) {
                tableLineMessageVo.setQ8("2");
            } else if (tableLineMessage.getHealth().getStatus().equals((short) 2)) {
                tableLineMessageVo.setQ8("3");
            } else if (tableLineMessage.getHealth().getStatus().equals((short) 3)) {
                tableLineMessageVo.setQ8("4");
            } else if (tableLineMessage.getHealth().getStatus().equals((short) 4)) {
                tableLineMessageVo.setQ8("5");
            }


            tableLineMessageVo.setQ9("1");


            tableLineMessageVo.setIndex(index + "");
            index++;

            tableLineMessageVo.setJoinid(tableLineMessage.getId());

            Random rand = new Random();
            int randNumber = rand.nextInt(50 - 30 + 1) + 30;
            tableLineMessageVo.setTimetaken(randNumber + "");

            tableLineMessageVo.setSubmittime(tableLineMessage.getMessage().getCreateDate());

            tableLineMessageVo.setSign(tableLineMessage.getId() + index + "fd67e52f-a229-4aff-a7c6-dd6c59259d43");

            result.add(tableLineMessageVo);
        }

        return result;
    }

    @Override
    public void run() {
        synchronized (this) {
            try {
                Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(LOCK, LOCK);
                if (!result) {
                    return;
                } else {
                    if (stringRedisTemplate.hasKey(LOCK)) {
                        stringRedisTemplate.expire(LOCK, 100000, TimeUnit.MILLISECONDS);
                    }
                }
                doJob(TempTimeUtils.dateToYMD(new Date()));
                Thread.sleep(100000);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (stringRedisTemplate.hasKey(LOCK)) {
                        stringRedisTemplate.delete(LOCK);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void doJob(String dayTime) {
        List<TableLineMessageVo> pushMsg = new ArrayList<>();
        try {
            pushMsg = getPushMsg(dayTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (TableLineMessageVo tableLineMessageVo : pushMsg) {
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(tableLineMessageVo);
            executorService.execute(() -> {
                try {
                    String resp = HttpUtil.postJson(POST_URL, jsonObject, "UTF-8");
                    logger.info("temp push result is -> {} , msg is -> {}", resp, jsonObject.toJSONString());
                } catch (IOException e) {
                    logger.error("temp push error , url -> {}, msg -> {}", POST_URL, jsonObject.toJSONString());
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * 任务启动
     */
    @PostConstruct
    public void startJob() {
        TimerUtil.dayJobStart(this, PUSH_TIME);
    }
}
