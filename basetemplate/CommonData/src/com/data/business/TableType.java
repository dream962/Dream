package com.data.business;

/**
 * 数据表类型
 */
public enum TableType
{
    NONE(0),
    
    /** 账号配置表*/
    T_U_ACCOUNT(1),

    /** 世界地图侦查表 */
    T_U_SCENE_EXPLORE(2),

    /** 世界地图行军战斗表 */
    T_U_SCENE_FIGHT(3),
    
    /** 玩家城市表 */
    T_U_CITY(4),
    
    /** 建筑表 */
    T_U_BUILDING(5),
    
    /** 道具配置表 */
    T_U_ITEM(6),
    
    /** 战斗记录表 */
    T_U_FIGHT_RECORD(7),

    ;

    private int value;

    private TableType(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    public static TableType parse(int value)
    {
        TableType[] states = TableType.values();

        for (TableType s : states)
        {
            if (s.getValue() == value)
                return s;
        }

        return NONE;
    }
}
