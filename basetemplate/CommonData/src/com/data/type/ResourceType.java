package com.data.type;

/**
 * 资源类型，对应物品表的资源物品ID（1000以下）
 * 
 * @author dream
 */
public enum ResourceType
{
    NONE(0),

    /** 金币 */
    COIN(101),
    /** 甜甜圈 */
    TTQ(102),

    ;

    private final int value;

    private ResourceType(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    public static ResourceType parse(int value)
    {
        for (ResourceType type : ResourceType.values())
        {
            if (type.getValue() == value)
                return type;
        }

        return ResourceType.NONE;
    }
}
