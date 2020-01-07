package com.base.net.client;

/**
 * 客户端连接的代理
 * 
 */
public interface IClientConnection
{
    String getClientIP();

    void setClientID(long clientID);

    long getClientID();

    int getPlatformID();

    int getAreaID();

    void setPlatformID(int id);

    void setAreaID(int id);

    /**
     * 获取数据包处理器。
     * 
     * @return
     */
    AbstractClientPacketHandler getPacketHandler();

    /**
     * 获取连接持有者。
     * 
     * @return
     */
    IConnectionHolder getHolder();

    /**
     * 设置连接持有者。
     * 
     * @param holder
     */
    void setHolder(IConnectionHolder holder);

    /**
     * 发送数据包。
     * 
     * @param packet
     */
    void send(Object packet);

    /**
     * 连接关闭时的回调。
     */
    void onDisconnect();

    public void setKeys(int[] encryptKeys, int[] decryptKeys);

    /**
     * 
     * @param immediately是否马上关闭
     */
    void closeConnection(boolean immediately);

    /**
     * 是否连接
     * 
     * @return
     */
    public boolean isConn();

}
