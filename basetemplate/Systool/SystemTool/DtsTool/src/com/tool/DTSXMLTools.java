package com.tool;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * @author dream
 * @date 2011-05-04
 * @version DTS工具
 * 
 */
public class DTSXMLTools
{
    private static String Version="npc";
    private static String SavePath="xml";
    private static String DeleteFlag="[D]";
    
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        String dirPath = System.getProperty("user.dir") + File.separator;
        run(dirPath);
    }

    public static boolean run(String dirPath)
    {
        File directory =new File(dirPath+Version);
        if(directory.isDirectory())
        {
            String[] files=directory.list();
            for(int a=0;a<files.length;a++)
            {
                String fileName=files[a].replace(".xls", "");
                try
                {
                    Document result = DocumentHelper.createDocument();
                    Element element = result.addElement("root");
                    
                    Workbook book = Workbook.getWorkbook(new File(dirPath+Version,files[a]));
                    Sheet sheet = book.getSheet(0);
                    if (sheet.getRows() >= 2)
                    {
                        Cell[] headCells=sheet.getRow(0);
                        
                        for(int i = 2; i < sheet.getRows(); i++)
                        {
                            Cell[] execlData = sheet.getRow(i);
                            if (execlData == null || execlData.length<1)
                                continue;
                            
                            //排除行
                            if(execlData[0].getContents().startsWith(DeleteFlag) || execlData[0].getContents().isEmpty())
                                continue;
                            
                            Element item = element.addElement("template");

                            if(execlData.length>16)
                            	System.err.println("length:"+execlData.length);
                            
                            for(int j=0;j<execlData.length;j++)
                            {
                                String content = "";
                                if (execlData[j].getType() == CellType.DATE)
                                {
                                    DateCell dateCell = (DateCell) execlData[j];
                                    TimeZone.setDefault(TimeZone.getTimeZone("GMT+0:00"));
                                    SimpleDateFormat dateformat = new SimpleDateFormat(
                                            "yyyy-MM-dd HH:mm:ss");
                                    content = dateformat.format(dateCell.getDate());
                                }
                                else
                                {
                                    content = execlData[j].getContents();
                                }
                                
                                if(j>16)
                                	System.err.println();
                                //排除列
                                if(headCells[j].getContents().startsWith(DeleteFlag))
                                    continue;
                                
                                item.addAttribute(headCells[j].getContents(), content);
                            }
                        }
                    }
                    
                    //CompressUtil.createCompressXml(dirPath+SavePath+File.separatorChar, result.asXML(), fileName, false, "");
                    
                    System.out.println(String.format("共%d个,当前第%d个,共%s记录导入%s文件成功"
                            ,files.length,a+1,sheet.getRows()-2,fileName));
                }
                catch (BiffException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    public static String formatString(String targetStr, int strLength)
    {
        if (targetStr==null || targetStr=="")
            return targetStr;
        
        int curLength = targetStr.getBytes().length;
        if (curLength > strLength)
        {
            targetStr = targetStr.substring(0, strLength);
        }

        int cutLength = strLength - targetStr.getBytes().length;
        
        StringBuilder sBuilder = new StringBuilder(targetStr);
        for (int i = 0; i < cutLength; i++)
            sBuilder.append(" ");
        
        return sBuilder.toString();
    }

}
