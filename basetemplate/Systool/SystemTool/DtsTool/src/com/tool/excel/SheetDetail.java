package com.tool.excel;

import java.util.ArrayList;
import java.util.List;

import com.tool.dts.DBConfig;
import com.tool.dts.ErrorMgr;

import jxl.Cell;
import jxl.Sheet;

public final class SheetDetail
{
	private Sheet sheet;
	private String tableName;
	private String sheetName;
	private String tableKeys;
	
	private String insert;
	private String create;
	private String delete;
	
	private static int START_ROW=4;
	
	public SheetDetail(Sheet sheet)
	{
		this.sheet=sheet;
	}
	
	public boolean init()
	{
		sheetName=sheet.getName();
		if(sheet.getCell(0, 0)==null)
		{
			PrintMgr.error("Excel Sheet 首列表名  格式不正确。");
			return false;
		}
		else
			tableName=sheet.getCell(0, 0).getContents();
		
		if(sheet.getCell(0, 1)==null)
		{
			PrintMgr.error("Excel Sheet 首列主键  格式不正确。");
			return false;
		}
		else
			tableKeys=sheet.getCell(1,0).getContents();
		
		return true;
	}
	
	public String getInsert()
	{
		if(insert==null)
		{
			if (sheet != null)
            {
                Cell[] cell = sheet.getRow(3);
                String keys = "";
                String values = "";
                for (int i = 0; i < cell.length; i++)
                {
                    if(cell[i].getContents().startsWith(DBConfig.DeleteFlag))
                            continue;
                    
                    if (cell[i].getContents() != "")
                    {
                        keys += "`" + cell[i].getContents() + "`,";
                        values += "?,";
                    }
                }
                keys = keys.substring(0, keys.length() - 1);
                values = values.substring(0, values.length() - 1);
                insert = "INSERT INTO `" + tableName + "`(" + keys
                        + ") VALUES(" + values + ")";
                
            }
		}
		
		return insert;
	}
	

	public void run(Config connection)
	{
		//判断表是否存在
		if(!ifExistTable(connection) || Config.IsCreate)
			createTable(connection);
		
		//删除数据
		deleteTable(connection);
		//插入数据
		insertTable(connection);
	}
	
	public boolean ifExistTable(Config connection)
	{
		String sql="SELECT table_name FROM information_schema.tables WHERE table_name = '%s';";
		String exeStr=String.format(sql, tableName);
		boolean result = connection.executeQuery(exeStr);
		return result;
	}
	
	public boolean createTable(Config connection)
	{
		StringBuilder builder=new StringBuilder();
		builder.append("drop table if exists ").append(tableName).append(";").append("\n");
		
		builder.append("create table ").append(tableName).append("(");
		Cell[] comments=sheet.getRow(1);
		Cell[] types=sheet.getRow(2);
		Cell[] fields=sheet.getRow(3);
		
		for(int i=0;i<fields.length;i++)
		{
			String comment=comments[i].getContents();
			String type=types[i].getContents();
			String field=fields[i].getContents();
			if(field==null || field.isEmpty() || field.startsWith(Config.DeleteFlag))
				continue;

			builder.append("`").append(field).append("`    ");

			builder.append(getType(type)).append("    ");
			
			if(tableKeys.indexOf(field)>=0)
				builder.append(" not null  ");
			
			builder.append("comment ").append("'").append(comment).append("',").append("\n");
		}
		
		builder.append("primary key(").append(tableKeys).append(")").append("\n");
		builder.append(");");
		
		return connection.execute(builder.toString());
	}
	
	private String getType(String type)
	{
		if(type.indexOf("int")>=0)
		{
			return "int(11)";
		}
		
		if(type.indexOf("long")>=0)
		{
			return "bigint(20)";
		}
		
		if(type.indexOf("short")>=0)
		{
			return "smallint(6)";
		}
		
		if(type.indexOf("boolean")>=0)
		{
			return "tinyint(1)";
		}
		
		if(type.indexOf("float")>=0)
		{
			return "float";
		}
		
		if(type.indexOf("double")>=0)
		{
			return "double";
		}
		
		if(type.indexOf("String")>=0)
		{
			String[] s=type.split("\\|");
			return "varchar("+s[1]+")";
		}
		return type;
	}
	
    public boolean insertTable(Config connection)
    {
        Cell[] cell = sheet.getRow(3);
        String keys = "";
        String values = "";
        StringBuilder keysBuilder = new StringBuilder("");
        StringBuilder valuesBuilder = new StringBuilder("");
        List<Integer> list=new ArrayList<>();
        for (int i = 0; i < cell.length; i++)
        {
            if (!cell[i].getContents().equals("") && !cell[i].getContents().startsWith(Config.DeleteFlag))
            {
                valuesBuilder.append("?,");
                keysBuilder.append("`").append(cell[i].getContents()).append("`,");
                list.add(i);
            }
        }
        keys = keysBuilder.toString().substring(0, keysBuilder.length() - 1);
        values = valuesBuilder.toString().substring(0, valuesBuilder.length() - 1);
        String sql= "INSERT INTO `" + tableName + "`(" + keys + ") VALUES(" + values+ ")";
        
        int count=0;
        int ignore=0;
        if (sheet.getRows() >= START_ROW)
        {
            for (int i = START_ROW; i < sheet.getRows(); i++)
            {
                Cell[] execlData = sheet.getRow(i);
                if (execlData != null)
                {
                    //排除行
                    if(execlData[0].getContents().startsWith(Config.DeleteFlag) || execlData[0].getContents().isEmpty())
                        continue;
                    
                    int result = connection.executeNoneQuery(sql,execlData,list);
                    ++count;
                    if (result == -1)
                    {
                        ignore = i;
                    }
                    if (ignore > 0)
                    {
                        ErrorMgr.info(String.format("添加表 %s 失败,第 %s 行出错!",tableName, ignore));
                        return false;
                    }
                    if(count%100==0)
                    	ErrorMgr.info(String.format("添加表 %s 成功%d行!",tableName, count));
                }
            }
            
            ErrorMgr.info(String.format("添加表 %s 成功,共%d行!",tableName, count));
        }
        else
        {
        	ErrorMgr.info(String.format("添加execl%s，没有数据!",tableName));
        }
        return true;
    }

    public boolean deleteTable(Config conn)
    {
    	return conn.execute("Delete From " + tableName);
    }
}

