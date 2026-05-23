package org.example.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.example.commands.CommandGodMode;


public class GodModeEvent implements Listener
{
    private final CommandGodMode commandGodMode;

    public GodModeEvent(CommandGodMode commandGodMode)
    {
        this.commandGodMode = commandGodMode;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event)
    {
        if(event.getEntity() instanceof Player)
        {
            Player player = (Player) event.getEntity();

            if(commandGodMode.godList.contains(player))
            {
                event.setCancelled(true);
            }
        }
    }
}
