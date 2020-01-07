package com.logic.type;

/**
 * 游戏内事件
 * 
 * @author dream
 *
 */
public enum GameEventType
{
    None(0),

    /** 游戏开始 */
    GameStart(1),

    /** 游戏结算 */
    GameOver(2),

    /** 游戏停止 */
    GameStop(3),
    
    /** 游戏内玩家被踢出*/
    GamePlayerKick(4),

    /** living死亡 */
    LivingDie(101),

    /** living攻击 */
    LivingAttacked(102),

    /** living受击 */
    LivingHurted(103),
    
    /** 阵营战数据 */
    Camp_Data(1006),
    ;

    private final byte value;

    private GameEventType(int value)
    {
        this.value = (byte) value;
    }

    public byte getValue()
    {
        return value;
    }

    public static GameEventType parse(int type)
    {
        for (GameEventType eventType : GameEventType.values())
        {
            if (type == eventType.value)
                return eventType;
        }

        return None;
    }

}
