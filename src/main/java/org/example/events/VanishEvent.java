package org.example.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.example.commands.CommandVanish;

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

        for(Player vanishedPlayer : commandVanish.vanishList)
        {
            joinPlayer.hidePlayer(commandVanish.getPlugin(), vanishedPlayer);
        }
    }
}