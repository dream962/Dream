package com.base.net;

import com.base.component.AbstractCommandComponent;

/**
 * 默认命令组件
 */
public class DefaultCommandComponent extends AbstractCommandComponent
{
    @Override
    public String getCommandPacketName()
    {
        return "com.game.command";
    }
}
