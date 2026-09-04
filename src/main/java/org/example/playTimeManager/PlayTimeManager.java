package org.example.playTimeManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.example.Main;
import org.example.playTimeManager.sourse.TimeRecorder;
import org.example.playerProfile.PlayerProfile;
import org.example.playerProfile.PlayerProfileManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayTimeManager
{
    private final PlayerProfileManager playerProfileManager;
    private final Main plugin;
    private final Map<UUID, Integer> playerTasks = new HashMap<>();




    public PlayTimeManager(Main plugin, PlayerProfileManager playerProfileManager)
    {
        this.plugin = plugin;
        this.playerProfileManager = playerProfileManager;
    }

    public void timeCounter(Player player)
    {

        if(player == null)
        {
            return;
        }

        if(playerTasks.containsKey(player.getUniqueId()))
        {
            return;
        }

        int taskId = Bukkit.getScheduler().runTaskTimer(plugin, () -> {

            if (!player.isOnline())
            {
                stopTimeCounter(player);
                return;
            }

            PlayerProfile profile = playerProfileManager.getProfile(player.getUniqueId());

            if (profile == null)
            {
                return;
            }

            profile.addPlayingTime(1);

        }, 20L, 20L).getTaskId();

        playerTasks.put(player.getUniqueId(), taskId);
    }

    public void stopTimeCounter(Player player)
    {

        Integer taskId = playerTasks.remove(player.getUniqueId());

        if (taskId != null)
        {
            Bukkit.getScheduler().cancelTask(taskId);
        }

        PlayerProfile profile = playerProfileManager.getProfile(player.getUniqueId());

        if(profile != null)
        {
            playerProfileManager.saveProfile(profile);
        }
    }

    public void stopAllTimeCounters()
    {
        for(UUID uuid : playerTasks.keySet())
        {
            Bukkit.getScheduler().cancelTask(playerTasks.get(uuid));

            PlayerProfile profile = playerProfileManager.getProfile(uuid);

            if(profile != null)
            {
                playerProfileManager.saveProfile(profile);
            }
        }

        playerTasks.clear();
    }

    public static TimeRecorder getPlayTime(long playTime)
    {
        int hours = (int) (playTime / 3600);
        int minutes = (int) (playTime % 3600) / 60;
        int seconds = (int) (playTime % 60);

        return new TimeRecorder(hours, minutes, seconds);
    }

    public static long setPlayTime(TimeRecorder time)
    {
        if(time == null)
        {
            return 0;
        }

        long hours = (long) time.hours() * 3600;
        long minutes = (long) time.minutes() * 60;
        long seconds = time.seconds();

        return hours + minutes + seconds;
    }


}