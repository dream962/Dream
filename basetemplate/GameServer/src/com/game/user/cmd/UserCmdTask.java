package com.game.user.cmd;

import com.base.executor.AbstractTask;
import com.base.net.CommonMessage;
import com.game.object.player.GamePlayer;
import com.util.print.LogFactory;

/**
 * 用户网络命令任务
 * 
 * @author dream.wang
 *
 */
public class UserCmdTask extends AbstractTask
{
    protected GamePlayer player;
    private AbstractUserCmd cmd;
    private CommonMessage message;

    public UserCmdTask(AbstractUserCmd cmd, CommonMessage message, GamePlayer player)
    {
        super(player.getCmdQueueModule().getQueue());
        this.player = player;
        this.cmd = cmd;
        this.message = message;
    }

    @Override
    public void execute()
    {
        long begin = System.currentTimeMillis();

        cmd.execute(this.player, this.message);

        int interval = (int) (System.currentTimeMillis() - begin);

        if (interval >= MAX_INTERVAL)
            LogFactory.warn(String.format("UserCmdTask:[%s] spend too much time.time:[%d]", cmd.getClass().getName(), interval));
    }

    @Override
    public int getCode()
    {
        if (message != null)
            return message.getCode();
        return 0;
    }

    @Override
    public String getName()
    {
        return cmd.getClass().getName();
    }
}
