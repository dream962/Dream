package com.upload.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

import com.upload.data.data.ServerListData;

public class ExcelUtils
{
    public static void main(String[] args) throws Exception
    {
    }

    @SuppressWarnings("deprecation")
    public static HSSFWorkbook exportAllServers(List<ServerListData> servers) throws Exception
    {
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 创建sheet
        HSSFSheet sheet = workbook.createSheet("服务器列表");
        // 合并单元格
        CellRangeAddress address = new CellRangeAddress(0, 0, 0, 23);
        sheet.addMergedRegion(address);

        // 第一行：标题
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        row.setHeightInPoints(25.8f);
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(HSSFColor.YELLOW.index);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setWrapText(true);
        cell.setCellStyle(style);
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(date);
        cell.setCellValue("服务器列表\n(导出日期:" + str + ")");

        // 字体
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setColor(HSSFFont.COLOR_NORMAL);
        font.setFontName("黑体");

        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFont(font);
        style2.setAlignment(HorizontalAlignment.CENTER);
        style2.setWrapText(true);
        
        // 第二行：表头
        List<String> list = getExcelHeader();
        HSSFRow row2 = sheet.createRow(1);
        HSSFCell celli;
        for (int i = 0; i < list.size(); i++)
        {
            celli = row2.createCell(i);
            celli.setCellStyle(style2);
            celli.setCellValue(list.get(i));
            // 列宽自适应
            sheet.autoSizeColumn(i);
        }

        // 填充数据:
        HSSFRow dataRow = null;
        HSSFCell dataCell = null;
        ServerListData data;
        for (int i = 0; i < servers.size(); i++)
        {
            // 创建行
            dataRow = sheet.createRow(i + 2);
            data = servers.get(i);
            // 反射获取数据
            Field[] fields = data.getClass().getDeclaredFields();
            for (int j = 2; j < fields.length; j++)
            {
                dataCell = dataRow.createCell(j - 2);
                String fieldName = fields[j].getName();
                String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method method = data.getClass().getMethod(getMethodName, new Class[] {});
                Object value = method.invoke(data, new Object[] {});
                String textValue = null;
                if (value instanceof Date)
                {
                    Date d = (Date) value;
                    textValue = format.format(d);
                }
                else 
                {
                    textValue = value.toString();
                }
                dataCell.setCellValue(textValue);
                sheet.autoSizeColumn(j - 2);
            }
        }
        return workbook;
    }

    /**
     * 表头数据
     * 
     * @return
     */
    private static List<String> getExcelHeader()
    {
        List<String> list = new ArrayList<>();
        list.add("服务器ID");
        list.add("IP地址");
        list.add("连接端口");
        list.add("TCP端口");
        list.add("WEB端口");
        list.add("服务器类型\n(1-游戏服,2-账户服,3-充值服)");
        list.add("服务器状态\n(0-开启,1-关闭,2-异常)");
        list.add("服务器部署路径");
        list.add("服务器名称");
        list.add("服务器版本");
        list.add("上次更新时间");
        list.add("上次开服时间");
        list.add("数据库部署路径");
        list.add("数据库启动脚本路径");
        list.add("数据库IP");
        list.add("数据库端口");
        list.add("redis缓存部署路径");
        list.add("redis启动脚本路径");
        list.add("redis缓存IP");
        list.add("redis缓存端口");
        list.add("tmux");
        list.add("备注");
        list.add("帐户名");
        list.add("密码");
        return list;
    }
}
