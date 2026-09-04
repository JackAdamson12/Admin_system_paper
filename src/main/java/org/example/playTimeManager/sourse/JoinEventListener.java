package org.example.playTimeManager.sourse;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.example.playTimeManager.PlayTimeManager;
import org.example.playerProfile.PlayerProfile;
import org.example.playerProfile.PlayerProfileManager;

public class JoinEventListener implements Listener
{

    private final PlayTimeManager playTimeManager;

    public JoinEventListener(PlayTimeManager playTimeManager)
    {
        this.playTimeManager = playTimeManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent playerJoinEvent)
    {
        Player target = playerJoinEvent.getPlayer();

        if(target.isOnline())
        {
            playTimeManager.timeCounter(target);
            return;
        }



    }
}
