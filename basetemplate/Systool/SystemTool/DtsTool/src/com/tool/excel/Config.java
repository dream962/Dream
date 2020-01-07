package com.tool.excel;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

import com.tool.dts.ErrorMgr;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;

/**
 * @version 数据库连接配置
 * 
 */
public class Config
{
    private String driverClassName = "com.mysql.jdbc.Driver";
    private String dbURL = "jdbc:mysql://192.168.1.97:3306/";// 设置提交地址的编码
    private String dbName = "db";
    private String user = "root";
    private String password = "root";
    private String paras = "";
    public static String DeleteFlag="[D]";
    public static boolean IsCreate=false;
    
    private Connection connection;

    public Config(String dirPath)
    {
        if (!load(dirPath))
        {
            PrintMgr.info("载入DB配置信息失败");
        }
    }

    public boolean init()
    {
        try
        {
            Class.forName(driverClassName);
            connection = DriverManager.getConnection(getUrl(), user, password);
            if(connection!=null)
            	return true;
            
            return false;
        }
        catch (ClassNotFoundException e)
        {
            PrintMgr.error("数据驱动错误", e);
            return false;
        }
        catch (SQLException e)
        {
            PrintMgr.error("数据连接错误", e);
            return false;
        }
    }

    private boolean load(String dirPath)
    {
        String filePath = dirPath + "db.ini";
        try
        {
            InputStream in = new BufferedInputStream(new FileInputStream(
                    filePath));
            Properties prop = new Properties();
            prop.load(in);
            driverClassName = prop.getProperty("driverClassName");
            dbURL = prop.getProperty("dbServer");
            dbName = prop.getProperty("database");
            user = prop.getProperty("user");
            password = prop.getProperty("password");
            paras = prop.getProperty("paras");
            IsCreate=Boolean.getBoolean(prop.getProperty("isCreate"));
            
            in.close();
        }
        catch (Exception e)
        {
            PrintMgr.error("没找到db信息配置文件", e);
            return false;
        }
        return true;
    }

    public String getUrl()
    {
        return dbURL + dbName + paras;
    }

    public int executeNoneQuery(String sqlText)
    {
        return executeNoneQuery(sqlText, null, null);
    }

    public int executeNoneQuery(String sqlText, Cell[] cell,List<Integer> fieldRow)
    {
        PreparedStatement pstmt = null;
        String execStr = "";
        try
        {
            pstmt = connection.prepareStatement(sqlText);
            if (cell != null)
            {
                for (int j = 0; j < fieldRow.size(); j++)
                {
                    int index = fieldRow.get(j);
                    Cell c = cell[index];
                    
                    //排除列
                    if(c.getContents().startsWith(DeleteFlag))
                        continue;
                    
                    String content = "";
                    if (c.getType() == CellType.DATE)
                    {
                        DateCell dateCell = (DateCell) c;
                        TimeZone.setDefault(TimeZone.getTimeZone("GMT+0:00"));
                        SimpleDateFormat dateformat = new SimpleDateFormat(
                                "yyyy-MM-dd HH:mm:ss");
                        content = dateformat.format(dateCell.getDate());
                    }
                    else
                    {
                        content = c.getContents();
                        if(content.trim()=="NULL" || content.trim().equals("NULL"))
                        {
                            pstmt.setNull(j + 1, Types.NULL);
                            execStr = pstmt.toString();
                            continue;
                        }
                    }
                    
                    pstmt.setString(j + 1, content);// 从1开始
                    execStr = pstmt.toString();
                }
            }
            return pstmt.executeUpdate();
        }
        catch (Exception ex)
        {
            PrintMgr.error(String.format("操作数据库失败:Sql语句%s,操作语句%s", sqlText,
                                         execStr), ex);
        }
        finally
        {
            if (pstmt != null)
            {
                try
                {
                    pstmt.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return -1;

    }
    
    public boolean executeQuery(String sqlText)
    {
        PreparedStatement pstmt = null;
        try
        {
            pstmt = connection.prepareStatement(sqlText);
            ResultSet set=pstmt.executeQuery(sqlText);
            if(set.next())
            {
	            if(set.getString(1)!=null)
	            	return true;
            }
        }
        catch (Exception ex)
        {
            PrintMgr.error(String.format("操作数据库失败:Sql语句%s,操作语句%s", sqlText,sqlText), ex);
        }
        finally
        {
            if (pstmt != null)
            {
                try
                {
                    pstmt.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return false;

    }
    
    public boolean execute(String sqlText)
    {
        PreparedStatement pstmt = null;
        try
        {
            pstmt = connection.prepareStatement(sqlText);
            boolean set=pstmt.execute(sqlText);
            if(set)
            	return true;
        }
        catch (Exception ex)
        {
            PrintMgr.error(String.format("操作数据库失败:Sql语句%s,操作语句%s", sqlText,sqlText), ex);
        }
        finally
        {
            if (pstmt != null)
            {
                try
                {
                    pstmt.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return false;

    }

	
    public void close()
    {
		if (connection != null) {
			try {
				if (!connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				ErrorMgr.error("关闭Connection出错", e);
			}
		}
	}
}
