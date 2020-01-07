package com.data.account.business;

import java.sql.Types;
import java.util.List;

import com.base.database.DBParamWrapper;
import com.data.account.data.TableData;
import com.data.account.factory.TableDataFactory;

public class SystemBusiness
{
    /**
     * 保存自增表信息
     * 
     * @param list
     */
    public static boolean updateTablesInfo(List<TableData> list)
    {
        for (TableData info : list)
        {
            DBParamWrapper params = new DBParamWrapper();
            String sql = "CALL proc_save_max_id(?,?);";
            params.put(Types.INTEGER, info.getTableID());
            params.put(Types.INTEGER, info.getValue());

            TableDataFactory.getDao().execute(sql, params);
        }

        return true;
    }

    public static TableData getTablesInfo(int id)
    {
        String sql = "SELECT * FROM t_u_tables WHERE TableID=?;";
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, id);
        TableData data = TableDataFactory.getDao().query(sql, params);
        return data;
    }

    public static List<TableData> getTableList()
    {
        String sql = "SELECT * FROM t_u_tables;";
        List<TableData> list = TableDataFactory.getDao().queryList(sql);
        return list;
    }

    /**
     * 获取自增id
     * 
     * @param tableID
     *            表的类型
     * @return
     */
    public static int getTableMaxID(int tableID)
    {
        DBParamWrapper params = new DBParamWrapper();
        String sql = "CALL proc_get_max_id(?);";
        params.put(Types.INTEGER, tableID);

        int result = TableDataFactory.getDao().queryOneColumnDataOne(sql, params);
        return result;
    }
}
