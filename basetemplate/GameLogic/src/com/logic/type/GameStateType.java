package com.logic.type;

/**
 * 游戏当前状态
 */
public enum GameStateType
{
    Inited(1),
    Prepare(2),
    Loading(3),
    SessionPrepared(4),
    Playing(5),
   PlayerOver(6),
    GameOver(7),
    Stopped(8);

    private final byte value;

    private GameStateType(int value)
    {
        this.value = (byte) value;
    }

    public byte getValue()
    {
        return value;
    }
    
    public GameStateType parse(int state)
    {
        GameStateType[] states=GameStateType.values();
        
        for(GameStateType s:states)
        {
            if(s.getValue()==state)
                return s;
        }
        
        return Inited;
    }
}
