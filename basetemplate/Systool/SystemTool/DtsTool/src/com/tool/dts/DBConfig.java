package com.tool.dts;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.TimeZone;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;

/**
 * @version 数据库连接配置
 * 
 */
public class DBConfig
{

    private String driverClassName = "com.mysql.jdbc.Driver";
    private String dbURL = "jdbc:mysql://192.168.1.97:3306/";// 设置提交地址的编码
    private String dbName = "db";
    private String user = "root";
    private String password = "root";
    private String paras = "";
    public static String DeleteFlag="[D]";

    public DBConfig(Properties prop)
    {
        if (!load(prop))
        {
            ErrorMgr.info("载入DB配置信息失败");
        }
    }

    public Connection getConnection()
    {
        Connection con = null;
        try
        {
            Class.forName(driverClassName);
            con = DriverManager.getConnection(getUrl(), user, password);
        }
        catch (ClassNotFoundException e)
        {
            ErrorMgr.error("数据驱动错误", e);
        }
        catch (SQLException e)
        {
            ErrorMgr.error("数据连接错误", e);

        }
        return con;
    }

    private boolean load(Properties prop)
    {
        driverClassName = prop.getProperty("driverClassName");
        dbURL = prop.getProperty("dbServer");
        dbName = prop.getProperty("database");
        user = prop.getProperty("user");
        password = prop.getProperty("password");
        paras = prop.getProperty("paras");
        return true;
    }

    public String getUrl()
    {
        return dbURL + dbName + paras;
    }

    public int executeNoneQuery(Connection conn, String sqlText)
    {
        return executeNoneQuery(conn, sqlText, null, null, null);
    }

    public int executeNoneQuery(Connection conn, String sqlText, Cell[] cell, Cell [] types,
            int[] fieldRow)
    {
        PreparedStatement pstmt = null;
        String execStr = "";
        try
        {
            pstmt = conn.prepareStatement(sqlText);
            if (cell != null)
            {
                for (int j = 0; j < fieldRow.length; j++)
                {
                    int index = fieldRow[j];
                    if(index>=cell.length)
                    {
                    	if(types[j].getContents().equals("int") || types[j].getContents().equals("float"))
                    	{
                            pstmt.setInt(j+1, 0);
                            execStr = pstmt.toString();
                        }
                    	else if(types[j].getContents().indexOf("String")>=0)
                    	{
                    		pstmt.setString(j+1, "");
                            execStr = pstmt.toString();
                    	}
                    	else
                    	{
                            pstmt.setNull(j + 1, Types.NULL);
                            execStr = pstmt.toString();
                        }
                    }
                    else
                    {
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
                        else if (c.getType() == CellType.EMPTY)
                        {
                        	content = c.getContents();
                            if(content.trim()=="" || content.trim()=="NULL" || content.trim().equals("NULL"))
                            {
                                if(types[j].getContents().equals("int") || types[j].getContents().equals("float")){
                                    pstmt.setInt(j+1, 0);
                                    execStr = pstmt.toString();
                                }else{
                                    pstmt.setNull(j + 1, Types.NULL);
                                    execStr = pstmt.toString();
                                }
                                
                                continue;
                            }
    					}
                        else if (c.getType()==CellType.BOOLEAN)
                        {
                        	content = c.getContents();
                            if(content.trim().toLowerCase()=="true")
                            {
                                pstmt.setString(j + 1, "1");
                                execStr = pstmt.toString();
                                continue;
                            }
                            else
                            {
                            	 pstmt.setString(j + 1, "0");
                                 execStr = pstmt.toString();
                                 continue;
                            }
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
            }
            return pstmt.executeUpdate();
        }
        catch (Exception ex)
        {
            ErrorMgr.error(String.format("操作数据库失败:Sql语句%s,操作语句%s", sqlText,
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

}
