package com.game.user.cmd.player;

import com.base.command.ICode;
import com.base.net.CommonMessage;
import com.game.component.RankComponent;
import com.game.object.player.GamePlayer;
import com.game.user.cmd.AbstractUserCmd;
import com.proto.command.UserCmdType.UserCmdInType;
import com.proto.command.UserCmdType.UserCmdOutType;
import com.proto.common.gen.CommonOutMsg.RankSectionType;
import com.proto.common.gen.CommonOutMsg.RankTimeType;
import com.proto.common.gen.CommonOutMsg.RankType;
import com.proto.user.gen.UserInMsg.DonateItemProtoIn;
import com.proto.user.gen.UserOutMsg.DonateItemProtoOut;
import com.util.print.LogFactory;

/**
 * 捐献物品
 * 
 * @author dream
 *
 */
@ICode(code = UserCmdInType.DONATE_ITEM_VALUE)
public class DonateCmd extends AbstractUserCmd
{
    @Override
    public void execute(GamePlayer player, CommonMessage packet)
    {
        try
        {
            DonateItemProtoIn proto = DonateItemProtoIn.parseFrom(packet.getBody());
            int count = proto.getItemCount();
            int itemID = proto.getItemID();
            boolean byad = proto.getByAdDonate();
            int consumeType = proto.getConsumeType();

            if (byad == false)
                player.removeResource(itemID, count);

            int value = player.getPlayerInfo().getDonateValue() + count;
            player.getPlayerInfo().setDonateValue(value);

            player.getDataModule().addRank(RankType.GoodPersion, count);

            player.getSenderModule().sendRes();

            DonateItemProtoOut.Builder builder = DonateItemProtoOut.newBuilder();
            builder.setItemID(itemID);
            builder.setItemCount(count);
            builder.setConsumeType(consumeType);
            player.sendMessage(UserCmdOutType.DONATE_ITEM_RETURN_VALUE, builder);

            // 捐献完成后，发送排行榜信息
            RankComponent.sendRank(player, RankType.GoodPersion, RankTimeType.Month, RankSectionType.World);
            RankComponent.sendRank(player, RankType.GoodPersion, RankTimeType.Total, RankSectionType.World);
            RankComponent.sendRank(player, RankType.GoodPersion, RankTimeType.Week, RankSectionType.World);
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }
    }
}
