package com.yanjoy.temp;

import com.alibaba.fastjson.JSONObject;
import com.yanjoy.temp.entity.push.TableLineMessageVo;
import com.yanjoy.temp.service.PushService;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
class TempApplicationTests {


    @Autowired
    private PushService pushService;

    @Test
    void contextLoads() throws InterruptedException {
  //      pushService.doJob("2020-03-05");
//        List<TableLineMessageVo> pushMsg = pushService.getPushMsg("2020-03-01");
//        System.out.println(JSONObject.toJSONString(pushMsg));
    //    Thread.sleep(100000);
    }


}
