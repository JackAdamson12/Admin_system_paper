package org.example.events;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.example.commands.punishmentManager.muteManager.MuteManager;

public class ChatEvent implements Listener
{
    private final MuteManager muteManager;

    public ChatEvent(MuteManager muteManager)
    {
        this.muteManager = muteManager;
    }

    @EventHandler
    public void onChat(AsyncChatEvent event)
    {
        Player target = event.getPlayer();

        if(muteManager.isMuted(target.getUniqueId()))
        {
            event.setCancelled(true);
            target.sendMessage("You are muted!");
        }

    }
}
