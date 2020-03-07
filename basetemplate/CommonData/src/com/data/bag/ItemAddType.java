package com.data.bag;

/**
 * 物品添加类型
 */
public enum ItemAddType
{
    /** 无 */
    NONE(0, "无"),

    AD(1, "AD"),

    CHEAT(2, "CHEAT"),

    EXCHANGE(3, "EXCHANGE"),

    GAME(4, "GAME"),

    CHARGE(5, "CHARGE"),
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
