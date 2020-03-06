package com.yanjoy.temp;

import com.yanjoy.temp.controller.PushController;
import com.yanjoy.temp.dao.TempEntryMapper;
import com.yanjoy.temp.dao.TempMessageMapper;
import com.yanjoy.temp.entity.entry.TempEntry;
import com.yanjoy.temp.entity.excel.TableLineMessage;
import com.yanjoy.temp.service.TempExcelService;
import com.yanjoy.temp.utils.TempTimeUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.util.*;


@SpringBootTest
class TempApplicationTests extends TimerTask{


    @Autowired
    private TempMessageMapper messageMapper;

    @Autowired
    private TempEntryMapper entryMapper;

    @Autowired
    private TempExcelService tempExcelService;


    @Test
    void contextLoads() throws ParseException {
        Date date = TempTimeUtils.ymdToDate("2019-02-10");
        System.out.println(TempTimeUtils.dateToFullStr(date));
    }



    public void run() {
        List<TableLineMessage> tableLineMsg = tempExcelService.pushMsg("2020-03-02");
        List<PushController.TableLineMessageVo> pushMessage = getPushMessage(tableLineMsg);
        System.out.println(pushMessage.size());
    }


    public static class TimerManager {
        public static void main(String[] args) {
            new TimerManager();
        }

        //时间间隔(一天)
        private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;

        public TimerManager() {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 18);
            calendar.set(Calendar.MINUTE, 38);
            calendar.set(Calendar.SECOND, 0);
            Date date = calendar.getTime();
            if (date.before(new Date())) {
                date = this.addDay(date, 1);
            }
            Timer timer = new Timer();
            TempApplicationTests task = new TempApplicationTests();
            timer.schedule(task, date, PERIOD_DAY);
        }

        public Date addDay(Date date, int num) {
            Calendar startDT = Calendar.getInstance();
            startDT.setTime(date);
            startDT.add(Calendar.DAY_OF_MONTH, num);
            return startDT.getTime();
        }

    }


    public List<PushController.TableLineMessageVo> getPushMessage(List<TableLineMessage> tableLineMsg){
        List<PushController.TableLineMessageVo> result = new ArrayList<>();
        int index=1;
        for (TableLineMessage tableLineMessage : tableLineMsg) {
            PushController.TableLineMessageVo tableLineMessageVo = new PushController.TableLineMessageVo();
            tableLineMessageVo.setActivity(tableLineMessage.getId());
            tableLineMessageVo.setIpaddress("124.42.243.98");
            if (tableLineMessage.getUser().getProjectId().equals("6245721945602523136")) {
                tableLineMessageVo.setName("广州市轨道交通十一号线疫情防控信息采集");
            } else if (tableLineMessage.getUser().getProjectId().equals("6245721945602523137")) {
                tableLineMessageVo.setName("广州市轨道交通十三号线二期疫情防控信息采集");
            } else if (tableLineMessage.getUser().getProjectId().equals("6245721945602523139")) {
                tableLineMessageVo.setName("广州市轨道交通七号线二期疫情防控信息采集");
            } else if (tableLineMessage.getUser().getProjectId().equals("6422195692059623424")) {
                tableLineMessageVo.setName("广州市中心城区地下综合管廊疫情防控信息采集");
            }

            tableLineMessageVo.setQ1(tableLineMessage.getUser().getUserName());
            tableLineMessageVo.setQ2(tableLineMessage.getUser().getPhone());
            tableLineMessageVo.setQ3(tableLineMessage.getUser().getOrgName());
            tableLineMessageVo.setQ4("广东省广州市[113.27,23.13]");

            if (tableLineMessage.getWorkStatus().getStatus().equals((short)0)) {
                tableLineMessageVo.setQ5("1");
            }else if (tableLineMessage.getWorkStatus().getStatus().equals((short)4)) {
                tableLineMessageVo.setQ5("2");
            }else if (tableLineMessage.getWorkStatus().getStatus().equals((short)2)) {
                tableLineMessageVo.setQ5("3");
            }else if (tableLineMessage.getWorkStatus().getStatus().equals((short)5)) {
                tableLineMessageVo.setQ5("4");
            }


            if (tableLineMessage.getContactHistory().getStatus().equals((short)0)) {
                tableLineMessageVo.setQ6("1");
            }else {
                tableLineMessageVo.setQ6("2");
            }

            if (tableLineMessage.getPersonHistory().getStatus().equals((short) 0)) {
                tableLineMessageVo.setQ7("1");
            }else {
                tableLineMessageVo.setQ7("2");
            }

            if (tableLineMessage.getHealth().getStatus().equals((short) 0)) {
                tableLineMessageVo.setQ8("1");
            }else if (tableLineMessage.getHealth().getStatus().equals((short) 1)){
                tableLineMessageVo.setQ8("2");
            }else if (tableLineMessage.getHealth().getStatus().equals((short) 2)){
                tableLineMessageVo.setQ8("3");
            }else if (tableLineMessage.getHealth().getStatus().equals((short) 3)){
                tableLineMessageVo.setQ8("4");
            }else if (tableLineMessage.getHealth().getStatus().equals((short) 4)){
                tableLineMessageVo.setQ8("5");
            }


            tableLineMessageVo.setQ9("1");


            tableLineMessageVo.setIndex(index+"");
            index++;

            tableLineMessageVo.setJoinid(tableLineMessage.getId());

            Random rand = new Random();
            int randNumber =rand.nextInt(50 - 30 + 1) + 30;
            tableLineMessageVo.setTimetaken(randNumber + "");

            tableLineMessageVo.setSubmittime(tableLineMessage.getMessage().getCreateDate());

            tableLineMessageVo.setSign(tableLineMessage.getId()+index+"fd67e52f-a229-4aff-a7c6-dd6c59259d43");

            result.add(tableLineMessageVo);
        }

        return result;
    }

}
