package com.logic.room;

/**
 * 房间状态类型
 * 
 * @author dream
 *
 */
public enum RoomStateType
{
    Ready(0),
    Using(1),
    Playing(2);

    private final int value;

    private RoomStateType(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    public static RoomStateType parse(int type)
    {
        for (RoomStateType dropType : RoomStateType.values())
        {
            if (type == dropType.value)
                return dropType;
        }

        return RoomStateType.Using;
    }
}
