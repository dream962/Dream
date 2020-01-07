package com.data.business;

import java.sql.Types;
import java.util.List;

import com.base.database.DBParamWrapper;
import com.base.orm.AmmentosORMComponent;
import com.data.factory.TablesInfoFactory;
import com.data.info.TablesInfo;

import it.biobytes.ammentos.PersistenceException;

public class SystemBusiness
{
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

        int result = TablesInfoFactory.getDao().queryOneColumnDataOne(sql, params);
        return result;
    }

    /**
     * 保存自增表信息
     * 
     * @param list
     */
    public static boolean updateTablesInfo(List<TablesInfo> list)
    {
        for (TablesInfo info : list)
        {
            DBParamWrapper params = new DBParamWrapper();
            String sql = "CALL proc_save_max_id(?,?);";
            params.put(Types.INTEGER, info.getTableID());
            params.put(Types.INTEGER, info.getValue());
            TablesInfoFactory.getDao().execute(sql, params);
        }

        return true;
    }

    /**
     * 获得模板列表
     * 
     * @param className
     * @return
     * @throws PersistenceException
     */
    public static <T> List<T> getBeanList(Class<T> className) throws PersistenceException
    {
        return AmmentosORMComponent.listAll(className);
    }

    public static <T> boolean removeData(T data)
    {
        return AmmentosORMComponent.removeData(data);
    }

    public static boolean saveData(List<?>... dataList)
    {
        return AmmentosORMComponent.addData(dataList);
    }

    public static List<TablesInfo> getTableList()
    {
        String sql = "SELECT * FROM t_u_tables;";
        List<TablesInfo> list = TablesInfoFactory.getDao().queryList(sql);
        return list;
    }

    public static boolean clean(String sql)
    {
        return TablesInfoFactory.getDao().execute(sql, null);
    }

    /*********************************************** 通用调用 ***********************************************/
}
