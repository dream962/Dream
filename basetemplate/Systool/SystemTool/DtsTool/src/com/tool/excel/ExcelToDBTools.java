package com.tool.excel;

import java.io.File;

import com.tool.dts.ErrorMgr;

import jxl.Sheet;
import jxl.Workbook;

/**
 * @author dream
 * @date 2011-05-04
 * @version DTS工具
 * 
 */
public class ExcelToDBTools {
	private static String Path = "test";
	private static String DBPath = "config";
	private static Config config;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String dirPath = System.getProperty("user.dir") + File.separator;

		if (init(dirPath))
			work(dirPath+Path);

		close();
	}

	private static boolean init(String path) {
		config = new Config(path + DBPath + File.separator);
		if (!config.init()) {
			ErrorMgr.info(String.format("第一步：获取Connection 失败,URL%s", config.getUrl()));
			return false;
		} else {
			ErrorMgr.info(String.format("第一步：获取Connection 成功,URL%s", config.getUrl()));
			return true;
		}
	}

	private static void work(String dirPath) {
		try {
			File directory = new File(dirPath);
			if (directory.isDirectory()) {
				for (String file : directory.list()) {
					if (file.indexOf(".xls") > 0) {
						if(!handler(dirPath + File.separator + file))
						{
							System.err.println("handler Sheet Data Stop.");
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			PrintMgr.error("导入数据异常：", e);
		}
		System.err.println("Export Data Success.");
	}
	
	private static boolean handler(String dirPath)
	{
		try 
		{
			Workbook book = Workbook.getWorkbook(new File(dirPath));
			Sheet[] sheets=book.getSheets();
	        for(int i=1;i<sheets.length;i++)
	        {
	        	SheetDetail detail = new SheetDetail(sheets[i]);
	        	if(detail.init())
	        	{
	        		detail.run(config);
	        	}
	        }
		} 
		catch (Exception e) 
		{
			PrintMgr.error("处理 sheet 异常：", e);
			return false;
		}
		
		return true;
	}

	public static void close() {
		config.close();
	}
}
