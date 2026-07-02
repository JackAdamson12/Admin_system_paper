package org.example.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.example.commands.CommandFreeze;

public class FreezeEvent implements Listener
{
    private final CommandFreeze commandFreeze;

    public FreezeEvent(CommandFreeze commandFreeze)
    {
        this.commandFreeze = commandFreeze;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();

        if(commandFreeze.getArr().contains(player.getUniqueId()))
        {
            event.setTo(event.getFrom());
        }
    }
}