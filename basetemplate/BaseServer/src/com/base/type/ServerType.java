package com.base.type;

/**
 * 服务器类型
 * 
 * @author dream.wang
 * 
 */
public interface ServerType
{
    /** 游戏服 */
    static final int GameServer = 1;
    
    /** 战斗服 */
    static final int FightServer = 2;

    /** 缓存服 */
    static final int CacheServer = 3;
}
