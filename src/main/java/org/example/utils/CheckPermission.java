package org.example.utils;

import org.bukkit.entity.Player;
import org.example.Main;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class CheckPermission
{
    private final HashSet<UUID> admins = new HashSet<>();

    private final Main plugin;

    public CheckPermission(Main plugin)
    {
        this.plugin = plugin;
    }

    public void loadAdmins()
    {
        List<String> adminList = plugin.getConfig().getStringList("admins");

        for(String id : adminList)
        {
            admins.add(UUID.fromString(id));
        }
    }

    public boolean checkIsAdmin(Player player)
    {
        return admins.contains(player.getUniqueId());
    }

    public void addAdmin(Player admin, Player target)
    {
        if(checkIsAdmin(admin))
        {
            admins.add(target.getUniqueId());
        }
    }

    public void removeAdmin(Player admin, Player target)
    {
        if(checkIsAdmin(admin))
        {
            admins.remove(target.getUniqueId());
        }
    }

    public void showUUID(Player player)
    {
        player.sendMessage(player.getUniqueId().toString());
    }
}