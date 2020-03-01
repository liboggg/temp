package com.yanjoy.temp;

import com.yanjoy.temp.dao.TempEntryMapper;
import com.yanjoy.temp.dao.TempMessageMapper;
import com.yanjoy.temp.entity.entry.TempEntry;
import com.yanjoy.temp.utils.TempTimeUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.util.Date;


@SpringBootTest
class TempApplicationTests {


    @Autowired
    private TempMessageMapper messageMapper;

    @Autowired
    private TempEntryMapper entryMapper;


    @Test
    void contextLoads() throws ParseException {
        Date date = TempTimeUtils.ymdToDate("2019-02-10");
        System.out.println(TempTimeUtils.dateToFullStr(date));
    }


}
