package com.base.config;

public class FightConfig
{
    /** 服务器计数ID */
    public int id;

    /** 服务器类型 */
    public String type;

    /** IP地址 */
    public String address;

    /** 配置多端口 */
    public String ports;

    /** KCP端口 */
    public int kcpPort;
    
    /** 秘钥 */
    public String privateModulus;
    
    /** 秘钥 */
    public String privateExponent;
    
    /** 最大人数 */
    public int maxPlayerCount;
    
    /** excel路径 */
    public String excelPath;
    
    /** 客户端战斗配置文件路径 */
    public String clientBattlePath;
}
