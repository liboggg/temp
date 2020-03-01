package com.yanjoy.temp.controller;

import com.alibaba.fastjson.JSONObject;
import com.yanjoy.temp.entity.base.TempParam;
import com.yanjoy.temp.entity.detail.TempDetail;
import com.yanjoy.temp.entity.excel.*;
import com.yanjoy.temp.entity.org.TempOrgTree;
import com.yanjoy.temp.service.TempExcelService;
import com.yanjoy.temp.utils.PoiUtils;
import lombok.Data;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/excel")
public class ExcelController {

    @Autowired
    private TempExcelService excelService;


    @GetMapping("/exportExcel")
    public void exportExcel(HttpServletResponse response, TempParam param) {
        try {
            TempExcelVo tempExcelVo = excelService.excelVo(param);
            PoiUtils.webExportExcel(response, getSituation(tempExcelVo), excelService.chooseProjectName(param.getProjectId()) + "体温报表（总表及日报）");
        } catch (ExecutionException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }


    public static HSSFWorkbook getSituation(TempExcelVo tempExcelVo) throws IOException {
        //总表
        SumTable sumTable = tempExcelVo.getSumTable();

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String person = "在穗人员(" + sumTable.getNowInArea() + "人)";
        String[] strings = {"日期", sumTable.getDateDay(), person};
        String[] detail = {"单位", "管理人员(" + sumTable.getManagerStatistics().getNowInArea() + "人)", "劳务人员(" + sumTable.getLabourStatistics().getNowInArea() + "人)"};
        String[] four = {"处于14天隔离期", "已完成14天隔离", "休假期间一直在穗", "体温异常", "未填报", "处于14天隔离期", "已完成14天隔离", "休假期间一直在穗", "体温异常", "未填报"};
      //  FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\ycl\\Desktop\\新建文件夹 (4)\\" + System.currentTimeMillis() + ".xls");


        //起始行
        int startRowIndex = 0;

        //创建工作簿  设置字体样式
        HSSFWorkbook work = new HSSFWorkbook();
        //宋体18
        HSSFCellStyle thinStyle = PoiUtils.getDefaultFontAndCenter(work);
        //微软12
        HSSFCellStyle weiRuan12 = PoiUtils.getWeiRruan(work);
        HSSFCellStyle weiRuan10 = PoiUtils.getWeiRruan10(work);
        HSSFCellStyle red10 = PoiUtils.getred10(work);
        HSSFCellStyle orange10 = PoiUtils.getOrange10(work);
        //宋体12
        HSSFCellStyle songTi12 = PoiUtils.getSongTi(work);
        HSSFCellStyle songTiNo12 = PoiUtils.getSongTiNo(work);
        HSSFCellStyle songTiNoleft = PoiUtils.getSongTiLeft(work);
        HSSFCellStyle thickStyle = PoiUtils.getBigFontAndCenter(work);

        HSSFSheet sheet = work.createSheet("总表");

        work.createSheet("管理人员体温日报");
        work.createSheet("劳务人员体温日报");
        work.createSheet("异常人员统计表");

        //第一行
        Row row1 = sheet.createRow(startRowIndex);
        Cell cell01 = row1.createCell(0);
        cell01.setCellValue(sumTable.getTitle());
        PoiUtils.mergeCell(sheet, 0, 0, 0, 15);
        PoiUtils.mergeForm(sheet, work, 0, 0, 0, 15);
        cell01.setCellStyle(songTi12);
        row1.setHeight((short) 800);
        sheet.setColumnWidth(0, 84 * 256);


        startRowIndex++;

        //第二行
        Row row2 = sheet.createRow(startRowIndex);
        for (int i = 0; i < strings.length; i++) {
            Cell cell2 = row2.createCell(i);
            cell2.setCellValue(strings[i]);
            cell2.setCellStyle(weiRuan12);
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, i, i);
            sheet.setColumnWidth(i, 11 * 256);
            if (i == 1) {
                sheet.setColumnWidth(i, 14 * 256);
            }
            if (i == 2) {
                PoiUtils.mergeCell(sheet, 1, 1, 2, 15);
                PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, i, 15);
            }
        }
        row2.setHeight((short) 600);

        startRowIndex++;

