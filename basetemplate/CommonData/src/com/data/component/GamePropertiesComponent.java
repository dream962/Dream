package com.data.component;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.component.AbstractComponent;
import com.base.database.annotation.ConfigPropertyAnnotation;
import com.data.bean.ConfigFloatBean;
import com.data.bean.ConfigStringBean;

/**
 * 系统参数组件
 */
public class GamePropertiesComponent extends AbstractComponent
{
    private static final Logger LOGGER = LoggerFactory.getLogger(GamePropertiesComponent.class.getName());

    /************************************* 系统基础 *************************************/
    @ConfigPropertyAnnotation(key = "log_save_interval", defaultValue = "300", description = "玩家日志保存时间间隔", type = Integer.class)
    public static int LOG_SAVE_INTERVAL;
    @ConfigPropertyAnnotation(key = "monitor_job", defaultValue = "*/5 * * * * ?", description = "每5秒调用一次系统监控", type = String.class)
    public static String MONITOR_JOB;
    @ConfigPropertyAnnotation(key = "job_ping_player", defaultValue = "10", description = "每10秒ping一次玩家", type = Integer.class)
    public static int JOB_PING_PLAYER;
    @ConfigPropertyAnnotation(key = "game_update_slow", defaultValue = "30000", description = "玩家都不在30秒后更新放缓", type = Integer.class)
    public static int GAME_UPDATE_SLOW;
    @ConfigPropertyAnnotation(key = "game_player_ping_interval", defaultValue = "5000", description = "游戏内玩家ping间隔 毫秒", type = Integer.class)
    public static int GAME_PLAYER_PING_INTERVAL;
    @ConfigPropertyAnnotation(key = "job_ping_fight", defaultValue = "5", description = "每5秒ping一次战斗服", type = Integer.class)
    public static int JOB_PING_FIGHT;
    @ConfigPropertyAnnotation(key = "job_save_player", defaultValue = "5", description = "定时器保存玩家时间间隔（秒）", type = Integer.class)
    public static int JOB_SAVE_PLAYER;
    @ConfigPropertyAnnotation(key = "save_player_time", defaultValue = "120", description = "每2分钟同步一次玩家信息（秒）", type = Integer.class)
    public static int SAVE_PLAYER_TIME;
    @ConfigPropertyAnnotation(key = "job_corn_refresh", defaultValue = "1 0 0 * * ? *", description = "每天凌晨刷新用户信息", type = String.class)
    public static String JOB_CORN_REFRESH;
    @ConfigPropertyAnnotation(key = "job_rank_refresh", defaultValue = "0 0 4 * * ? *", description = "每天凌晨刷新排行信息", type = String.class)
    public static String JOB_RANK_REFRESH;
    @ConfigPropertyAnnotation(key = "ping_max_time", defaultValue = "300", description = "ping延时最大时间(秒)", type = Integer.class)
    public static int PING_MAX_TIME;
    @ConfigPropertyAnnotation(key = "room_max_count", defaultValue = "3000", description = "起始房间最大个数", type = Integer.class)
    public static int ROOM_MAX_COUNT;
    @ConfigPropertyAnnotation(key = "sunday_refresh", defaultValue = "0 0 0 ? * 1 *", description = "周日凌晨刷新数据", type = String.class)
    public static String SUNDAY_REFRESH;
    @ConfigPropertyAnnotation(key = "month_refresh", defaultValue = "0 0 0 1 * ? *", description = "每月刷新数据", type = String.class)
    public static String MONTH_REFRESH;
    @ConfigPropertyAnnotation(key = "monitor_room_interval", defaultValue = "60", description = "每60秒调用一次房间监控", type = Integer.class)
    public static int MONITOR_ROOM_INTERVAL;
    @ConfigPropertyAnnotation(key = "fight_player_ping_interval", defaultValue = "3", description = "战斗服玩家ping间隔", type = Integer.class)
    public static int FIGHT_PLAYER_PING_INTERVAL;
    @ConfigPropertyAnnotation(key = "battle_game_time", defaultValue = "2400000", description = "PvP战斗最大时间（毫秒）", type = Integer.class)
    public static int BATTLE_GAME_TIME;

