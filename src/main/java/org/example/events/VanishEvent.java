package org.example.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.example.commands.CommandVanish;

import java.util.UUID;

public class VanishEvent implements Listener
{
    private final CommandVanish commandVanish;

    public VanishEvent(CommandVanish commandVanish)
    {
        this.commandVanish = commandVanish;
    }

    @EventHandler
    public void hideJoinPlayers(PlayerJoinEvent event)
    {
        Player joinPlayer = event.getPlayer();

        for(UUID vanishedUUID : commandVanish.vanishList)
        {
            Player vanishedPlayer = Bukkit.getPlayer(vanishedUUID);

            if(vanishedPlayer != null)
            {
                joinPlayer.hidePlayer(commandVanish.getPlugin(), vanishedPlayer);
            }
        }
    }
}