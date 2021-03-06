package com.game.user.cmd.player;

import com.base.code.ErrorCodeType;
import com.base.command.ICode;
import com.base.net.CommonMessage;
import com.data.bag.ItemAddType;
import com.data.bag.ItemRemoveType;
import com.data.bean.ExchangeBean;
import com.data.bean.factory.ExchangeBeanFactory;
import com.game.object.player.GamePlayer;
import com.game.user.cmd.AbstractUserCmd;
import com.proto.command.UserCmdType.UserCmdInType;
import com.proto.command.UserCmdType.UserCmdOutType;
import com.proto.user.gen.UserInMsg.ExchangeItemProtoIn;
import com.proto.user.gen.UserOutMsg.ExchangeItemProtoOut;
import com.util.print.LogFactory;

@ICode(code = UserCmdInType.EXCHANGE_ITEM_VALUE)
public class ExchangeItemCmd extends AbstractUserCmd
{
    @Override
    public void execute(GamePlayer player, CommonMessage packet)
    {
        try
        {
            ExchangeItemProtoIn proto = ExchangeItemProtoIn.parseFrom(packet.getBody());
            int configID = proto.getExchangeConfigID();

            ExchangeBean bean = ExchangeBeanFactory.getExchangeBean(configID);
            if (bean == null)
            {
                player.sendErrorCode(ErrorCodeType.Config_Error, "兑换配置无效");
                return;
            }

            if (!player.checkResource(bean.getConsumeItemID(), bean.getConsumeItemCount()))
            {
                player.sendErrorCode(ErrorCodeType.Not_Enough_Resource, "资源不足.");
                return;
            }

            player.removeResource(bean.getConsumeItemID(), bean.getConsumeItemCount(), ItemRemoveType.EXCHANGE);
            player.addResource(bean.getTargetItemID(), bean.getTargetItemCount(), ItemAddType.EXCHANGE);
            player.getSenderModule().sendRes();

            ExchangeItemProtoOut.Builder builder = ExchangeItemProtoOut.newBuilder();
            builder.setItemCount(bean.getTargetItemCount());
            builder.setItemID(bean.getTargetItemID());

            player.sendMessage(UserCmdOutType.EXCHANGE_ITEM_RETURN_VALUE, builder);
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }
    }
}
