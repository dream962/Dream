package com.logic.type;

/**
 * 关卡类型
 */
public enum MissionType
{
    None(0),
    /** 普通副本 */
    Simple(1),

    /** pve测试 */
    PVP_Test(100),
    ;

    private final int value;

    private MissionType(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    public static MissionType parse(int value)
    {
        MissionType[] types = MissionType.values();
        for (MissionType type : types)
        {
            if (type.getValue() == value)
                return type;
        }

        return None;
    }
}
