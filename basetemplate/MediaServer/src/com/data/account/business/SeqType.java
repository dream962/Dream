package com.data.account.business;

/**
 * 数据表类型
 */
public enum SeqType
{
    NONE(0),

    /** 账户服user表 */
    T_U_USER(100),

    /** 服务器表 */
    T_U_SERVER(101),

    /** 数据表 */
    T_U_DATA(102),
    ;

    private int value;

    private SeqType(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    public static SeqType parse(int value)
    {
        SeqType[] states = SeqType.values();

        for (SeqType s : states)
        {
            if (s.getValue() == value)
                return s;
        }

        return NONE;
    }
}
