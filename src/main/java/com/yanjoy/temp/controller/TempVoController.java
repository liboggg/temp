package com.yanjoy.temp.controller;

import com.github.pagehelper.PageInfo;
import com.yanjoy.temp.entity.base.Response;
import com.yanjoy.temp.entity.base.TempParam;
import com.yanjoy.temp.entity.vo.AppTempVo;
import com.yanjoy.temp.service.TempVoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PC
 */
@RestController
@RequestMapping("/vo")
public class TempVoController {

    @Autowired
    private TempVoService service;

    @GetMapping("/appList")
    public Response appList(TempParam param) {
        List<AppTempVo> list = new ArrayList<>();
        try {
            list = service.appListPage(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PageInfo pageInfo = new PageInfo(list);
        return Response.SUCCESS(pageInfo);
    }

    @GetMapping("/appSingle")
    public Response appSingle(TempParam param) {
        AppTempVo data = new AppTempVo();
        try {
            data = service.appSingle(param);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ERROR(data, e.getMessage());
        }
        return Response.SUCCESS(data);
    }
}
