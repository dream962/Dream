package com.data.account.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.base.database.DBParamWrapper;
import com.base.redis.AbstractCache;
import com.base.redis.RedisClient;
import com.base.rmi.IRemoteCode;
import com.data.account.data.UserData;
import com.data.account.factory.UserDataFactory;
import com.util.print.LogFactory;

import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

/**
 * This file is generated by system automatically.Don't Modify It.
 *
 * @author System
 */
@IRemoteCode(code = "CachePublicUser", type = "public", desc = "t_p_user : [UserID]")
public final class CachePublicUser extends AbstractCache
{
    private final String KEY_USER = "User:%s";

    private final String KEY_NAME = "User:%s";

    private final String KEY_USERNAME = "UserName:%s";

    private Map<Integer, List<String>> keyMap = new HashMap<>();

    public long lockUser(long userID)
    {
        String key = String.format(KEY_USER, userID);
        RedisClient client = getRedisClient();
        return client.lock(key);
    }

    public void unlockUser(long userID, long value)
    {
        String key = String.format(KEY_USER, userID);
        RedisClient client = getRedisClient();
        client.unlock(key, value);
    }

    public void reloadUserName()
    {
        List<String> list = UserDataFactory.getDao().queryOneColumnData("select UserName from t_p_user;", null);
        if (null != list)
        {
            RedisClient client = getRedisClient();
            String key = "";
            for (String data : list)
            {
                key = String.format(KEY_USERNAME, data);
                client.set(key, 0);
            }

            LogFactory.error("-------------Save UserName Total ={}-----------------", list.size());
        }
    }

    public boolean checkUserName(String userName)
    {
        String key = String.format(KEY_USERNAME, userName);
        RedisClient client = getRedisClient();
        Long userID = client.get(key, Long.class);
        if (userID != null)
            return true;
        else
            return false;
    }

    public boolean saveUserIDByName(long userID, String userName)
    {
        String key = String.format(KEY_USERNAME, userName);
        RedisClient client = getRedisClient();
        return client.set(key, userID);
    }

    public UserData getUserData(long userID)
    {
        String key = String.format(KEY_USER, userID);
        RedisClient client = getRedisClient();
        UserData info = client.get(key, UserData.class);
        if (info == null)
        {
            info = UserDataFactory.getDao().query("select * from t_p_user where `UserID`=?;", userID);
            if (null != info)
            {
                client.set(key, info);
            }
        }
        resetChanged(info);
        return info;
    }

    public UserData getUserDataByLock(long userID)
    {
        long lock = lockUser(userID);
        try
        {
            return getUserData(userID);
        }
        finally
        {
            unlockUser(userID, lock);
        }
    }

    public void updateUserData(final UserData info)
    {
        if (!info.isChanged())
            return;

        RedisClient client = getRedisClient();

        String key = String.format(KEY_USER, info.getUserID());
        client.set(key, info);

        synchronized (keyMap)
        {
            if (!keyMap.containsKey(client.getRedisID()))
                keyMap.put(client.getRedisID(), new ArrayList<>());

            keyMap.get(client.getRedisID()).add(key);
        }

        resetChanged(info);
    }

    public void addUserData(final UserData info)
    {
        if (!info.isChanged())
            return;

        RedisClient client = getRedisClient();

        String key = String.format(KEY_USER, info.getUserID());
        client.set(key, info);

        saveUserIDByName(info.getUserID(), info.getUserName());

        synchronized (keyMap)
        {
            if (!keyMap.containsKey(client.getRedisID()))
                keyMap.put(client.getRedisID(), new ArrayList<>());

            keyMap.get(client.getRedisID()).add(key);
        }

        resetChanged(info);
    }

    public void updateUserDataByLock(final UserData info)
    {
        long lock = lockUser(info.getUserID());
        try
        {
            updateUserData(info);
        }
        finally
        {
            unlockUser(info.getUserID(), lock);
        }
    }

