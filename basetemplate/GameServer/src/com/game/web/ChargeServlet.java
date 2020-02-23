// package com.game.web;
//
// import java.util.Date;
//
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
//
// import com.base.code.ErrorCodeType;
// import com.base.component.GlobalConfigComponent;
// import com.base.web.PlayerHandlerServlet;
// import com.base.web.WebHandleAnnotation;
// import com.data.bean.ShopBean;
// import com.data.bean.factory.ShopBeanFactory;
// import com.data.factory.ChargeInfoFactory;
// import com.data.info.ChargeInfo;
// import com.data.info.PlayerInfo;
// import com.game.component.GamePlayerComponent;
// import com.game.object.player.GamePlayer;
// import com.util.JsonUtil;
// import com.util.print.LogFactory;
//
/// **
// * 创建充值订单
// *
// * @author dream
// *
// */
// @WebHandleAnnotation(cmdName = "/charge", description = "")
// public class ChargeServlet extends PlayerHandlerServlet
// {
// private static final long serialVersionUID = 1L;
//
// /**
// * 创建支付订单数据
// *
// * @author dream
// *
// */
// public static final class ResponseData
// {
// public String game_key;// 这里是游戏中心提供的game_key（required）
// public String open_id; // 小伙伴游戏游戏中心提供的用户id（required）
// public String total_fee; // 道具支付金额（单位元），精确到小数点后两位，（required）
// public String game_orderno; // 游戏生成的订单号（required，唯一）
// public String subject; // 游戏道具名称（required）
// public String notify_url; // 支付完成后通知URL（required）, 不要单独对这个参数进行encode
// public String timestamp; // 时间戳，1970-1-1至今秒数 （required）
// public String nonce; // 随机字符串（required）
// public String signature;// 签名（required）
//
// public String description;// 商品描述（用来保存自定义参数）
// }
//
// public class OrderInfo
// {
// public int orderID; // 订单ID
// public int serverID; // 服务器ID
// public long userID; // 账户服ID
// public String playerName; // 玩家昵称
// public String goodName; // 商品名称
// public int money; // 分为单位
// public int platform; // 充值平台
// public int shopID; // 商品ID
// public String goodDesc; // 商品描述
// }
//
// public class OrderReturn
// {
// public String orderID;
//
// }
//
// public static class RequestData
// {
// public int productId;
// public long userID;
// public String openID;
// }
//
// public static class ResponseReturn
// {
// public int code;
// public String message;
// }
//
// @Override
// protected String execute(String json, HttpServletRequest request, HttpServletResponse response)
// {
// ResponseReturn responseReturn = new ResponseReturn();
// try
// {
// RequestData requestData = JsonUtil.parseStringToObject(json, RequestData.class);
//
// int productId = requestData.productId;
// long userID = requestData.userID;
// String openID = requestData.openID;
//
// GamePlayer player = GamePlayerComponent.getPlayerByUserID(userID);
// if (player == null)
// {
// LogFactory.error("玩家不在线,UserID=" + userID);
// responseReturn.message = "玩家不在线.";
// responseReturn.code = ErrorCodeType.Charge_Player.getValue();
// return JsonUtil.parseObjectToString(responseReturn);
// }
//
// ShopBean shopBean = ShopBeanFactory.getShopBean(productId);
// if (shopBean == null)
// {
// LogFactory.error("充值配置不存在,productId=" + productId);
// responseReturn.message = "充值配置不存在.";
// responseReturn.code = ErrorCodeType.Charge_Config.getValue();
// return JsonUtil.parseObjectToString(responseReturn);
// }
//
// PlayerInfo playerInfo = player.getPlayerInfo();
//
// OrderInfo req = new OrderInfo();
// req.serverID = GlobalConfigComponent.getConfig().server.id;
// req.userID = playerInfo.getUserID();
// req.money = shopBean.getBuyCount1();
// req.goodName = shopBean.getShopName();
// req.playerName = playerInfo.getPlayerName();
// req.platform = 0;
// req.shopID = shopBean.getShopID();
//
// // req.money = 1;
//
// String order = HttpClient.getFromCharge("createOrder", JsonUtil.parseObjectToString(req));
// if (order == null || order.isEmpty())
// {
// LogFactory.error("请求SDK服创建订单异常,content=" + JsonUtil.parseObjectToString(req));
// responseReturn.message = "请求创建订单失败.";
// responseReturn.code = ErrorCodeType.Charge_Order.getValue();
// return JsonUtil.parseObjectToString(responseReturn);
// }
//
// OrderReturn orderReturn = JsonUtil.parseStringToObject(order, OrderReturn.class);
// if (orderReturn == null)
// {
// LogFactory.error("Order解析异常,productId=" + productId);
// responseReturn.message = "Order解析异常.";
// responseReturn.code = ErrorCodeType.Charge_Order.getValue();
// return JsonUtil.parseObjectToString(responseReturn);
// }
//
// responseReturn.message = order.trim();
//
// ChargeInfo charge = new ChargeInfo();
// charge.setOrderId(orderReturn.orderID);
// charge.setUserID(playerInfo.getUserID());
// charge.setAccountName(playerInfo.getPlayerName());
// charge.setMoneyType("RMB");
// charge.setPayTime(new Date());
// charge.setPayMoney(shopBean.getBuyCount1());
// charge.setPayPoint(shopBean.getBuyCount2());
// charge.setShopID(shopBean.getShopID());
// charge.setCreateTime(new Date());
// charge.setIsGet(false);
// charge.setIsNoticeChargeServer(false);
// charge.setOrderStatus(0);
// charge.setOs(0);
//
// if (!ChargeInfoFactory.getDao().addOrUpdate(charge))
// {
// LogFactory.error("订单入库失败,content=" + charge.getOrderId() + ",playerID:" + playerInfo.getUserID());
// responseReturn.message = "订单插入失败.";
// responseReturn.code = ErrorCodeType.Charge_Order_Insert.getValue();
// return JsonUtil.parseObjectToString(responseReturn);
// }
//
// responseReturn.code = ErrorCodeType.Success.getValue();
// return JsonUtil.parseObjectToString(responseReturn);
// }
// catch (Exception e)
// {
// LogFactory.error("", e);
// responseReturn.message = e.getMessage();
// responseReturn.code = ErrorCodeType.Error.getValue();
// return JsonUtil.parseObjectToString(responseReturn);
// }
// }
//
// }
