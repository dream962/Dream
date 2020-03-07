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

    DONATE(101, "DONATE"),

    EXCHANGE(102, "EXCHANGE"),

    REVIVE(103, "REVIVE"),

    REMOVE_AD(104, "REMOVE_AD"),

    GAME_UNLOCK(105, "GAME_UNLOCK"),

    UNLOCKHEADID(106, "UNLOCKHEADID"),

    UNLOCKROLE(107, "UNLOCKROLE"),

    UNLOCKPLATFORM(108, "UNLOCKPLATFORM"),
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
