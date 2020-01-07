/**
 * 
 */
package com.tool.dts;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;

/**
 * @date 2016年2月26日 下午2:51:29
 * @author dansen
 * @desc
 */
public class ExcelFile
{
    public static final int START_INDEX = 1;
    /**
     * 文件路径
     */
    private String dirPath;

    /**
     * execl 表名
     */
    private String excelName = "";

    private Workbook book = null;
    
    public List<ExcelSheet> excelSheets = new ArrayList<>();

    public ExcelFile(String dirPath, String excelName)
    {
        try
        {
            this.excelName = excelName;
            this.dirPath = dirPath;
            Workbook book = Workbook.getWorkbook(new File(this.dirPath, this.excelName));
            this.book = book;
            
            for(int i=0; i<book.getNumberOfSheets(); ++i){
                ExcelSheet sheet = new ExcelSheet(book, i);
                excelSheets.add(sheet);
            }
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public int sheetCount()
    {
        return book.getNumberOfSheets();
    }
    
    public ExcelSheet getSheet(int index)
    {
        return excelSheets.get(index);
    }
    
    public void close()
    {
        book.close();
    }
}






