package com.yanjoy.temp.controller;

import com.yanjoy.temp.entity.base.PageVo;
import com.yanjoy.temp.entity.base.Response;
import com.yanjoy.temp.entity.config.TempConfig;
import com.yanjoy.temp.entity.base.TempParam;
import com.yanjoy.temp.entity.excel.*;
import com.yanjoy.temp.service.TempExcelService;
import com.yanjoy.temp.service.TempMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 无验证接口
 */
@RestController
@RequestMapping("/pass")
public class PassController {

    @Autowired
    private TempExcelService excelService;

    @Autowired
    private TempMonitorService service;


    /**
     * 阈值配置
     */
    @PostMapping("/saveConfig")
    public Response saveConfig(TempConfig param) {
        int i = 0;
        try {
            i = service.saveConfig(param);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ERROR(false, e.getMessage());
        }
        return Response.SUCCESS(i > 0);
    }

    @GetMapping("/getConfig")
    public Response getConfig() {
        BigDecimal config = new BigDecimal(0);
        try {
            config = service.getConfig();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ERROR(config, e.getMessage());
        }
        return Response.SUCCESS(config);
    }

    /**
     * 劳务/管理列表 内存分页
     */
    @GetMapping("/webChildTable")
    public Response webChildTable(TempParam param) {
        ChildTable data = new ChildTable();
        PageVo pageInfo = new PageVo();
        try {
            data = excelService.childTable(param);
            pageInfo = new PageVo(param.getPageNum(), param.getPageSize(), data.getList());
            data.setList(pageInfo.pagedList());
            pageInfo.setList(new ArrayList());
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ERROR_PAGEVO(data, pageInfo, e.getMessage());
        }
        return Response.SUCCESS_PAGEVO(data, pageInfo);
    }

    /**
     * 劳务/管理列表 内存分页
     */
    @GetMapping("/unNormalChild")
    public Response unNormalChild(TempParam param) {
        UnNormalChildTable data = new UnNormalChildTable();
        try {
            data = excelService.unNormalChild(param);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ERROR(data, e.getMessage());
        }
        return Response.SUCCESS(data);
    }

    /**
     * 总表
     */
    @GetMapping("/sumTable")
    public Response sumTable(TempParam param) {
        SumTable data = new SumTable();
        try {
            data = excelService.sumTable(param);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ERROR(data, e.getMessage());
        }
        return Response.SUCCESS(data);
    }

    /**
     * excel
     */
    @GetMapping("/excelVo")
    public Response excelVo(TempParam param) {
        TempExcelVo data = new TempExcelVo();
        try {
            data = excelService.excelVo(param);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ERROR(data, e.getMessage());
        }
        return Response.SUCCESS(data);
    }

    /**
     * 报警数量
     */
    @GetMapping("/alarmCount")
    public Response alarmCount(TempParam param) {
        int data = 0;
        try {
            List<TableLineMessage> tableLineMsg = excelService.getTableLineMsg(param);
            data = (int) tableLineMsg.stream()
                    .filter(a -> (a.getDetailAlarm() > 0 || a.getBloodAlarm() > 0 || a.getNucleaseAlarm() > 0))
                    .count();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ERROR(data, e.getMessage());
        }
        return Response.SUCCESS(data);
    }



}