    @ConfigPropertyAnnotation(key = "line_base_count", defaultValue = "5", description = "每日基础次数", type = Integer.class)
    public static int LINE_BASE_COUNT;

    @ConfigPropertyAnnotation(key = "base_step_time", defaultValue = "500", description = "每一层跳跃时间", type = Integer.class)
    public static int BASE_STEP_TIME;

    @ConfigPropertyAnnotation(key = "base_step_time_rate", defaultValue = "1.5", description = "相差比率", type = Float.class)
    public static float BASE_STEP_TIME_RATE;

    @ConfigPropertyAnnotation(key = "base_success", defaultValue = "70", description = "每一层成功概率", type = Integer.class)
    public static int BASE_SUCCESS;

    @ConfigPropertyAnnotation(key = "base_success_rate", defaultValue = "2", description = "每一等级成功增加值", type = Integer.class)
    public static int BASE_SUCCESS_RATE;

    @ConfigPropertyAnnotation(key = "challengeUnlockCount", defaultValue = "200", description = "挑战模式解锁里程数", type = Integer.class)
    public static int CHALLENGE_UNLOCK_COUNT;

    @ConfigPropertyAnnotation(key = "unlockChallengeItemID", defaultValue = "102", description = "解锁挑战模式的消耗物品ID", type = Integer.class)
    public static int UNLOCK_CHALLENGE_ITEM_ID;

    @ConfigPropertyAnnotation(key = "unlockChallengeItemCount", defaultValue = "50", description = "解锁挑战模式的消耗物品数量", type = Integer.class)
    public static int UNLOCK_CHALLENGE_ITEM_COUNT;

    /**
     * 解析指定值到指定类型
     * 
     * @param fieldType
     *            字段类型
     * @param value
     *            以字符串表示的待转换值
     * @return 转换后的结果
     */
    private Object parseValue(Class<?> fieldType, String value)
    {
        Object fieldValue = null;
        value = value.trim();

        try
        {
            if (fieldType == Integer.class)
            {
                fieldValue = (int) Float.parseFloat(value);
            }
            else if (fieldType == Boolean.class)
            {
                fieldValue = Boolean.parseBoolean(value);
            }
            else if (fieldType == Date.class)
            {
                fieldValue = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(value);
            }
            else if (fieldType == Double.class)
            {
                fieldValue = Double.parseDouble(value);
            }
            else if (fieldType == Float.class)
            {
                fieldValue = Float.parseFloat(value);
            }
            else
            {
                fieldValue = value;
            }
            return fieldValue;
        }
        catch (Exception e)
        {
            LOGGER.error("Exception : ServerProperties Load: ", e);
            return null;
        }
    }

    @Override
    public boolean initialize()
    {
        try
        {
            Field[] allFields = GamePropertiesComponent.class.getFields();

            // 字符串配置、float配置
            List<ConfigFloatBean> floatList = SystemDataComponent.getBeanList(ConfigFloatBean.class);
            List<ConfigStringBean> stringList = SystemDataComponent.getBeanList(ConfigStringBean.class);

            Map<String, String> temp = new HashMap<>();
            floatList.forEach(p -> temp.put(p.getKeyID(), String.valueOf(p.getValue())));
            stringList.forEach(p -> temp.put(p.getKeyID(), p.getValue()));

            for (Field field : allFields)
            {
                ConfigPropertyAnnotation annotation = field.getAnnotation(ConfigPropertyAnnotation.class);

                if (annotation != null)
                {
                    String valueString = annotation.defaultValue();

                    boolean find = false;

                    for (Entry<String, String> bean : temp.entrySet())
                    {
                        if (bean.getKey().equalsIgnoreCase(annotation.key()))
                        {
                            valueString = bean.getValue();
                            find = true;
                            break;
                        }
                    }

                    if (!find)
                        LOGGER.error("Cannot find server property keep it default value.  -- " + annotation.key()
                                + " = " + valueString);

                    try
                    {
                        field.set(null, parseValue(annotation.type(), valueString));
                    }
                    catch (Exception e)
                    {
                        LOGGER.error(annotation.key() + " " + field.getName() + "fail to load GamePropertiesComponent",
                                e);
                        return false;
                    }
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("加载配置出错", e);
            return false;
        }

        return true;
    }

    @Override
    public void stop()
    {

    }

}
