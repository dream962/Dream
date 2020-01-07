package com.game.user.cmd;

import com.base.command.ICommand;
import com.base.net.CommonMessage;
import com.base.net.client.IClientConnection;
import com.game.module.PlayerCmdQueueModule;
import com.game.object.player.GamePlayer;
import com.logic.object.module.AbstractPlayerModule;
import com.logic.object.module.PlayerNetworkModule;
import com.util.print.LogFactory;

/**
 * 抽象用户命令调度器
 * 
 * @author weihua.cui
 * 
 */

public abstract class AbstractUserCmd extends ICommand
{
    /**
     * 玩家登陆时的处理方法
     * 
     * @param
     */
    public void executeConnect(IClientConnection conn, CommonMessage packet)
    {

    }

    /**
     * 用户任务的调度分配， Handler调用
     */
    @Override
    public final void execute(IClientConnection conn, CommonMessage packet)
    {
        long time = System.currentTimeMillis();
        if (conn == null)
        {
            LogFactory.error("AbstractUserCmd IClientConnection Is Null.");
            return;
        }

        AbstractPlayerModule<?> networkModule = (PlayerNetworkModule) conn.getHolder();
        if (networkModule == null)
        {
            ConnectionCmdTask task = new ConnectionCmdTask(this, packet, conn);
            PlayerCmdQueueModule.getPool().getWorkerService().submit(task);
        }
        else
        {
            GamePlayer player = (GamePlayer) networkModule.getGamePlayer();
            UserCmdTask task = new UserCmdTask(this, packet, player);
            if (player.getCmdQueueModule().getQueue().add(task) == false)
            {
                player.disconnect(player.getNetworkModule().getClientConnection(), true);
                LogFactory.error("Player Add Too Much Action Over 200.Disconnect it.UserID:" + player.getUserID());
            }
        }

        if (System.currentTimeMillis() - time > 20)
        {
            LogFactory.error("AbstractUserCmd:" + this.getClass().getName() + " execute overtime. time:" + (System.currentTimeMillis() - time));
        }
    }

    public abstract void execute(GamePlayer player, CommonMessage packet);

}
