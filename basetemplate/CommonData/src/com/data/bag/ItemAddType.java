package com.data.bag;

/**
 * 物品添加类型
 */
public enum ItemAddType
{
    /** 无 */
    NONE(0, "无"),

    /** 使用道具 */
    USE(1, "使用道具"),

    /** 售卖 */
    SELL(2, "售卖"),

    /** 任务 */
    TASK(3, "任务"),

    /** 人口自然增长 */
    BUILDING_POLULACE(4, "人口自然增长"),
    /** 工业生产 */
    BUILDING_INDUSTRY_PRODUCT(5, "工业生产"),
    /** 招募居民 */
    BUILDING_POLULACE_CALL(6, "招募居民"),
    /** 招募士兵 */
    BUILDING_SOLDIER_CALL(7, "招募士兵"),
    /** 抽卡 */
    HERO_CARD(8, "英雄抽卡"),
    /** 矿点采集资源 */
    ARMY_MINE(9, "矿点采集资源"),
    /** 土地区块定时收益 */
    AREA_REFRESH(10, "土地区块定时收益"),

    ;

    private int value;

    private String name;

    ItemAddType(int value, String name)
    {
        this.value = value;
        this.name = name;
    }

    public int getValue()
    {
        return value;
    }

    public String getName()
    {
        return name;
    }

    public static ItemAddType parse(int value)
    {
        for (ItemAddType type : ItemAddType.values())
        {
            if (type.getValue() == value)
                return type;
        }

        return ItemAddType.NONE;
    }
}
