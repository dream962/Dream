package com.game.component;

import com.base.command.ICommand;
import com.base.component.AbstractCommandComponent;
import com.proto.command.UserCmdType.GameCmdType;

public class UserCommandComponent extends AbstractCommandComponent
{
    @Override
    public String getCommandPacketName()
    {
        return "com.game.user.cmd";
    }

    /**
     * 二级协议特殊处理
     */
    public ICommand getCommand(short code)
    {
        ICommand cmd = cmdCache.get(code);
        if (cmd != null)
            return cmd;

        if (code > GameCmdType.TWO_MIN_VALUE && code < GameCmdType.TWO_MAX_VALUE)
            return cmdCache.get((short) GameCmdType.TWO_MAX_VALUE);

        return null;
    }
}
