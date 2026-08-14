package org.example.playerProfile.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.example.playerProfile.PlayerProfile;
import org.example.playerProfile.PlayerProfileManager;

public class PlayerProfileListener implements Listener
{

    private final PlayerProfileManager playerProfileManager;

    public PlayerProfileListener(PlayerProfileManager playerProfileManager)
    {
        this.playerProfileManager = playerProfileManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent playerJoinEvent)
    {
        Player target = playerJoinEvent.getPlayer();

        if(playerProfileManager.isNewProfile(target))
        {
            PlayerProfile playerProfile = new PlayerProfile(target);
            playerProfileManager.saveProfile(playerProfile);
            return;
        }


    }

}
