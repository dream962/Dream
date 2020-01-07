package com.logic.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.net.CommonMessage;
import com.logic.cmd.GameCommandComponent;
import com.logic.cmd.ICommandHandler;
import com.logic.game.AbstractGame;
import com.logic.living.Player;

/**
 * 处理数据包
 * 
 * @version
 */
public class ProcessPacketAction implements IAction
{
    private static Logger logger = LoggerFactory.getLogger(ProcessPacketAction.class.getName());

    private Player player;

    private CommonMessage packet;

    public ProcessPacketAction(Player player, CommonMessage pkg)
    {
        this.player = player;
        this.packet = pkg;
    }

    public void execute(AbstractGame game, long tick)
    {
        try
        {
            short cmdId = this.packet.getCode();
            ICommandHandler handleCommand = GameCommandComponent.loadCommandHandler(cmdId);

            if (handleCommand != null)
            {
                handleCommand.handleCommand((AbstractGame) game, this.player, this.packet);
            }
        }
        catch (Exception e)
        {
            logger.error("ProcessPacketAction error!", e);
        }
    }

    public boolean isFinished(AbstractGame game, long tick)
    {
        return true;
    }

}