        //第三行
        Row row3 = sheet.createRow(startRowIndex);
        for (int i = 0; i <= 9; i++) {
            Cell cell3 = row3.createCell(i);
            if (1 != 1) {
                PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, i, i);
            }
            if (i == 0) {
                cell3.setCellValue(detail[0]);
                cell3.setCellStyle(weiRuan12);
            }
            if (i == 2) {
                cell3.setCellValue(detail[1]);
                cell3.setCellStyle(weiRuan12);
            }
            if (i == 9) {
                cell3.setCellValue(detail[2]);
                cell3.setCellStyle(weiRuan12);
            }
        }
        PoiUtils.mergeCell(sheet, 2, 3, 0, 1);
        PoiUtils.mergeCell(sheet, 2, 2, 2, 8);
        PoiUtils.mergeCell(sheet, 2, 2, 9, 15);
        row3.setHeight((short) 340);

        startRowIndex++;

        //第四行
        Row row4 = sheet.createRow(startRowIndex);

        for (int i = 0; i < four.length; i++) {
            Cell cell4 = row4.createCell(i + 2);
            cell4.setCellValue(four[i]);
            cell4.setCellStyle(weiRuan12);
            sheet.setColumnWidth(i + 2, 11 * 256);
        }

        PoiUtils.mergeForm(sheet, work, 2, 3, 0, 1);
        PoiUtils.mergeForm(sheet, work, 2, 2, 2, 8);
        PoiUtils.mergeForm(sheet, work, 2, 2, 9, 15);
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 2, 2);
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 3, 3);
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 4, 4);
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 5, 5);
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 6, 6);
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 7, 7);
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 8, 8);
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 9, 9);
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 10, 10);
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 11, 11);
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 12, 12);
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 13, 13);
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 14, 14);
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 15, 15);

        row4.setHeight((short) 660);

        startRowIndex++;

        //第五行
        Row row5 = sheet.createRow(startRowIndex);
        Cell cell5 = row5.createCell(0);
        cell5.setCellValue("统计");
        PoiUtils.mergeCell(sheet, startRowIndex, startRowIndex, 0, 1);
        cell5.setCellStyle(weiRuan12);

        Cell row5Cell4 = row5.createCell(2);
        row5Cell4.setCellValue(sumTable.getManagerStatistics().getInIso());
        row5Cell4.setCellStyle(weiRuan10);
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 2, 2);

        Cell row5Cell = row5.createCell(3);
        row5Cell.setCellValue(sumTable.getManagerStatistics().getOverIso());
        row5Cell.setCellStyle(weiRuan10);
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 3, 3);


        Cell row5Cell1 = row5.createCell(4);
        row5Cell1.setCellValue(sumTable.getManagerStatistics().getInIso());
        row5Cell1.setCellStyle(weiRuan10);
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 4, 4);

        Cell row5Cell2 = row5.createCell(5);
        row5Cell2.setCellValue(sumTable.getManagerStatistics().getTempAbnormal());
        row5Cell2.setCellStyle(weiRuan10);
        if (sumTable.getManagerStatistics().getTempAbnormal() != 0) {
            PoiUtils.setBackColor(work, row5Cell2);
            //row5Cell2.setCellStyle(red10);
        }
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 5, 5);

        Cell row5Cell3 = row5.createCell(6);
        row5Cell3.setCellValue(sumTable.getManagerStatistics().getNoReport());
        row5Cell3.setCellStyle(weiRuan10);
        if (sumTable.getManagerStatistics().getNoReport() != 0) {
            row5Cell3.setCellStyle(orange10);
        }

        //血液检测、核酸检测
        Cell cell29 = row5.createCell(7);
        cell29.setCellValue(sumTable.getManagerStatistics().getBloodAlarm());
        cell29.setCellStyle(weiRuan10);
        if (sumTable.getManagerStatistics().getBloodAlarm() != 0) {
            cell29.setCellStyle(red10);
            PoiUtils.setBackColor(work, cell29);
        }
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 7, 7);

        Cell cell30 = row5.createCell(8);
        cell30.setCellValue(sumTable.getManagerStatistics().getNucleaseAlarm());
        cell30.setCellStyle(weiRuan10);
        if (sumTable.getManagerStatistics().getNucleaseAlarm() != 0) {
            cell30.setCellStyle(red10);
            PoiUtils.setBackColor(work, cell30);
        }
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 8, 8);

        Cell row5Cell5 = row5.createCell(9);
        row5Cell5.setCellValue(sumTable.getLabourStatistics().getInIso());
        row5Cell5.setCellStyle(weiRuan10);
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 9, 9);

        Cell row5Cell6 = row5.createCell(10);
        row5Cell6.setCellValue(sumTable.getLabourStatistics().getOverIso());
        row5Cell6.setCellStyle(weiRuan10);
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 10, 10);

        Cell row5Cell7 = row5.createCell(11);
        row5Cell7.setCellValue(sumTable.getLabourStatistics().getInArea());
        row5Cell7.setCellStyle(weiRuan10);
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 11, 11);

        Cell row5Cell8 = row5.createCell(12);
        row5Cell8.setCellValue(sumTable.getLabourStatistics().getTempAbnormal());
        row5Cell8.setCellStyle(weiRuan10);
        if (sumTable.getLabourStatistics().getTempAbnormal() != 0) {
            PoiUtils.setBackColor(work, row5Cell8);
        }
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 12, 12);

        Cell row5Cell9 = row5.createCell(13);
        row5Cell9.setCellValue(sumTable.getLabourStatistics().getNoReport());
        row5Cell9.setCellStyle(weiRuan10);
        if (sumTable.getLabourStatistics().getNoReport() != 0) {
            row5Cell9.setCellStyle(orange10);
        }
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 13, 13);

        //血液检测、核酸检测
        Cell cell27 = row5.createCell(14);
        cell27.setCellValue(sumTable.getLabourStatistics().getBloodAlarm());
        cell27.setCellStyle(weiRuan10);
        if (sumTable.getLabourStatistics().getBloodAlarm() != 0) {
            cell27.setCellStyle(red10);
            PoiUtils.setBackColor(work, cell27);
        }
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 14, 14);

        Cell cell28 = row5.createCell(15);
        cell28.setCellValue(sumTable.getLabourStatistics().getNucleaseAlarm());
        cell28.setCellStyle(weiRuan10);
        if (sumTable.getLabourStatistics().getNucleaseAlarm() != 0) {
            cell28.setCellStyle(red10);
            PoiUtils.setBackColor(work, cell28);
        }
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 15, 15);


        row5.setHeight((short) 500);
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 0, 1);

        startRowIndex++;

        //第六行
        Row row6 = sheet.createRow(startRowIndex);
        Cell cell6 = row6.createCell(0);
        cell6.setCellValue("经理部");
        PoiUtils.mergeCell(sheet, startRowIndex, startRowIndex, 0, 1);
        cell6.setCellStyle(weiRuan10);
        if (1 == 1) {
            AlarmCalculate managerStatistics = sumTable.getList().get(0).getNextChild().get(0).getManagerStatistics();
            AlarmCalculate labourStatistics = sumTable.getList().get(0).getNextChild().get(0).getLabourStatistics();

            Cell row6Cell = row6.createCell(2);
            row6Cell.setCellValue(managerStatistics.getInIso());
            row6Cell.setCellStyle(weiRuan10);
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 2, 2);

            Cell row6Cell1 = row6.createCell(3);
            row6Cell1.setCellValue(managerStatistics.getOverIso());
            row6Cell1.setCellStyle(weiRuan10);
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 3, 3);


            Cell row6Cell2 = row6.createCell(4);
            row6Cell2.setCellValue(managerStatistics.getInArea());
            row6Cell2.setCellStyle(weiRuan10);
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 4, 4);

            Cell row6Cell3 = row6.createCell(5);
            row6Cell3.setCellValue(managerStatistics.getTempAbnormal());
            row6Cell3.setCellStyle(weiRuan10);
            if (managerStatistics.getTempAbnormal() != 0) {
                PoiUtils.setBackColor(work, row6Cell3);
            }
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 5, 5);

            Cell row6Cell4 = row6.createCell(6);
            row6Cell4.setCellValue(managerStatistics.getNoReport());
            row6Cell4.setCellStyle(weiRuan10);
            if (managerStatistics.getNoReport() != 0) {
                row6Cell4.setCellStyle(orange10);
            }
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 6, 6);

            //血液检测、核酸检测
            Cell row6Cell12 = row6.createCell(7);
            if (!sumTable.getList().isEmpty() && sumTable.getList().get(0).getManagerStatistics() != null) {
                row6Cell12.setCellValue(sumTable.getList().get(0).getManagerStatistics().getBloodAlarm());
                row6Cell12.setCellStyle(weiRuan10);
                if (sumTable.getList().get(0).getManagerStatistics().getBloodAlarm() != 0) {
                    row6Cell12.setCellStyle(red10);
                    PoiUtils.setBackColor(work, row6Cell12);
                }
            }else {
                row6Cell12.setCellValue("0");
                row6Cell12.setCellStyle(weiRuan10);
            }

            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 7, 7);

            Cell row6Cell13 = row6.createCell(8);
            if (!sumTable.getList().isEmpty() && sumTable.getList().get(0).getManagerStatistics() != null) {
                row6Cell13.setCellValue(sumTable.getList().get(0).getManagerStatistics().getNucleaseAlarm());
                row6Cell13.setCellStyle(weiRuan10);
                if (sumTable.getList().get(0).getManagerStatistics().getNucleaseAlarm()!= 0) {
                    row6Cell13.setCellStyle(red10);
                    PoiUtils.setBackColor(work, row6Cell13);
                }
            }else {
                row6Cell13.setCellValue("0");
                row6Cell13.setCellStyle(weiRuan10);
            }

            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 8, 8);

            Cell row6Cell5 = row6.createCell(9);
            row6Cell5.setCellValue(labourStatistics.getInArea());
            row6Cell5.setCellStyle(weiRuan10);
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 7, 9);

            Cell row6Cell6 = row6.createCell(10);
            row6Cell6.setCellValue(labourStatistics.getOverIso());
            row6Cell6.setCellStyle(weiRuan10);
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 10, 10);

            Cell row6Cell7 = row6.createCell(11);
            row6Cell7.setCellValue(labourStatistics.getInArea());
            row6Cell7.setCellStyle(weiRuan10);
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 11, 11);

            Cell row6Cell8 = row6.createCell(12);
            row6Cell8.setCellValue(labourStatistics.getTempAbnormal());
            row6Cell8.setCellStyle(weiRuan10);
            if (labourStatistics.getTempAbnormal() != 0) {
                PoiUtils.setBackColor(work, row6Cell8);
            }
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 12, 12);

            Cell row6Cell9 = row6.createCell(13);
            row6Cell9.setCellValue(labourStatistics.getNoReport());
            row6Cell9.setCellStyle(weiRuan10);
            if (labourStatistics.getNoReport() != 0) {
                row6Cell9.setCellStyle(orange10);
            }
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 13, 13);

            row6.setHeight((short) 480);

            //血液检测、核酸检测
            Cell row6Cell10 = row6.createCell(14);
            if (sumTable.getList().get(0).getLabourStatistics() != null) {
                row6Cell10.setCellValue(sumTable.getList().get(0).getLabourStatistics().getBloodAlarm());
                row6Cell10.setCellStyle(weiRuan10);
                if (sumTable.getList().get(0).getLabourStatistics().getBloodAlarm()!= 0) {
                    row6Cell10.setCellStyle(red10);
                    PoiUtils.setBackColor(work, row6Cell10);
                }
            }else {
                row6Cell10.setCellValue("0");
                row6Cell10.setCellStyle(weiRuan10);
            }
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 14, 14);

            Cell row6Cell11 = row6.createCell(15);
            if (sumTable.getList().get(0).getLabourStatistics() != null) {
                row6Cell11.setCellValue(sumTable.getList().get(0).getLabourStatistics().getNucleaseAlarm());
                row6Cell11.setCellStyle(weiRuan10);
                if (sumTable.getList().get(0).getLabourStatistics().getNucleaseAlarm()!= 0) {
                    row6Cell11.setCellStyle(red10);
                    PoiUtils.setBackColor(work, row6Cell11);
                }
            }else {
                row6Cell11.setCellValue("0");
                row6Cell11.setCellStyle(weiRuan10);
            }

            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 15, 15);

            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 0, 1);

        }


        startRowIndex++;

        //第七行
        Row row7 = sheet.createRow(startRowIndex);
        Cell cell7 = row7.createCell(0);
        cell7.setCellValue("一分部\n" + "（一局）");
        PoiUtils.mergeCell(sheet, 6, 8, 0, 0);
        cell7.setCellStyle(weiRuan10);


        Cell cell71 = row7.createCell(1);
        cell71.setCellValue("本分部");
        cell71.setCellStyle(weiRuan10);
        if (1 == 1) {
            AlarmCalculate managerStatistics = sumTable.getList().get(1).getNextChild().get(0).getManagerStatistics();
            AlarmCalculate labourStatistics = sumTable.getList().get(1).getNextChild().get(0).getLabourStatistics();

            Cell row6Cell = row7.createCell(2);
            row6Cell.setCellValue(managerStatistics.getInIso());
            row6Cell.setCellStyle(weiRuan10);
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 2, 2);

            Cell row6Cell1 = row7.createCell(3);
            row6Cell1.setCellValue(managerStatistics.getOverIso());
            row6Cell1.setCellStyle(weiRuan10);
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 3, 3);


            Cell row6Cell2 = row7.createCell(4);
            row6Cell2.setCellValue(managerStatistics.getInArea());
            row6Cell2.setCellStyle(weiRuan10);
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 4, 4);

            Cell row6Cell3 = row7.createCell(5);
            row6Cell3.setCellValue(managerStatistics.getTempAbnormal());
            row6Cell3.setCellStyle(weiRuan10);
            if (managerStatistics.getTempAbnormal() != 0) {
                PoiUtils.setBackColor(work, row6Cell3);
            }
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 5, 5);

            Cell row6Cell4 = row7.createCell(6);
            row6Cell4.setCellValue(managerStatistics.getNoReport());
            row6Cell4.setCellStyle(weiRuan10);
            if (managerStatistics.getNoReport() != 0) {
                row6Cell4.setCellStyle(orange10);
            }
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 6, 6);

            //血液检测、核酸检测
            Cell row6Cell12 = row7.createCell(7);
            if (sumTable.getList().get(1).getManagerStatistics() != null) {
                row6Cell12.setCellValue(sumTable.getList().get(1).getManagerStatistics().getBloodAlarm());
                row6Cell12.setCellStyle(weiRuan10);
                if (sumTable.getList().get(1).getManagerStatistics().getBloodAlarm()!= 0) {
                    row6Cell12.setCellStyle(red10);
                    PoiUtils.setBackColor(work, row6Cell12);
                }
            }else {
                row6Cell12.setCellValue("0");
                row6Cell12.setCellStyle(weiRuan10);
            }

            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 7, 7);

            Cell row6Cell13 = row7.createCell(8);
            if (sumTable.getList().get(1).getManagerStatistics() != null) {
                row6Cell13.setCellValue(sumTable.getList().get(1).getManagerStatistics().getNucleaseAlarm());
                row6Cell13.setCellStyle(weiRuan10);
                if (sumTable.getList().get(1).getManagerStatistics().getNucleaseAlarm()!= 0) {
                    row6Cell13.setCellStyle(red10);
                    PoiUtils.setBackColor(work, row6Cell13);
                }
            }else {
                row6Cell13.setCellValue("0");
                row6Cell13.setCellStyle(weiRuan10);
            }

            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 8, 8);

            Cell row6Cell5 = row7.createCell(9);
            row6Cell5.setCellValue(labourStatistics.getInIso());
            row6Cell5.setCellStyle(weiRuan10);
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 7, 9);

            Cell row6Cell6 = row7.createCell(10);
            row6Cell6.setCellValue(labourStatistics.getOverIso());
            row6Cell6.setCellStyle(weiRuan10);
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 10, 10);

            Cell row6Cell7 = row7.createCell(11);
            row6Cell7.setCellValue(labourStatistics.getInArea());
            row6Cell7.setCellStyle(weiRuan10);
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 11, 11);

            Cell row6Cell8 = row7.createCell(12);
            row6Cell8.setCellValue(labourStatistics.getTempAbnormal());
            row6Cell8.setCellStyle(weiRuan10);
            if (labourStatistics.getTempAbnormal() != 0) {
                PoiUtils.setBackColor(work, row6Cell8);
            }
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 12, 12);

            Cell row6Cell9 = row7.createCell(13);
            row6Cell9.setCellValue(labourStatistics.getNoReport());
            row6Cell9.setCellStyle(weiRuan10);
            if (labourStatistics.getNoReport() != 0) {
                row6Cell9.setCellStyle(orange10);
            }
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 13, 13);

            //血液检测、核酸检测
            Cell row7Cell = row7.createCell(14);
            if (sumTable.getList().get(1).getLabourStatistics() != null) {
                row7Cell.setCellValue(sumTable.getList().get(1).getLabourStatistics().getBloodAlarm());
                row7Cell.setCellStyle(weiRuan10);
                if (sumTable.getList().get(1).getLabourStatistics().getBloodAlarm() != 0) {
                    row7Cell.setCellStyle(red10);
                    PoiUtils.setBackColor(work, row7Cell);
                }
            }else {
                row7Cell.setCellValue("0");
                row7Cell.setCellStyle(weiRuan10);
            }

            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 14, 14);

            Cell row7Cell1 = row7.createCell(15);
            if (sumTable.getList().get(1).getLabourStatistics() != null) {
                row7Cell1.setCellValue(sumTable.getList().get(1).getLabourStatistics().getNucleaseAlarm());
                row7Cell1.setCellStyle(weiRuan10);
                if (sumTable.getList().get(1).getLabourStatistics().getNucleaseAlarm()!= 0) {
                    row7Cell1.setCellStyle(red10);
                    PoiUtils.setBackColor(work, row7Cell1);
                }
            }else {
                row7Cell1.setCellValue("0");
                row7Cell1.setCellStyle(weiRuan10);
            }

            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 15, 15);
        }

        row7.setHeight((short) 360);

        startRowIndex++;

        //第八行
        Row row8 = sheet.createRow(startRowIndex);
        Cell cell8 = row8.createCell(1);
        cell8.setCellValue("一工区");
        cell8.setCellStyle(weiRuan10);

        if (1 == 1) {
            AlarmCalculate managerStatistics = sumTable.getList().get(1).getNextChild().get(1).getManagerStatistics();
            AlarmCalculate labourStatistics = sumTable.getList().get(1).getNextChild().get(1).getLabourStatistics();

            Cell row6Cell = row8.createCell(2);
            row6Cell.setCellValue(managerStatistics.getInIso());
            row6Cell.setCellStyle(weiRuan10);
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 2, 2);

            Cell row6Cell1 = row8.createCell(3);
            row6Cell1.setCellValue(managerStatistics.getOverIso());
            row6Cell1.setCellStyle(weiRuan10);
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 3, 3);


            Cell row6Cell2 = row8.createCell(4);
            row6Cell2.setCellValue(managerStatistics.getInArea());
            row6Cell2.setCellStyle(weiRuan10);
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 4, 4);

            Cell row6Cell3 = row8.createCell(5);
            row6Cell3.setCellValue(managerStatistics.getTempAbnormal());
            row6Cell3.setCellStyle(weiRuan10);
            if (managerStatistics.getTempAbnormal() != 0) {
                PoiUtils.setBackColor(work, row6Cell3);
            }
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 5, 5);

            Cell row6Cell4 = row8.createCell(6);
            row6Cell4.setCellValue(managerStatistics.getNoReport());
            row6Cell4.setCellStyle(weiRuan10);
            if (managerStatistics.getNoReport() != 0) {
                row6Cell4.setCellStyle(orange10);
            }
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 6, 6);

            //血液检测、核酸检测
            Cell row6Cell12 = row8.createCell(7);
            if (sumTable.getList().get(1).getNextChild().get(1).getManagerStatistics() != null) {
                row6Cell12.setCellValue(sumTable.getList().get(1).getNextChild().get(1).getManagerStatistics().getBloodAlarm());
                row6Cell12.setCellStyle(weiRuan10);
                if (sumTable.getList().get(1).getNextChild().get(1).getManagerStatistics().getBloodAlarm()!= 0) {
                    row6Cell12.setCellStyle(red10);
                    PoiUtils.setBackColor(work, row6Cell12);
                }
            }else {
                row6Cell12.setCellValue("0");
                row6Cell12.setCellStyle(weiRuan10);
            }

            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 7, 7);

            Cell row6Cell13 = row8.createCell(8);
            if (sumTable.getList().get(1).getNextChild().get(1).getManagerStatistics() != null) {
                row6Cell13.setCellValue(sumTable.getList().get(1).getNextChild().get(1).getManagerStatistics().getNucleaseAlarm());
                row6Cell13.setCellStyle(weiRuan10);
                if (sumTable.getList().get(1).getNextChild().get(1).getManagerStatistics().getNucleaseAlarm()!= 0) {
                    row6Cell13.setCellStyle(red10);
                    PoiUtils.setBackColor(work, row6Cell13);
                }
            }else {
                row6Cell13.setCellValue("0");
                row6Cell13.setCellStyle(weiRuan10);
            }
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 8, 8);

            Cell row6Cell5 = row8.createCell(9);
            row6Cell5.setCellValue(labourStatistics.getInIso());
            row6Cell5.setCellStyle(weiRuan10);
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 7, 9);

            Cell row6Cell6 = row8.createCell(10);
            row6Cell6.setCellValue(labourStatistics.getOverIso());
            row6Cell6.setCellStyle(weiRuan10);
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 10, 10);

            Cell row6Cell7 = row8.createCell(11);
            row6Cell7.setCellValue(labourStatistics.getInArea());
            row6Cell7.setCellStyle(weiRuan10);
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 11, 11);

            Cell row6Cell8 = row8.createCell(12);
            row6Cell8.setCellValue(labourStatistics.getTempAbnormal());
            row6Cell8.setCellStyle(weiRuan10);
            if (labourStatistics.getTempAbnormal() != 0) {
                PoiUtils.setBackColor(work, row6Cell8);
            }
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 12, 12);

            Cell row6Cell9 = row8.createCell(13);
            row6Cell9.setCellValue(labourStatistics.getNoReport());
            row6Cell9.setCellStyle(weiRuan10);
            if (labourStatistics.getNoReport() != 0) {
                row6Cell9.setCellStyle(orange10);
            }
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 13, 13);

            //血液检测、核酸检测
            Cell row8Cell = row8.createCell(14);
            if (sumTable.getList().get(1).getNextChild().get(1).getLabourStatistics() != null) {
                row8Cell.setCellValue(sumTable.getList().get(1).getNextChild().get(1).getLabourStatistics().getBloodAlarm());
                row8Cell.setCellStyle(weiRuan10);
                if (sumTable.getList().get(1).getNextChild().get(1).getLabourStatistics().getBloodAlarm()!= 0) {
                    row8Cell.setCellStyle(red10);
                    PoiUtils.setBackColor(work, row8Cell);
                }
            }else {
                row8Cell.setCellValue("0");
                row8Cell.setCellStyle(weiRuan10);

            }
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 14, 14);

            Cell row8Cell1 = row8.createCell(15);
            if (sumTable.getList().get(1).getNextChild().get(1).getLabourStatistics() != null) {
                row8Cell1.setCellValue(sumTable.getList().get(1).getNextChild().get(1).getLabourStatistics().getNucleaseAlarm());
                row8Cell1.setCellStyle(weiRuan10);
                if (sumTable.getList().get(1).getNextChild().get(1).getLabourStatistics().getNucleaseAlarm()!= 0) {
                    row8Cell1.setCellStyle(red10);
                    PoiUtils.setBackColor(work, row8Cell1);
                }
            }else {
                row8Cell1.setCellValue("0");
                row8Cell1.setCellStyle(weiRuan10);

            }
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 15, 15);
        }

        startRowIndex++;

        //第九行
        Row row9 = sheet.createRow(startRowIndex);
        Cell cell9 = row9.createCell(1);
        cell9.setCellValue("二工区");
        cell9.setCellStyle(weiRuan10);

        if (1 == 1) {
            AlarmCalculate managerStatistics = sumTable.getList().get(1).getNextChild().get(2).getManagerStatistics();
            AlarmCalculate labourStatistics = sumTable.getList().get(1).getNextChild().get(2).getLabourStatistics();

            Cell row6Cell = row9.createCell(2);
            row6Cell.setCellValue(managerStatistics.getInIso());
            row6Cell.setCellStyle(weiRuan10);
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 2, 2);

            Cell row6Cell1 = row9.createCell(3);
            row6Cell1.setCellValue(managerStatistics.getOverIso());
            row6Cell1.setCellStyle(weiRuan10);
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 3, 3);


            Cell row6Cell2 = row9.createCell(4);
            row6Cell2.setCellValue(managerStatistics.getInArea());
            row6Cell2.setCellStyle(weiRuan10);
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 4, 4);

            Cell row6Cell3 = row9.createCell(5);
            row6Cell3.setCellValue(managerStatistics.getTempAbnormal());
            row6Cell3.setCellStyle(weiRuan10);
            if (managerStatistics.getTempAbnormal() != 0) {
                PoiUtils.setBackColor(work, row6Cell3);
            }
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 5, 5);

            Cell row6Cell4 = row9.createCell(6);
            row6Cell4.setCellValue(managerStatistics.getNoReport());
            row6Cell4.setCellStyle(weiRuan10);
            if (managerStatistics.getNoReport() != 0) {
                row6Cell4.setCellStyle(orange10);
            }
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 6, 6);

            //血液检测、核酸检测
            Cell row9Cell = row9.createCell(7);
            if (managerStatistics != null) {
                row9Cell.setCellValue(managerStatistics.getBloodAlarm());
                row9Cell.setCellStyle(weiRuan10);
                if (managerStatistics.getBloodAlarm()!= 0) {
                    row9Cell.setCellStyle(red10);
                    PoiUtils.setBackColor(work, row9Cell);
                }
            }else {
                row9Cell.setCellValue("0");
                row9Cell.setCellStyle(weiRuan10);
            }
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 7, 7);

            Cell row9Cell1 = row9.createCell(8);
            if (managerStatistics != null) {
                row9Cell1.setCellValue(managerStatistics.getNucleaseAlarm());
                row9Cell1.setCellStyle(weiRuan10);
                if (managerStatistics.getNucleaseAlarm()!= 0) {
                    row9Cell1.setCellStyle(red10);
                    PoiUtils.setBackColor(work, row9Cell1);
                }
            }else {
                row9Cell1.setCellValue("0");
                row9Cell1.setCellStyle(weiRuan10);
            }
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 8, 8);

            Cell row6Cell5 = row9.createCell(9);
            row6Cell5.setCellValue(labourStatistics.getInIso());
            row6Cell5.setCellStyle(weiRuan10);
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 9, 9);

            Cell row6Cell6 = row9.createCell(10);
            row6Cell6.setCellValue(labourStatistics.getOverIso());
            row6Cell6.setCellStyle(weiRuan10);
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 10, 10);

            Cell row6Cell7 = row9.createCell(11);
            row6Cell7.setCellValue(labourStatistics.getInArea());
            row6Cell7.setCellStyle(weiRuan10);
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 11, 11);

            Cell row6Cell8 = row9.createCell(12);
            row6Cell8.setCellValue(labourStatistics.getTempAbnormal());
            row6Cell8.setCellStyle(weiRuan10);
            if (labourStatistics.getTempAbnormal() != 0) {
                PoiUtils.setBackColor(work, row6Cell8);
            }
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 12, 12);

            Cell row6Cell9 = row9.createCell(13);
            row6Cell9.setCellValue(labourStatistics.getNoReport());
            row6Cell9.setCellStyle(weiRuan10);
            if (labourStatistics.getNoReport() != 0) {
                row6Cell9.setCellStyle(orange10);
            }
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 13, 13);

            //血液检测、核酸检测
            Cell row8Cell = row9.createCell(14);
            if (labourStatistics != null) {
                row8Cell.setCellValue(labourStatistics.getBloodAlarm());
                row8Cell.setCellStyle(weiRuan10);
                if (labourStatistics.getBloodAlarm()!= 0) {
                    row8Cell.setCellStyle(red10);
                    PoiUtils.setBackColor(work, row8Cell);
                }
            }else {
                row8Cell.setCellValue("0");
                row8Cell.setCellStyle(weiRuan10);
            }
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 14, 14);

            Cell row8Cell1 = row9.createCell(15);
            if (labourStatistics != null) {
                row8Cell1.setCellValue(labourStatistics.getNucleaseAlarm());
                row8Cell1.setCellStyle(weiRuan10);
                if (labourStatistics.getBloodAlarm()!= 0) {
                    row8Cell1.setCellStyle(red10);
                    PoiUtils.setBackColor(work, row8Cell1);
                }
            }else {
                row8Cell1.setCellValue("0");
                row8Cell1.setCellStyle(weiRuan10);
            }
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 15, 15);
        }

        startRowIndex++;

        //二分部
        String[] orgTwo = {"本分部", "一工区", "二工区", "三工区", "四工区", "五工区"};
        RowAndNum rowNum = getColumnDetail(startRowIndex, work, sheet, "二分部\n" + "（二局）", orgTwo, weiRuan10, sumTable, 2, red10, orange10);
        PoiUtils.mergeForm(sheet, work, 9, 14, 0, 0);

        //三分部
        String[] orgThree = {"本分部", "一工区"};
        RowAndNum rowNum1 = getColumnDetail(rowNum.getStartRowIndex(), work, sheet, "三分部\n" + "（三局）", orgThree, weiRuan10, sumTable, 3, red10, orange10);
        PoiUtils.mergeForm(sheet, work, 15, 16, 0, 0);

        //四分部
        String[] orgFour = {"本分部", "一工区", "二工区", "三工区"};
        RowAndNum rowNum2 = getColumnDetail(rowNum1.getStartRowIndex(), work, sheet, "四分部\n" + "（五局）", orgFour, weiRuan10, sumTable, 4, red10, orange10);
        PoiUtils.mergeForm(sheet, work, 17, 20, 0, 0);

        //五分部
        String[] orgFive = {"本分部", "一工区"};
        RowAndNum rowNum3 = getColumnDetail(rowNum2.getStartRowIndex(), work, sheet, "五分部\n" + "（隧道局）", orgFive, weiRuan10, sumTable, 5, red10, orange10);
        PoiUtils.mergeForm(sheet, work, 21, 22, 0, 0);


        //六分部
        String[] orgSix = {"本分部", "一工区", "二工区"};
        RowAndNum rowNum4 = getColumnDetail(rowNum3.getStartRowIndex(), work, sheet, "六分部\n" + "（广州局）", orgSix, weiRuan10, sumTable, 6, red10, orange10);
        PoiUtils.mergeForm(sheet, work, 23, 25, 0, 0);

        //七分部
        String[] orgSeven = {"本分部", "一工区"};
        RowAndNum rowNum5 = getColumnDetail(rowNum4.getStartRowIndex(), work, sheet, "七分部\n" + "（上海局）", orgSeven, weiRuan10, sumTable, 7, red10, orange10);
        PoiUtils.mergeForm(sheet, work, 26, 27, 0, 0);


        //八分部
        String[] orgEight = {"本分部", "一工区"};
        RowAndNum rowNum6 = getColumnDetail(rowNum5.getStartRowIndex(), work, sheet, "八分部\n" + "（建工）", orgEight, weiRuan10, sumTable, 8, red10, orange10);
        PoiUtils.mergeForm(sheet, work, 28, 29, 0, 0);

        //九分部
        String[] orgNine = {"本分部", "一工区", "二工区", "三工区", "四工区"};
        RowAndNum rowNum7 = getColumnDetail(rowNum6.getStartRowIndex(), work, sheet, "广建分部", orgNine, weiRuan10, sumTable, 9, red10, orange10);
        PoiUtils.mergeForm(sheet, work, 30, 34, 0, 0);


        PoiUtils.mergeForm(sheet, work, 6, 8, 0, 0);
        //工区单元格设置
        for (int i = 6; i <= 34; i++) {
            PoiUtils.mergeForm(sheet, work, i, i, 1, 1);

        }


        //最后设置单元格边框
        getmergeCell(row5, weiRuan10, sheet, work, 4);
        getmergeCell(row6, weiRuan10, sheet, work, 6);
        getmergeCellList(rowNum, weiRuan10, sheet, work, 9);
        getmergeCellList(rowNum1, weiRuan10, sheet, work, 15);
        getmergeCellList(rowNum2, weiRuan10, sheet, work, 17);
        getmergeCellList(rowNum3, weiRuan10, sheet, work, 21);
        getmergeCellList(rowNum4, weiRuan10, sheet, work, 23);
        getmergeCellList(rowNum5, weiRuan10, sheet, work, 26);
        getmergeCellList(rowNum6, weiRuan10, sheet, work, 28);
        getmergeCellList(rowNum7, weiRuan10, sheet, work, 30);
        getmergeCell(row6, weiRuan10, sheet, work, 5);


        //管理

        //起始行
        int manageRowIndex = 0;

        ChildTable managerChild = tempExcelVo.getManagerChild();

        HSSFSheet sheet1 = work.getSheetAt(1);

        //第一行
        Row sheetRow1 = sheet1.createRow(manageRowIndex);
        Cell sheetCell1 = sheetRow1.createCell(0);
        sheetCell1.setCellValue(managerChild.getProjectName() + "体温日报");
        PoiUtils.mergeCell(sheet1, 0, 0, 0, 27);
        sheetCell1.setCellStyle(thinStyle);
        sheetRow1.setHeight((short) 460);
        PoiUtils.mergeForm(sheet1, work, 0, 0, 0, 27);

        manageRowIndex++;

        //第二行
        Row sheetRow2 = sheet1.createRow(manageRowIndex);
        Cell sheetCell2 = sheetRow2.createCell(0);
        sheetCell2.setCellValue("总体情况");
        sheetCell2.setCellStyle(songTiNo12);
        PoiUtils.mergeForm(sheet1, work, 1, 1, 0, 0);
        sheet1.setColumnWidth(0, 10 * 256);


        Cell sheetRow2Cell = sheetRow2.createCell(1);
        PoiUtils.mergeCell(sheet1, 1, 1, 1, 24);
        sheetRow2Cell.setCellValue(managerChild.getTitle());
        sheetRow2Cell.setCellStyle(songTiNoleft);
        PoiUtils.mergeForm(sheet1, work, 1, 1, 1, 24);

        Cell sheetRow2Cell1 = sheetRow2.createCell(25);
        sheetRow2Cell1.setCellValue("日期");
        sheetRow2Cell1.setCellStyle(songTi12);
        PoiUtils.mergeForm(sheet1, work, 1, 1, 25, 25);

        Cell sheetRow2Cell2 = sheetRow2.createCell(26);
        sheetRow2Cell2.setCellValue(managerChild.getDateDay());
        sheetRow2Cell2.setCellStyle(songTi12);
        PoiUtils.mergeCell(sheet1, 1, 1, 26, 27);
        PoiUtils.mergeForm(sheet1, work, 1, 1, 26, 27);


        sheetRow2.setHeight((short) 410);


        manageRowIndex++;

        //第三行
        Row sheetRow3 = threeRow(sheet1, manageRowIndex, work, songTi12, managerChild, 0);

        sheetRow3.setHeight((short) 1820);

        manageRowIndex++;

        //第四行
        Row sheetRow4 = sheet1.createRow(manageRowIndex);
        Cell row4Cell = sheetRow4.createCell(14);
        row4Cell.setCellValue("上午8:30时");
        sheet1.setColumnWidth(14, 9 * 256);
        row4Cell.setCellStyle(songTi12);
        PoiUtils.mergeForm(sheet1, work, 3, 3, 14, 14);

        Cell row4Cell1 = sheetRow4.createCell(15);
        row4Cell1.setCellValue("中午13:30时");
        sheet1.setColumnWidth(15, 10 * 256);
        row4Cell1.setCellStyle(songTi12);
        PoiUtils.mergeForm(sheet1, work, 3, 3, 15, 15);

        Cell row4Cell2 = sheetRow4.createCell(16);
        row4Cell2.setCellValue("下午19:30时");
        sheet1.setColumnWidth(16, 10 * 256);
        row4Cell2.setCellStyle(songTi12);
        PoiUtils.mergeForm(sheet1, work, 3, 3, 16, 16);

        PoiUtils.mergeForm(sheet1, work, 2, 3, 0, 0);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 1, 1);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 2, 2);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 3, 3);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 4, 4);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 5, 5);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 6, 6);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 7, 7);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 8, 8);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 9, 9);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 10, 10);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 11, 11);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 12, 12);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 13, 13);
        PoiUtils.mergeForm(sheet1, work, 2, 2, 14, 16);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 16, 16);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 17, 17);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 18, 18);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 19, 19);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 20, 20);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 21, 21);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 22, 22);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 23, 23);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 24, 24);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 25, 25);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 26, 26);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 27, 27);


        sheetRow4.setHeight((short) 600);

        manageRowIndex++;
        //第五行++
        //序号
        int num = 1;
        if (managerChild.getList() != null) {
            for (TableLineMessage tableLineMessage : managerChild.getList()) {
                Row sheetRow5 = sheet1.createRow(manageRowIndex);
                Cell cell = sheetRow5.createCell(0);
                cell.setCellValue(num);
                cell.setCellStyle(weiRuan10);
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 0, 0);

                Cell cell1 = sheetRow5.createCell(1);
                if (managerChild.getProjectId().equals("6245721945602523136")) {
                    cell1.setCellValue("广州市轨道交通十一号线");
                } else if (managerChild.getProjectId().equals("6245721945602523137")) {
                    cell1.setCellValue("广州市轨道交通十三号线二期");
                } else if (managerChild.getProjectId().equals("6245721945602523139")) {
                    cell1.setCellValue("广州市轨道交通七号线二期");
                } else if (managerChild.getProjectId().equals("6422195692059623424")) {
                    cell1.setCellValue("广州市中心城区地下综合管廊");
                }
                cell1.setCellStyle(weiRuan10);
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 1, 1);

                Cell cell2 = sheetRow5.createCell(2);
                cell2.setCellValue(tableLineMessage.getUser().getOrgName());
                cell2.setCellStyle(weiRuan10);
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 2, 2);

                Cell cell3 = sheetRow5.createCell(3);
                cell3.setCellValue(tableLineMessage.getUser().getUserName());
                cell3.setCellStyle(weiRuan10);
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 3, 3);

                if (!tableLineMessage.getTemp().isEmpty()) {
                    Cell cell4 = sheetRow5.createCell(4);
                    if (tableLineMessage.getDetailAlarm() != 0) {
                        cell4.setCellValue("异常");
                        PoiUtils.setBackColor(work, cell4);
                    } else {
                        cell4.setCellValue("正常");
                        cell4.setCellStyle(weiRuan10);
                    }
                }

                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 4, 4);

                Cell cell10 = sheetRow5.createCell(5);
                if (tableLineMessage.getMsgTotalAlarm() != 0) {
                    cell10.setCellValue("有");
                    PoiUtils.setBackColor(work, cell10);
                } else {
                    cell10.setCellValue("无");
                    cell10.setCellStyle(weiRuan10);
                }

                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 5, 5);

                //管理血液检测、核酸检测
                Cell sheetRow5Cell = sheetRow5.createCell(6);
                if (tableLineMessage.getBloodTest() != null) {
                    if(tableLineMessage.getBloodTest().getStatus() == 0){
                        sheetRow5Cell.setCellValue("否");
                        sheetRow5Cell.setCellStyle(weiRuan10);
                    }else {
                        sheetRow5Cell.setCellValue(tableLineMessage.getBloodTest().getName());
                        sheetRow5Cell.setCellStyle(weiRuan10);
                        if (tableLineMessage.getBloodTest().getTestResult() == (short)1) {
                            sheetRow5Cell.setCellStyle(red10);
                            PoiUtils.setBackColor(work,sheetRow5Cell);
                        }

                    }
                }
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 6, 6);

                Cell sheetRow5Cell3 = sheetRow5.createCell(7);
                if (tableLineMessage.getBloodTest()!=null&&tableLineMessage.getBloodTest().getTestDate()!=null){
                    sheetRow5Cell3.setCellValue(changeDate(tableLineMessage.getBloodTest().getTestDate()));
                    sheet1.setColumnWidth(7, 11 * 256);
                }

                sheetRow5Cell3.setCellStyle(weiRuan10);
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 7, 7);

                Cell sheetRow5Cell1 = sheetRow5.createCell(8);
                if (tableLineMessage.getNucleaseTest() != null) {
                    if (tableLineMessage.getNucleaseTest().getStatus() == 0) {
                        sheetRow5Cell1.setCellValue("否");
                        sheetRow5Cell1.setCellStyle(weiRuan10);
                    }else {
                        sheetRow5Cell1.setCellValue(tableLineMessage.getNucleaseTest().getName());
                        sheetRow5Cell1.setCellStyle(weiRuan10);
                        if (tableLineMessage.getNucleaseTest().getTestResult() == (short)1) {
                            sheetRow5Cell1.setCellStyle(red10);
                            PoiUtils.setBackColor(work,sheetRow5Cell1);
                        }
                    }
                }
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 8, 8);


                Cell sheetRow5Cell2 = sheetRow5.createCell(9);
                if (tableLineMessage.getNucleaseTest() != null&&tableLineMessage.getNucleaseTest().getTestDate()!=null) {
                    sheetRow5Cell2.setCellValue(changeDate(tableLineMessage.getNucleaseTest().getTestDate()));
                    sheet1.setColumnWidth(9, 11 * 256);
                }
                sheetRow5Cell2.setCellStyle(weiRuan10);
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 9, 9);

                Cell sheetRow5Cell4 = sheetRow5.createCell(10);
                if (tableLineMessage.getWorkStatus() != null) {
                    sheetRow5Cell4.setCellValue(tableLineMessage.getWorkStatus().getName());
                    sheetRow5Cell4.setCellStyle(weiRuan10);
                }
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 10, 10);

                Cell cell11 = sheetRow5.createCell(11);
                cell11.setCellValue(tableLineMessage.getUser().getAge());
                cell11.setCellStyle(weiRuan10);
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 11, 11);

                Cell cell12 = sheetRow5.createCell(12);
                cell12.setCellValue(tableLineMessage.getUser().getPhone());
                cell12.setCellStyle(weiRuan10);
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 12, 12);

                Cell cell13 = sheetRow5.createCell(13);
                if (tableLineMessage.getUser().getIdCard().length() == 18) {
                    String substring = tableLineMessage.getUser().getIdCard().substring(0, 10);
                    cell13.setCellValue(substring+"********");
                }else {
                    cell13.setCellValue(tableLineMessage.getUser().getIdCard());
                }
                cell13.setCellStyle(weiRuan10);
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 13, 13);

                for (TempDetail tempDetail : tableLineMessage.getTemp()) {
                    if (tempDetail.getTempType().equals((short) 1)) {
                        Cell cell14 = sheetRow5.createCell(14);
                        cell14.setCellValue(tempDetail.getTemperature().doubleValue());
                        cell14.setCellStyle(weiRuan10);
                        if (tempDetail.getTemperature().doubleValue() > tempDetail.getUpperLimit().doubleValue()) {
                            PoiUtils.setBackColor(work, cell14);
                        }
                    } else if (tempDetail.getTempType().equals((short) 2)) {
                        Cell cell14 = sheetRow5.createCell(15);
                        cell14.setCellValue(tempDetail.getTemperature().doubleValue());
                        cell14.setCellStyle(weiRuan10);
                        if (tempDetail.getTemperature().doubleValue() > tempDetail.getUpperLimit().doubleValue()) {
                            PoiUtils.setBackColor(work, cell14);
                        }
                    } else if (tempDetail.getTempType().equals((short) 3)) {
                        Cell cell14 = sheetRow5.createCell(16);
                        cell14.setCellValue(tempDetail.getTemperature().doubleValue());
                        cell14.setCellStyle(weiRuan10);
                        if (tempDetail.getTemperature().doubleValue() > tempDetail.getUpperLimit().doubleValue()) {
                            PoiUtils.setBackColor(work, cell14);
                        }
                    }
                }
                Cell cell14 = sheetRow5.createCell(17);
                if (tableLineMessage.getUser().getStayStatus().equals((short) 1)) {
                    cell14.setCellValue("处于14天隔离期");

                } else if (tableLineMessage.getUser().getStayStatus().equals((short) 2)) {
                    cell14.setCellValue("已完成14天隔离");

                } else if (tableLineMessage.getUser().getStayStatus().equals((short) 3)) {
                    cell14.setCellValue("休假期间一直在穗");
                }
                cell14.setCellStyle(weiRuan10);
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 17, 17);

                Cell cell15 = sheetRow5.createCell(18);
                cell15.setCellValue(tableLineMessage.getUser().getComeDate());
                cell15.setCellStyle(weiRuan10);
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 18, 18);

                Cell cell16 = sheetRow5.createCell(19);
                cell16.setCellValue(tableLineMessage.getUser().getFinishDate());
                cell16.setCellStyle(weiRuan10);
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 19, 19);
                if (tableLineMessage.getMessage() != null) {
                    Cell cell17 = sheetRow5.createCell(20);
                    cell17.setCellValue("广州市" + tableLineMessage.getMessage().getProvince());
                    cell17.setCellStyle(weiRuan10);

                    Cell cell18 = sheetRow5.createCell(21);
                    cell18.setCellValue(tableLineMessage.getMessage().getAddress());
                    cell18.setCellStyle(weiRuan10);

                    Cell cell24 = sheetRow5.createCell(27);
                    cell24.setCellValue(tableLineMessage.getMessage().getNote());
                    cell24.setCellStyle(weiRuan10);
                }
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 20, 20);
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 21, 21);
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 27, 27);

                if (tableLineMessage.getRoute() != null) {
                    Cell cell19 = sheetRow5.createCell(22);
                    if (tableLineMessage.getRoute().getStatus().equals((short)0)) {
                        cell19.setCellValue("否");
                    } else {
                        cell19.setCellValue(tableLineMessage.getRoute().getName());
                    }
                    cell19.setCellStyle(weiRuan10);
                }
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 22, 22);


                if (tableLineMessage.getContactHistory() != null) {
                    Cell cell20 = sheetRow5.createCell(23);
                    if (tableLineMessage.getContactHistory().getStatus() == 0) {
                        cell20.setCellValue("否");
                        cell20.setCellStyle(weiRuan10);
                    }else {
                        if (tableLineMessage.getContactHistory().getName() != null) {
                            cell20.setCellValue(tableLineMessage.getContactHistory().getName());
                            cell20.setCellStyle(weiRuan10);
                            PoiUtils.setBackColor(work, cell20);
                        }
                    }

                }
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 23, 23);


                if (tableLineMessage.getAreaHistory() != null) {
                    Cell cell21 = sheetRow5.createCell(24);
                    if (tableLineMessage.getAreaHistory().getStatus() == 0) {
                        cell21.setCellValue("否");
                    }else {
                        cell21.setCellValue(tableLineMessage.getAreaHistory().getName());
                    }
                    cell21.setCellStyle(weiRuan10);
                }
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 24, 24);


                if (tableLineMessage.getPersonHistory() != null) {
                    Cell cell22 = sheetRow5.createCell(25);
                    if (tableLineMessage.getPersonHistory().getStatus() == 0) {
                        cell22.setCellValue("否");
                    }else {
                        cell22.setCellValue(tableLineMessage.getPersonHistory().getName());
                    }
                    cell22.setCellStyle(weiRuan10);
                }
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 25, 25);


                if (tableLineMessage.getHealth() != null) {
                    Cell cell23 = sheetRow5.createCell(26);
                    if (tableLineMessage.getHealth().getStatus().equals((short)0)) {
                        cell23.setCellValue("一切正常");
                    } else if (tableLineMessage.getPersonHistory().getStatus().equals((short)1)) {
                        cell23.setCellValue("疑似新型冠状病毒肺炎");
                    } else if (tableLineMessage.getPersonHistory().getStatus().equals((short)2)) {
                        cell23.setCellValue("确认新型冠状病毒肺炎");
                    } else if (tableLineMessage.getPersonHistory().getStatus().equals((short)3)) {
                        cell23.setCellValue("治愈新型冠状病毒肺炎");
                    } else if (tableLineMessage.getPersonHistory().getStatus().equals((short)4)) {
                        cell23.setCellValue("其他状态（如咳嗽、流鼻涕、嗓子发炎.");
                    }
                    cell23.setCellStyle(weiRuan10);
                }
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 26, 26);


                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 14, 14);
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 15, 15);
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 16, 16);

                manageRowIndex++;
                num++;
            }
        }

        //劳务
        changePerson(tempExcelVo, work, thinStyle, songTiNo12, songTi12, weiRuan10, red10,songTiNoleft);


        //异常表
        int lobourIndex = 0;
        HSSFSheet sheetAt = work.getSheetAt(3);
        UnNormalChildTable unNormalChild = tempExcelVo.getUnNormalChild();
        //第一行
        Row hssfRow = sheetAt.createRow(lobourIndex);
        Cell sheet1RowCell = hssfRow.createCell(0);
        sheet1RowCell.setCellValue(unNormalChild.getProjectName() + "体温日报");
        PoiUtils.mergeCell(sheetAt, 0, 0, 0, 28);
        sheet1RowCell.setCellStyle(thinStyle);
        hssfRow.setHeight((short) 460);
        PoiUtils.mergeForm(sheetAt, work, 0, 0, 0, 28);

        lobourIndex++;


        //第二行
        Row hssfRow1 = sheetAt.createRow(lobourIndex);
        Cell hssfRow1Cell = hssfRow1.createCell(0);
        hssfRow1Cell.setCellValue("总体情况");
        sheetAt.setColumnWidth(0, 10 * 256);
        hssfRow1Cell.setCellStyle(songTiNo12);
        PoiUtils.mergeForm(sheetAt, work, 1, 1, 0, 0);

        Cell cell25 = hssfRow1.createCell(1);
        PoiUtils.mergeCell(sheetAt, 1, 1, 1, 1);
        cell25.setCellValue("日期");
        cell25.setCellStyle(songTiNo12);
        sheetAt.setColumnWidth(1, 7 * 256);
        PoiUtils.mergeForm(sheetAt, work, 1, 1, 1, 1);

        Cell cell26 = hssfRow1.createCell(2);
        PoiUtils.mergeCell(sheetAt, 1, 1, 2, 2);
        cell26.setCellValue(unNormalChild.getDateDay());
        sheetAt.setColumnWidth(2, 12 * 256);
        cell26.setCellStyle(songTiNo12);
        PoiUtils.mergeForm(sheetAt, work, 1, 1, 2, 2);

        Cell hssfRow1Cell1 = hssfRow1.createCell(3);
        PoiUtils.mergeCell(sheetAt, 1, 1, 3, 25);
        hssfRow1Cell1.setCellValue(unNormalChild.getTitle());
        hssfRow1Cell1.setCellStyle(songTiNoleft);
        PoiUtils.mergeForm(sheetAt, work, 1, 1, 3, 25);

        Cell hssfRow1Cell2 = hssfRow1.createCell(26);
        hssfRow1Cell2.setCellValue("日期");
        hssfRow1Cell2.setCellStyle(songTiNo12);
        PoiUtils.mergeForm(sheetAt, work, 1, 1, 26, 26);

        Cell hssfRow1Cell3 = hssfRow1.createCell(27);
        hssfRow1Cell3.setCellValue(unNormalChild.getDateDay());
        hssfRow1Cell3.setCellStyle(songTiNo12);
        PoiUtils.mergeCell(sheetAt, 1, 1, 27, 28);
        PoiUtils.mergeForm(sheetAt, work, 1, 1, 27, 28);


        hssfRow1.setHeight((short) 410);


        lobourIndex++;

        //第三行
        Row cells = threeRow(sheetAt, lobourIndex, work, songTi12, tempExcelVo.getManagerChild(), 1);

        cells.setHeight((short) 1820);

        lobourIndex++;

        //第四行
        Row sheetAtRow = sheetAt.createRow(lobourIndex);
        Cell rowCell = sheetAtRow.createCell(15);
        rowCell.setCellValue("上午8:30时");
        sheetAt.setColumnWidth(15, 9 * 256);
        rowCell.setCellStyle(songTi12);
        PoiUtils.mergeForm(sheetAt, work, 3, 3, 15, 15);

        Cell rowCell1 = sheetAtRow.createCell(16);
        rowCell1.setCellValue("中午13:30时");
        sheetAt.setColumnWidth(16, 10 * 256);
        rowCell1.setCellStyle(songTi12);
        PoiUtils.mergeForm(sheetAt, work, 3, 3, 16, 16);

        Cell rowCell2 = sheetAtRow.createCell(17);
        rowCell2.setCellValue("下午19:30时");
        sheetAt.setColumnWidth(17, 10 * 256);
        rowCell2.setCellStyle(songTi12);
        PoiUtils.mergeForm(sheetAt, work, 3, 3, 17, 17);

        PoiUtils.mergeForm(sheetAt, work, 2, 3, 0, 0);
        PoiUtils.mergeForm(sheetAt, work, 2, 3, 1, 1);
        PoiUtils.mergeForm(sheetAt, work, 2, 3, 2, 2);
        PoiUtils.mergeForm(sheetAt, work, 2, 3, 3, 3);
        PoiUtils.mergeForm(sheetAt, work, 2, 3, 4, 4);
        PoiUtils.mergeForm(sheetAt, work, 2, 3, 5, 5);
        PoiUtils.mergeForm(sheetAt, work, 2, 3, 6, 6);
        PoiUtils.mergeForm(sheetAt, work, 2, 3, 7, 7);
        PoiUtils.mergeForm(sheetAt, work, 2, 3, 8, 8);
        PoiUtils.mergeForm(sheetAt, work, 2, 3, 9, 9);
        PoiUtils.mergeForm(sheetAt, work, 2, 3, 10, 10);
        PoiUtils.mergeForm(sheetAt, work, 2, 3, 11, 11);
        PoiUtils.mergeForm(sheetAt, work, 2, 3, 12, 12);
        PoiUtils.mergeForm(sheetAt, work, 2, 3, 13, 13);
        PoiUtils.mergeForm(sheetAt, work, 2, 3, 14, 14);
        PoiUtils.mergeForm(sheetAt, work, 2, 2, 15, 17);
        PoiUtils.mergeForm(sheetAt, work, 2, 3, 18, 18);
        PoiUtils.mergeForm(sheetAt, work, 2, 3, 19, 19);
        PoiUtils.mergeForm(sheetAt, work, 2, 3, 20, 20);
        PoiUtils.mergeForm(sheetAt, work, 2, 3, 21, 21);
        PoiUtils.mergeForm(sheetAt, work, 2, 3, 22, 22);
        PoiUtils.mergeForm(sheetAt, work, 2, 3, 22, 22);
        PoiUtils.mergeForm(sheetAt, work, 2, 3, 23, 23);
        PoiUtils.mergeForm(sheetAt, work, 2, 3, 24, 24);
        PoiUtils.mergeForm(sheetAt, work, 2, 3, 25, 25);
        PoiUtils.mergeForm(sheetAt, work, 2, 3, 26, 26);
        PoiUtils.mergeForm(sheetAt, work, 2, 3, 27, 27);
        PoiUtils.mergeForm(sheetAt, work, 2, 3, 28, 28);


        sheetAtRow.setHeight((short) 600);

        lobourIndex++;

        if (1 == 1) {
            //序号
            int no = 1;
            //经理部
            Row sheetAtRow1sheetAtRow = sheetAt.createRow(lobourIndex);
            Cell rowCell3 = sheetAtRow1sheetAtRow.createCell(0);
            rowCell3.setCellValue(no);
            rowCell3.setCellStyle(weiRuan10);

            Cell row1sheetAtRowCell = sheetAtRow1sheetAtRow.createCell(1);
            row1sheetAtRowCell.setCellValue("经理部");
            row1sheetAtRowCell.setCellStyle(weiRuan10);
            PoiUtils.mergeCell(sheetAt, lobourIndex, lobourIndex, 1, 2);


            List<TempOrgTree> nextChild = tempExcelVo.getUnNormalChild().getList().get(0).getNextChild();

            for (TempOrgTree tempOrgTree : nextChild) {
                if (sheetAt.getRow(lobourIndex) != null) {
                    Cell row1sheetAtRowCell1 = sheetAtRow1sheetAtRow.createCell(3);
                    if (! tempOrgTree.getLineMessages().isEmpty()&&tempOrgTree.getLineMessages().get(0) != null ) {
                        if (tempOrgTree.getLineMessages().get(0).getUser().getPersonType().equals("gl")) {
                            row1sheetAtRowCell1.setCellValue("管理人员");
                        } else {
                            row1sheetAtRowCell1.setCellValue("劳务人员");
                        }
                    }
                    row1sheetAtRowCell1.setCellStyle(weiRuan10);

                    Cell rowCell4 = sheetAtRow1sheetAtRow.createCell(4);
                    if (!tempOrgTree.getLineMessages().isEmpty()) {
                        rowCell4.setCellValue(tempOrgTree.getLineMessages().get(0).getUser().getUserName());
                    }
                    rowCell4.setCellStyle(weiRuan10);

                    Cell row1sheetAtRowCell2 = sheetAtRow1sheetAtRow.createCell(5);
                    if (!tempOrgTree.getLineMessages().isEmpty()&&!tempOrgTree.getLineMessages().get(0).getTemp().isEmpty()) {
                        if (tempOrgTree.getLineMessages().get(0).getDetailAlarm() != 0) {
                            row1sheetAtRowCell2.setCellValue("异常");
                            PoiUtils.setBackColor(work, row1sheetAtRowCell2);
                        } else {
                            row1sheetAtRowCell2.setCellValue("正常");
                            row1sheetAtRowCell2.setCellStyle(weiRuan10);
                        }
                    }

                    Cell cell10 = sheetAtRow1sheetAtRow.createCell(6);
                    if (tempExcelVo.getUnNormalChild() !=null&&!tempExcelVo.getUnNormalChild().getList().isEmpty()&&!tempExcelVo.getUnNormalChild().getList().get(0).getNextChild().isEmpty()&&!tempExcelVo.getUnNormalChild().getList().get(0).getNextChild().get(0).getLineMessages().isEmpty()){
                        if (tempExcelVo.getUnNormalChild().getList().get(0).getNextChild().get(0).getLineMessages().get(0).getMsgTotalAlarm() == 0) {
                            cell10.setCellValue("无");
                            cell10.setCellStyle(weiRuan10);
                        } else {
                            cell10.setCellValue("有");
                            PoiUtils.setBackColor(work, cell10);
                        }
                    }

                    cell10.setCellStyle(weiRuan10);
                    PoiUtils.mergeCell(sheetAt, lobourIndex, lobourIndex, 6, 6);

                    //异常血液检测、核酸检测
                    Cell sheetAtRowCell = sheetAtRow1sheetAtRow.createCell(7);
                    if (!tempOrgTree.getLineMessages().isEmpty()) {
                        if (tempOrgTree.getLineMessages().get(0).getBloodTest().getStatus() == 0) {
                            sheetAtRowCell.setCellValue("否");
                            sheetAtRowCell.setCellStyle(weiRuan10);
                        }else {
                            sheetAtRowCell.setCellValue(tempOrgTree.getLineMessages().get(0).getBloodTest().getName());
                            sheetAtRowCell.setCellStyle(weiRuan10);
                            if (tempOrgTree.getLineMessages().get(0).getBloodTest().getTestResult() == (short)1) {
                                sheetAtRowCell.setCellStyle(red10);
                                PoiUtils.setBackColor(work,sheetAtRowCell);
                            }
                        }
                    }
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 7, 7);

                    Cell sheetAtRow1sheetAtRowCell = sheetAtRow1sheetAtRow.createCell(8);
                    if (!tempOrgTree.getLineMessages().isEmpty()&&tempOrgTree.getLineMessages().get(0).getBloodTest().getTestDate()!=null){
                        sheetAtRow1sheetAtRowCell.setCellValue(changeDate(tempOrgTree.getLineMessages().get(0).getBloodTest().getTestDate()));
                    }
                    sheetAtRow1sheetAtRowCell.setCellStyle(weiRuan10);
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 8, 8);

                    Cell atRowCell = sheetAtRow1sheetAtRow.createCell(9);
                    if (!tempOrgTree.getLineMessages().isEmpty() ) {
                        if (tempOrgTree.getLineMessages().get(0).getNucleaseTest().getStatus() == 0) {
                            atRowCell.setCellValue("否");
                            atRowCell.setCellStyle(weiRuan10);
                        }else {
                            atRowCell.setCellValue(tempOrgTree.getLineMessages().get(0).getNucleaseTest().getName());
                            atRowCell.setCellStyle(weiRuan10);
                            if (tempOrgTree.getLineMessages().get(0).getNucleaseTest().getTestResult() == (short)1) {
                                atRowCell.setCellStyle(red10);
                                PoiUtils.setBackColor(work,atRowCell);
                            }
                        }
                    }
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 9, 9);

                    Cell atRowCell1 = sheetAtRow1sheetAtRow.createCell(10);
                    if (!tempOrgTree.getLineMessages().isEmpty()&&tempOrgTree.getLineMessages().get(0).getNucleaseTest().getTestDate()!=null){
                        atRowCell1.setCellValue(changeDate(tempOrgTree.getLineMessages().get(0).getNucleaseTest().getTestDate()));
                    }
                    atRowCell1.setCellStyle(weiRuan10);
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 10, 10);

                    Cell atRow1sheetAtRowCell = sheetAtRow1sheetAtRow.createCell(11);
                    if (!tempOrgTree.getLineMessages().isEmpty()){
                        atRow1sheetAtRowCell.setCellValue(tempOrgTree.getLineMessages().get(0).getWorkStatus().getName());
                    }
                    atRow1sheetAtRowCell.setCellStyle(weiRuan10);
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 11, 11);

                    Cell row1sheetAtRowCell3 = sheetAtRow1sheetAtRow.createCell(12);
                    if (!tempOrgTree.getLineMessages().isEmpty()){
                        row1sheetAtRowCell3.setCellValue(tempOrgTree.getLineMessages().get(0).getUser().getAge());
                    }
                    row1sheetAtRowCell3.setCellStyle(weiRuan10);
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 12, 12);

                    Cell row1sheetAtRowCell4 = sheetAtRow1sheetAtRow.createCell(13);
                    if (!tempOrgTree.getLineMessages().isEmpty()){
                        row1sheetAtRowCell4.setCellValue(tempOrgTree.getLineMessages().get(0).getUser().getPhone());
                    }
                    row1sheetAtRowCell4.setCellStyle(weiRuan10);
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 13, 13);

                    Cell row1sheetAtRowCell5 = sheetAtRow1sheetAtRow.createCell(14);
                    if (!tempOrgTree.getLineMessages().isEmpty()) {
                        if (tempOrgTree.getLineMessages().get(0).getUser().getIdCard().length()== 18) {
                            String substring = tempOrgTree.getLineMessages().get(0).getUser().getIdCard().substring(0, 10);
                            row1sheetAtRowCell5.setCellValue(substring+"********");
                        }else {
                            row1sheetAtRowCell5.setCellValue(tempOrgTree.getLineMessages().get(0).getUser().getIdCard());
                        }
                    }

                    row1sheetAtRowCell5.setCellStyle(weiRuan10);
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 14, 14);

                    if (!tempOrgTree.getLineMessages().isEmpty()) {
                        for (TempDetail tempDetail : tempOrgTree.getLineMessages().get(0).getTemp()) {
                            if (tempDetail.getTempType().equals((short) 1)) {
                                Cell cell14 = sheetAtRow1sheetAtRow.createCell(15);
                                cell14.setCellValue(tempDetail.getTemperature().doubleValue());
                                cell14.setCellStyle(weiRuan10);
                                if (tempDetail.getTemperature().doubleValue() > tempDetail.getUpperLimit().doubleValue()) {
                                    cell14.setCellStyle(red10);
                                }
                            } else if (tempDetail.getTempType().equals((short) 2)) {
                                Cell cell14 = sheetAtRow1sheetAtRow.createCell(16);
                                cell14.setCellValue(tempDetail.getTemperature().doubleValue());
                                cell14.setCellStyle(weiRuan10);
                                if (tempDetail.getTemperature().doubleValue() > tempDetail.getUpperLimit().doubleValue()) {
                                    cell14.setCellStyle(red10);
                                }
                            } else if (tempDetail.getTempType().equals((short) 3)) {
                                Cell cell14 = sheetAtRow1sheetAtRow.createCell(17);
                                cell14.setCellValue(tempDetail.getTemperature().doubleValue());
                                cell14.setCellStyle(weiRuan10);
                                if (tempDetail.getTemperature().doubleValue() > tempDetail.getUpperLimit().doubleValue()) {
                                    cell14.setCellStyle(red10);
                                }
                            }
                        }
                    }

                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 15, 15);
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 16, 16);
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 17, 17);

                    Cell cell14 = sheetAtRow1sheetAtRow.createCell(18);
                    if (!tempOrgTree.getLineMessages().isEmpty()) {
                        if (tempOrgTree.getLineMessages().get(0).getUser().getStayStatus().equals((short) 1)) {
                            cell14.setCellValue("处于14天隔离期");
                        } else if (tempOrgTree.getLineMessages().get(0).getUser().getStayStatus().equals((short) 2)) {
                            cell14.setCellValue("已完成14天隔离");

                        } else if (tempOrgTree.getLineMessages().get(0).getUser().getStayStatus().equals((short) 3)) {
                            cell14.setCellValue("休假期间一直在穗");
                        }
                    }

                    cell14.setCellStyle(weiRuan10);
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 18, 18);

                    Cell cell15 = sheetAtRow1sheetAtRow.createCell(19);
                    if (!tempOrgTree.getLineMessages().isEmpty()) {
                        cell15.setCellValue(tempOrgTree.getLineMessages().get(0).getUser().getComeDate());
                        cell15.setCellStyle(weiRuan10);
                        PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 19, 19);

                        Cell cell16 = sheetAtRow1sheetAtRow.createCell(20);
                        cell16.setCellValue(tempOrgTree.getLineMessages().get(0).getUser().getFinishDate());
                        cell16.setCellStyle(weiRuan10);
                    }
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 20, 20);

                    if (!tempOrgTree.getLineMessages().isEmpty()&&tempOrgTree.getLineMessages().get(0).getMessage() != null) {
                        Cell cell17 = sheetAtRow1sheetAtRow.createCell(21);
                        cell17.setCellValue("广州市" + tempOrgTree.getLineMessages().get(0).getMessage().getProvince());
                        cell17.setCellStyle(weiRuan10);

                        Cell cell18 = sheetAtRow1sheetAtRow.createCell(22);
                        cell18.setCellValue(tempOrgTree.getLineMessages().get(0).getMessage().getAddress());
                        cell18.setCellStyle(weiRuan10);

                        Cell cell24 = sheetAtRow1sheetAtRow.createCell(28);
                        cell24.setCellValue(tempOrgTree.getLineMessages().get(0).getMessage().getNote());
                        cell24.setCellStyle(weiRuan10);
                    }
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 21, 21);
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 22, 22);
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 28, 28);

                    if (!tempOrgTree.getLineMessages().isEmpty()&&tempOrgTree.getLineMessages().get(0).getRoute() != null) {
                        Cell cell19 = sheetAtRow1sheetAtRow.createCell(23);
                        if (tempOrgTree.getLineMessages().get(0).getRoute().getStatus().equals((short)0)) {
                            cell19.setCellValue("否");
                        } else {
                            cell19.setCellValue(tempOrgTree.getLineMessages().get(0).getRoute().getName());
                        }
                        cell19.setCellStyle(weiRuan10);
                    }
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 23, 23);


                    if (!tempOrgTree.getLineMessages().isEmpty()&&tempOrgTree.getLineMessages().get(0).getContactHistory() != null) {
                        Cell cell20 = sheetAtRow1sheetAtRow.createCell(24);
                        if (tempOrgTree.getLineMessages().get(0).getContactHistory().getStatus() == 0) {
                            cell20.setCellValue("否");
                        }else {
                            if (tempOrgTree.getLineMessages().get(0).getContactHistory().getName() != null) {
                                cell20.setCellValue(tempOrgTree.getLineMessages().get(0).getContactHistory().getName());
                                PoiUtils.setBackColor(work, cell20);
                            }
                        }
                        cell20.setCellStyle(weiRuan10);

                    }
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 24, 24);


                    if (!tempOrgTree.getLineMessages().isEmpty()&&tempOrgTree.getLineMessages().get(0).getAreaHistory() != null) {
                        Cell cell21 = sheetAtRow1sheetAtRow.createCell(25);
                        if (tempOrgTree.getLineMessages().get(0).getAreaHistory().getStatus() == 0) {
                            cell21.setCellValue("否");
                        }else {
                            cell21.setCellValue(tempOrgTree.getLineMessages().get(0).getAreaHistory().getName());
                        }
                        cell21.setCellStyle(weiRuan10);
                    }
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 25, 25);


                    if (!tempOrgTree.getLineMessages().isEmpty()&&tempOrgTree.getLineMessages().get(0).getPersonHistory() != null) {
                        Cell cell22 = sheetAtRow1sheetAtRow.createCell(26);
                        if (tempOrgTree.getLineMessages().get(0).getPersonHistory().getStatus() == 0) {
                            cell22.setCellValue("否");
                        }else {
                            cell22.setCellValue(tempOrgTree.getLineMessages().get(0).getPersonHistory().getName());
                        }
                        cell22.setCellStyle(weiRuan10);
                    }
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 26, 26);


                    if (!tempOrgTree.getLineMessages().isEmpty()&&tempOrgTree.getLineMessages().get(0).getHealth() != null) {
                        Cell cell23 = sheetAtRow1sheetAtRow.createCell(27);
                        if (tempOrgTree.getLineMessages().get(0).getHealth().getStatus().equals((short)0)) {
                            cell23.setCellValue("一切正常");
                        } else if (tempOrgTree.getLineMessages().get(0).getHealth().getStatus().equals((short)1)) {
                            cell23.setCellValue("疑似新型冠状病毒肺炎");
                        } else if (tempOrgTree.getLineMessages().get(0).getHealth().getStatus().equals((short)2)) {
                            cell23.setCellValue("确认新型冠状病毒肺炎");
                        } else if (tempOrgTree.getLineMessages().get(0).getHealth().getStatus().equals((short)3)) {
                            cell23.setCellValue("治愈新型冠状病毒肺炎");
                        } else if (tempOrgTree.getLineMessages().get(0).getHealth().getStatus().equals((short)4)) {
                            cell23.setCellValue("其他状态（如咳嗽、流鼻涕、嗓子发炎.");
                        }
                        cell23.setCellStyle(weiRuan10);
                    }
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 27, 27);

                    lobourIndex++;

                    PoiUtils.mergeForm(sheetAt, work, 4, 4, 0, 0);
                    PoiUtils.mergeForm(sheetAt, work, 4, 4, 1, 2);
                    PoiUtils.mergeForm(sheetAt, work, 4, 4, 3, 3);
                    PoiUtils.mergeForm(sheetAt, work, 4, 4, 4, 4);
                    PoiUtils.mergeForm(sheetAt, work, 4, 4, 5, 5);
                    PoiUtils.mergeForm(sheetAt, work, 4, 4, 6, 6);
                }else {
                    Row hssfRow2 = sheetAt.createRow(lobourIndex);
                    Cell row1sheetAtRowCell1 = hssfRow2.createCell(3);
                    if (! tempOrgTree.getLineMessages().isEmpty()&&tempOrgTree.getLineMessages().get(0) != null ) {
                        if (tempOrgTree.getLineMessages().get(0).getUser().getPersonType().equals("gl")) {
                            row1sheetAtRowCell1.setCellValue("管理人员");
                        } else {
                            row1sheetAtRowCell1.setCellValue("劳务人员");
                        }
                    }
                    row1sheetAtRowCell1.setCellStyle(weiRuan10);

                    Cell rowCell4 = hssfRow2.createCell(4);
                    if (!tempOrgTree.getLineMessages().isEmpty()) {
                        rowCell4.setCellValue(tempOrgTree.getLineMessages().get(0).getUser().getUserName());
                    }
                    rowCell4.setCellStyle(weiRuan10);

                    Cell row1sheetAtRowCell2 = hssfRow2.createCell(5);
                    if (!tempOrgTree.getLineMessages().isEmpty()&&!tempOrgTree.getLineMessages().get(0).getTemp().isEmpty()) {
                        if (tempOrgTree.getLineMessages().get(0).getDetailAlarm() != 0) {
                            row1sheetAtRowCell2.setCellValue("异常");
                            PoiUtils.setBackColor(work, row1sheetAtRowCell2);
                        } else {
                            row1sheetAtRowCell2.setCellValue("正常");
                            row1sheetAtRowCell2.setCellStyle(weiRuan10);
                        }
                    }

                    Cell cell10 = hssfRow2.createCell(6);
                    if (tempExcelVo.getUnNormalChild() !=null&&!tempExcelVo.getUnNormalChild().getList().isEmpty()&&!tempExcelVo.getUnNormalChild().getList().get(0).getNextChild().isEmpty()&&!tempExcelVo.getUnNormalChild().getList().get(0).getNextChild().get(0).getLineMessages().isEmpty()){
                        if (tempExcelVo.getUnNormalChild().getList().get(0).getNextChild().get(0).getLineMessages().get(0).getMsgTotalAlarm() == 0) {
                            cell10.setCellValue("无");
                            cell10.setCellStyle(weiRuan10);
                        } else {
                            cell10.setCellValue("有");
                            PoiUtils.setBackColor(work, cell10);
                        }
                    }
                    cell10.setCellStyle(weiRuan10);
                    PoiUtils.mergeCell(sheetAt, lobourIndex, lobourIndex, 6, 6);

                    //异常血液检测、核酸检测
                    Cell sheetAtRowCell = hssfRow2.createCell(7);
                    if (tempOrgTree.getLineMessages().get(0).getBloodTest() != null) {
                        if (tempOrgTree.getLineMessages().get(0).getBloodTest().getStatus() == 0) {
                            sheetAtRowCell.setCellValue("否");
                            sheetAtRowCell.setCellStyle(weiRuan10);
                        }else {
                            sheetAtRowCell.setCellValue(tempOrgTree.getLineMessages().get(0).getBloodTest().getName());
                            sheetAtRowCell.setCellStyle(weiRuan10);
                            if (tempOrgTree.getLineMessages().get(0).getBloodTest().getTestResult() == (short)1) {
                                sheetAtRowCell.setCellStyle(red10);
                                PoiUtils.setBackColor(work,sheetAtRowCell);
                            }
                        }
                    }
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 7, 7);

                    Cell sheetAtRow1sheetAtRowCell = hssfRow2.createCell(8);
                    if (tempOrgTree.getLineMessages().get(0).getBloodTest()!=null&&tempOrgTree.getLineMessages().get(0).getBloodTest().getTestDate()!=null){
                        sheetAtRow1sheetAtRowCell.setCellValue(changeDate(tempOrgTree.getLineMessages().get(0).getBloodTest().getTestDate()));
                    }
                    sheetAtRow1sheetAtRowCell.setCellStyle(weiRuan10);
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 8, 8);

                    Cell atRowCell = hssfRow2.createCell(9);
                    if (tempOrgTree.getLineMessages().get(0).getNucleaseTest() != null) {
                        if (tempOrgTree.getLineMessages().get(0).getNucleaseTest().getStatus() == 0) {
                            atRowCell.setCellValue("否");
                            atRowCell.setCellStyle(weiRuan10);
                        }else {
                            atRowCell.setCellValue(tempOrgTree.getLineMessages().get(0).getNucleaseTest().getName());
                            atRowCell.setCellStyle(weiRuan10);
                            if (tempOrgTree.getLineMessages().get(0).getNucleaseTest().getTestResult() == (short)1) {
                                atRowCell.setCellStyle(red10);
                                PoiUtils.setBackColor(work,atRowCell);
                            }
                        }
                    }
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 9, 9);

                    Cell atRowCell1 = hssfRow2.createCell(10);
                    if (tempOrgTree.getLineMessages().get(0).getNucleaseTest() != null&&tempOrgTree.getLineMessages().get(0).getNucleaseTest().getTestDate()!=null){
                        atRowCell1.setCellValue(changeDate(tempOrgTree.getLineMessages().get(0).getNucleaseTest().getTestDate()));
                    }
                    atRowCell1.setCellStyle(weiRuan10);
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 10, 10);

                    Cell atRow1sheetAtRowCell = hssfRow2.createCell(10);
                    if (tempOrgTree.getLineMessages().get(0).getWorkStatus() != null){
                        atRow1sheetAtRowCell.setCellValue(tempOrgTree.getLineMessages().get(0).getWorkStatus().getName());
                    }
                    atRow1sheetAtRowCell.setCellStyle(weiRuan10);
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 10, 10);

                    Cell row1sheetAtRowCell3 = hssfRow2.createCell(12);
                    if (!tempOrgTree.getLineMessages().isEmpty()){
                        row1sheetAtRowCell3.setCellValue(tempOrgTree.getLineMessages().get(0).getUser().getAge());
                    }
                    row1sheetAtRowCell3.setCellStyle(weiRuan10);
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 12, 12);

                    Cell row1sheetAtRowCell4 = hssfRow2.createCell(13);
                    if (!tempOrgTree.getLineMessages().isEmpty()){
                        row1sheetAtRowCell4.setCellValue(tempOrgTree.getLineMessages().get(0).getUser().getPhone());
                    }
                    row1sheetAtRowCell4.setCellStyle(weiRuan10);
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 13, 13);

                    Cell row1sheetAtRowCell5 = hssfRow2.createCell(14);
                    if (!tempOrgTree.getLineMessages().isEmpty()) {
                        if (tempOrgTree.getLineMessages().get(0).getUser().getIdCard().length()== 18) {
                            String substring = tempOrgTree.getLineMessages().get(0).getUser().getIdCard().substring(0, 10);
                            row1sheetAtRowCell5.setCellValue(substring+"********");
                        }else {
                            row1sheetAtRowCell5.setCellValue(tempOrgTree.getLineMessages().get(0).getUser().getIdCard());
                        }
                    }

                    row1sheetAtRowCell5.setCellStyle(weiRuan10);
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 14, 14);

                    if (!tempOrgTree.getLineMessages().isEmpty()) {
                        for (TempDetail tempDetail : tempOrgTree.getLineMessages().get(0).getTemp()) {
                            if (tempDetail.getTempType().equals((short) 1)) {
                                Cell cell14 = hssfRow2.createCell(15);
                                cell14.setCellValue(tempDetail.getTemperature().doubleValue());
                                cell14.setCellStyle(weiRuan10);
                                if (tempDetail.getTemperature().doubleValue() > tempDetail.getUpperLimit().doubleValue()) {
                                    cell14.setCellStyle(red10);
                                }
                            } else if (tempDetail.getTempType().equals((short) 2)) {
                                Cell cell14 = hssfRow2.createCell(16);
                                cell14.setCellValue(tempDetail.getTemperature().doubleValue());
                                cell14.setCellStyle(weiRuan10);
                                if (tempDetail.getTemperature().doubleValue() > tempDetail.getUpperLimit().doubleValue()) {
                                    cell14.setCellStyle(red10);
                                }
                            } else if (tempDetail.getTempType().equals((short) 3)) {
                                Cell cell14 = hssfRow2.createCell(17);
                                cell14.setCellValue(tempDetail.getTemperature().doubleValue());
                                cell14.setCellStyle(weiRuan10);
                                if (tempDetail.getTemperature().doubleValue() > tempDetail.getUpperLimit().doubleValue()) {
                                    cell14.setCellStyle(red10);
                                }
                            }
                        }
                    }

                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 15, 15);
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 16, 16);
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 17, 17);

                    Cell cell14 = hssfRow2.createCell(18);
                    if (!tempOrgTree.getLineMessages().isEmpty()) {
                        if (tempOrgTree.getLineMessages().get(0).getUser().getStayStatus().equals((short) 1)) {
                            cell14.setCellValue("处于14天隔离期");
                        } else if (tempOrgTree.getLineMessages().get(0).getUser().getStayStatus().equals((short) 2)) {
                            cell14.setCellValue("已完成14天隔离");

                        } else if (tempOrgTree.getLineMessages().get(0).getUser().getStayStatus().equals((short) 3)) {
                            cell14.setCellValue("休假期间一直在穗");
                        }
                    }

                    cell14.setCellStyle(weiRuan10);
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 18, 18);

                    Cell cell15 = hssfRow2.createCell(19);
                    if (!tempOrgTree.getLineMessages().isEmpty()) {
                        cell15.setCellValue(tempOrgTree.getLineMessages().get(0).getUser().getComeDate());
                        cell15.setCellStyle(weiRuan10);
                        PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 19, 19);

                        Cell cell16 = hssfRow2.createCell(20);
                        cell16.setCellValue(tempOrgTree.getLineMessages().get(0).getUser().getFinishDate());
                        cell16.setCellStyle(weiRuan10);
                    }
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 20, 20);

                    if (!tempOrgTree.getLineMessages().isEmpty()&&tempOrgTree.getLineMessages().get(0).getMessage() != null) {
                        Cell cell17 = hssfRow2.createCell(21);
                        cell17.setCellValue("广州市" + tempOrgTree.getLineMessages().get(0).getMessage().getProvince());
                        cell17.setCellStyle(weiRuan10);

                        Cell cell18 = hssfRow2.createCell(22);
                        cell18.setCellValue(tempOrgTree.getLineMessages().get(0).getMessage().getAddress());
                        cell18.setCellStyle(weiRuan10);

                        Cell cell24 = hssfRow2.createCell(28);
                        cell24.setCellValue(tempOrgTree.getLineMessages().get(0).getMessage().getNote());
                        cell24.setCellStyle(weiRuan10);
                    }
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 21, 21);
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 22, 22);
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 28, 28);

                    if (!tempOrgTree.getLineMessages().isEmpty()&&tempOrgTree.getLineMessages().get(0).getRoute() != null) {
                        Cell cell19 = hssfRow2.createCell(23);
                        if (tempOrgTree.getLineMessages().get(0).getRoute().getStatus().equals((short)0)) {
                            cell19.setCellValue("否");
                        } else {
                            cell19.setCellValue(tempOrgTree.getLineMessages().get(0).getRoute().getName());
                        }
                        cell19.setCellStyle(weiRuan10);
                    }
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 23, 23);


                    if (!tempOrgTree.getLineMessages().isEmpty()&&tempOrgTree.getLineMessages().get(0).getContactHistory() != null) {
                        Cell cell20 = hssfRow2.createCell(24);
                        if (tempOrgTree.getLineMessages().get(0).getContactHistory().getStatus() == 0) {
                            cell20.setCellValue("否");
                        }else {
                            if (tempOrgTree.getLineMessages().get(0).getContactHistory().getName() != null) {
                                cell20.setCellValue(tempOrgTree.getLineMessages().get(0).getContactHistory().getName());
                                PoiUtils.setBackColor(work, cell20);
                            }
                        }
                        cell20.setCellStyle(weiRuan10);

                    }
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 24, 24);


                    if (!tempOrgTree.getLineMessages().isEmpty()&&tempOrgTree.getLineMessages().get(0).getAreaHistory() != null) {
                        Cell cell21 = hssfRow2.createCell(25);
                        if (tempOrgTree.getLineMessages().get(0).getAreaHistory().getStatus() == 0) {
                            cell21.setCellValue("否");
                        }else {
                            cell21.setCellValue(tempOrgTree.getLineMessages().get(0).getAreaHistory().getName());
                        }
                        cell21.setCellStyle(weiRuan10);
                    }
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 25, 25);


                    if (!tempOrgTree.getLineMessages().isEmpty()&&tempOrgTree.getLineMessages().get(0).getPersonHistory() != null) {
                        Cell cell22 = hssfRow2.createCell(26);
                        if (tempOrgTree.getLineMessages().get(0).getPersonHistory().getStatus() == 0) {
                            cell22.setCellValue("否");
                        }else {
                            cell22.setCellValue(tempOrgTree.getLineMessages().get(0).getPersonHistory().getName());
                        }
                        cell22.setCellStyle(weiRuan10);
                    }
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 26, 26);


                    if (!tempOrgTree.getLineMessages().isEmpty()&&tempOrgTree.getLineMessages().get(0).getHealth() != null) {
                        Cell cell23 = hssfRow2.createCell(27);
                        if (tempOrgTree.getLineMessages().get(0).getHealth().getStatus().equals((short)0)) {
                            cell23.setCellValue("一切正常");
                        } else if (tempOrgTree.getLineMessages().get(0).getHealth().getStatus().equals((short)1)) {
                            cell23.setCellValue("疑似新型冠状病毒肺炎");
                        } else if (tempOrgTree.getLineMessages().get(0).getHealth().getStatus().equals((short)2)) {
                            cell23.setCellValue("确认新型冠状病毒肺炎");
                        } else if (tempOrgTree.getLineMessages().get(0).getHealth().getStatus().equals((short)3)) {
                            cell23.setCellValue("治愈新型冠状病毒肺炎");
                        } else if (tempOrgTree.getLineMessages().get(0).getHealth().getStatus().equals((short)4)) {
                            cell23.setCellValue("其他状态（如咳嗽、流鼻涕、嗓子发炎.");
                        }
                        cell23.setCellStyle(weiRuan10);
                    }
                    PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 27, 27);

                    lobourIndex++;

                    PoiUtils.mergeForm(sheetAt, work, 4, 4, 0, 0);
                    PoiUtils.mergeForm(sheetAt, work, 4, 4, 1, 2);
                    PoiUtils.mergeForm(sheetAt, work, 4, 4, 3, 3);
                    PoiUtils.mergeForm(sheetAt, work, 4, 4, 4, 4);
                    PoiUtils.mergeForm(sheetAt, work, 4, 4, 5, 5);
                    PoiUtils.mergeForm(sheetAt, work, 4, 4, 6, 6);
                }

            }

            if (!tempExcelVo.getUnNormalChild().getList().isEmpty()) {
                for (TempOrgTree orgTree : tempExcelVo.getUnNormalChild().getList()) {
                    if (!orgTree.getOrgName().equals("十一号线经理部")) {
                        Row row = sheetAt.createRow(lobourIndex);
                        Cell cell = row.createCell(0);
                        cell.setCellValue(no);
                        cell.setCellStyle(weiRuan10);
                        PoiUtils.mergeCell(sheetAt, lobourIndex, lobourIndex + orgTree.getNextChild().size() - 1, 0, 0);

                        //分部
                        Cell cell1 = row.createCell(1);
                        cell1.setCellValue(orgTree.getOrgName());
                        cell1.setCellStyle(weiRuan10);
                        sheetAt.setColumnWidth(1,7*256);
                        PoiUtils.mergeCell(sheetAt, lobourIndex, lobourIndex + orgTree.getNextChild().size() - 1, 1, 1);

                        //工区
                        for (TempOrgTree tree : orgTree.getNextChild()) {
                            HSSFRow row10 = sheetAt.getRow(lobourIndex);
                            sheetAt.setColumnWidth(2,12*256);
                            if (row10 != null) {
                                Cell cell2 = row.createCell(2);
                                cell2.setCellValue(tree.getOrgName());
                                cell2.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 2, 2);

                                Cell cell3 = row.createCell(3);
                                if (!tree.getLineMessages().isEmpty()) {
                                    if (tree.getLineMessages().get(0).getUser().getPersonType().equals("gl")) {
                                        cell3.setCellValue("管理人员");
                                    } else {
                                        cell3.setCellValue("劳务人员");
                                    }
                                }
                                cell3.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 3, 3);


                                Cell cell4 = row.createCell(4);
                                if (!tree.getLineMessages().isEmpty()) {
                                    cell4.setCellValue(tree.getLineMessages().get(0).getUser().getUserName());
                                }
                                cell4.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 4, 4);

                                Cell rowCell5 = row.createCell(5);
                                if (!tree.getLineMessages().isEmpty()) {
                                    if (tree.getLineMessages().get(0).getDetailAlarm() != 0) {
                                        rowCell5.setCellValue("异常");
                                        PoiUtils.setBackColor(work, rowCell5);
                                    } else {
                                        rowCell5.setCellValue("正常");
                                        rowCell5.setCellStyle(weiRuan10);
                                    }
                                }
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 5, 5);

                                Cell rowCell6 = row.createCell(6);
                                if (!tree.getLineMessages().isEmpty()) {
                                    if (tree.getLineMessages().get(0).getMsgTotalAlarm() != 0) {
                                        rowCell6.setCellValue("有");
                                        PoiUtils.setBackColor(work, rowCell6);
                                    } else {
                                        rowCell6.setCellValue("无");
                                        rowCell6.setCellStyle(weiRuan10);
                                    }
                                }
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 6, 6);

                                //异常血液检测、核酸检测
                                Cell sheetAtRowCell = row.createCell(7);
                                if (!tree.getLineMessages().isEmpty()) {
                                    if (tree.getLineMessages().get(0).getBloodTest().getStatus() == 0) {
                                        sheetAtRowCell.setCellValue("否");
                                        sheetAtRowCell.setCellStyle(weiRuan10);
                                    }else {
                                        sheetAtRowCell.setCellValue(tree.getLineMessages().get(0).getBloodTest().getName());
                                        sheetAtRowCell.setCellStyle(weiRuan10);
                                        if (tree.getLineMessages().get(0).getBloodTest().getTestResult() == (short) 1) {
                                            sheetAtRowCell.setCellStyle(red10);
                                            PoiUtils.setBackColor(work,sheetAtRowCell);
                                        }
                                    }
                                }
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 7, 7);

                                Cell rowCell4 = row.createCell(8);
                                if (!tree.getLineMessages().isEmpty()&&tree.getLineMessages().get(0).getBloodTest().getTestDate()!=null) {
                                    rowCell4.setCellValue(changeDate(tree.getLineMessages().get(0).getBloodTest().getTestDate()));
                                }
                                rowCell4.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 8, 8);

                                Cell atRowCell = row.createCell(9);
                                if (!tree.getLineMessages().isEmpty()) {
                                    if (tree.getLineMessages().get(0).getNucleaseTest().getStatus() == 0) {
                                        atRowCell.setCellValue("否");
                                        atRowCell.setCellStyle(weiRuan10);
                                    }else {
                                        atRowCell.setCellValue(tree.getLineMessages().get(0).getNucleaseTest().getName());
                                        atRowCell.setCellStyle(weiRuan10);
                                        if (tree.getLineMessages().get(0).getNucleaseTest().getTestResult() == (short) 1) {
                                            atRowCell.setCellStyle(red10);
                                            PoiUtils.setBackColor(work,atRowCell);
                                        }
                                    }
                                }
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 9, 9);

                                Cell atRowCell1 = row.createCell(10);
                                if (!tree.getLineMessages().isEmpty()&&tree.getLineMessages().get(0).getNucleaseTest().getTestDate()!=null) {
                                    atRowCell1.setCellValue(changeDate(tree.getLineMessages().get(0).getNucleaseTest().getTestDate()));
                                }
                                atRowCell1.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 10, 10);

                                Cell rowCell14 = row.createCell(11);
                                if (!tree.getLineMessages().isEmpty()) {
                                    rowCell14.setCellValue(tree.getLineMessages().get(0).getWorkStatus().getName());
                                }
                                rowCell14.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 11, 11);

                                Cell rowCell7 = row.createCell(12);
                                if (!tree.getLineMessages().isEmpty()) {
                                    rowCell7.setCellValue(tree.getLineMessages().get(0).getUser().getAge());
                                }
                                rowCell7.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 12, 12);

                                Cell rowCell8 = row.createCell(13);
                                if (!tree.getLineMessages().isEmpty()) {
                                    rowCell8.setCellValue(tree.getLineMessages().get(0).getUser().getPhone());
                                }
                                rowCell8.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 13, 13);

                                Cell rowCell9 = row.createCell(14);
                                if (!tree.getLineMessages().isEmpty()) {
                                    if (tree.getLineMessages().get(0).getUser().getIdCard().length() == 18) {
                                        String substring1 = tree.getLineMessages().get(0).getUser().getIdCard().substring(0, 10);
                                        rowCell9.setCellValue(substring1+"********");
                                    }else {
                                        rowCell9.setCellValue(tree.getLineMessages().get(0).getUser().getIdCard());
                                    }
                                }
                                rowCell9.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 14, 14);


                                if (!tree.getLineMessages().isEmpty()) {
                                    for (TempDetail tempDetail : tree.getLineMessages().get(0).getTemp()) {
                                        if (tempDetail.getTempType().equals((short) 1)) {
                                            Cell cell11 = row.createCell(15);
                                            cell11.setCellValue(tempDetail.getTemperature().doubleValue());
                                            cell11.setCellStyle(weiRuan10);
                                            if (tempDetail.getTemperature().doubleValue() > tempDetail.getUpperLimit().doubleValue()) {
                                                PoiUtils.setBackColor(work,cell11);
                                            }
                                        } else if (tempDetail.getTempType().equals((short) 2)) {
                                            Cell cell11 = row.createCell(16);
                                            cell11.setCellValue(tempDetail.getTemperature().doubleValue());
                                            cell11.setCellStyle(weiRuan10);
                                            if (tempDetail.getTemperature().doubleValue() > tempDetail.getUpperLimit().doubleValue()) {
                                                PoiUtils.setBackColor(work,cell11);
                                            }
                                        } else if (tempDetail.getTempType().equals((short) 3)) {
                                            Cell cell11 = row.createCell(17);
                                            cell11.setCellValue(tempDetail.getTemperature().doubleValue());
                                            cell11.setCellStyle(weiRuan10);
                                            if (tempDetail.getTemperature().doubleValue() > tempDetail.getUpperLimit().doubleValue()) {
                                                PoiUtils.setBackColor(work,cell11);
                                            }
                                        }
                                    }
                                }
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 15, 15);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 16, 16);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 17, 17);

                                Cell rowCell10 = row.createCell(18);
                                if (!tree.getLineMessages().isEmpty()) {
                                    if (tree.getLineMessages().get(0).getUser().getStayStatus().equals((short) 1)) {
                                        rowCell10.setCellValue("处于14天隔离期");
                                    } else if (tree.getLineMessages().get(0).getUser().getStayStatus().equals((short) 2)) {
                                        rowCell10.setCellValue("已完成14天隔离");
                                    } else if (tree.getLineMessages().get(0).getUser().getStayStatus().equals((short) 3)) {
                                        rowCell10.setCellValue("休假期间一直在穗");
                                    }
                                }
                                rowCell10.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 18, 18);

                                Cell rowCell11 = row.createCell(19);
                                if (!tree.getLineMessages().isEmpty()) {
                                    rowCell11.setCellValue(tree.getLineMessages().get(0).getUser().getComeDate());
                                }
                                rowCell11.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 19, 19);

                                Cell rowCell12 = row.createCell(20);
                                if (!tree.getLineMessages().isEmpty()) {
                                    rowCell12.setCellValue(tree.getLineMessages().get(0).getUser().getFinishDate());
                                }
                                rowCell12.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 20, 20);

                                Cell cell11 = row.createCell(21);
                                if (!tree.getLineMessages().isEmpty() && tree.getLineMessages().get(0).getMessage() != null) {
                                    cell11.setCellValue("广州市" + tree.getLineMessages().get(0).getMessage().getProvince());
                                }
                                cell11.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 21, 21);

                                Cell cell12 = row.createCell(22);
                                if (!tree.getLineMessages().isEmpty() && tree.getLineMessages().get(0).getMessage() != null) {
                                    cell12.setCellValue(tree.getLineMessages().get(0).getMessage().getAddress());
                                }
                                cell12.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 22, 22);

                                Cell cell13 = row.createCell(23);
                                if (!tree.getLineMessages().isEmpty() && tree.getLineMessages().get(0).getRoute() != null) {
                                    if (tree.getLineMessages().get(0).getRoute().getStatus().equals((short)0)) {
                                        cell13.setCellValue("");
                                    } else {
                                        cell13.setCellValue(tree.getLineMessages().get(0).getRoute().getName());
                                    }

                                }
                                cell13.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 23, 23);


                                Cell cell17 = row.createCell(24);
                                if (!tree.getLineMessages().isEmpty() && tree.getLineMessages().get(0).getContactHistory() != null) {
                                    cell17.setCellValue(tree.getLineMessages().get(0).getContactHistory().getName());
                                }
                                cell17.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 24, 24);

                                Cell cell18 = row.createCell(25);
                                if (!tree.getLineMessages().isEmpty() && tree.getLineMessages().get(0).getAreaHistory() != null) {
                                    cell18.setCellValue(tree.getLineMessages().get(0).getAreaHistory().getName());
                                }
                                cell18.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 25, 25);

                                Cell cell19 = row.createCell(26);
                                if (!tree.getLineMessages().isEmpty() && tree.getLineMessages().get(0).getPersonHistory() != null) {
                                    cell19.setCellValue(tree.getLineMessages().get(0).getPersonHistory().getName());
                                }
                                cell19.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 26, 26);

                                Cell rowCell13 = row.createCell(27);
                                if (!tree.getLineMessages().isEmpty() && tree.getLineMessages().get(0).getHealth() != null) {
                                    if (tree.getLineMessages().get(0).getHealth().getStatus().equals((short)0)) {
                                        rowCell13.setCellValue("一切正常");
                                    } else if (tree.getLineMessages().get(0).getHealth().getStatus().equals((short)1)) {
                                        rowCell13.setCellValue("疑似新型冠状病毒肺炎");
                                    } else if (tree.getLineMessages().get(0).getHealth().getStatus().equals((short)2)) {
                                        rowCell13.setCellValue("确认新型冠状病毒肺炎");
                                    } else if (tree.getLineMessages().get(0).getHealth().getStatus().equals((short)3)) {
                                        rowCell13.setCellValue("治愈新型冠状病毒肺炎");
                                    } else if (tree.getLineMessages().get(0).getHealth().getStatus().equals((short)4)) {
                                        rowCell13.setCellValue("其他状态（如咳嗽、流鼻涕、嗓子发炎.");
                                    }
                                }
                                rowCell13.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 27, 27);

                                Cell cell20 = row.createCell(28);
                                if (!tree.getLineMessages().isEmpty() && tree.getLineMessages().get(0).getMessage() != null) {
                                    cell20.setCellValue(tree.getLineMessages().get(0).getMessage().getNote());
                                }
                                cell20.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 28, 28);


                                lobourIndex++;
                            } else {
                                HSSFRow row11 = sheetAt.createRow(lobourIndex);
                                Cell cell2 = row11.createCell(2);
                                cell2.setCellValue(tree.getOrgName());
                                cell2.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 2, 2);

                                Cell cell3 = row11.createCell(3);
                                if (!tree.getLineMessages().isEmpty()) {
                                    if (tree.getLineMessages().get(0).getUser().getPersonType().equals("gl")) {
                                        cell3.setCellValue("管理人员");
                                    } else {
                                        cell3.setCellValue("劳务人员");
                                    }
                                }
                                cell3.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 3, 3);


                                Cell cell4 = row11.createCell(4);
                                if (!tree.getLineMessages().isEmpty()) {
                                    cell4.setCellValue(tree.getLineMessages().get(0).getUser().getUserName());
                                }
                                cell4.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 4, 4);

                                Cell rowCell5 = row11.createCell(5);
                                if (!tree.getLineMessages().isEmpty()) {
                                    if (tree.getLineMessages().get(0).getDetailAlarm() != 0) {
                                        rowCell5.setCellValue("异常");
                                        PoiUtils.setBackColor(work,rowCell5);
                                    } else {
                                        rowCell5.setCellValue("正常");
                                        rowCell5.setCellStyle(weiRuan10);
                                    }
                                }
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 5, 5);

                                Cell rowCell6 = row11.createCell(6);
                                if (!tree.getLineMessages().isEmpty()) {
                                    if (tree.getLineMessages().get(0).getMsgTotalAlarm() != 0) {
                                        rowCell6.setCellValue("有");
                                        PoiUtils.setBackColor(work,rowCell6);
                                    } else {
                                        rowCell6.setCellValue("无");
                                        rowCell6.setCellStyle(weiRuan10);
                                    }
                                }
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 6, 6);

                                //异常血液检测、核酸检测
                                Cell sheetAtRowCell = row11.createCell(7);
                                if (!tree.getLineMessages().isEmpty()) {
                                    if (tree.getLineMessages().get(0).getBloodTest().getStatus() == 0) {
                                        sheetAtRowCell.setCellValue("否");
                                        sheetAtRowCell.setCellStyle(weiRuan10);
                                    }else {
                                        sheetAtRowCell.setCellValue(tree.getLineMessages().get(0).getBloodTest().getName());
                                        sheetAtRowCell.setCellStyle(weiRuan10);
                                        if (tree.getLineMessages().get(0).getBloodTest().getTestResult() == (short) 1) {
                                            sheetAtRowCell.setCellStyle(red10);
                                            PoiUtils.setBackColor(work,sheetAtRowCell);
                                        }
                                    }
                                }
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 7, 7);

                                Cell rowCell4 = row11.createCell(8);
                                if (!tree.getLineMessages().isEmpty()&&tree.getLineMessages().get(0).getBloodTest().getTestDate()!=null) {
                                    rowCell4.setCellValue(changeDate(tree.getLineMessages().get(0).getBloodTest().getTestDate()));
                                }
                                rowCell4.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 8, 8);

                                Cell atRowCell = row11.createCell(9);
                                if (!tree.getLineMessages().isEmpty()) {
                                    if (tree.getLineMessages().get(0).getNucleaseTest().getStatus() == 0) {
                                        atRowCell.setCellValue("否");
                                        atRowCell.setCellStyle(weiRuan10);
                                    }else {
                                        atRowCell.setCellValue(tree.getLineMessages().get(0).getNucleaseTest().getName());
                                        atRowCell.setCellStyle(weiRuan10);
                                        if (tree.getLineMessages().get(0).getNucleaseTest().getTestResult() == (short) 1) {
                                            atRowCell.setCellStyle(red10);
                                            PoiUtils.setBackColor(work,atRowCell);
                                        }
                                    }
                                }
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 9, 9);

                                Cell atRowCell1 = row11.createCell(10);
                                if (!tree.getLineMessages().isEmpty()&&tree.getLineMessages().get(0).getNucleaseTest().getTestDate()!=null) {
                                    atRowCell1.setCellValue(changeDate(tree.getLineMessages().get(0).getNucleaseTest().getTestDate()));
                                }
                                atRowCell1.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 10, 10);

                                Cell rowCell14 = row11.createCell(11);
                                if (!tree.getLineMessages().isEmpty()) {
                                    rowCell14.setCellValue(tree.getLineMessages().get(0).getWorkStatus().getName());
                                }
                                rowCell14.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 11, 11);

                                Cell rowCell7 = row11.createCell(12);
                                if (!tree.getLineMessages().isEmpty()) {
                                    rowCell7.setCellValue(tree.getLineMessages().get(0).getUser().getAge());
                                }
                                rowCell7.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 12, 12);

                                Cell rowCell8 = row11.createCell(13);
                                if (!tree.getLineMessages().isEmpty()) {
                                    rowCell8.setCellValue(tree.getLineMessages().get(0).getUser().getPhone());
                                }
                                rowCell8.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 13, 13);

                                Cell rowCell9 = row11.createCell(14);
                                if (!tree.getLineMessages().isEmpty()) {
                                    if (tree.getLineMessages().get(0).getUser().getIdCard().length() == 18) {
                                        String substring1 = tree.getLineMessages().get(0).getUser().getIdCard().substring(0, 10);
                                        rowCell9.setCellValue(substring1+"********");
                                    }else {
                                        rowCell9.setCellValue(tree.getLineMessages().get(0).getUser().getIdCard());
                                    }
                                }
                                rowCell9.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 14, 14);


                                if (!tree.getLineMessages().isEmpty()) {
                                    for (TempDetail tempDetail : tree.getLineMessages().get(0).getTemp()) {
                                        if (tempDetail.getTempType().equals((short) 1)) {
                                            Cell cell11 = row11.createCell(15);
                                            cell11.setCellValue(tempDetail.getTemperature().doubleValue());
                                            cell11.setCellStyle(weiRuan10);
                                            if (tempDetail.getTemperature().doubleValue() > tempDetail.getUpperLimit().doubleValue()) {
                                                PoiUtils.setBackColor(work,cell11);
                                            }
                                        } else if (tempDetail.getTempType().equals((short) 2)) {
                                            Cell cell11 = row11.createCell(16);
                                            cell11.setCellValue(tempDetail.getTemperature().doubleValue());
                                            cell11.setCellStyle(weiRuan10);
                                            if (tempDetail.getTemperature().doubleValue() > tempDetail.getUpperLimit().doubleValue()) {
                                                PoiUtils.setBackColor(work,cell11);
                                            }
                                        } else if (tempDetail.getTempType().equals((short) 3)) {
                                            Cell cell11 = row11.createCell(17);
                                            cell11.setCellValue(tempDetail.getTemperature().doubleValue());
                                            cell11.setCellStyle(weiRuan10);
                                            if (tempDetail.getTemperature().doubleValue() > tempDetail.getUpperLimit().doubleValue()) {
                                                PoiUtils.setBackColor(work,cell11);
                                            }
                                        }
                                    }
                                }
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 15, 15);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 16, 16);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 17, 17);

                                Cell rowCell10 = row11.createCell(18);
                                if (!tree.getLineMessages().isEmpty()) {
                                    if (tree.getLineMessages().get(0).getUser().getStayStatus().equals((short) 1)) {
                                        rowCell10.setCellValue("处于14天隔离期");
                                    } else if (tree.getLineMessages().get(0).getUser().getStayStatus().equals((short) 2)) {
                                        rowCell10.setCellValue("已完成14天隔离");
                                    } else if (tree.getLineMessages().get(0).getUser().getStayStatus().equals((short) 3)) {
                                        rowCell10.setCellValue("休假期间一直在穗");
                                    }
                                }
                                rowCell10.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 18, 18);

                                Cell rowCell11 = row11.createCell(19);
                                if (!tree.getLineMessages().isEmpty()) {
                                    rowCell11.setCellValue(tree.getLineMessages().get(0).getUser().getComeDate());
                                }
                                rowCell11.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 19, 19);

                                Cell rowCell12 = row11.createCell(20);
                                if (!tree.getLineMessages().isEmpty()) {
                                    rowCell12.setCellValue(tree.getLineMessages().get(0).getUser().getFinishDate());
                                }
                                rowCell12.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 20, 20);

                                Cell cell11 = row11.createCell(21);
                                if (!tree.getLineMessages().isEmpty() && tree.getLineMessages().get(0).getMessage() != null) {
                                    cell11.setCellValue("广州市" + tree.getLineMessages().get(0).getMessage().getProvince());
                                }
                                cell11.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 21, 21);

                                Cell cell12 = row11.createCell(22);
                                if (!tree.getLineMessages().isEmpty() && tree.getLineMessages().get(0).getMessage() != null) {
                                    cell12.setCellValue(tree.getLineMessages().get(0).getMessage().getAddress());
                                }
                                cell12.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 22, 22);

                                Cell cell13 = row11.createCell(23);
                                if (!tree.getLineMessages().isEmpty() && tree.getLineMessages().get(0).getRoute() != null) {
                                    if (tree.getLineMessages().get(0).getRoute().getStatus().equals((short)0)) {
                                        cell13.setCellValue("");
                                    } else {
                                        cell13.setCellValue(tree.getLineMessages().get(0).getRoute().getName());
                                    }

                                }
                                cell13.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 23, 23);


                                Cell cell17 = row11.createCell(24);
                                if (!tree.getLineMessages().isEmpty() && tree.getLineMessages().get(0).getContactHistory() != null) {
                                    cell17.setCellValue(tree.getLineMessages().get(0).getContactHistory().getName());
                                }
                                cell17.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 24, 24);

                                Cell cell18 = row11.createCell(25);
                                if (!tree.getLineMessages().isEmpty() && tree.getLineMessages().get(0).getAreaHistory() != null) {
                                    cell18.setCellValue(tree.getLineMessages().get(0).getAreaHistory().getName());
                                }
                                cell18.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 25, 25);

                                Cell cell19 = row11.createCell(26);
                                if (!tree.getLineMessages().isEmpty() && tree.getLineMessages().get(0).getPersonHistory() != null) {
                                    cell19.setCellValue(tree.getLineMessages().get(0).getPersonHistory().getName());
                                }
                                cell19.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 26, 26);

                                Cell rowCell13 = row11.createCell(27);
                                if (!tree.getLineMessages().isEmpty() && tree.getLineMessages().get(0).getHealth() != null) {
                                    if (tree.getLineMessages().get(0).getHealth().getStatus().equals((short)0)) {
                                        rowCell13.setCellValue("一切正常");
                                    } else if (tree.getLineMessages().get(0).getHealth().getStatus().equals((short)1)) {
                                        rowCell13.setCellValue("疑似新型冠状病毒肺炎");
                                    } else if (tree.getLineMessages().get(0).getHealth().getStatus().equals((short)2)) {
                                        rowCell13.setCellValue("确认新型冠状病毒肺炎");
                                    } else if (tree.getLineMessages().get(0).getHealth().getStatus().equals((short)3)) {
                                        rowCell13.setCellValue("治愈新型冠状病毒肺炎");
                                    } else if (tree.getLineMessages().get(0).getHealth().getStatus().equals((short)4)) {
                                        rowCell13.setCellValue("其他状态（如咳嗽、流鼻涕、嗓子发炎.");
                                    }
                                }
                                rowCell13.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 27, 27);

                                Cell cell20 = row11.createCell(28);
                                if (!tree.getLineMessages().isEmpty() && tree.getLineMessages().get(0).getMessage() != null) {
                                    cell20.setCellValue(tree.getLineMessages().get(0).getMessage().getNote());
                                }
                                cell20.setCellStyle(weiRuan10);
                                PoiUtils.mergeForm(sheetAt, work, lobourIndex, lobourIndex, 28, 28);


                                lobourIndex++;
                            }


                        }

                    }
                    no++;
                }

            }


            PoiUtils.mergeForm(sheetAt, work, 5, 7, 0, 0);
            PoiUtils.mergeForm(sheetAt, work, 5, 7, 1, 1);
            PoiUtils.mergeForm(sheetAt, work, 8, 13, 0, 0);
            PoiUtils.mergeForm(sheetAt, work, 8, 13, 1, 1);
            PoiUtils.mergeForm(sheetAt, work, 14, 15, 0, 0);
            PoiUtils.mergeForm(sheetAt, work, 14, 15, 1, 1);
            PoiUtils.mergeForm(sheetAt, work, 16, 19, 0, 0);
            PoiUtils.mergeForm(sheetAt, work, 16, 19, 1, 1);
            PoiUtils.mergeForm(sheetAt, work, 20, 21, 0, 0);
            PoiUtils.mergeForm(sheetAt, work, 20, 21, 1, 1);
            PoiUtils.mergeForm(sheetAt, work, 22, 24, 0, 0);
            PoiUtils.mergeForm(sheetAt, work, 22, 24, 1, 1);
            PoiUtils.mergeForm(sheetAt, work, 25, 26, 0, 0);
            PoiUtils.mergeForm(sheetAt, work, 25, 26, 1, 1);
            PoiUtils.mergeForm(sheetAt, work, 27, 28, 0, 0);
            PoiUtils.mergeForm(sheetAt, work, 27, 28, 1, 1);
            PoiUtils.mergeForm(sheetAt, work, 29, 33, 0, 0);
            PoiUtils.mergeForm(sheetAt, work, 29, 33, 1, 1);
            PoiUtils.mergeForm(sheetAt, work, 34, 35, 0, 0);
            PoiUtils.mergeForm(sheetAt, work, 34, 35, 1, 1);
        }


