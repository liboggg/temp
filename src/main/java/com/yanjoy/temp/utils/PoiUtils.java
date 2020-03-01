package com.yanjoy.temp.utils;

import org.apache.poi.common.usermodel.Hyperlink;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.springframework.util.StringUtils;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author 95
 * @create 2019/11/5
 */
public class PoiUtils {

    public static void write(HttpServletResponse response, Object o) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.println(o.toString());
        out.flush();
        out.close();
    }

    /**
     * 导出Excel
     * @param response  HttpServletResponse
     * @param wb HSSFWorkbook
     * @param fileName fileName
     * @throws IOException io
     */
    public static void export(HttpServletResponse response, HSSFWorkbook wb, String fileName) throws IOException {
        response.setHeader("Context-Disposition",
                "attachment;filename" + new String(fileName.getBytes(StandardCharsets.UTF_8),"iso8859-1"));
        response.setContentType("application/ynd.ms-excel;charset=UTF-8");
        OutputStream outputStream = response.getOutputStream();
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }


    /**
     * 读取模板写入返回 Workbook
     * @param results 需要写入的数据（此处模板值使用需重新封装）
     * @param templateFileName 模板名
     * @return Workbook
     * @throws IOException io异常
     */
    public static Workbook fillExcelDataWithTemplate(List<String> results, String templateFileName) throws IOException {
        //模板相对路径
        InputStream inputStream = PoiUtils.class.getResourceAsStream("com/lb/template/" + templateFileName);
        POIFSFileSystem poifsFileSystem = new POIFSFileSystem(inputStream);
        Workbook workbook = new HSSFWorkbook(poifsFileSystem);
        Sheet sheet = workbook.getSheetAt(0);
        //获取列数
        int cellNums = sheet.getRow(0).getLastCellNum();
        int rowIndex = 1;
        //写入值
        for (String result : results) {
            Row row = sheet.createRow(rowIndex++);
            for (int i = 0; i < cellNums; i++) {
                row.createCell(i).setCellValue(result);
            }
        }
        return workbook;
    }

    /**
     * 格式化单元格值
     * @param hssfCell 单元格
     * @return 读取的单元格格式化后的值
     */
    public static String cellFormat(HSSFCell hssfCell){
        if(hssfCell == null){
            return  "";
        }else {
            if(hssfCell.getCellStyle().getIndex() == HSSFCell.CELL_TYPE_BOOLEAN){
                return String.valueOf(hssfCell.getBooleanCellValue());
            }else if(hssfCell.getCellStyle().getIndex() == HSSFCell.CELL_TYPE_NUMERIC){
                return String.valueOf(hssfCell.getNumericCellValue());
            }else if (hssfCell.getCellStyle().getIndex() == HSSFCell.CELL_TYPE_STRING){
                return String.valueOf(hssfCell.getStringCellValue());
            }
        }
        return "";
    }

    /**
     * 创建一个单元格并为其设定指定的对齐方式
     * @param wb 工作簿
     * @param row 行
     * @param column 列
     * @param halign 单元格水平方向对齐方式
     * @param valign 单元格垂直方向对齐方式
     * @param value 值
     */
    public static void createCell(Workbook wb, Row row, short column, short halign, short valign, String value){
        //创建单元格
        Cell cell = row.createCell(column);
        //设置值（富文本字体）
        cell.setCellValue(new HSSFRichTextString(value));
        //创建单元格样式
        CellStyle cellStyle = wb.createCellStyle();
        //设置单元格水平方向对齐方式
        cellStyle.setAlignment(halign);
        //设置单元格垂直方向对齐方式
        cellStyle.setVerticalAlignment(valign);
        //设置单元格样式
        cell.setCellStyle(cellStyle);
    }

    /**
     * 去掉poi导入包含的点
     * @param string string
     * @return string
     */
    public static String delPoint(String string){
        if(StringUtils.isEmpty(string)){
            return string;
        }
        int i = string.indexOf(".");
        if(i > 0){
            string = string.substring(0,i);
        }
        return string;
    }

    /**
     *普通字体垂直居中
     * @param workbook HSSFWorkbook
     * @return HSSFCellStyle
     */
    public static HSSFCellStyle getDefaultFontAndCenter(HSSFWorkbook workbook){
        HSSFCellStyle bigFontStyle = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 18);//设置字体大小
        bigFontStyle.setFont(font);
        bigFontStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        bigFontStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        bigFontStyle.setWrapText(true);//自动换行
        return bigFontStyle;
    }

    public static HSSFCellStyle getWeiRruan(HSSFWorkbook workbook){
        HSSFCellStyle bigFontStyle = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setFontName("微软雅黑");
        font.setFontHeightInPoints((short) 12);//设置字体大小
        bigFontStyle.setFont(font);
        bigFontStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        bigFontStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        bigFontStyle.setWrapText(true);//自动换行
        return bigFontStyle;
    }

    public static HSSFCellStyle getWeiRruan10(HSSFWorkbook workbook){
        HSSFCellStyle bigFontStyle = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setFontName("微软雅黑");
        font.setFontHeightInPoints((short) 10);//设置字体大小
        bigFontStyle.setFont(font);
        bigFontStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        bigFontStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        bigFontStyle.setWrapText(true);//自动换行
        return bigFontStyle;
    }

    public static HSSFCellStyle getred10(HSSFWorkbook workbook){
        HSSFCellStyle bigFontStyle = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setFontName("微软雅黑");
        font.setColor(HSSFColor.RED.index);
        font.setFontHeightInPoints((short) 10);//设置字体大小
        bigFontStyle.setFont(font);
        bigFontStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        bigFontStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        bigFontStyle.setWrapText(true);//自动换行
        return bigFontStyle;
    }

    public static HSSFCellStyle getOrange10(HSSFWorkbook workbook){
        HSSFCellStyle bigFontStyle = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setFontName("微软雅黑");
        font.setColor(HSSFColor.ORANGE.index);
        font.setFontHeightInPoints((short) 10);//设置字体大小
        bigFontStyle.setFont(font);
        bigFontStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        bigFontStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        bigFontStyle.setWrapText(true);//自动换行
        return bigFontStyle;
    }

    public static HSSFCellStyle getSongTiNo(HSSFWorkbook workbook){
        HSSFCellStyle bigFontStyle = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 12);//设置字体大小
        bigFontStyle.setFont(font);
        bigFontStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        bigFontStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        bigFontStyle.setWrapText(true);//自动换行
        return bigFontStyle;
    }

    public static HSSFCellStyle getSongTiLeft(HSSFWorkbook workbook){
        HSSFCellStyle bigFontStyle = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 12);//设置字体大小
        bigFontStyle.setFont(font);
        //bigFontStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        bigFontStyle.setVerticalAlignment(HSSFCellStyle.ALIGN_LEFT);//靠左
        bigFontStyle.setWrapText(true);//自动换行
        return bigFontStyle;
    }


    public static HSSFCellStyle getSongTi(HSSFWorkbook workbook){
        HSSFCellStyle bigFontStyle = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 12);//设置字体大小
        bigFontStyle.setFont(font);
        bigFontStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        bigFontStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        bigFontStyle.setWrapText(true);//自动换行
        return bigFontStyle;
    }

    public static void setBackColor(HSSFWorkbook workbook,Cell cell){

        CellStyle style = workbook.createCellStyle();
        style = workbook.createCellStyle();
        HSSFPalette customPalette = workbook.getCustomPalette();
        customPalette.setColorAtIndex(HSSFColor.GREY_25_PERCENT.index, (byte) 217, (byte) 217, (byte) 217);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        HSSFFont font = workbook.createFont();
        font.setFontName("微软雅黑");
        font.setColor(HSSFColor.RED.index);
        font.setFontHeightInPoints((short) 10);//设置字体大小
        style.setFont(font);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        style.setWrapText(true);//自动换行
        cell.setCellStyle(style);

    }

    /**
     * 创建超链接单元格
     * @param hssfWorkbook 工作簿
     * @param row 行
     * @fromCellNum 超链接列Index
     * @param toSheetAddress 被连接的sheet名  "#"表示本文档  "明细页面"表示sheet页名称  "A10"表示第A列第10行
     * @param fromName 单元格显示的链接名
     *
     */
    public static void createLink(HSSFWorkbook hssfWorkbook, HSSFRow row,
                                  Integer fromCellNum,
                                  String toSheetAddress,
                                  String fromName
    ){
        /* 连接跳转*/
        HSSFCell linkCell = row.createCell(fromCellNum);
        HSSFHyperlink hssfHyperlink = new HSSFHyperlink(Hyperlink.LINK_DOCUMENT);
        // "#"表示本文档    "明细页面"表示sheet页名称  "A10"表示第几列第几行
        // hssfHyperlink.setAddress("#明细页面!A10");
        hssfHyperlink.setAddress(toSheetAddress);
        // 点击进行跳转
        linkCell.setHyperlink(hssfHyperlink);
        linkCell.setCellValue(fromName);

        /* 设置为超链接的样式*/
        HSSFCellStyle linkStyle = hssfWorkbook.createCellStyle();
        HSSFFont cellFont= hssfWorkbook.createFont();
        cellFont.setUnderline(Font.U_NONE);
        cellFont.setColor(HSSFColor.BLUE.index);
        linkStyle.setFont(cellFont);
        linkStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        linkStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        linkCell.setCellStyle(linkStyle);

    }

    /**
     * 给单元格设置边框
     * @param sheet
     * @param wb
     * @param firstRow
     * @param lastRow
     * @param firstCol
     * @param lastCol
     * @throws Exception
     */
    public static void mergeForm(Sheet sheet, Workbook wb, int firstRow, int lastRow, int firstCol, int lastCol){
        RegionUtil.setBorderLeft(1, new CellRangeAddress(firstRow,lastRow,firstCol,lastCol), sheet, wb);
        RegionUtil.setBorderBottom(1, new CellRangeAddress(firstRow,lastRow,firstCol,lastCol), sheet, wb);
        RegionUtil.setBorderRight(1, new CellRangeAddress(firstRow,lastRow,firstCol,lastCol), sheet, wb);
        RegionUtil.setBorderTop(1, new CellRangeAddress(firstRow,lastRow,firstCol,lastCol), sheet, wb);
    }


    /**
     *加粗字体垂直居中
     * @param workbook HSSFWorkbook
     * @return HSSFCellStyle
     */
    public static HSSFCellStyle getBigFontAndCenter(HSSFWorkbook workbook){
        HSSFCellStyle bigFontStyle = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 16);//设置字体大小
        bigFontStyle.setFont(font);
        bigFontStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        bigFontStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        bigFontStyle.setWrapText(true);//自动换行
        return bigFontStyle;
    }

    /**
     *加粗字体水平局左
     * @param workbook HSSFWorkbook
     * @return HSSFCellStyle
     */
    public static HSSFCellStyle getBigFontAndLeft(HSSFWorkbook workbook){
        HSSFCellStyle bigFontStyle = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 16);//设置字体大小
        bigFontStyle.setFont(font);
        bigFontStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);//水平居中
        bigFontStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        return bigFontStyle;
    }

    /**
     *
     * @param wb Workbook
     * @param sheet HSSFSheet
     * @param dxLeft 左像素距离
     * @param dyTop 上像素距离
     * @param dxRight 右像素距离
     * @param dyBottom 下像素距离
     * @param cellNumLeftTop 图片的左上角放在第几个列
     * @param rowNumLeftTop 图片的左上角放在第几个行
     * @param cellNumRightBottom 图片的右下角放在第几个列
     * @param rowNumRightBottom 图片的右下角放在第几个行
     * @param imagePath 图片路径
     * @throws IOException io流异常
     */
    public static void importImage(Workbook wb, HSSFSheet sheet,
                                   int dxLeft, int dyTop, int dxRight, int dyBottom,
                                   short cellNumLeftTop,
                                   int rowNumLeftTop,
                                   short cellNumRightBottom,
                                   int rowNumRightBottom,
                                   String imagePath) throws IOException {
        if(!StringUtils.hasText(imagePath)){
            return;
        }
        File file = new File(imagePath);
        if(!file.isFile()){
            return;
        }
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        BufferedImage bufferImg = ImageIO.read(file);
        ImageIO.write(bufferImg, "png", byteArrayOut);
        // 利用HSSFPatriarch将图片写入EXCEL
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        HSSFClientAnchor anchor = new HSSFClientAnchor(dxLeft, dyTop, dxRight, dyBottom,
                (short) cellNumLeftTop, rowNumLeftTop, (short) cellNumRightBottom, rowNumRightBottom);
        // 插入图片
        patriarch.createPicture(anchor, wb.addPicture(byteArrayOut
                .toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG));
    }


    /**
     * 合并表格
     * @param sheet HSSFSheet
     * @param firstRow firstRow 要合并的第一行行数
     * @param lastRow lastRow  要合并的最后一行行数
     * @param firstCol firstCol 要合并的第一列列数
     * @param lastCol lastCol 要合并的最后一列列数
     */
    public static void mergeCell(HSSFSheet sheet, int firstRow, int lastRow, int firstCol, int lastCol){
        sheet.addMergedRegion(new CellRangeAddress(
                firstRow,  //起始行
                lastRow,  //结束行
                firstCol,  //起始列
                lastCol  //结束列
        ));
    }

    /**
     * 创建表格
     * @param sheet HSSFSheet
     * @param rowSize 行数
     * @param cellSize 列数
     * @param rowStartIndex 起始行下标
     */
    public  static void createCol(HSSFSheet sheet, Integer rowSize, Integer cellSize, Integer rowStartIndex){
        for (int i = 0; i < rowSize; i++) {
            HSSFRow row = sheet.createRow(rowStartIndex + i);
            for (int j = 0; j < cellSize; j++) {
                row.createCell(j);
            }
        }
    }

    /**
     * 替换字符
     * @param name 需要替换的字符
     * @param replaceLength 前多少位
     * @return  String
     */
    public static String changeChar (String name,Integer replaceLength){
        if(!StringUtils.hasText(name)){
            return "";
        }
        char [] bytes = name.toCharArray();
        if(bytes.length > replaceLength) {
            for (int i = 0; i < replaceLength; i++) {
                bytes[i] = "*".toCharArray()[0];
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (char aByte : bytes) {
            stringBuilder.append(aByte);
        }
        return stringBuilder.toString();
    }

    /**
     * 格式化0.0
     * @param string
     * @return
     */
    public static String format00(String string) {
        if(!StringUtils.hasText(string)){
            return "";
        }
        if ("0.0".equals(string)) {
            return "0";
        }
        return string;
    }


    /**
     * web导出Excel
     * @param response HttpServletResponse
     * @param workbook HSSFWorkbook
     * @param wbName Excel名
     * @throws IOException
     */
    public static void webExportExcel(HttpServletResponse response, HSSFWorkbook workbook, String wbName) throws IOException {
        response.setHeader("Content-Disposition", "attachment;filename=" +
                new String(wbName.getBytes("gb2312"), "ISO-8859-1") + ".xls");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        out.close();
        response.getOutputStream().write(out.toByteArray());
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }


    /**
     *
     * 判断行是否为空
     */
    public static boolean isRowEmpty(Row row) {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK){
                return false;
            }
        }
        return true;
    }
}
