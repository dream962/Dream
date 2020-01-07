package com.logic.actions;

import com.logic.game.AbstractGame;
import com.logic.game.PVPGame;

/**
 * PVP状态机处理
 * 
 * @author dream.wang
 *
 */
public class CheckPVPGameStateAction implements IAction
{
    private long tick;
    private boolean isFinished;

    public CheckPVPGameStateAction(int delay)
    {
        this.isFinished = false;
        this.tick += System.currentTimeMillis() + delay;
    }

    public void execute(AbstractGame game, long tick)
    {
        if (this.tick <= tick)
        {
            PVPGame pvp = null;

            if (game instanceof PVPGame)
                pvp = (PVPGame) game;

            if (pvp != null)
            {
                switch (pvp.getGameState())
                {
                case Inited:
                    pvp.prepare();
                    break;

                case Prepare:
                    pvp.loading();
                    break;

                case Loading:
                    if (pvp.isAllComplete())
                        pvp.start();
                    else
                        pvp.pause(1000);
                    break;

                case Playing:
                    if (pvp.checkGameOver())
                        pvp.gameover();

                    break;

                case GameOver:
                    if (pvp.getActionCount() <= 1)
                        pvp.stop();
                    break;

                default:
                    break;
                }
            }
            this.isFinished = true;
        }
    }

    public boolean isFinished(AbstractGame game, long tick)
    {
        return this.isFinished;
    }
}
