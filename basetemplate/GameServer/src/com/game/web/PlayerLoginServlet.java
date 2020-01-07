package com.game.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.base.web.PlayerHandlerServlet;
import com.base.web.WebHandleAnnotation;

/**
 * 玩家登陆
 * 
 * @author dream
 *
 */
@WebHandleAnnotation(cmdName = "/login", description = "")
public class PlayerLoginServlet extends PlayerHandlerServlet
{
    private static final long serialVersionUID = -5283616801360439187L;

    public static class PlayerLoginData
    {
        public long accountID;
        public String accountName;
        public String playerName;
    }

    public static class PlayerLoginReturn
    {
        public long userID;
        public String address;
        public String port;
    }

    @Override
    protected String execute(String json, HttpServletRequest request, HttpServletResponse response)
    {
        // PlayerLoginData loginData = JsonUtil.parseSingleObject(json, PlayerLoginData.class);
        // if (loginData == null)
        // {
        // return JsonUtil.parseObjectToString(new RetInfo(ErrorCodeType.Param_Error, "参数错误"));
        // }
        //
        // PlayerInfo playerInfo = PlayerBusiness.queryPlayer(loginData.playerName, loginData.accountID);
        // if (playerInfo == null)
        // {
        // // 玩家不存在,注册玩家
        // long userID = SystemBusiness.getTableMaxID(TableType.T_U_ACCOUNT.getValue());
        // playerInfo = new PlayerInfo();
        // playerInfo.setUserID(userID);
        // playerInfo.setGold(0);
        // playerInfo.setAccountID(loginData.accountID);
        // playerInfo.setAccountName(loginData.accountName);
        // playerInfo.setIsAdHidden(false);
        // playerInfo.setPlayerName(loginData.playerName);
        //
        // PlayerBusiness.addOrUpdatePlayer(playerInfo);
        // }
        //
        // String host = GlobalConfigComponent.getConfig().server.address;
        // String port = GlobalConfigComponent.getConfig().server.ports;
        //
        // PlayerLoginReturn returnStr = new PlayerLoginReturn();
        // returnStr.address = host;
        // returnStr.port = port;
        // returnStr.userID = playerInfo.getUserID();
        //
        // return JsonUtil.parseObjectToString(new RetInfo(ErrorCodeType.Success, "用户登陆成功."));
        return "";
    }

}
