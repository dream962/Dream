package com.game.user.cmd.player;

import com.base.command.ICode;
import com.base.net.CommonMessage;
import com.data.business.PlayerBusiness;
import com.game.object.player.GamePlayer;
import com.game.user.cmd.AbstractUserCmd;
import com.game.web.HttpClient;
import com.proto.command.UserCmdType.UserCmdInType;
import com.proto.user.gen.UserInMsg.BindAccountProtoIn;
import com.util.JsonUtil;
import com.util.print.LogFactory;

/**
 * 账号绑定
 *
 * @author dream
 *
 */
@ICode(code = UserCmdInType.BIND_ACCOUNT_VALUE)
public class AccountBindCmd extends AbstractUserCmd
{
    public static class BindInfo
    {
        public int code;
        public String msg;
        public String keyword;
    }

    @Override
    public void execute(GamePlayer player, CommonMessage packet)
    {
        try
        {
            BindAccountProtoIn proto = BindAccountProtoIn.parseFrom(packet.getBody());
            String name = proto.getName();
            String machineCode = proto.getMachinecode();
            String gName = proto.getGoogleName();
            String openID = proto.getOpenID();

            player.getPlayerInfo().setPlayerName(name);
            player.getPlayerInfo().setAccountName(gName);

            PlayerBusiness.addOrUpdatePlayer(player.getPlayerInfo());

            String params = String.format("{\"name\":\"%s\",\"machineCode\":\"%s\",\"gName\":\"%s\",\"openID\":\"%s\"}", name, machineCode,
                    gName, openID);
            // 同步账号服
            String result = HttpClient.getFromAccount("bindAccount", params);
            if (result != null)
            {
                BindInfo info = JsonUtil.parseStringToObject(result, BindInfo.class);
                if (info != null && info.msg.equalsIgnoreCase("ok"))
                {
                    LogFactory.error("绑定成功.{},{}", openID, gName);
                }
            }
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }
    }
}
