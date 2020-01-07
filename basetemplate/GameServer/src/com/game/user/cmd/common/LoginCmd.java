package com.game.user.cmd.common;

import java.util.Date;
import java.util.List;

import com.base.command.ICode;
import com.base.net.CommonMessage;
import com.base.net.client.IClientConnection;
import com.data.bean.HeaderBean;
import com.data.bean.MapBean;
import com.data.bean.RoleBean;
import com.data.bean.UnlockBean;
import com.data.bean.factory.HeaderBeanFactory;
import com.data.bean.factory.MapBeanFactory;
import com.data.bean.factory.RoleBeanFactory;
import com.data.bean.factory.UnlockBeanFactory;
import com.data.business.PlayerBusiness;
import com.data.business.SystemBusiness;
import com.data.business.TableType;
import com.data.info.PlayerInfo;
import com.game.component.GamePlayerComponent;
import com.game.object.player.GamePlayer;
import com.game.user.cmd.AbstractUserCmd;
import com.google.protobuf.InvalidProtocolBufferException;
import com.proto.command.UserCmdType.UserCmdInType;
import com.proto.user.gen.UserInMsg.LoginProtoIn;
import com.util.TimeUtil;
import com.util.TokenUtil;
import com.util.TokenUtil.TokenD;
import com.util.print.LogFactory;
import com.util.print.PrintFactory;

@ICode(code = UserCmdInType.USER_LOGIN_VALUE)
public class LoginCmd extends AbstractUserCmd
{
    private static final int OVER_TIME = 20 * 60 * 1000;

    @Override
    public void execute(GamePlayer gamePlayer, CommonMessage packet)
    {
        gamePlayer.disconnect(gamePlayer.getNetworkModule().getClientConnection(), true);
        GamePlayerComponent.removePlayer(gamePlayer.getUserID());
        LogFactory.error("Error : UserLoginCmd execute.");
    }

    /**
     * 玩家登陆时的处理方法
     * 
     * @param
     */
    public void executeConnect(IClientConnection conn, CommonMessage packet)
    {
        try
        {
            LoginProtoIn proto = LoginProtoIn.parseFrom(packet.getBody());
            String accountName = proto.getNickName();

            String token = proto.getToken();
            TokenD td = TokenUtil.decrypt(token);

            long accountID = 1;
            if (td != null)
            {
                accountID = td.id;
                // 防止超时
                if (td.time - System.currentTimeMillis() > OVER_TIME)
                {
                    LogFactory.error("登陆token过期");
                    return;
                }
            }
            else
            {
                LogFactory.error("登录协议错误." + token);
                return;
            }

            List<Integer> keys = proto.getKeysList();
            int[] key = null;
            if (keys != null)
            {
                key = new int[keys.size()];
                for (int i = 0; i < keys.size(); i++)
                    key[i] = keys.get(i);
            }

            PlayerInfo playerInfo = PlayerBusiness.queryPlayerByAccountID(accountID);
            if (playerInfo == null)
            {
                // 玩家不存在,注册玩家
                int userID = SystemBusiness.getTableMaxID(TableType.T_U_ACCOUNT.getValue());
                playerInfo = new PlayerInfo();
                playerInfo.setUserID(userID);
                playerInfo.setGold(0);
                playerInfo.setMoney(0);
                playerInfo.setAccountID(accountID);
                playerInfo.setAccountName(accountName);
                playerInfo.setAdExpireTime(TimeUtil.getInitDate());
                playerInfo.setPlayerName(accountName);
                playerInfo.setCreateTime(new Date());

                String temp = "";
                List<RoleBean> roles = RoleBeanFactory.getAll();
                for (RoleBean bean : roles)
                {
                    if (bean.getCount() <= 0)
                        temp += bean.getObjectID() + ",";

                }
                playerInfo.setRoleTypes(temp);

                temp = "";
                List<MapBean> maps = MapBeanFactory.getAll();
                for (MapBean bean : maps)
                {
                    if (bean.getCount() <= 0)
                        temp += bean.getObjectID() + ",";

                }
                playerInfo.setTheme(temp);

                temp = "";
                List<UnlockBean> unlocks = UnlockBeanFactory.getAll();
                for (UnlockBean bean : unlocks)
                {
                    if (bean.getItemCount() <= 0)
                        temp += bean.getModeType() + ",";

                }
                playerInfo.setModes(temp);

                temp = "";
                List<HeaderBean> headers = HeaderBeanFactory.getAll();
                for (HeaderBean bean : headers)
                {
                    if (bean.getConsumeItemCount() <= 0)
                        temp += bean.getHeadID() + ",";

                }
                playerInfo.setHeaders(temp);
                playerInfo.setHeaderID(0);
                playerInfo.setWinCount(0);
                playerInfo.setFailCount(0);
                playerInfo.setHeaderID(0);
                playerInfo.setIsCanUnlockCoinModeByAD(false);

                PlayerBusiness.addOrUpdatePlayer(playerInfo);
            }

            GamePlayer oldPlayer = GamePlayerComponent.getPlayerByUserID(playerInfo.getUserID());
            if (oldPlayer == null)
            {
                oldPlayer = new GamePlayer();
                oldPlayer.setPlayerInfo(playerInfo);
                if (conn != null)
                {
                    conn.setHolder(oldPlayer.getNetworkModule());
                    conn.setClientID(oldPlayer.getUserID());
                }

                oldPlayer.getNetworkModule().setClientConnection(conn);
                updateKey(conn, key);

                GamePlayerComponent.addPlayer(playerInfo.getUserID(), oldPlayer);
                if (!oldPlayer.login())
                {
                    GamePlayerComponent.removePlayer(playerInfo.getUserID());
                }
            }
            else
            {
                // 重登陆,如果玩家在线踢除玩家
                if (oldPlayer.getNetworkModule().isConnect())
                {
                    oldPlayer.getSenderModule().sendKickPlayer();
                    oldPlayer.disconnect(oldPlayer.getNetworkModule().getClientConnection(), true);

                    if (conn != null)
                    {
                        conn.setHolder(oldPlayer.getNetworkModule());
                        conn.setClientID(oldPlayer.getUserID());
                    }

                    oldPlayer.getNetworkModule().setClientConnection(conn);
                    updateKey(conn, key);
                    oldPlayer.relogin();

                    GamePlayerComponent.addPlayer(oldPlayer.getUserID(), oldPlayer);

                    LogFactory.warn("player was kicked success.id:" + oldPlayer.getUserID());
                }
                else
                {
                    if (conn != null)
                    {
                        conn.setHolder(oldPlayer.getNetworkModule());
                        conn.setClientID(oldPlayer.getUserID());
                    }
                    oldPlayer.getNetworkModule().setClientConnection(conn);
                    updateKey(conn, key);

                    oldPlayer.relogin();
                }
            }

            PrintFactory.error("player login success.id:" + oldPlayer.getUserID() + "," + oldPlayer.getNickName());
        }
        catch (InvalidProtocolBufferException e)
        {
            LogFactory.error("", e);
        }
    }

    /**
     * 更新玩家的加解密密钥
     * 
     * @param newKey
     *            新密钥
     */
    public void updateKey(IClientConnection client, int[] newKey)
    {
        try
        {
            if (client != null && newKey != null && newKey.length >= 8)
            {

                int[] temp1, temp2;
                temp1 = new int[newKey.length];
                System.arraycopy(newKey, 0, temp1, 0, newKey.length);
                temp2 = new int[newKey.length];
                System.arraycopy(newKey, 0, temp2, 0, newKey.length);

                client.setKeys(temp1, temp2);
            }
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }
    }

}
