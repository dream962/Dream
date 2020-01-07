package com.compare.data;

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
import java.util.Set;

import com.compare.data.ConfigData.DataBaseConfig;

public class DBSource implements IDataSource
{
    private DataBaseConfig data;
    
    public List<String> dBList=new ArrayList<>();
    
    public List<String> TableList=new ArrayList<>();

    public DBSource(DataBaseConfig data)
    {
        this.data = data;
    }

    public Connection getConn()
    {
        try
        {
            Class.forName(data.driverName);
            Connection conn = DriverManager.getConnection(data.url, data.userName, data.password);
            return conn;
        }
        catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public Connection getConn(String dbName)
    {
        try
        {
            Class.forName(data.driverName);
            Connection conn = DriverManager.getConnection(data.url
                    + "/" + dbName + "?allowMultiQueries=true", data.userName, data.password);
            return conn;
        }
        catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public void close(ResultSet rs, Statement stmt, Connection conn)
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
    public List<String> getDatabase()
    {
        Connection conn = null;
        DatabaseMetaData metaData = null;
        ResultSet rs = null;
        List<String> databases = new ArrayList<String>();
        try
        {
            conn = getConn();
            metaData = conn.getMetaData();
            rs = metaData.getCatalogs();

            while (rs.next())
            {
                String database = rs.getString("TABLE_CAT");
                databases.add(database);
            }
            
            dBList.clear();
            dBList.addAll(databases);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            close(rs, null, conn);
        }
        return databases;
    }

    /**
     * 获得当前数据库所有表名
     */
    public List<String> getTables(String dbName)
    {
        Connection conn = null;
        DatabaseMetaData metaData = null;
        ResultSet rs = null;

        try
        {
            conn = getConn(dbName);
            metaData = conn.getMetaData();
            rs = metaData.getTables(null, null, null, new String[] { "TABLE" });

            TableList.clear();
            while (rs.next())
            {
                String tableName = rs.getString("TABLE_NAME");
                TableList.add(tableName);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            close(rs, null, conn);
        }
        return TableList;
    }

    /**
     * 获得数据表具体信息
     */
    public Map<String, FieldInfo> getTableFieldList(String dbName,String tableName)
    {
        Map<String, FieldInfo> fieldMap = new LinkedHashMap<String, FieldInfo>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ResultSetMetaData rsMetaData = null;
        try
        {
            // 获得数据表各个字段的注释
            conn = getConn("information_schema");
            String sql = "SELECT * FROM COLUMNS WHERE TABLE_SCHEMA='" + dbName + "' AND TABLE_NAME='" + tableName + "'";
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();

            while (rs.next())
            {
                FieldInfo field = new FieldInfo();
                field.setName(rs.getString("column_name"));
                field.setComment(rs.getString("column_comment"));
                fieldMap.put(rs.getString("column_name"), field);
                field.setNull(rs.getBoolean("is_nullable"));
            }

            conn = getConn(dbName);
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
                else if (javaType == "java.lang.Boolean")
                {
                    javaType = "boolean";
                }
                else if (javaType == "java.lang.Integer")
                {
                    javaType = "int";
                }
                else if (javaType == "java.lang.Long")
                {
                    if (SqlType.toLowerCase().equals("bigint"))
                        javaType = "long";
                    else
                        javaType = "int";
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
                    if (rsMetaData.getColumnDisplaySize(i) == 1)
                        javaType = "boolean";
                    if (rsMetaData.getColumnDisplaySize(i) == 11)
                        javaType = "int";
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
                ResultSet pkRSet = conn.getMetaData().getPrimaryKeys(null,
                        dbName, tableName);
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
                ResultSet clmSet = conn.getMetaData().getColumns(null, dbName,tableName, clmName);
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
            close(rs, pstm, conn);
        }
        
        System.err.println("load table:"+tableName);
        return fieldMap;
    }

    public int execute(String dbName, String sql)
    {
        Connection connection = null;
        PreparedStatement pstm = null;
        try
        {
            connection = getConn(dbName);
            pstm = connection.prepareStatement(sql);
            int result = pstm.executeUpdate();
            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            System.err.println(sql);
            close(null, pstm, connection);
        }
        
        return -1;
    }
}
