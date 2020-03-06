package com.yanjoy.temp;

import com.yanjoy.temp.entity.push.TableLineMessageVo;
import com.yanjoy.temp.service.PushService;
import com.yanjoy.temp.utils.TempTimeUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;


@SpringBootTest
class TempApplicationTests {


    @Autowired
    private PushService pushService;

    @Test
    void contextLoads() {
//        List<TableLineMessageVo> pushMsg = pushService.getPushMsg("2020-03-01");
//        System.out.println(pushMsg);

    }


}
