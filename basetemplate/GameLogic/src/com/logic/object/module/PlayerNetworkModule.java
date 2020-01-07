package com.logic.object.module;

import com.base.net.client.IClientConnection;
import com.base.net.client.IConnectionHolder;
import com.logic.object.AbstractGamePlayer;

public class PlayerNetworkModule extends AbstractPlayerModule<AbstractGamePlayer> implements IConnectionHolder
{
    protected IClientConnection client;

    public PlayerNetworkModule(AbstractGamePlayer player)
    {
        super(player);
    }

    public boolean isConnect()
    {
        if (client != null)
            return true;

        return false;
    }

    @Override
    public IClientConnection getClientConnection()
    {
        return client;
    }

    @Override
    public void setClientConnection(IClientConnection conn)
    {
        this.client = conn;
    }

    @Override
    public void onDisconnect(IClientConnection conn)
    {
        player.disconnect(conn, false);
    }
}
