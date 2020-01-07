package com.game.module;

import com.base.event.EventSource;
import com.game.object.player.GamePlayer;
import com.logic.object.module.AbstractPlayerModule;

public class PlayerEventModule extends AbstractPlayerModule<GamePlayer>
{
    private EventSource event = new EventSource();;

    public PlayerEventModule(GamePlayer player)
    {
        super(player);
    }

    public EventSource getEvent()
    {
        return event;
    }
}
