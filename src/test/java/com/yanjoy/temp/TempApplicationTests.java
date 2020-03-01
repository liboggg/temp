package com.yanjoy.temp;

import com.yanjoy.temp.dao.TempEntryMapper;
import com.yanjoy.temp.dao.TempMessageMapper;
import com.yanjoy.temp.entity.entry.TempEntry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
class TempApplicationTests {


    @Autowired
    private TempMessageMapper messageMapper;

    @Autowired
    private TempEntryMapper entryMapper;


    @Test
    void contextLoads() {
        TempEntry lastEntryByIdCardAndType = entryMapper.getLastEntryByIdCardAndType("123", "123");
        System.out.println(lastEntryByIdCardAndType);
    }


}
