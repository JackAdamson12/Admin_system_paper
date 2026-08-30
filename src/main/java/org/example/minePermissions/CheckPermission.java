package org.example.minePermissions;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.Main;
import org.example.minePermissions.roleCommandsManager.RoleCommandsManager;
import org.example.playerProfile.PlayerProfileManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class CheckPermission
{
    private final HashSet<UUID> admins = new HashSet<>();

    private final Main plugin;
    private final StaffActionManager staffActionManager;

    public CheckPermission(Main plugin, PlayerProfileManager playerProfileManager)
    {
        this.plugin = plugin;
        staffActionManager = new StaffActionManager(playerProfileManager, plugin);


    }
    public boolean checkPermission(CommandSender sender, String command)
    {
        return staffActionManager.cheakPermission(sender,command);
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

    public boolean cheakIsOwner(CommandSender sender)
    {
        return staffActionManager.cheakIsOwner(sender);
    }
    public boolean cheakIsOwner(Player admin)
    {
        return staffActionManager.cheakIsOwner(admin);
    }
    public boolean cheakIsOwner(UUID adminUUID)
    {
        return staffActionManager.cheakIsOwner(adminUUID);
    }


    public boolean cheakIsHelper(Player player)
    {
        return staffActionManager.cheakIsHelper(player);
    }



    public boolean checkIsAdmin(Player player)
    {

        return staffActionManager.cheakModerationStatus(player);

    }
    public boolean checkIsAdmin(UUID uuid)
    {
        return staffActionManager.cheakModerationStatus(uuid);
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