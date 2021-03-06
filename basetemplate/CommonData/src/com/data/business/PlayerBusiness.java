package com.data.business;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.base.database.DBParamWrapper;
import com.data.factory.ChargeInfoFactory;
import com.data.factory.PlayerInfoFactory;
import com.data.factory.RankInfoFactory;
import com.data.info.ChargeInfo;
import com.data.info.PlayerInfo;
import com.data.info.RankInfo;
import com.util.StringUtil;

public class PlayerBusiness
{
    public static PlayerInfo queryPlayerByAccountID(long accountID)
    {
        String sql = "SELECT * FROM t_u_player WHERE AccountID=?;";
        DBParamWrapper param = new DBParamWrapper();
        param.put(accountID);
        return PlayerInfoFactory.getDao().query(sql, param);
    }

    public static PlayerInfo getPlayerInfoByOpenID(String openID)
    {
        String sql = "SELECT * FROM t_u_player WHERE openid = ?;";
        DBParamWrapper param = new DBParamWrapper();
        param.put(openID);
        PlayerInfo info = PlayerInfoFactory.getDao().query(sql, param);
        return info;
    }

    public static PlayerInfo queryPlayer(long userID)
    {
        String sql = "SELECT * FROM t_u_player WHERE UserID=?;";
        DBParamWrapper param = new DBParamWrapper();
        param.put(userID);
        return PlayerInfoFactory.getDao().query(sql, param);
    }

    public static boolean addOrUpdatePlayer(PlayerInfo playerInfo)
    {
        return PlayerInfoFactory.getDao().addOrUpdate(playerInfo);
    }

    public static List<PlayerInfo> queryRobot()
    {
        String sql = "SELECT * FROM t_u_player WHERE UserID<=1000;";
        return PlayerInfoFactory.getDao().queryList(sql);
    }

    public static void addRobotList(List<PlayerInfo> list)
    {
        PlayerInfoFactory.getDao().addOrUpdateBatch(list);
    }

    public static List<String> queryNameList()
    {
        String sql = "SELECT PlayerName FROM t_u_player;";
        List<String> list = PlayerInfoFactory.getDao().queryOneColumnData(sql, null);
        return list;
    }

    /*********************************************************************/

    public static List<RankInfo> queryPlayerRank(long playerID)
    {
        String sql = "SELECT * FROM t_u_rank WHERE UserID=?;";
        DBParamWrapper param = new DBParamWrapper();
        param.put(playerID);
        List<RankInfo> list = RankInfoFactory.getDao().queryList(sql, param);
        if (list != null)
        {
            list.forEach(p -> {
                p.resetChanged();
            });
        }
        return list;
    }

    /**
     * 根据排行的时间类型查询信息
     * 
     * @param timeType
     * @return <排行类型，同一时间类型和同一排行类型的列表>
     */
    public static Map<Integer, List<RankInfo>> queryRankTypeListByTime(int timeType)
    {
        String sql = "SELECT a.RankType,a.TimeType,a.UserID,a.RankValue,a.UpdateTime,a.Order,b.HeaderID as Header,b.PlayerName as NickName FROM t_u_rank AS a LEFT JOIN t_u_player AS b ON a.UserID=b.UserID WHERE a.TimeType=? and a.RankValue>0;";
        DBParamWrapper param = new DBParamWrapper();
        param.put(timeType);
        List<RankInfo> list = RankInfoFactory.getDao().queryList(sql, param);

        Map<Integer, List<RankInfo>> map = new HashMap<>();

        if (list != null)
        {
            list.forEach(p -> {
                // 排行类型
                map.computeIfAbsent(p.getRankType(), k -> new ArrayList<>()).add(p);
            });
        }

        return map;
    }

    public static void addOrUpdateRank(RankInfo info)
    {
        if (RankInfoFactory.getDao().addOrUpdate(info))
        {
            info.resetChanged();
        }
    }

    public static boolean updateRank(int timeType)
    {
        String sql = "UPDATE t_u_rank SET RankValue=0, UpdateTime=NOW() WHERE TimeType=?;";
        DBParamWrapper param = new DBParamWrapper();
        param.put(timeType);

        return RankInfoFactory.getDao().execute(sql, param);
    }

    public static boolean addOrUpdateChargeList(List<ChargeInfo> list)
    {
        int[] result = ChargeInfoFactory.getDao().addOrUpdateBatch(list);
        if (result != null)
        {
            for (ChargeInfo info : list)
            {
                info.setChanged(false);
            }
        }
        return true;
    }

    public static ChargeInfo getChargeInfo(int userID, String orderID)
    {
        String sql = "SELECT * FROM t_u_charge WHERE OrderId=? AND UserID=?;";
        DBParamWrapper param = new DBParamWrapper();
        param.put(orderID);
        param.put(userID);
        ChargeInfo info = ChargeInfoFactory.getDao().query(sql, param);
        return info;
    }

    public static List<ChargeInfo> getChargeInfoList()
    {
        String sql = "SELECT * FROM t_u_charge WHERE OrderStatus=1;";
        List<ChargeInfo> infos = ChargeInfoFactory.getDao().queryList(sql);
        return infos;
    }

    public static void removePlayer(int userID)
    {
        PlayerInfoFactory.getDao().deleteByKey(userID);

        String sql = "delete from t_u_rank where `UserID`=?;";
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, userID);

        RankInfoFactory.getDao().execute(sql, params);
    }

    public static List<PlayerInfo> getPlayerInfoByName(String name)
    {
        if (StringUtil.isNumber(name))
        {
            String sql = "SELECT * FROM t_u_player WHERE userid=?;";
            DBParamWrapper param = new DBParamWrapper();
            param.put(name);
            return PlayerInfoFactory.getDao().queryList(sql, param);
        }
        else
        {
            String sql = "SELECT * FROM t_u_player WHERE PlayerName LIKE '%" + name + "%' or AccountName like '%" + name + "%' or accuntGName like '%" + name + "%';";
            DBParamWrapper param = new DBParamWrapper();
            return PlayerInfoFactory.getDao().queryList(sql, param);
        }
    }
}
