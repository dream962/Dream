package com.logic.object;

import java.util.LinkedHashMap;
import java.util.Map;

import com.base.code.ErrorCodeType;
import com.base.net.CommonMessage;
import com.base.net.client.IClientConnection;
import com.data.info.PlayerInfo;
import com.google.protobuf.GeneratedMessage.Builder;
import com.logic.object.module.AbstractPlayerModule;
import com.logic.object.module.PlayerNetworkModule;
import com.logic.object.module.PlayerRoomModule;
import com.proto.command.UserCmdType.UserCmdOutType;
import com.proto.common.gen.CommonOutMsg.ModeType;
import com.proto.user.gen.UserOutMsg.ErrorCodeProtoOut;

/**
 * 游戏玩家接口
 * 
 * @author dream.wang
 *
 */
public abstract class AbstractGamePlayer
{
    /** 玩家组件 */
    protected Map<Integer, AbstractPlayerModule<?>> moduleMap = new LinkedHashMap<Integer, AbstractPlayerModule<?>>();

    protected PlayerInfo playerInfo;

    protected boolean isRobot;

    protected int robotLevel = 1;

    public AbstractGamePlayer()
    {
        initModule();
    }

    public int getRobotLevel()
    {
        return this.robotLevel;
    }

    public void setRobotLevel(int level)
    {
        this.robotLevel = level;
    }

    public boolean getIsRobot()
    {
        return this.isRobot;
    }

    public void setPlayerInfo(PlayerInfo info)
    {
        this.playerInfo = info;
    }

    public PlayerInfo getPlayerInfo()
    {
        return playerInfo;
    }

    public int getUserID()
    {
        return playerInfo.getUserID();
    }

    public String getNickName()
    {
        return playerInfo.getPlayerName();
    }

    protected void initModule()
    {
        moduleMap.put(AbstractPlayerModuleType.NETWORK.getValue(), new PlayerNetworkModule(this));
        moduleMap.put(AbstractPlayerModuleType.ROOM.getValue(), new PlayerRoomModule(this));
    }

    public void addModule(AbstractPlayerModuleType type, AbstractPlayerModule<?> module)
    {
        moduleMap.put(type.getValue(), module);
    }

    @SuppressWarnings("unchecked")
    public <T extends AbstractPlayerModule<?>> T getModule(AbstractPlayerModuleType type)
    {
        return (T) moduleMap.get(type.getValue());
    }

    public PlayerNetworkModule getNetworkModule()
    {
        return getModule(AbstractPlayerModuleType.NETWORK);
    }

    public PlayerRoomModule getRoomModule()
    {
        return getModule(AbstractPlayerModuleType.ROOM);
    }

    /**************************************************************************/

    public void sendMessage(CommonMessage packet)
    {
        if (getNetworkModule().isConnect())
        {
            packet.setParam(getUserID());

            if (getNetworkModule().getClientConnection() != null)
                getNetworkModule().getClientConnection().send(packet);
        }
    }

    public void sendMessage(int code, Builder<?> builder)
    {
        if (getNetworkModule().isConnect())
        {
            CommonMessage packet = new CommonMessage(code);

            if (builder != null)
            {
                packet.setBody(builder.build().toByteArray());
            }

            sendMessage(packet);
        }
    }

    public void sendErrorCode(ErrorCodeType code, String... strings)
    {
        ErrorCodeProtoOut.Builder builder = ErrorCodeProtoOut.newBuilder();
        builder.setErrorID(code.getValue());
        sendMessage(UserCmdOutType.ERROR_CODE_RETURN_VALUE, builder);
    }

    /**************************************************************************/

    public abstract void disconnect(IClientConnection conn, boolean isForceKick);

    public void onGameOver(ModeType type, int value, int diamondCount, int ttqCount)
    {

    }

    public void onExitBout()
    {

    }
}
