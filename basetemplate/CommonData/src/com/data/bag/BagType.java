package com.data.bag;

/**
 * 背包类型
 * 
 * @author dream
 *
 */
public enum BagType
{
    NULL(0),                                // 没有放入任何背包的物品
    /** 主背包 */
    MAIN(1),                                // 主背包
    ;
    private byte value;

    private BagType(int value)
    {
        this.value = (byte) value;
    }

    /**
     * @return the value
     */
    public int getValue()
    {
        return value;
    }

    public static BagType parse(int value)
    {
        BagType[] states = BagType.values();

        for (BagType s : states)
        {
            if (s.getValue() == value)
                return s;
        }

        return NULL;
    }
}
