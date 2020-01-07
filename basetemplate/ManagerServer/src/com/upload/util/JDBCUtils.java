package com.upload.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JDBCUtils
{
    private static Connection conn = null;
    private static Statement stmt = null;
    private static ResultSet rs = null;

    public static class TableColumn
    {
        public String name = "";
        public int pos = -1;
        public String type = "";
        public int typeValue;
    }

    public static class TableCell
    {
        public List<Object> value = new ArrayList<>();
    }

    public static class DataTable
    {
        public List<TableColumn> columns = new ArrayList<>();
        public List<TableCell> cells = new ArrayList<>();
    }

    /**
     * 执行SQL查询
     * 
     * @param ip
     * @param port
     * @param username
     * @param password
     * @param dbName
     * @param sql
     * @param isSave
     * @return
     * @throws Exception
     */
    public static List<DataTable> querySql(String ip, String port, String username, String password, String dbName, String sql)
    {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName + "?allowMultiQueries=true&characterEncoding=utf8";

        List<DataTable> tables = new ArrayList<>();

        try
        {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);

            stmt = conn.createStatement();
            String[] sqlArr = sql.split(";");
            for (String real : sqlArr)
            {
                if (!StringUtil.isNullOrEmpty(real))
                {
                    real = real.replace("\n", " ");
                    rs = stmt.executeQuery(real);
                    if (rs != null)
                    {
                        DataTable table = new DataTable();
                        ResultSetMetaData data = rs.getMetaData();
                        int count = data.getColumnCount();
                        for (int i = 1; i <= count; i++)
                        {
                            TableColumn column = new TableColumn();
                            column.name = data.getColumnName(i);
                            column.pos = i;
                            column.type = data.getColumnTypeName(i);
                            column.typeValue = data.getColumnType(i);

                            table.columns.add(column);
                        }

                        while (rs.next())
                        {
                            TableCell cell = new TableCell();
                            for (int i = 1; i <= count; i++)
                            {
                                Object object = rs.getObject(i);
                                if (object instanceof Date)
                                {
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    object = formatter.format(object);
                                }
                                cell.value.add(object);
                            }
                            table.cells.add(cell);
                        }

                        tables.add(table);

                        try
                        {
                            rs.close();
                        }
                        catch (SQLException e)
                        {
                            e.printStackTrace();
                            LogFactory.error("", e);
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LogFactory.error("", e);
        }
        finally
        {
            close();
        }

        return tables;
    }

    /**
     * SQL执行
     * 
     * @param ip
     * @param port
     * @param username
     * @param password
     * @param dbName
     * @param sql
     * @return
     * @throws Exception
     */
    public static boolean excuteSql(String ip, String port, String username, String password, String dbName, String sql) throws Exception
    {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName + "?allowMultiQueries=true&characterEncoding=utf8";

        try
        {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
            if (sql.contains("PROCEDURE") || sql.contains("procedure") ||
                    ((sql.contains("EVENT") || sql.contains("event")) && (sql.contains("ON SCHEDULE")||sql.contains("on schedule"))) )
            {
                stmt = conn.prepareStatement(sql);
                stmt.execute(sql);
            }
            else
            {
                stmt = conn.createStatement();
                String[] sqlArr = sql.split(";");
                for (String real : sqlArr)
                {
                    if (!StringUtil.isNullOrEmpty(real))
                    {
                        stmt.execute(real.trim() + ";");
                    }
                }
            }

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            close();
        }
    }

    public static void close()
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
                if (!conn.isClosed())
                {
                    conn.close();
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args)
    {
        String ip = "192.168.1.96";
        String port = "13619";
        String username = "daomu2_admin";
        String password = "daomu2!@#";
        String dbName = "db_game_tlcs_log_0001";
        String sql = "DELIMITER $$ \n"
                + "CREATE EVENT `e_every_day` ON SCHEDULE EVERY 1 DAY STARTS '2019-01-01 02:00:00' ON COMPLETION NOT PRESERVE ENABLE DO BEGIN CALL proc_every_day(); END$$\n"
                + " DELIMITER ;\n";

        sql = "CREATE EVENT `e_every_day` ON SCHEDULE EVERY 1 DAY STARTS '2019-01-01 02:00:00' ON COMPLETION PRESERVE ENABLE DO CALL proc_every_day()";

        System.err.println(sql);

        try
        {
            boolean result = excuteSql(ip, port, username, password, dbName, sql);
            System.err.println(result);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
