package org.example.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TeleportUtils {
    public void teleportTo(Player player, Player target)
    {
        player.teleport(target);

    }

    public void teleportPlayerToPlayer(Player target, Player target2)
    {
        target.teleport(target2);

    }

    public void teleportToMe(Player player, Player target)
    {
        target.teleport(player);
    }

    public void allTeleportToMe(Player player)
    {
        for(Player onlinePlayer : Bukkit.getOnlinePlayers())
        {
            if(onlinePlayer != player)
            {
                onlinePlayer.teleport(player);
            }

        }

    }
}
