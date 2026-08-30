package org.example.playerInfoCommand.sourse;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.example.Main;
import org.example.luckPerms.role.listRols.StaffRole;
import org.example.playerProfile.PlayerProfile;
import org.example.playerProfile.PlayerStaffStatus;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PlayerInfoManager
{

    private final Main plugin;
    private final File file;
    private FileConfiguration config;
    private final Map<UUID, PlayerProfile> playerProfileMap;

    public PlayerInfoManager(Main plugin)
    {
        playerProfileMap = new HashMap<>();
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(),"players.yml");

        if(!file.exists())
        {
            return;
        }
        this.config = YamlConfiguration.loadConfiguration(file);
        loadPlayers();

    }
    public void loadPlayers()
    {
        playerProfileMap.clear();

        if(config.getConfigurationSection("Player") == null)
        {
            return;
        }

        Set<String> players = config.getConfigurationSection("Player").getKeys(false);

        for(String playerUUID : players)
        {
            UUID uuid;

            try
            {
                uuid = UUID.fromString(playerUUID);
            }
            catch(IllegalArgumentException exception)
            {
                plugin.getLogger().warning("Invalid UUID in players.yml: " + playerUUID);
                continue;
            }

            String start = "Player." + playerUUID;

            String nickName = config.getString(start + ".Nick_Name");
            String roleName = config.getString(start + ".Role");


            if(nickName == null)
            {
                plugin.getLogger().warning("Nick_Name is null for: " + playerUUID);
                continue;
            }

            if(roleName == null)
            {
                plugin.getLogger().warning("Role is null for: " + playerUUID);
                continue;
            }

            StaffRole staffRole;

            try
            {
                staffRole = StaffRole.valueOf(roleName);
            }
            catch(IllegalArgumentException exception)
            {
                plugin.getLogger().warning("Invalid role for player " + playerUUID);
                continue;
            }
            PlayerProfile playerProfile;

            if(staffRole == StaffRole.PLAYER)
            {
                playerProfile = new PlayerProfile(uuid, nickName, staffRole);
            }
            else
            {
                playerProfile = new PlayerProfile(uuid, nickName, staffRole, PlayerStaffStatus.OPEN);
            }
            playerProfileMap.put(uuid, playerProfile);
        }
    }

    public String cheakRole(PlayerProfile playerProfile)
    {
        String res;
        if (playerProfile.getStaffRole() == StaffRole.PLAYER) {
            res = ("Status: " + ChatColor.GRAY + playerProfile.getStaffRole().toString());
            return res;
        }
        if (playerProfile.getStaffRole() == StaffRole.HELPER) {
            res = ("Status: " + ChatColor.GREEN + playerProfile.getStaffRole().toString());
            return res;
        }
        if (playerProfile.getStaffRole() == StaffRole.ADMIN) {
            res = ("Status: " + ChatColor.AQUA + playerProfile.getStaffRole().toString());
            return res;
        }
        if (playerProfile.getStaffRole() == StaffRole.HEAD_ADMIN) {
            res =("Status: " + ChatColor.RED + playerProfile.getStaffRole().toString());
            return res;
        }

        res = ("Status: " + ChatColor.MAGIC + ChatColor.DARK_AQUA + playerProfile.getStaffRole().toString());
        return res;

    }

    public PlayerProfile getProfile(UUID uuid)
    {
        if(uuid == null)
        {
            return null;
        }
        return playerProfileMap.get(uuid);
    }
}
