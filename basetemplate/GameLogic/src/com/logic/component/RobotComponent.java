package com.logic.component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.base.component.AbstractComponent;
import com.data.bean.NameBean;
import com.data.bean.factory.NameBeanFactory;
import com.data.business.PlayerBusiness;
import com.data.info.PlayerInfo;
import com.logic.object.RobotPlayer;
import com.util.ThreadSafeRandom;
import com.util.print.LogFactory;

/**
 * 机器人组件信息
 * 
 * @author dream
 *
 */
public class RobotComponent extends AbstractComponent
{
    private static final int ROBOT_COUNT = 1000;

    /** 机器人列表 */
    private static Map<Integer, RobotPlayer> robotMap = new ConcurrentHashMap<>();

    /** 名称列表 */
    private static Set<String> nameSet = new HashSet<>();

    @Override
    public boolean initialize()
    {
        List<String> nameList = PlayerBusiness.queryNameList();
        nameSet.addAll(nameList);

        List<PlayerInfo> list = PlayerBusiness.queryRobot();
        if (list == null || list.size() < ROBOT_COUNT)
        {
            for (int i = 0; i < ROBOT_COUNT; i++)
            {
                PlayerInfo info = new PlayerInfo();
                info.setAccountID(i + 1);
                info.setAccountName("account" + i);
                info.setGold(100);
                info.setMoney(100);
                info.setPlayerName(getNewName(ThreadSafeRandom.next(0, 2)));
                info.setUserID(i + 1);

                list.add(info);

                RobotPlayer player = new RobotPlayer();
                player.setPlayerInfo(info);

                robotMap.put(player.getUserID(), player);
            }

            PlayerBusiness.addRobotList(list);
        }
        else
        {
            for (PlayerInfo info : list)
            {
                RobotPlayer player = new RobotPlayer();
                player.setPlayerInfo(info);

                robotMap.put(player.getUserID(), player);
            }
        }

        LogFactory.error("********************Robot Count:{}**************************", robotMap.size());
        return true;
    }

    @Override
    public void stop()
    {
        robotMap.clear();
    }

    public static void addName(String name)
    {
        nameSet.add(name);
    }

    public static String getNewName(int sex)
    {
        List<NameBean> all = NameBeanFactory.getAll();
        int nameSize = all.size();
        int times = 0;
        String name;
        String xingStr;
        String nameStr;
        do
        {
            // 名字范围有 700 * 700 * 2 那么大, 很低的几率
            if (times > 100)
            {
                LogFactory.error("生成昵称异常");
                name = "";
                break;
            }
            xingStr = all.get(ThreadSafeRandom.next(0, nameSize - 1)).getXing();
            if (sex == 1)
            {
                nameStr = all.get(ThreadSafeRandom.next(0, nameSize - 1)).getBoy();
            }
            else
            {
                nameStr = all.get(ThreadSafeRandom.next(0, nameSize - 1)).getGirl();
            }
            name = xingStr + nameStr;
            times++;
        }
        while (checkName(name));
        return name;
    }

    public static boolean checkName(String name)
    {
        return nameSet.contains(name);
    }

    public static RobotPlayer getRobot()
    {
        for (RobotPlayer player : robotMap.values())
        {
            if (player.getRoomModule().getCurrentRoom() == null
                    || (player.getRoomModule().getCurrentRoom() != null && player.getRoomModule().getCurrentRoom().getAllPlayer().size() <= 1))
                return player;
        }

        return null;
    }
}
