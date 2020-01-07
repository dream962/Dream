/**
 * 
 */
package com.tool.data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tool.code.FieldInfo;

import jxl.Workbook;

public class ExcelFile
{
    /** 第一个sheet页是说明 */
    public static final int START_INDEX = 1;

    /** 文件路径 */
    private String dirPath;

    /** excel 表名 */
    private String excelName;

    public Map<String, ExcelSheet> excelSheets = new HashMap<>();

    public ExcelFile(String dirPath, String excelName)
    {
        try
        {
            this.excelName = excelName;
            this.dirPath = dirPath;
            Workbook book = Workbook.getWorkbook(new File(this.dirPath, this.excelName));

            // 第一个sheet页是说明
            for (int i = START_INDEX; i < book.getNumberOfSheets(); ++i)
            {
                if (book.getSheet(i) != null)
                {
                    ExcelSheet sheet = new ExcelSheet();
                    if (sheet.init(book.getSheet(i)))
                    {
                        excelSheets.put(sheet.getTableName(), sheet);
                    }
                    else
                    {
                        System.err.println(String.format("Sheet : table's name is empty. excel:%s,sheet:%s",
                                excelName, book.getSheet(i).getName()));
                    }
                }
            }

            book.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public List<ExcelSheet> getSheetList()
    {
        List<ExcelSheet> list = new ArrayList<>();
        list.addAll(excelSheets.values());
        return list;
    }

    public List<String> getTableNameList()
    {
        List<String> list = new ArrayList<>();
        list.addAll(excelSheets.keySet());
        return list;
    }

    public Map<String, FieldInfo> getTableColumnMap(String tableName)
    {
        ExcelSheet sheet = excelSheets.get(tableName);
        return sheet.getFieldMap();
    }
}
