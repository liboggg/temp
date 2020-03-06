package com.yanjoy.temp.controller;


import com.yanjoy.temp.entity.base.TempParam;
import com.yanjoy.temp.entity.excel.TableLineMessage;
import com.yanjoy.temp.service.TempExcelService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/test")
public class PushController {

    @Autowired
    private TempExcelService tempExcelService;

        @RequestMapping("/pushMessage")
    public ModelAndView pushMessage(String dateDay) {
            ModelAndView result = new ModelAndView();
        List<TableLineMessage> tableLineMsg = tempExcelService.pushMsg(dateDay);
       // List<TableLineMessageVo> pushMessage = getPushMessage(tableLineMsg);
       //     result.addObject("data", pushMessage);
        return result;
        }



    @Data
    public static class TableLineMessageVo{
            private String activity;
            private String name;
            private String ipaddress;
            private String q1;
            private String q2;
            private String q3;
            private String q4;
            private String q5;
            private String q6;
            private String q7;
            private String q8;
            private String q9;
            private String index;
            private String joinid;
            private String timetaken;
            private Date submittime;
            private String sign;
    }

    @Data
    public static class QVo{
        private String status;
        private String name;
    }


}
