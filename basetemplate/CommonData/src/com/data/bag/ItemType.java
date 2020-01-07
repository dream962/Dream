package com.data.bag;

/**
 * 物品类型
 * 
 * @author dream
 *
 */
public enum ItemType
{
    /** 无*/
    NONE(-1, "无"),
    
    /** 资源*/
    RESOURCE(0, "资源"),

    /** 材料 */
    MATERIAL(1, "材料"),

    /** 加速材料 */
    TASK_ITEM(2, "加速材料"),

    /** 道具礼包 */
    TASK_REWARD(3, "道具礼包"),

    /** 士兵礼包 */
    ITEM_GP(4, "士兵礼包"),

    /** 随从 */
    ITEM_EVIL(5, "随从"),

    ;

    private int value;

    private String name;

    private ItemType(int value, String name)
    {
        this.value = (byte) value;
        this.setName(name);
    }

    public int getValue()
    {
        return value;
    }

    public static ItemType parse(int value)
    {
        ItemType[] states = ItemType.values();

        for (ItemType s : states)
        {
            if (s.getValue() == value)
                return s;
        }

        return NONE;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
