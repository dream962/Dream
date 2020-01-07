
package com.logic.cmd;

import com.base.net.CommonMessage;
import com.logic.game.AbstractGame;
import com.logic.living.Player;

/**
 * 战斗服游戏命令
 * @author dream.wang
 *
 */
public interface ICommandHandler
{
    void handleCommand(AbstractGame game, Player player, CommonMessage packet);
}
