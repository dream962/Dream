package com.logic.object;

/**
 * gameplayer的模块类型
 * 
 * @author dream
 *
 */
public enum AbstractPlayerModuleType
{
    NONE(0),

    DATA(1),

    /** 命令自驱动组件 */
    CMD_QUEUE(2),

    /** 网络组件 */
    NETWORK(3),

    /** 房间组件 */
    ROOM(4),

    /** 事件组件 */
    EVENT(5),

    /** UDP网络组件 */
    KCP(6),

    /** 代理发送模块 */
    PROXY_SENDER(11),

    /** Ping */
    PING(12),

    /** 战斗服连接 */
    FIGHT_CONN(14),

    /** 背包 */
    BAG(16),

    /** 战斗服与游戏服网络连接模块 */
    FIGHT_TCP(101),
    /** 战斗服代理玩家ping */
    FIGHT_PING(102),

    /** 脚本 */
    SCRIPT(99),
    ;

    private final int value;

    private AbstractPlayerModuleType(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    public static AbstractPlayerModuleType parse(int value)
    {
        for (AbstractPlayerModuleType type : AbstractPlayerModuleType.values())
        {
            if (type.getValue() == value)
                return type;
        }

        return AbstractPlayerModuleType.NONE;
    }
}
