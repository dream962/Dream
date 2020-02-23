package com.game.component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.base.component.AbstractComponent;
import com.util.ThreadSafeRandom;

/**
 * 掉落奖励组件
 * 
 * @author dream
 *
 */
public class RewardDropComponent extends AbstractComponent
{
    private static ReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public boolean initialize()
    {
        lock.writeLock().lock();
        try
        {

        }
        finally
        {
            lock.writeLock().unlock();
        }

        return true;
    }

    /**
     * 圆盘算法，返回位置索引
     * 
     * @param list
     * @return
     */
    private static int calculateCircleRandomInt(List<Integer> list)
    {
        List<Integer> temp = new ArrayList<>();
        int sum = 0;
        for (int i = 0; i < list.size(); i++)
        {
            sum += list.get(i);
            temp.add(sum);
        }

        int value = ThreadSafeRandom.next(0, sum + 1);
        for (int i = 0; i < temp.size(); i++)
        {
            if (value <= temp.get(i))
                return i;
        }
        return 0;
    }

    public static RewardDropData createRewards(int rewardID)
    {
        RewardDropData data = new RewardDropData();

        return data;
    }

    // public static List<ItemDetail> wrapReward(RewardDropData data)
    // {
    // List<ItemDetail> list = new ArrayList<>();
    //
    // for (RewardDetail detail : data.list)
    // {
    // ItemDetail.Builder builder = ItemDetail.newBuilder();
    // builder.setItemCount(detail.itemCount);
    // builder.setItemID(detail.itemID);
    //
    // list.add(builder.build());
    // }
    //
    // return list;
    // }

    @Override
    public void stop()
    {

    }

    /**
     * 掉落的数据
     * 
     * @author dream
     *
     */
    public static class RewardDropData
    {
        public List<RewardDetail> list = new ArrayList<>();
    }

    /**
     * 掉落详细
     * 
     * @author dream
     *
     */
    public static class RewardDetail
    {
        public int itemID;
        public int itemCount;
    }

}
