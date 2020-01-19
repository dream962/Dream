package com.game.user.cmd.common;

import java.util.List;

import com.base.code.ErrorCodeType;
import com.base.command.ICode;
import com.base.net.CommonMessage;
import com.game.component.RankComponent;
import com.game.object.player.GamePlayer;
import com.game.user.cmd.AbstractUserCmd;
import com.logic.room.AbstractRoom;
import com.proto.command.UserCmdType.UserCmdInType;
import com.proto.command.UserCmdType.UserCmdOutType;
import com.proto.user.gen.UserInMsg.HeadIDProtoIn;
import com.proto.user.gen.UserOutMsg.ChangeHeaderProtoOut;
import com.util.print.LogFactory;

/**
 * 头像Header设置
 * 
 * @author dream
 *
 */
@ICode(code = UserCmdInType.SET_HEADID_VALUE)
public class SetHeaderCmd extends AbstractUserCmd
{
    @Override
    public void execute(GamePlayer player, CommonMessage packet)
    {
        try
        {
            HeadIDProtoIn proto = HeadIDProtoIn.parseFrom(packet.getBody());
            int headerID = proto.getHeadID();

            boolean iscontain = false;
            List<Integer> headers = player.getHeaders();
            for (int i : headers)
            {
                if (i == headerID)
                {
                    iscontain = true;
                    break;
                }
            }

            if (iscontain)
            {
                player.getPlayerInfo().setHeaderID(headerID);
                player.getSenderModule().sendRes();
                player.getDataModule().changeHeader(headerID);
                RankComponent.changeHeader(player.getUserID(), headerID);

                // 同步给房间其他人
                AbstractRoom room = player.getRoomModule().getCurrentRoom();
                if (room != null)
                {
                    ChangeHeaderProtoOut.Builder builder = ChangeHeaderProtoOut.newBuilder();
                    builder.setHeader(headerID);
                    builder.setUserID(player.getUserID());
                    room.sendToAll(builder, UserCmdOutType.CHANGE_HEADER_VALUE, player);
                }
            }
            else
            {
                player.sendErrorCode(ErrorCodeType.Item_Not_Exist, "资源无效.");
            }
        }
        catch (Exception e)
        {
            LogFactory.error("异常", e);
        }
    }
}