//        work.write(fileOutputStream);
//        fileOutputStream.close();

        return work;
    }


    public static void changePerson(TempExcelVo tempExcelVo, HSSFWorkbook work, HSSFCellStyle thinStyle, HSSFCellStyle songTiNo12, HSSFCellStyle songTi12, HSSFCellStyle weiRuan10, HSSFCellStyle red10, HSSFCellStyle songTiNoleft) {
        //起始行
        int manageRowIndex = 0;

        ChildTable managerChild = tempExcelVo.getLabourChild();

        HSSFSheet sheet1 = work.getSheetAt(2);

        //第一行
        Row sheetRow1 = sheet1.createRow(manageRowIndex);
        Cell sheetCell1 = sheetRow1.createCell(0);
        sheetCell1.setCellValue(managerChild.getProjectName() + "体温日报");
        PoiUtils.mergeCell(sheet1, 0, 0, 0, 27);
        sheetCell1.setCellStyle(thinStyle);
        sheetRow1.setHeight((short) 460);
        PoiUtils.mergeForm(sheet1, work, 0, 0, 0, 27);

        manageRowIndex++;

        //第二行
        Row sheetRow2 = sheet1.createRow(manageRowIndex);
        Cell sheetCell2 = sheetRow2.createCell(0);
        sheetCell2.setCellValue("总体情况");
        sheet1.setColumnWidth(0, 10 * 256);
        sheetCell2.setCellStyle(songTiNo12);
        PoiUtils.mergeForm(sheet1, work, 1, 1, 0, 0);

        Cell sheetRow2Cell = sheetRow2.createCell(1);
        PoiUtils.mergeCell(sheet1, 1, 1, 1, 24);
        sheetRow2Cell.setCellValue(managerChild.getTitle());
        sheetRow2Cell.setCellStyle(songTiNoleft);
        PoiUtils.mergeForm(sheet1, work, 1, 1, 1, 24);

        Cell sheetRow2Cell1 = sheetRow2.createCell(25);
        sheetRow2Cell1.setCellValue("日期");
        sheetRow2Cell1.setCellStyle(songTi12);
        PoiUtils.mergeForm(sheet1, work, 1, 1, 25, 25);

        Cell sheetRow2Cell2 = sheetRow2.createCell(26);
        sheetRow2Cell2.setCellValue(managerChild.getDateDay());
        sheetRow2Cell2.setCellStyle(songTi12);
        PoiUtils.mergeCell(sheet1, 1, 1, 26, 27);
        PoiUtils.mergeForm(sheet1, work, 1, 1, 26, 27);


        sheetRow2.setHeight((short) 410);


        manageRowIndex++;

        //第三行
        Row sheetRow3 = threeRow(sheet1, manageRowIndex, work, songTi12, managerChild, 0);

        sheetRow3.setHeight((short) 1820);

        manageRowIndex++;

        //第四行
        Row sheetRow4 = sheet1.createRow(manageRowIndex);
        Cell row4Cell = sheetRow4.createCell(14);
        row4Cell.setCellValue("上午8:30时");
        sheet1.setColumnWidth(14, 9 * 256);
        row4Cell.setCellStyle(songTi12);
        PoiUtils.mergeForm(sheet1, work, 3, 3, 14, 14);

        Cell row4Cell1 = sheetRow4.createCell(15);
        row4Cell1.setCellValue("中午13:30时");
        sheet1.setColumnWidth(15, 10 * 256);
        row4Cell1.setCellStyle(songTi12);
        PoiUtils.mergeForm(sheet1, work, 3, 3, 15, 15);

        Cell row4Cell2 = sheetRow4.createCell(16);
        row4Cell2.setCellValue("下午19:30时");
        sheet1.setColumnWidth(16, 10 * 256);
        row4Cell2.setCellStyle(songTi12);
        PoiUtils.mergeForm(sheet1, work, 3, 3, 16, 16);

        PoiUtils.mergeForm(sheet1, work, 2, 3, 0, 0);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 1, 1);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 2, 2);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 3, 3);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 4, 4);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 5, 5);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 6, 6);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 7, 7);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 8, 8);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 9, 9);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 10, 10);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 11, 11);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 12, 12);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 13, 13);
        PoiUtils.mergeForm(sheet1, work, 2, 2, 14, 16);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 17, 17);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 18, 18);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 19, 19);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 20, 20);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 21, 21);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 22, 22);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 23, 23);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 24, 24);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 25, 25);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 26, 26);
        PoiUtils.mergeForm(sheet1, work, 2, 3, 27, 27);


        sheetRow4.setHeight((short) 600);

        manageRowIndex++;
        //第五行++
        //序号
        int num = 1;
        if (managerChild.getList() != null) {
            for (TableLineMessage tableLineMessage : managerChild.getList()) {
                Row sheetRow5 = sheet1.createRow(manageRowIndex);
                Cell cell = sheetRow5.createCell(0);
                cell.setCellValue(num);
                cell.setCellStyle(weiRuan10);
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 0, 0);

                Cell cell1 = sheetRow5.createCell(1);
                if (managerChild.getProjectId().equals("6245721945602523136")) {
                    cell1.setCellValue("广州市轨道交通十一号线");
                } else if (managerChild.getProjectId().equals("6245721945602523137")) {
                    cell1.setCellValue("广州市轨道交通十三号线二期");
                } else if (managerChild.getProjectId().equals("6245721945602523139")) {
                    cell1.setCellValue("广州市轨道交通七号线二期");
                } else if (managerChild.getProjectId().equals("6422195692059623424")) {
                    cell1.setCellValue("广州市中心城区地下综合管廊");
                }
                cell1.setCellStyle(weiRuan10);
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 1, 1);

                Cell cell2 = sheetRow5.createCell(2);
                cell2.setCellValue(tableLineMessage.getUser().getOrgName());
                cell2.setCellStyle(weiRuan10);
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 2, 2);

                Cell cell3 = sheetRow5.createCell(3);
                cell3.setCellValue(tableLineMessage.getUser().getUserName());
                cell3.setCellStyle(weiRuan10);
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 3, 3);

                if (!tableLineMessage.getTemp().isEmpty()) {
                    Cell cell4 = sheetRow5.createCell(4);
                    if (tableLineMessage.getTemp().get(0).getAlarm() != 0) {
                        cell4.setCellValue("异常");
                        PoiUtils.setBackColor(work, cell4);
                    } else {
                        cell4.setCellValue("正常");
                        cell4.setCellStyle(weiRuan10);
                    }
                }
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 4, 4);

                Cell cell10 = sheetRow5.createCell(5);
                if (tableLineMessage.getMsgNoReport() == 0) {
                    cell10.setCellValue("无");
                    cell10.setCellStyle(weiRuan10);
                } else {
                    cell10.setCellValue("有");
                    PoiUtils.setBackColor(work, cell10);
                }
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 5, 5);

                //劳务血液检测、核酸检测
                Cell sheetRow5Cell = sheetRow5.createCell(6);
                if (tableLineMessage.getBloodTest() != null) {
                    if (tableLineMessage.getBloodTest().getStatus() == 0) {
                        sheetRow5Cell.setCellValue("否");
                        sheetRow5Cell.setCellStyle(weiRuan10);
                    }else {
                        sheetRow5Cell.setCellValue(tableLineMessage.getBloodTest().getName());
                        sheetRow5Cell.setCellStyle(weiRuan10);
                        if (tableLineMessage.getBloodTest().getTestResult() == (short) 1) {
                            sheetRow5Cell.setCellStyle(red10);
                            PoiUtils.setBackColor(work,sheetRow5Cell);
                        }
                    }
                }
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 6, 6);

                Cell sheetRow5Cell3 = sheetRow5.createCell(7);
                if (tableLineMessage.getBloodTest() != null&&tableLineMessage.getBloodTest().getTestDate()!=null) {
                    sheetRow5Cell3.setCellValue(changeDate(tableLineMessage.getBloodTest().getTestDate()));
                    sheet1.setColumnWidth(7, 11 * 256);
                }
                sheetRow5Cell3.setCellStyle(weiRuan10);
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 7, 7);

                Cell sheetRow5Cell1 = sheetRow5.createCell(8);
                if (tableLineMessage.getNucleaseTest() != null) {
                    if (tableLineMessage.getNucleaseTest().getStatus() == 0) {
                        sheetRow5Cell1.setCellValue("否");
                        sheetRow5Cell1.setCellStyle(weiRuan10);
                    }else {
                        sheetRow5Cell1.setCellValue(tableLineMessage.getNucleaseTest().getName());
                        sheetRow5Cell1.setCellStyle(weiRuan10);
                        if (tableLineMessage.getNucleaseTest().getTestResult() == (short) 1) {
                            sheetRow5Cell1.setCellStyle(red10);
                            PoiUtils.setBackColor(work,sheetRow5Cell1);
                        }
                    }
                }
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 8, 8);

                Cell sheetRow5Cell2 = sheetRow5.createCell(9);
                if (tableLineMessage.getNucleaseTest() != null&&tableLineMessage.getNucleaseTest().getTestDate()!=null) {
                    sheetRow5Cell2.setCellValue(changeDate(tableLineMessage.getNucleaseTest().getTestDate()));
                    sheet1.setColumnWidth(9, 11 * 256);
                }
                sheetRow5Cell2.setCellStyle(weiRuan10);
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 9, 9);

                Cell sheetRow5Cell4 = sheetRow5.createCell(10);
                if (tableLineMessage.getWorkStatus() != null) {
                    sheetRow5Cell4.setCellValue(tableLineMessage.getWorkStatus().getName());
                }
                sheetRow5Cell4.setCellStyle(weiRuan10);
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 10, 10);

                Cell cell11 = sheetRow5.createCell(11);
                cell11.setCellValue(tableLineMessage.getUser().getAge());
                cell11.setCellStyle(weiRuan10);
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 11, 11);

                Cell cell12 = sheetRow5.createCell(12);
                cell12.setCellValue(tableLineMessage.getUser().getPhone());
                cell12.setCellStyle(weiRuan10);
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 12, 12);

                Cell cell13 = sheetRow5.createCell(13);
                if (tableLineMessage.getUser().getIdCard().length() == 18) {
                    String substring = tableLineMessage.getUser().getIdCard().substring(0, 10);
                    cell13.setCellValue(substring+"********");
                }else {
                    cell13.setCellValue(tableLineMessage.getUser().getIdCard());
                }
                cell13.setCellStyle(weiRuan10);
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 13, 13);

                for (TempDetail tempDetail : tableLineMessage.getTemp()) {
                    if (tempDetail.getTempType().equals((short) 1)) {
                        Cell cell14 = sheetRow5.createCell(14);
                        cell14.setCellValue(tempDetail.getTemperature().doubleValue());
                        cell14.setCellStyle(weiRuan10);
                        if (tempDetail.getTemperature().doubleValue() > tempDetail.getUpperLimit().doubleValue()) {
                            cell14.setCellStyle(red10);
                        }
                    } else if (tempDetail.getTempType().equals((short) 2)) {
                        Cell cell14 = sheetRow5.createCell(15);
                        cell14.setCellValue(tempDetail.getTemperature().doubleValue());
                        cell14.setCellStyle(weiRuan10);
                        if (tempDetail.getTemperature().doubleValue() > tempDetail.getUpperLimit().doubleValue()) {
                            cell14.setCellStyle(red10);
                        }
                    } else if (tempDetail.getTempType().equals((short) 3)) {
                        Cell cell14 = sheetRow5.createCell(16);
                        cell14.setCellValue(tempDetail.getTemperature().doubleValue());
                        cell14.setCellStyle(weiRuan10);
                        if (tempDetail.getTemperature().doubleValue() > tempDetail.getUpperLimit().doubleValue()) {
                            cell14.setCellStyle(red10);
                        }
                    }
                }
                Cell cell14 = sheetRow5.createCell(17);
                if (tableLineMessage.getUser().getStayStatus().equals((short) 1)) {
                    cell14.setCellValue("处于14天隔离期");

                } else if (tableLineMessage.getUser().getStayStatus().equals((short) 2)) {
                    cell14.setCellValue("已完成14天隔离");

                } else if (tableLineMessage.getUser().getStayStatus().equals((short) 3)) {
                    cell14.setCellValue("休假期间一直在穗");
                }
                cell14.setCellStyle(weiRuan10);
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 17, 17);

                Cell cell15 = sheetRow5.createCell(18);
                cell15.setCellValue(tableLineMessage.getUser().getComeDate());
                cell15.setCellStyle(weiRuan10);
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 18, 18);

                Cell cell16 = sheetRow5.createCell(19);
                cell16.setCellValue(tableLineMessage.getUser().getFinishDate());
                cell16.setCellStyle(weiRuan10);
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 19, 19);
                if (tableLineMessage.getMessage() != null) {
                    Cell cell17 = sheetRow5.createCell(20);
                    cell17.setCellValue("广州市" + tableLineMessage.getMessage().getProvince());
                    cell17.setCellStyle(weiRuan10);

                    Cell cell18 = sheetRow5.createCell(21);
                    cell18.setCellValue(tableLineMessage.getMessage().getAddress());
                    cell18.setCellStyle(weiRuan10);

                    Cell cell24 = sheetRow5.createCell(27);
                    cell24.setCellValue(tableLineMessage.getMessage().getNote());
                    cell24.setCellStyle(weiRuan10);
                }
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 20, 20);
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 21, 21);
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 27, 27);

                if (tableLineMessage.getRoute() != null) {
                    Cell cell19 = sheetRow5.createCell(22);
                    if (tableLineMessage.getRoute().getStatus().equals((short)0)) {
                        cell19.setCellValue("否");
                    } else {
                        cell19.setCellValue(tableLineMessage.getRoute().getName());
                    }
                    cell19.setCellStyle(weiRuan10);
                }
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 22, 22);


                if (tableLineMessage.getContactHistory() != null) {
                    Cell cell20 = sheetRow5.createCell(23);
                    cell20.setCellStyle(weiRuan10);
                    if (tableLineMessage.getContactHistory().getStatus()==0){
                        cell20.setCellValue("否");
                    }else {
                        if (tableLineMessage.getContactHistory().getName() != null) {
                            cell20.setCellValue(tableLineMessage.getContactHistory().getName());
                            PoiUtils.setBackColor(work, cell20);
                        }
                    }
                }
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 23, 23);


                if (tableLineMessage.getAreaHistory() != null) {
                    Cell cell21 = sheetRow5.createCell(24);
                    if (tableLineMessage.getAreaHistory().getStatus() == 0) {
                        cell21.setCellValue("否");
                    }else {
                        cell21.setCellValue(tableLineMessage.getAreaHistory().getName());
                    }

                    cell21.setCellStyle(weiRuan10);
                }
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 24, 24);


                if (tableLineMessage.getPersonHistory() != null) {
                    Cell cell22 = sheetRow5.createCell(25);
                    if (tableLineMessage.getPersonHistory().getStatus() == 0) {
                        cell22.setCellValue("否");
                    }else {
                        cell22.setCellValue(tableLineMessage.getPersonHistory().getName());
                    }
                    cell22.setCellStyle(weiRuan10);
                }
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 25, 25);


                if (tableLineMessage.getHealth() != null) {
                    Cell cell23 = sheetRow5.createCell(26);
                    if (tableLineMessage.getHealth().getStatus().equals((short)0)) {
                        cell23.setCellValue("一切正常");
                    } else if (tableLineMessage.getHealth().getStatus().equals((short)1)) {
                        cell23.setCellValue("疑似新型冠状病毒肺炎");
                    } else if (tableLineMessage.getHealth().getStatus().equals((short)2)) {
                        cell23.setCellValue("确认新型冠状病毒肺炎");
                    } else if (tableLineMessage.getHealth().getStatus().equals((short)3)) {
                        cell23.setCellValue("治愈新型冠状病毒肺炎");
                    } else if (tableLineMessage.getHealth().getStatus().equals((short)4)) {
                        cell23.setCellValue("其他状态（如咳嗽、流鼻涕、嗓子发炎.");
                    }
                    cell23.setCellStyle(weiRuan10);
                }
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 26, 26);


                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 14, 14);
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 15, 15);
                PoiUtils.mergeForm(sheet1, work, manageRowIndex, manageRowIndex, 16, 16);

                manageRowIndex++;
                num++;
            }
        }
    }

    public static void getPersonDetail(Row row, HSSFWorkbook work, HSSFCellStyle weiRuan10, HSSFSheet sheet, int startRowIndex, SumTable sumTable, int i) {
        AlarmCalculate managerStatistics = sumTable.getList().get(i).getNextChild().get(0).getManagerStatistics();
        AlarmCalculate labourStatistics = sumTable.getList().get(i).getNextChild().get(0).getLabourStatistics();

        Cell row6Cell = row.createCell(2);
        row6Cell.setCellValue(managerStatistics.getInIso());
        row6Cell.setCellStyle(weiRuan10);
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 2, 2);

        Cell row6Cell1 = row.createCell(3);
        row6Cell1.setCellValue(managerStatistics.getInIso());
        row6Cell1.setCellStyle(weiRuan10);
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 3, 3);


        Cell row6Cell2 = row.createCell(4);
        row6Cell2.setCellValue(managerStatistics.getOverIso());
        row6Cell2.setCellStyle(weiRuan10);
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 4, 4);

        Cell row6Cell3 = row.createCell(5);
        row6Cell3.setCellValue(managerStatistics.getTempAbnormal());
        row6Cell3.setCellStyle(weiRuan10);
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 5, 5);

        Cell row6Cell4 = row.createCell(6);
        row6Cell4.setCellValue(managerStatistics.getNoReport());
        row6Cell4.setCellStyle(weiRuan10);
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 6, 6);

        Cell row6Cell5 = row.createCell(7);
        row6Cell5.setCellValue(labourStatistics.getNoReport());
        row6Cell5.setCellStyle(weiRuan10);
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 7, 7);

        Cell row6Cell6 = row.createCell(8);
        row6Cell6.setCellValue(labourStatistics.getNoReport());
        row6Cell6.setCellStyle(weiRuan10);
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 8, 8);

        Cell row6Cell7 = row.createCell(9);
        row6Cell7.setCellValue(labourStatistics.getNoReport());
        row6Cell7.setCellStyle(weiRuan10);
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 9, 9);

        Cell row6Cell8 = row.createCell(10);
        row6Cell8.setCellValue(labourStatistics.getNoReport());
        row6Cell8.setCellStyle(weiRuan10);
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 10, 10);

        Cell row6Cell9 = row.createCell(11);
        row6Cell9.setCellValue(labourStatistics.getNoReport());
        row6Cell9.setCellStyle(weiRuan10);
        PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 11, 11);
    }


    public static Row threeRow(HSSFSheet sheet1, int manageRowIndex, HSSFWorkbook work, HSSFCellStyle songTi12, ChildTable managerChild, int k) {
        String month = managerChild.getDateDay().substring(5, 7);
        String day = managerChild.getDateDay().substring(8, 10);
        String time = null;
        if (month.substring(0, 0).equals("0")) {
            time = month.substring(0, 2) + "月" + day + "日";
        } else {
            time = month.substring(0, 2) + "月" + day + "日";
        }

        int anInt = Integer.parseInt(day) - 14;
        String date = month + "月" + anInt + "日";
        Row sheetRow3 = sheet1.createRow(manageRowIndex);
        Cell row3Cell = sheetRow3.createCell(0);
        row3Cell.setCellValue("序号");
        // sheet1.setColumnWidth(0, 5 * 256);
        row3Cell.setCellStyle(songTi12);
        PoiUtils.mergeCell(sheet1, 2, 3, 0, 0);


        if (k != 0) {
            Cell row3Cell1 = sheetRow3.createCell(1);
            row3Cell1.setCellValue("单位");
            sheet1.setColumnWidth(1, 15 * 256);
            row3Cell1.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 1, 2);

            Cell cell = sheetRow3.createCell(3);
            cell.setCellValue("角色");
            sheet1.setColumnWidth(1, 15 * 256);
            cell.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 3, 3);

            Cell row3Cell3 = sheetRow3.createCell(4);
            row3Cell3.setCellValue("姓名");
            sheet1.setColumnWidth(3, 7 * 256);
            row3Cell3.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 4, 4);

            Cell sheetRow3Cell = sheetRow3.createCell(5);
            sheetRow3Cell.setCellValue("体温状况");
            sheet1.setColumnWidth(4, 7 * 256);
            sheetRow3Cell.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 5, 5);

            Cell sheetRow3Cell1 = sheetRow3.createCell(6);
            sheetRow3Cell1.setCellValue("基本情况有无变化");
            sheet1.setColumnWidth(5, 9 * 256);
            sheetRow3Cell1.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 6, 6);

            //异常血液检测、核酸检测
            Cell sheetRow3Cell17 = sheetRow3.createCell(7);
            sheetRow3Cell17.setCellValue("血液检测");
            sheet1.setColumnWidth(7, 6 * 256);
            sheetRow3Cell17.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 7, 7);

            Cell sheetRow3Cell20 = sheetRow3.createCell(8);
            sheetRow3Cell20.setCellValue("血液检测时间");
            sheet1.setColumnWidth(8, 6 * 256);
            sheetRow3Cell20.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 8, 8);

            Cell sheetRow3Cell18 = sheetRow3.createCell(9);
            sheetRow3Cell18.setCellValue("核酸检测");
            sheet1.setColumnWidth(9, 6 * 256);
            sheetRow3Cell18.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 9, 9);

            Cell sheetRow3Cell21 = sheetRow3.createCell(10);
            sheetRow3Cell21.setCellValue("核酸检测时间");
            sheet1.setColumnWidth(10, 6 * 256);
            sheetRow3Cell21.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 10, 10);

            Cell sheetRow3Cell19 = sheetRow3.createCell(11);
            sheetRow3Cell19.setCellValue("今日到岗情况");
            sheet1.setColumnWidth(11, 6 * 256);
            sheetRow3Cell19.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 11, 11);


            Cell sheetRow3Cell2 = sheetRow3.createCell(12);
            sheetRow3Cell2.setCellValue("年龄");
            sheet1.setColumnWidth(12, 7 * 256);
            sheetRow3Cell2.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 12, 12);

            Cell sheetRow3Cell3 = sheetRow3.createCell(13);
            sheetRow3Cell3.setCellValue("手机号码");
            sheet1.setColumnWidth(13, 7 * 256);
            sheetRow3Cell3.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 13, 13);

            Cell sheetRow3Cell4 = sheetRow3.createCell(14);
            sheetRow3Cell4.setCellValue("身份证号码");
            sheet1.setColumnWidth(14 ,9 * 256);
            sheetRow3Cell4.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 14, 14);

            Cell sheetRow3Cell5 = sheetRow3.createCell(15);
            sheetRow3Cell5.setCellValue("个人体温");
            //  sheet1.setColumnWidth(9,9*256);
            sheetRow3Cell5.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 2, 15, 17);

            Cell sheetRow3Cell6 = sheetRow3.createCell(18);
            sheetRow3Cell6.setCellValue("目前状态");
            sheet1.setColumnWidth(16, 14 * 256);
            sheetRow3Cell6.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 18, 18);

            Cell sheetRow3Cell7 = sheetRow3.createCell(19);
            sheetRow3Cell7.setCellValue("来穗时间");
            sheet1.setColumnWidth(19, 13 * 256);
            sheetRow3Cell7.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 19, 19);


            Cell sheetRow3Cell8 = sheetRow3.createCell(20);
            sheetRow3Cell8.setCellValue("应隔离完成日期");
            sheet1.setColumnWidth(20, 11 * 256);
            sheetRow3Cell8.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 20, 20);

            Cell sheetRow3Cell9 = sheetRow3.createCell(21);
            sheetRow3Cell9.setCellValue(time + "居住的省市区");
            sheet1.setColumnWidth(21, 15 * 256);
            sheetRow3Cell9.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 21, 21);


            Cell sheetRow3Cell10 = sheetRow3.createCell(22);
            sheetRow3Cell10.setCellValue(time + "居住详细地址");
            sheet1.setColumnWidth(22, 11 * 256);
            sheetRow3Cell10.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 22, 22);

            Cell sheetRow3Cell11 = sheetRow3.createCell(23);
            sheetRow3Cell11.setCellValue(date + "起至今是否外出离开日常工作城市（如有，请填写行程时间、地点、交通方式、航班车次）");
            sheet1.setColumnWidth(23, 25 * 256);
            sheetRow3Cell11.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 23, 23);


            Cell sheetRow3Cell12 = sheetRow3.createCell(24);
            sheetRow3Cell12.setCellValue(date + "起至今14天内有否与确诊或疑似病例接触史（如有，请填写接触时间、地点）");
            sheet1.setColumnWidth(24, 33 * 256);
            sheetRow3Cell12.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 24, 24);


            Cell sheetRow3Cell13 = sheetRow3.createCell(25);
            sheetRow3Cell13.setCellValue(date + "起至今14天内有无经过湖北或其他有疫情严重的地区（如有，请填写时间、地区、交通方式、航班车次）");
            sheet1.setColumnWidth(25, 23 * 256);
            sheetRow3Cell13.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 25, 25);


            Cell sheetRow3Cell14 = sheetRow3.createCell(26);
            sheetRow3Cell14.setCellValue(date + "起至今14天内有无与湖北等地区人员接触史（如有，请填写接触时间、地点）");
            sheet1.setColumnWidth(26, 19 * 256);
            sheetRow3Cell14.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 26, 26);

            Cell sheetRow3Cell15 = sheetRow3.createCell(27);
            sheetRow3Cell15.setCellValue("个人健康状况（如有问题请填写时间、隔离地点）");
            sheet1.setColumnWidth(27, 11 * 256);
            sheetRow3Cell15.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 27, 27);


            Cell sheetRow3Cell16 = sheetRow3.createCell(28);
            sheetRow3Cell16.setCellValue("备注");
            sheet1.setColumnWidth(28, 11 * 256);
            sheetRow3Cell16.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 28, 28);
        } else {
            Cell row3Cell1 = sheetRow3.createCell(1);
            row3Cell1.setCellValue("单位");
            sheet1.setColumnWidth(1, 15 * 256);
            row3Cell1.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 1, 1);

            Cell row3Cell2 = sheetRow3.createCell(2);
            row3Cell2.setCellValue("所属分部-工区");
            sheet1.setColumnWidth(2, 15 * 256);
            row3Cell2.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 2, 2);

            Cell row3Cell3 = sheetRow3.createCell(3);
            row3Cell3.setCellValue("姓名");
            sheet1.setColumnWidth(3, 6* 256);
            row3Cell3.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 3, 3);

            Cell sheetRow3Cell = sheetRow3.createCell(4);
            sheetRow3Cell.setCellValue("体温状况");
            sheet1.setColumnWidth(4, 6 * 256);
            sheetRow3Cell.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 4, 4);

            Cell sheetRow3Cell1 = sheetRow3.createCell(5);
            sheetRow3Cell1.setCellValue("基本情况有无变化");
            sheet1.setColumnWidth(5, 6 * 256);
            sheetRow3Cell1.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 5, 5);

            //管理血液检测、核酸检测
            Cell sheetRow3Cell17 = sheetRow3.createCell(6);
            sheetRow3Cell17.setCellValue("血液检测");
            sheet1.setColumnWidth(6, 6 * 256);
            sheetRow3Cell17.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 6, 6);

            Cell sheetRow3Cell20 = sheetRow3.createCell(7);
            sheetRow3Cell20.setCellValue("血液检测时间");
            sheet1.setColumnWidth(7, 6 * 256);
            sheetRow3Cell20.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 7, 7);

            Cell sheetRow3Cell21 = sheetRow3.createCell(8);
            sheetRow3Cell21.setCellValue("核酸检测");
            sheet1.setColumnWidth(8, 6 * 256);
            sheetRow3Cell21.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 8, 8);

            Cell sheetRow3Cell18 = sheetRow3.createCell(9);
            sheetRow3Cell18.setCellValue("核酸检测时间");
            sheet1.setColumnWidth(9, 6 * 256);
            sheetRow3Cell18.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 9, 9);

            Cell sheetRow3Cell19 = sheetRow3.createCell(10);
            sheetRow3Cell19.setCellValue("今日到岗情况");
            sheet1.setColumnWidth(10, 6 * 256);
            sheetRow3Cell19.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 10, 10);

            Cell sheetRow3Cell2 = sheetRow3.createCell(11);
            sheetRow3Cell2.setCellValue("年龄");
            sheet1.setColumnWidth(11, 5 * 256);
            sheetRow3Cell2.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 11, 11);

            Cell sheetRow3Cell3 = sheetRow3.createCell(12);
            sheetRow3Cell3.setCellValue("手机号码");
            sheet1.setColumnWidth(12, 7 * 256);
            sheetRow3Cell3.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 12, 12);

            Cell sheetRow3Cell4 = sheetRow3.createCell(13);
            sheetRow3Cell4.setCellValue("身份证号码");
            sheet1.setColumnWidth(13, 9 * 256);
            sheetRow3Cell4.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 13, 13);

            Cell sheetRow3Cell5 = sheetRow3.createCell(14);
            sheetRow3Cell5.setCellValue("个人体温");
            //  sheet1.setColumnWidth(9,9*256);
            sheetRow3Cell5.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 2, 14, 16);

            Cell sheetRow3Cell6 = sheetRow3.createCell(17);
            sheetRow3Cell6.setCellValue("目前状态");
            sheet1.setColumnWidth(17, 14 * 256);
            sheetRow3Cell6.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 17 ,17);

            Cell sheetRow3Cell7 = sheetRow3.createCell(18);
            sheetRow3Cell7.setCellValue("来穗时间");
            sheet1.setColumnWidth(18, 13 * 256);
            sheetRow3Cell7.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 18, 18);


            Cell sheetRow3Cell8 = sheetRow3.createCell(19);
            sheetRow3Cell8.setCellValue("应隔离完成日期");
            sheet1.setColumnWidth(19, 11 * 256);
            sheetRow3Cell8.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 19, 19);

            Cell sheetRow3Cell9 = sheetRow3.createCell(20);
            sheetRow3Cell9.setCellValue(time + "居住的省市区");
            sheet1.setColumnWidth(20, 15 * 256);
            sheetRow3Cell9.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 20, 20);


            Cell sheetRow3Cell10 = sheetRow3.createCell(21);
            sheetRow3Cell10.setCellValue(time + "居住详细地址");
            sheet1.setColumnWidth(21, 11 * 256);
            sheetRow3Cell10.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 21, 21);

            Cell sheetRow3Cell11 = sheetRow3.createCell(22);
            sheetRow3Cell11.setCellValue(date + "起至今是否外出离开日常工作城市（如有，请填写行程时间、地点、交通方式、航班车次）");
            sheet1.setColumnWidth(22, 25 * 256);
            sheetRow3Cell11.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 22, 22);


            Cell sheetRow3Cell12 = sheetRow3.createCell(23);
            sheetRow3Cell12.setCellValue(date + "起至今14天内有否与确诊或疑似病例接触史（如有，请填写接触时间、地点）");
            sheet1.setColumnWidth(23, 33 * 256);
            sheetRow3Cell12.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 23, 23);


            Cell sheetRow3Cell13 = sheetRow3.createCell(24);
            sheetRow3Cell13.setCellValue(date + "起至今14天内有无经过湖北或其他有疫情严重的地区（如有，请填写时间、地区、交通方式、航班车次）");
            sheet1.setColumnWidth(24, 23 * 256);
            sheetRow3Cell13.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 24, 24);


            Cell sheetRow3Cell14 = sheetRow3.createCell(25);
            sheetRow3Cell14.setCellValue(date + "起至今14天内有无与湖北等地区人员接触史（如有，请填写接触时间、地点）");
            sheet1.setColumnWidth(25, 19 * 256);
            sheetRow3Cell14.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 25, 25);

            Cell sheetRow3Cell15 = sheetRow3.createCell(26);
            sheetRow3Cell15.setCellValue("个人健康状况（如有问题请填写时间、隔离地点）");
            sheet1.setColumnWidth(26, 20 * 256);
            sheetRow3Cell15.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 26, 26);


            Cell sheetRow3Cell16 = sheetRow3.createCell(27);
            sheetRow3Cell16.setCellValue("备注");
            sheet1.setColumnWidth(27, 11 * 256);
            sheetRow3Cell16.setCellStyle(songTi12);
            PoiUtils.mergeCell(sheet1, 2, 3, 27 ,27);
        }


        return sheetRow3;
    }


    public static void getmergeCell(Row row, HSSFCellStyle weiRuan, HSSFSheet sheet, HSSFWorkbook work, int rowNum) {
        for (int i = 0; i <= 9; i++) {
            Cell rowCell = row.getCell(i + 2);
            if (rowCell != null) {
                PoiUtils.mergeForm(sheet, work, rowNum, rowNum, i + 2, i + 2);
            } else {
                Cell cell = row.createCell(i + 2);
                cell.setCellValue("");
                cell.setCellStyle(weiRuan);
                PoiUtils.mergeForm(sheet, work, rowNum, rowNum, i + 2, i + 2);
            }
        }
    }

    public static void getmergeCellList(RowAndNum rowAndNum, HSSFCellStyle weiRuan, HSSFSheet sheet, HSSFWorkbook work, int rowNum) {
        for (Row row : rowAndNum.getRowList()) {
            for (int i = 0; i <= 9; i++) {
                Cell rowCell = row.getCell(i + 2);
                if (rowCell != null) {
                    PoiUtils.mergeForm(sheet, work, row.getRowNum(), row.getRowNum(), i + 2, i + 2);
                } else {
                    Cell cell = row.createCell(i + 2);
                    cell.setCellValue("");
                    cell.setCellStyle(weiRuan);
                    PoiUtils.mergeForm(sheet, work, row.getRowNum(), row.getRowNum(), i + 2, i + 2);
                }
            }
        }

    }


    public static RowAndNum getColumnDetail(int startRowIndex, HSSFWorkbook work, HSSFSheet sheet, String orgName, String[] strings, HSSFCellStyle weiRuan, SumTable sumTable, int k, HSSFCellStyle red10, HSSFCellStyle orange10) {
        RowAndNum rowAndNum = new RowAndNum();
        List<Row> rowList = new ArrayList<>();
        //分部
        Row row = sheet.createRow(startRowIndex);
        Cell cell = row.createCell(0);
        cell.setCellValue(orgName);
        cell.setCellStyle(weiRuan);
        PoiUtils.mergeCell(sheet, startRowIndex, startRowIndex + strings.length - 1, 0, 0);
        rowList.add(row);

        Cell cell1 = row.createCell(1);
        cell1.setCellValue(strings[0]);
        cell1.setCellStyle(weiRuan);
        if (1 == 1) {
            AlarmCalculate managerStatistics = sumTable.getList().get(k).getNextChild().get(0).getManagerStatistics();
            AlarmCalculate labourStatistics = sumTable.getList().get(k).getNextChild().get(0).getLabourStatistics();

            Cell row6Cell = row.createCell(2);
            row6Cell.setCellValue(managerStatistics.getInIso());
            row6Cell.setCellStyle(weiRuan);
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 2, 2);

            Cell row6Cell1 = row.createCell(3);
            row6Cell1.setCellValue(managerStatistics.getOverIso());
            row6Cell1.setCellStyle(weiRuan);
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 3, 3);


            Cell row6Cell2 = row.createCell(4);
            row6Cell2.setCellValue(managerStatistics.getInArea());
            row6Cell2.setCellStyle(weiRuan);
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 4, 4);

            Cell row6Cell3 = row.createCell(5);
            row6Cell3.setCellValue(managerStatistics.getTempAbnormal());
            row6Cell3.setCellStyle(weiRuan);
            if (managerStatistics.getTempAbnormal() != 0) {
                PoiUtils.setBackColor(work, row6Cell3);
            }
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 5, 5);

            Cell row6Cell4 = row.createCell(6);
            row6Cell4.setCellValue(managerStatistics.getNoReport());
            row6Cell4.setCellStyle(weiRuan);
            if (managerStatistics.getNoReport() != 0) {
                row6Cell3.setCellStyle(orange10);
            }
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 6, 6);

            //血液检测、核酸检测
            Cell row6Cell12 = row.createCell(7);
            if (managerStatistics != null) {
                row6Cell12.setCellValue(managerStatistics.getBloodAlarm());
                row6Cell12.setCellStyle(weiRuan);
                if (managerStatistics.getBloodAlarm()!= 0) {
                    row6Cell12.setCellStyle(red10);
                    PoiUtils.setBackColor(work, row6Cell12);
                }
            }else {
                row6Cell12.setCellValue("0");
                row6Cell12.setCellStyle(weiRuan);
            }
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 7, 7);

            Cell row6Cell13 = row.createCell(8);
            if (managerStatistics != null) {
                row6Cell13.setCellValue(managerStatistics.getNucleaseAlarm());
                row6Cell13.setCellStyle(weiRuan);
                if (managerStatistics.getNucleaseAlarm()!= 0) {
                    row6Cell13.setCellStyle(red10);
                    PoiUtils.setBackColor(work, row6Cell13);
                }
            }else {
                row6Cell13.setCellValue("0");
                row6Cell13.setCellStyle(weiRuan);
            }

            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 8, 8);

            Cell row6Cell5 = row.createCell(9);
            row6Cell5.setCellValue(labourStatistics.getInIso());
            row6Cell5.setCellStyle(weiRuan);
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 9, 9);

            Cell row6Cell6 = row.createCell(10);
            row6Cell6.setCellValue(labourStatistics.getOverIso());
            row6Cell6.setCellStyle(weiRuan);
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 10, 10);

            Cell row6Cell7 = row.createCell(11);
            row6Cell7.setCellValue(labourStatistics.getInArea());
            row6Cell7.setCellStyle(weiRuan);
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 11, 11);

            Cell row6Cell8 = row.createCell(12);
            row6Cell8.setCellValue(labourStatistics.getTempAbnormal());
            row6Cell8.setCellStyle(weiRuan);
            if (labourStatistics.getTempAbnormal() != 0) {
                PoiUtils.setBackColor(work, row6Cell8);
            }
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 12, 12);

            Cell row6Cell9 = row.createCell(13);
            row6Cell9.setCellValue(labourStatistics.getNoReport());
            row6Cell9.setCellStyle(weiRuan);
            if (labourStatistics.getNoReport() != 0) {
                row6Cell9.setCellStyle(orange10);
            }
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 13, 13);

            //血液检测、核酸检测
            Cell row8Cell = row.createCell(14);
            if (labourStatistics != null) {
                row8Cell.setCellValue(labourStatistics.getBloodAlarm());
                row8Cell.setCellStyle(weiRuan);
                if (labourStatistics.getBloodAlarm()!= 0) {
                    row8Cell.setCellStyle(red10);
                    PoiUtils.setBackColor(work, row8Cell);
                }
            }else {
                row8Cell.setCellValue("0");
                row8Cell.setCellStyle(weiRuan);

            }
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 14, 14);

            Cell row8Cell1 = row.createCell(15);
            if (labourStatistics != null) {
                row8Cell1.setCellValue(labourStatistics.getNucleaseAlarm());
                row8Cell1.setCellStyle(weiRuan);
                if (labourStatistics.getNucleaseAlarm()!= 0) {
                    row8Cell1.setCellStyle(red10);
                    PoiUtils.setBackColor(work, row8Cell1);
                }
            }else {
                row8Cell1.setCellValue("0");
                row8Cell1.setCellStyle(weiRuan);

            }
            PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 15, 15);
        }

        startRowIndex++;

        //工区
        for (int i = 1; i < strings.length; i++) {
            Row sheetRow = sheet.createRow(startRowIndex);
            Cell rowCell = sheetRow.createCell(1);
            rowCell.setCellValue(strings[i]);
            rowCell.setCellStyle(weiRuan);
            if (1 == 1) {
                AlarmCalculate managerStatistics = sumTable.getList().get(k).getNextChild().get(i).getManagerStatistics();
                AlarmCalculate labourStatistics = sumTable.getList().get(k).getNextChild().get(i).getLabourStatistics();

                Cell row6Cell = sheetRow.createCell(2);
                row6Cell.setCellValue(managerStatistics.getInIso());
                row6Cell.setCellStyle(weiRuan);
                PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 2, 2);

                Cell row6Cell1 = sheetRow.createCell(3);
                row6Cell1.setCellValue(managerStatistics.getOverIso());
                row6Cell1.setCellStyle(weiRuan);
                PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 3, 3);


                Cell row6Cell2 = sheetRow.createCell(4);
                row6Cell2.setCellValue(managerStatistics.getInArea());
                row6Cell2.setCellStyle(weiRuan);
                PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 4, 4);

                Cell row6Cell3 = sheetRow.createCell(5);
                row6Cell3.setCellValue(managerStatistics.getTempAbnormal());
                row6Cell3.setCellStyle(weiRuan);
                if (managerStatistics.getTempAbnormal() != 0) {
                    PoiUtils.setBackColor(work, row6Cell3);
                }
                PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 5, 5);

                Cell row6Cell4 = sheetRow.createCell(6);
                row6Cell4.setCellValue(managerStatistics.getNoReport());
                row6Cell4.setCellStyle(weiRuan);
                if (managerStatistics.getNoReport() != 0) {
                    row6Cell4.setCellStyle(orange10);
                }
                PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 6, 6);

                //血液检测、核酸检测
                Cell row6Cell12 = sheetRow.createCell(7);
                if (managerStatistics != null) {
                    row6Cell12.setCellValue(managerStatistics.getBloodAlarm());
                    row6Cell12.setCellStyle(weiRuan);
                    if (managerStatistics.getBloodAlarm()!= 0) {
                        row6Cell12.setCellStyle(red10);
                        PoiUtils.setBackColor(work, row6Cell12);
                    }
                }else {
                    row6Cell12.setCellValue("0");
                    row6Cell12.setCellStyle(weiRuan);
                }
                PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 7, 7);

                Cell row6Cell13 = sheetRow.createCell(8);
                if (managerStatistics != null) {
                    row6Cell13.setCellValue(managerStatistics.getNucleaseAlarm());
                    row6Cell13.setCellStyle(weiRuan);
                    if (managerStatistics.getNucleaseAlarm()!= 0) {
                        row6Cell13.setCellStyle(red10);
                        PoiUtils.setBackColor(work, row6Cell13);
                    }
                }else {
                    row6Cell13.setCellValue("0");
                    row6Cell13.setCellStyle(weiRuan);
                }

                Cell row6Cell5 = sheetRow.createCell(9);
                row6Cell5.setCellValue(labourStatistics.getInIso());
                row6Cell5.setCellStyle(weiRuan);
                PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 9, 9);

                Cell row6Cell6 = sheetRow.createCell(10);
                row6Cell6.setCellValue(labourStatistics.getOverIso());
                row6Cell6.setCellStyle(weiRuan);
                PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 10, 10);

                Cell row6Cell7 = sheetRow.createCell(11);
                row6Cell7.setCellValue(labourStatistics.getInArea());
                row6Cell7.setCellStyle(weiRuan);
                PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 11, 11);

                Cell row6Cell8 = sheetRow.createCell(12);
                row6Cell8.setCellValue(labourStatistics.getTempAbnormal());
                row6Cell8.setCellStyle(weiRuan);
                if (labourStatistics.getTempAbnormal() != 0) {
                    PoiUtils.setBackColor(work, row6Cell8);
                }
                PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 12, 12);

                Cell row6Cell9 = sheetRow.createCell(13);
                row6Cell9.setCellValue(labourStatistics.getNoReport());
                row6Cell9.setCellStyle(weiRuan);
                if (labourStatistics.getNoReport() != 0) {
                    row6Cell9.setCellStyle(orange10);
                }
                PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 13, 13);

                //血液检测、核酸检测
                Cell row8Cell = sheetRow.createCell(14);
                if (labourStatistics != null) {
                    row8Cell.setCellValue(labourStatistics.getBloodAlarm());
                    row8Cell.setCellStyle(weiRuan);
                    if (labourStatistics.getBloodAlarm()!= 0) {
                        row8Cell.setCellStyle(red10);
                        PoiUtils.setBackColor(work, row8Cell);
                    }
                }else {
                    row8Cell.setCellValue("0");
                    row8Cell.setCellStyle(weiRuan);
                }
                PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 14, 14);

                Cell row8Cell1 = sheetRow.createCell(15);
                if (labourStatistics != null) {
                    row8Cell1.setCellValue(labourStatistics.getNucleaseAlarm());
                    row8Cell1.setCellStyle(weiRuan);
                    if (labourStatistics.getNucleaseAlarm()!= 0) {
                        row8Cell1.setCellStyle(red10);
                        PoiUtils.setBackColor(work, row8Cell1);
                    }
                }else {
                    row8Cell1.setCellValue("0");
                    row8Cell1.setCellStyle(weiRuan);
                }

                PoiUtils.mergeForm(sheet, work, startRowIndex, startRowIndex, 15, 15);

            }
            rowList.add(sheetRow);
            startRowIndex++;
        }

        rowAndNum.setRowList(rowList);
        rowAndNum.setStartRowIndex(startRowIndex);
        return rowAndNum;
    }

    public static String changeDate(Date date){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String format = sf.format(date);

        return format.substring(0, 10);
    }


    @Data
    public static class RowAndNum implements Serializable {

        private List<Row> rowList;
        private int startRowIndex;
    }

    @Data
    public static class OrgVo implements Serializable {

        private String name;
        private List<ChildOrgVO> childOrgVOList;
    }

    @Data
    public static class ChildOrgVO implements Serializable {
        private String name;
    }
}