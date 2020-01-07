package com.tool.data;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import com.tool.code.FieldInfo;
import com.tool.data.ExcelSheet.OneRowData;

public class DBSource implements IDataSource
{
    public static Properties prop = new Properties();
    
    public void init(String path)
    {
        InputStream in = null;
        try
        {
            in = new FileInputStream(path);
            prop.load(in);

            System.err.println("********rootPath:" + prop.getProperty("rootPath"));
            System.err.println("********rootPackage:" + prop.getProperty("rootPackage"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            try
            {
                in.close();
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
            System.exit(1);
        }
    }

    public static Connection getConn() throws ClassNotFoundException,
            SQLException
    {
        Class.forName(prop.getProperty("driverName"));
        Connection conn = DriverManager.getConnection(prop.getProperty("url"),
                prop.getProperty("userName"), prop.getProperty("password"));
        return conn;
    }

    public static Connection getConn(String dbName)
            throws ClassNotFoundException, SQLException
    {
        Class.forName(prop.getProperty("driverName"));
        Connection conn = DriverManager.getConnection(prop.getProperty("url")
                + "/" + dbName +"?allowMultiQueries=true", prop.getProperty("userName"), prop.getProperty("password"));
        return conn;
    }

    public static void close(ResultSet rs, Statement stmt, Connection conn)
    {
        if (rs != null)
        {
            try
            {
                rs.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        if (stmt != null)
        {
            try
            {
                stmt.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        if (conn != null)
        {
            try
            {
                conn.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获得可连接的所有数据库名称
     */
    public List<String> getDatabaseOrExcel()
    {
        Connection conn = null;
        DatabaseMetaData metaData = null;
        ResultSet rs = null;
        List<String> databases = new ArrayList<String>();
        try
        {
            conn = DBSource.getConn();
            metaData = conn.getMetaData();
            rs = metaData.getCatalogs();

            String tables = DBSource.prop.getProperty("db");
            while (rs.next())
            {
                String database = rs.getString("TABLE_CAT");

                if (tables.indexOf(database) >= 0)
                {
                    //System.err.println(database);
                    databases.add(database);
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        finally
        {
            DBSource.close(rs, null, conn);
        }
        return databases;
    }

    /**
     * 获得当前数据库所有表名
     */
    public List<String> getTables(String dbName)
    {
        List<String> tables = new ArrayList<String>();
        if (prop.getProperty("db").indexOf(dbName) < 0)
            return tables;
        
        Connection conn = null;
        DatabaseMetaData metaData = null;
        ResultSet rs = null;
        
        try
        {
            conn = DBSource.getConn(dbName);
            metaData = conn.getMetaData();
            rs = metaData.getTables(null, null, null, new String[] { "TABLE","VIEW" });

            while (rs.next())
            {
                String tableName = rs.getString("TABLE_NAME");
                //System.err.println(tableName);
                tables.add(tableName);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        finally
        {
            DBSource.close(rs, null, conn);
        }
        return tables;
    }

    /**
     * 获得数据表具体信息
     */
    public Map<String, FieldInfo> getTableFieldList(String dbName,
            String tableName)
    {
        Map<String, FieldInfo> fieldMap = new LinkedHashMap<String, FieldInfo>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ResultSetMetaData rsMetaData = null;
        try
        {
            // 获得数据表各个字段的注释
            conn = DBSource.getConn("information_schema");
            String sql = "SELECT * FROM COLUMNS WHERE TABLE_SCHEMA='" + dbName+ "' AND TABLE_NAME='" + tableName + "'";
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();

            while (rs.next())
            {
                System.out.printf(" %1$s\t\t", rs.getString("column_name"));
                System.out.printf(rs.getString("column_comment").isEmpty() ? "无"
                        : rs.getString("column_comment"));
                System.out.println();
                
                FieldInfo field = new FieldInfo();
                field.setName(rs.getString("column_name"));
                field.setComment(rs.getString("column_comment"));
                fieldMap.put(rs.getString("column_name"), field);
                field.setNull(rs.getBoolean("is_nullable"));
            }

            conn = DBSource.getConn(dbName);
            sql = "select * from " + tableName;
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();
            rsMetaData = rs.getMetaData();
            pstm.getParameterMetaData();

            int cols = rsMetaData.getColumnCount(); // 查询获取结果的列数

            for (int i = 1; i <= cols; i++)
            {
                // 字段名
                String name = rsMetaData.getColumnName(i);

                // 字段Java中值类型
                String javaType = rsMetaData.getColumnClassName(i);
                String SqlType = rsMetaData.getColumnTypeName(i);
                
                if (SqlType.contains("UNSIGNED"))
                {
                    SqlType = SqlType.replace("UNSIGNED", "").trim();
                }

                if (javaType == "[B")
                {
                    javaType = "byte[]";
                }
                else if (javaType == "java.math.BigDecimal")
                {
                    javaType = "float";
                }
                else if (javaType == "java.lang.Boolean")
                {
                    javaType = "boolean";
                }
                else if (javaType == "java.lang.Integer")
                {
                    javaType = "int";
                    if(SqlType.toLowerCase().equalsIgnoreCase("tinyint"))
                        javaType="byte";
                    else if(SqlType.toLowerCase().equalsIgnoreCase("smallint"))
                        javaType="short";
                }
                else if (javaType == "java.lang.Long")
                {
                    if(SqlType.toLowerCase().equals("bigint"))
                        javaType = "long";
                    else
                        javaType="int";
                }
                else if (javaType == "java.lang.Float")
                {
                    javaType = "float";
                }
                else if (javaType == "java.lang.String")
                {
                    javaType = "String";
                }
                else if (javaType == "java.sql.Timestamp")
                {
                    javaType = "Date";
                }
                else if (javaType == "java.math.BigInteger")
                {
                    javaType = "long";
                    if(rsMetaData.getColumnDisplaySize(i)==1)
                        javaType="boolean";
                    if(rsMetaData.getColumnDisplaySize(i)==11)
                        javaType="int";
                }

                if (fieldMap.get(name) != null)
                {
                    fieldMap.get(name).setJavaType(javaType);

                    // 字段 SQL Type
                    fieldMap.get(name).setSqlType(SqlType);
                    
                    // 字段长度
                    fieldMap.get(name).setLen(
                            (rsMetaData.getColumnDisplaySize(i)));
                }

                // TODO 获取key字段
                ResultSet pkRSet = conn.getMetaData().getPrimaryKeys(null,dbName, tableName);
                while (pkRSet.next())
                {
                    FieldInfo key = fieldMap.get(pkRSet.getObject(4));
                    if (key != null)
                    {
                        key.setPrimaryKey(true);
                    }
                }
            }

            Set<Entry<String, FieldInfo>> set = fieldMap.entrySet();
            for (Entry<String, FieldInfo> entry : set)
            {
                FieldInfo fieldInfo = entry.getValue();
                if (!fieldInfo.isPrimaryKey())
                {
                    continue;
                }
                String clmName = fieldInfo.getName();
                ResultSet clmSet = conn.getMetaData().getColumns(null, dbName,
                        tableName, clmName);
                while (clmSet.next())
                {
                    String flag = (String) clmSet.getObject("IS_AUTOINCREMENT");
                    if (flag.equals("YES"))
                    {
                        fieldInfo.setAotuIncreamte(true);
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            DBSource.close(rs, pstm, conn);
        }
        return fieldMap;
    }

    @Override
    public String getCommont(String name)
    {
        return null;
    }

    @Override
    public List<OneRowData> getData(String name)
    {
        return null;
    }

}
