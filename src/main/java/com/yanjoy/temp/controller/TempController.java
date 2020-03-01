package com.yanjoy.temp.controller;

import com.yanjoy.temp.entity.base.Response;
import com.yanjoy.temp.entity.detail.SaveDetailPo;
import com.yanjoy.temp.service.TempMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fill")
public class TempController {
    @Autowired
    private TempMonitorService service;

    @PostMapping("/save")
    private Response save(@RequestBody SaveDetailPo param) {
        int i = 0;
        try {
            i = service.save(param);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ERROR(false, e.getMessage());
        }
        return Response.SUCCESS(i > 0);
    }

}
