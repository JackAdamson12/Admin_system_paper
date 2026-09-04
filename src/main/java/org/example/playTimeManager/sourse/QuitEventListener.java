package org.example.playTimeManager.sourse;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.example.playTimeManager.PlayTimeManager;

public class QuitEventListener implements Listener
{
    private final PlayTimeManager playTimeManager;

    public QuitEventListener(PlayTimeManager playTimeManager)
    {
        this.playTimeManager = playTimeManager;
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();

        playTimeManager.stopTimeCounter(player);
    }
}
