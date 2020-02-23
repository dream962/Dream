// package com.game.user.cmd.common;
//
// import com.base.command.ICode;
// import com.base.net.CommonMessage;
// import com.game.object.player.GamePlayer;
// import com.game.user.cmd.AbstractUserCmd;
// import com.proto.command.UserCmdType.UserCmdInType;
// import com.proto.command.UserCmdType.UserCmdOutType;
// import com.proto.user.gen.UserInMsg.RechargeProtoIn;
// import com.proto.user.gen.UserOutMsg.RechargeProtoOut;
// import com.util.print.LogFactory;
//
/// **
// * 充值
// *
// * @author dream
// *
// */
// @ICode(code = UserCmdInType.RECHARGE_VALUE)
// public class RechargeCmd extends AbstractUserCmd
// {
// @Override
// public void execute(GamePlayer player, CommonMessage packet)
// {
// try
// {
// RechargeProtoIn proto = RechargeProtoIn.parseFrom(packet.getBody());
// int configID = proto.getRechargeConfigID();
//
// RechargeProtoOut.Builder builder = RechargeProtoOut.newBuilder();
// builder.setOrderNum("");
// player.sendMessage(UserCmdOutType.RECHARGE_RESULT_VALUE, builder);
//
// }
// catch (Exception e)
// {
// LogFactory.error("异常", e);
// }
// }
// }