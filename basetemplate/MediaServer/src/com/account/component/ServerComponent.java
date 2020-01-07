/**
 * 
 */
package com.account.component;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import com.base.component.AbstractComponent;
import com.base.database.DBParamWrapper;
import com.data.account.business.SeqType;
import com.data.account.business.SystemBusiness;
import com.data.account.factory.DataInfoFactory;
import com.data.account.info.DataInfo;
import com.util.ThreadPoolUtil;

/**
 * 服务器管理组件
 * 
 * @author dream
 *
 */
public class ServerComponent extends AbstractComponent
{
    private static ScheduledExecutorService schedule;

    @Override
    public boolean initialize()
    {
        schedule = ThreadPoolUtil.singleScheduledExecutor("account-server");
        return true;
    }

    @Override
    public void stop()
    {
    }

    public static DataInfo getDataByKey(long userID, String key)
    {
        String sql = "select * from t_u_data where userid=? and KeyID=?;";
        DBParamWrapper param = new DBParamWrapper();
        param.put(userID);
        param.put(key);
        return DataInfoFactory.getDao().query(sql, param);
    }

    public static List<DataInfo> getDataByGroup(long userID, String group)
    {
        String sql = "select * from t_u_data where userid=? and GroupID=?;";
        DBParamWrapper param = new DBParamWrapper();
        param.put(userID);
        param.put(group);
        return DataInfoFactory.getDao().queryList(sql, param);
    }

    public static List<DataInfo> getDataByUser(long userID)
    {
        String sql = "select * from t_u_data where userid=?;";
        DBParamWrapper param = new DBParamWrapper();
        param.put(userID);
        return DataInfoFactory.getDao().queryList(sql, param);
    }

    public static boolean addData(DataInfo requestInfo)
    {
        if (requestInfo.getKeyID() <= 0)
            requestInfo.setKeyID(SystemBusiness.getTableMaxID(SeqType.T_U_DATA.getValue()));

        return DataInfoFactory.getDao().addOrUpdate(requestInfo);
    }

    public static boolean removeDataByKeys(List<Long> keyList)
    {
        for (long key : keyList)
        {
            String sql = "delete from t_u_data where KeyID=?;";
            DBParamWrapper param = new DBParamWrapper();
            param.put(key);
            DataInfoFactory.getDao().execute(sql, param);
        }

        return true;
    }
}
