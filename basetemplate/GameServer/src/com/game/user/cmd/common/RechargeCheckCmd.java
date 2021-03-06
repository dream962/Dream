package com.game.user.cmd.common;

import com.base.command.ICode;
import com.base.net.CommonMessage;
import com.game.object.player.GamePlayer;
import com.game.user.cmd.AbstractUserCmd;
import com.proto.command.UserCmdType.UserCmdInType;
import com.proto.user.gen.UserInMsg.RechargeVerifyProtoIn;
import com.util.print.LogFactory;

/**
 * 充值验证
 * 
 * @author dream
 */
@ICode(code = UserCmdInType.RECHARGE_VERIFY_VALUE)
public class RechargeCheckCmd extends AbstractUserCmd
{
    @Override
    public void execute(GamePlayer player, CommonMessage packet)
    {
        try
        {
            RechargeVerifyProtoIn proto = RechargeVerifyProtoIn.parseFrom(packet.getBody());
            String orderID = proto.getOrderID();
            String purchaseToken = proto.getPurchaseToken();
            player.getDataModule().chargeCheck(orderID, purchaseToken);
        }
        catch (Exception e)
        {
            LogFactory.error("异常", e);
        }
    }
}
