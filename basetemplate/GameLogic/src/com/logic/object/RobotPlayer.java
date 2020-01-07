package com.logic.object;

import com.base.net.client.IClientConnection;
import com.proto.common.gen.CommonOutMsg.ModeType;

public class RobotPlayer extends AbstractGamePlayer
{
    public RobotPlayer()
    {
        super();
        this.isRobot = true;
    }

    @Override
    public void disconnect(IClientConnection conn, boolean isForceKick)
    {

    }

    @Override
    public void onGameOver(ModeType type, int value, int diamond, int ttq)
    {
        switch (type)
        {
        case RandomMatch:
            if (value == 1)
            {
                getRoomModule().setWinCount(getRoomModule().getWinCount() + 1);
            }
            else
            {
                getPlayerInfo().setFailCount(getPlayerInfo().getFailCount() + 1);
            }
            break;
        default:
            break;
        }
    }

}
