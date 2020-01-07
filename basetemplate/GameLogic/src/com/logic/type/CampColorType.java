package com.logic.type;

public enum CampColorType
{
    None(0),

    /** 红队 */
    RED(1),

    /** 蓝队 */
    BLUE(2),

    ;
    private final byte value;

    private CampColorType(int value)
    {
        this.value = (byte) value;
    }

    public byte getValue()
    {
        return value;
    }

    public static CampColorType parse(int type)
    {
        for (CampColorType eventType : CampColorType.values())
        {
            if (type == eventType.value)
                return eventType;
        }

        return None;
    }
}
