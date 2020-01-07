package com.base.net.client;

/**
 * 连接持有者接口。一般由充当客户端角色的类来实现本接口，如GamePlayer。
 * 
 */
public interface IConnectionHolder
{
    public void onDisconnect(IClientConnection conn);

    public IClientConnection getClientConnection();

    public void setClientConnection(IClientConnection conn);
}
