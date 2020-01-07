package com.upload.data.business;

import java.sql.Types;

import com.upload.data.data.StaffBean;
import com.upload.data.factory.StaffBeanFactory;
import com.upload.database.DBParamWrapper;
import com.upload.util.MD5Util;

/**
 * 
 * @date 2018-04-24 14:34:58
 * @description
 *              在此输入描述信息
 */
public class StaffBusiness
{
    public static StaffBean getStaffBean(String accountName, String password)
    {
        String code = MD5Util.md5(password);
        String sql = "select * from t_s_staff where `accountName` = ? and `password` = ?";
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.VARCHAR, accountName);
        params.put(Types.VARCHAR, code);
        return StaffBeanFactory.getDao().query(sql, params);
    }

    public static StaffBean getStaffBean(String accountName)
    {
        String sql = "select * from t_s_staff where `accountName` = ?;";
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.VARCHAR, accountName);
        return StaffBeanFactory.getDao().query(sql, params);
    }

    public static void updateStaffBean(StaffBean bean)
    {
        StaffBeanFactory.getDao().update(bean);
    }

    public static void main(String[] args)
    {
        String password1 = "dream_yj1569dwgfsOUYT";
        String password = "hui_yj1569dwgfsOUYT";
        String code = MD5Util.md5(password1);
        System.err.println(code);
    }

}
