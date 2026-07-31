package org.example.utils;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.Main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class CheckPermission {
    private final HashSet<UUID> admins = new HashSet<>();

    private final Main plugin;

    public CheckPermission(Main plugin) {
        this.plugin = plugin;
    }
    public boolean checkPermission(CommandSender sender, String permission)
    {
        return sender.hasPermission(permission);
    }

    public void loadAdmins()
    {
        admins.clear();

        List<String> adminList = plugin.getConfig().getStringList("admins");

        for (String id : adminList)
        {
            try
            {
                admins.add(UUID.fromString(id));
            }
            catch (IllegalArgumentException exception)
            {
                plugin.getLogger().warning("Invalid admin UUID in config: " + id);
            }
        }
    }

    public boolean checkIsAdmin(Player player)
    {

        return player.hasPermission("minepaper.admin") || admins.contains(player.getUniqueId());

    }
    public boolean checkIsAdmin(UUID uuid)
    {
        return admins.contains(uuid);
    }
    public boolean checkIsAdmin(CommandSender sender)
    {
        if (!(sender instanceof Player player))
        {
            return true;
        }

        return checkIsAdmin(player);
    }

    public void addAdmin(Player admin, Player target)
    {
        if (checkIsAdmin(admin))
        {
            admins.add(target.getUniqueId());
            saveAdmin();
        }
    }


    public void removeAdmin(Player admin, Player target)
    {
        if (checkIsAdmin(admin))
        {
            admins.remove(target.getUniqueId());
            saveAdmin();

            if (target.getGameMode() != GameMode.SURVIVAL)
            {
                admin.sendMessage(ChatColor.YELLOW + "Warning: " + target.getName() + " still has gamemode: " + target.getGameMode().name());
                admin.sendMessage(ChatColor.YELLOW + "Changing gamemode for " + target.getName() + " to survival");

                target.setGameMode(GameMode.SURVIVAL);
            }
        }
    }


    public void showUUID(Player player) {
        player.sendMessage(player.getUniqueId().toString());
    }

    public void saveAdmin()
    {
        List<String> adminsListToSave = new ArrayList<>();

        for (UUID id : admins) {
            adminsListToSave.add(id.toString());
        }

        plugin.getConfig().set("admins", adminsListToSave);
        plugin.saveConfig();
    }
}