package com.upload.data;

public interface ServerType
{
    /** 游戏服/网关服 */
    public static final int GAME_SERVER = 1;

    /** 账户服 */
    public static final int ACCOUNT_SERVER = 2;

    /** SDK服 */
    public static final int CHARGE_SERVER = 3;

    /** 跨服 */
    public static final int CROSS_SERVER = 4;

    /** 活动服 */
    public static final int ACTIVITY_SERVER = 5;
    
    /** 资源服 */
    public static final int RESOURCE_SERVER = 6;
}