    public boolean saveDB(List<UserData> list)
    {
        int[] result = UserDataFactory.getDao().addOrUpdateBatch(list);
        if (result == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public boolean save()
    {
        Map<Integer, List<String>> tempMap = new HashMap<>();

        synchronized (keyMap)
        {
            tempMap.putAll(keyMap);
            keyMap.clear();
        }

        Set<Integer> redisList = tempMap.keySet();
        for (int redisID : redisList)
        {
            RedisClient client = getRedisClient(redisID);
            List<UserData> infoList = new ArrayList<>();
            List<String> keyList = tempMap.get(redisID);

            try
            {
                for (String str : keyList)
                {
                    UserData info = client.get(str, UserData.class);
                    if (info != null)
                        infoList.add(info);
                    else
                        LogFactory.error("Update UserData -- info is not exist.key:{},client:{}", str, client.getRedisID());
                }

                if (!saveDB(infoList))
                {
                    synchronized (keyMap)
                    {
                        if (keyMap.containsKey(redisID))
                            keyMap.get(redisID).addAll(keyList);
                        else
                            keyMap.put(redisID, keyList);
                    }

                    LogFactory.error("Update UserData Failed,client:" + client.getRedisID());
                }
            }
            catch (Exception e)
            {
                synchronized (keyMap)
                {
                    if (keyMap.containsKey(redisID))
                        keyMap.get(redisID).addAll(keyList);
                    else
                        keyMap.put(redisID, keyList);
                }

                LogFactory.error("Update UserData Exception,client:" + client.getRedisID(), e);
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean saveAll()
    {
        List<RedisClient> list = new ArrayList<>();
        list.add(getRedisClient());
        for (RedisClient client : list)
        {
            try
            {
                boolean isFinish = false;
                String cursor = "0";
                while (!isFinish)
                {
                    ScanResult<String> resultList = client.scan(cursor, SAVE_COUNT, "User:*");
                    cursor = resultList.getCursor();
                    isFinish = resultList.getCursor().equals(ScanParams.SCAN_POINTER_START);

                    List<UserData> infoList = new ArrayList<>();
                    List<String> keyList = resultList.getResult();
                    for (String key : keyList)
                    {
                        UserData info = client.get(key, UserData.class);
                        if (info != null)
                        {
                            infoList.add(info);
                        }
                    }

                    boolean result = saveDB(infoList);
                    if (!result)
                    {
                        LogFactory.error("UserData Save All Failed.size:{}", infoList.size());
                    }
                }
            }
            catch (Exception e)
            {
                LogFactory.error("UserData Save All Exception.", e);
            }
        }

        return true;
    }

    @Override
    public boolean reload(long userID)
    {
        String key = String.format(KEY_USER, userID);
        RedisClient client = getRedisClient();
        UserData info = UserDataFactory.getDao().query("select * from t_p_user where `UserID`=?;", userID);
        if (null != info)
        {
            client.set(key, info);
            saveUserIDByName(info.getUserID(), info.getUserName());
        }
        return true;
    }

    public UserData getUserInfoByName(String gName, String openID)
    {
        String key = String.format(KEY_NAME, gName + "-openid-" + openID);
        RedisClient client = getRedisClient();
        Long userID = client.get(key, Long.class);
        if (userID == null)
        {
            DBParamWrapper param = new DBParamWrapper();
            param.put(gName);
            param.put(openID);
            userID = UserDataFactory.getDao().queryOneColumnDataOne("select UserID from t_p_user where `GName`=? And `OpenID`=?;", param);
            if (null != userID && userID > 0)
            {
                client.set(key, userID);
            }
        }

        if (userID != null && userID > 0)
        {
            UserData data = getUserData(userID);
            saveUserIDByName(data.getUserID(), data.getUserName());
            return data;
        }
        else
            return null;
    }

    public void updateUserGName(String gName, String openID, long userID)
    {
        String key = String.format(KEY_NAME, gName + "-openid-" + openID);
        RedisClient client = getRedisClient();
        client.set(key, userID);
    }

    public UserData getUserInfoByNameTest(String userName)
    {
        DBParamWrapper param = new DBParamWrapper();
        param.put(userName);
        Long userID = UserDataFactory.getDao().queryOneColumnDataOne("select UserID from t_p_user where `UserName`=?;", param);
        if (userID != null && userID > 0)
        {
            UserData data = getUserData(userID);
            saveUserIDByName(data.getUserID(), data.getUserName());
            return data;
        }
        else
            return null;
    }

    public UserData getUserInfoByMachineCode(String userName, String machineCode)
    {
        String key = String.format(KEY_NAME, userName + "-code-" + machineCode);
        RedisClient client = getRedisClient();
        Long userID = client.get(key, Long.class);
        if (userID == null)
        {
            DBParamWrapper param = new DBParamWrapper();
            param.put(userName);
            param.put(machineCode);
            userID = UserDataFactory.getDao().queryOneColumnDataOne("select UserID from t_p_user where `UserName`=? And `MachineCode`=?;",
                    param);
            if (null != userID && userID > 0)
            {
                client.set(key, userID);
            }
        }

        if (userID != null && userID > 0)
        {
            UserData data = getUserData(userID);
            saveUserIDByName(data.getUserID(), data.getUserName());
            return data;
        }
        else
            return null;
    }

}
