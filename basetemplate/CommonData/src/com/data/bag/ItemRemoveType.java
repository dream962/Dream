package com.data.bag;

/**
 * 物品的移除类型
 * 
 * @author dream
 *
 */
public enum ItemRemoveType
{
    /** 空 */
    NONE(0, "空"),

    /** 物品使用 */
    USE(1, "物品使用 "),

    /** 建筑创建 */
    BUILDING_CREATE(10, "建筑创建"),
    /** 建筑升级 */
    BUILDING_UPDATE(11, "建筑升级"),
    /** 人口自然增长 */
    BUILDING_POLULACE(12, "人口自然增长"),
    /** 工业生产 */
    BUILDING_INDUSTRY_PRODUCT(13, "工业生产"),
    /** 招募居民 */
    BUILDING_POLULACE_CALL(14, "招募居民"),
    /** 招募士兵 */
    BUILDING_SOLDIER_CALL(15, "招募士兵"),
    /** 抽卡花费 */
    HERO_CARD(16, "抽卡花费"),
    /** 科技加速 */
    SCIENCE_SPEED(17, "科技加速"),
    
    /** 一键侦查迷雾 */
    ARMY_EXPLORE(30, "一键侦查迷雾"),

    /** 任务 */
    TASK_QUICK_COMPLETE(100, "任务 "),

    /** 任务刷新 */
    TASK_REFRESH(101, "任务刷新 "),

    /** 任务完成 */
    TASK_FINISH(102, "任务完成 "),

    ;

    private byte value;

    private String name;

    ItemRemoveType(int value, String name)
    {
        this.value = (byte) value;
        this.name = name;
    }

    public byte getValue()
    {
        return value;
    }

    public String getName()
    {
        return name;
    }

    public static ItemRemoveType parse(int value)
    {
        for (ItemRemoveType type : ItemRemoveType.values())
        {
            if (type.getValue() == value)
                return type;
        }

        return ItemRemoveType.NONE;
    }
}
