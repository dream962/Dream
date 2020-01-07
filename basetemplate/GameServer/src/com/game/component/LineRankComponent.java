package com.game.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.base.component.AbstractComponent;
import com.data.component.GamePropertiesComponent;
import com.data.factory.LineRankInfoFactory;
import com.data.info.LineRankInfo;

public class LineRankComponent extends AbstractComponent
{
    private static final int LINE_RANGE = 30;
    /** <UserID,数据> */
    private static Map<Integer, LineRankInfo> lineRankMap = new ConcurrentHashMap<>();
    /** <order,数据列表> */
    private static Map<Long, Map<Integer, LineRankInfo>> orderMap = new ConcurrentHashMap<>();
    /** 修改的信息<UserID,UserID> */
    private static Map<Integer, Integer> changedMap = new ConcurrentHashMap<>();

    private static ReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public boolean initialize()
    {
        List<LineRankInfo> list = LineRankInfoFactory.getDao().listAll();

        list.sort((p1, p2) -> {
            if (p1.getLevel() == p2.getLevel())
                return p1.getUserID() - p2.getUserID();
            else if (p1.getLevel() > p2.getLevel())
                return 1;
            else
                return -1;
        });

        list.forEach(p -> {
            lineRankMap.put(p.getUserID(), p);
            orderMap.computeIfAbsent(p.getLevel(), k -> new HashMap<>()).put(p.getUserID(), p);
        });

        return true;
    }

    public void addChanged(int userID)
    {
        changedMap.put(userID, userID);
    }

    /**
     * 添加玩家信息
     * 
     * @param info
     */
    public void addLineRankInfo(LineRankInfo info)
    {
        lock.writeLock().lock();
        try
        {
            orderMap.computeIfAbsent(info.getLevel(), k -> new HashMap<>()).put(info.getUserID(), info);
            lineRankMap.put(info.getUserID(), info);
            addChanged(info.getUserID());
        }
        finally
        {
            lock.writeLock().unlock();
        }
    }

    /**
     * 更新玩家信息
     * 
     * @param info
     */
    public void updateLineRankInfo(int userID, long level)
    {
        lock.writeLock().lock();
        try
        {
            LineRankInfo info = null;
            // 先移除
            for (Entry<Long, Map<Integer, LineRankInfo>> e : orderMap.entrySet())
            {
                Map<Integer, LineRankInfo> map = e.getValue();
                if (map.containsKey(userID))
                {
                    info = map.remove(userID);
                    break;
                }
            }
            // 再添加
            if (info == null)
            {
                info = new LineRankInfo();
                info.setUserID(userID);
                info.setLevel(level);
                info.setLastAdTime(null);
                info.setLeftCount(GamePropertiesComponent.LINE_BASE_COUNT);
                addChanged(userID);
            }
            orderMap.computeIfAbsent(info.getLevel(), k -> new HashMap<>()).put(info.getUserID(), info);
        }
        finally
        {
            lock.writeLock().unlock();
        }
    }

    /**
     * 取得当前等级范围的玩家列表
     * 
     * @param level
     * @return
     */
    public List<LineRankInfo> getLineRankInfoByLength(int level)
    {
        int min = level - LINE_RANGE < 0 ? 0 : level - LINE_RANGE;
        int max = level + LINE_RANGE;

        List<LineRankInfo> list = new ArrayList<LineRankInfo>();

        lock.readLock().lock();
        try
        {
            for (int i = min; i <= max; i++)
            {
                Map<Integer, LineRankInfo> map = orderMap.get(i);
                list.addAll(map.values());
            }
        }
        finally
        {
            lock.readLock().unlock();
        }
        return list;
    }

    @Override
    public void stop()
    {
        orderMap.clear();
        lineRankMap.clear();
    }

}
