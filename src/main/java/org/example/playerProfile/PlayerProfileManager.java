package org.example.playerProfile;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.example.Main;
import org.example.luckPerms.role.RoleManager;
import org.example.luckPerms.role.listRols.StaffRole;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PlayerProfileManager
{
    private final Main plugin;
    private final File file;
    private FileConfiguration config;
    private final Map<UUID,PlayerProfile> playersProfile;
 //   private final RoleManager roleManager;


    public PlayerProfileManager(Main plugin)
    {
       // this.roleManager = new RoleManager(this);
        playersProfile = new HashMap<>();
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(),"players.yml");

        if(!file.exists())
        {
            try
            {
                file.createNewFile();
            }
            catch(IOException exception)
            {
                plugin.getLogger().severe("Error with players.yml");
                exception.printStackTrace();
            }
        }
        this.config = YamlConfiguration.loadConfiguration(file);
        loadPlayers();

    }

    public void saveProfile(PlayerProfile playerProfile)
    {
        if(playerProfile == null)
        {
            return;
        }
        playersProfile.put(playerProfile.getUuid(), playerProfile);

        String start = "Player." + playerProfile.getUuid();
        config.set(start  + ".Nick_Name", playerProfile.getNickName());
        config.set(start + ".Role", playerProfile.getStaffRole().name());

        saveFile();
    }


    public boolean isNewProfile(Player player)
    {
        if(playersProfile.containsKey(player.getUniqueId()))
        {
            return false;
        }
        return true;
    }


    public void loadPlayers()
    {
        playersProfile.clear();

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

//            if(nickName == null || roleName == null)
//            {
//                plugin.getLogger().warning("Invalid player data in players.yml: " + playerUUID);
//                continue;
//            }

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

            PlayerProfile playerProfile = new PlayerProfile(uuid, nickName, staffRole);

            playersProfile.put(uuid, playerProfile);
        }
    }

    public void reload()
    {
        config = YamlConfiguration.loadConfiguration(file);

        loadPlayers();

        for(Player player : Bukkit.getOnlinePlayers())
        {
            if(isNewProfile(player))
            {
                PlayerProfile playerProfile = new PlayerProfile(player);
                saveProfile(playerProfile);
            }
        }
    }

    private void saveFile()
    {
        try
        {
            config.save(file);
        }
        catch(IOException exception)
        {
            plugin.getLogger().severe("Could not save players.yml");
            exception.printStackTrace();
        }
    }

    public PlayerProfile getProfile(Player player)
    {
        if(player == null)
        {
            return null;
        }

        return playersProfile.get(player.getUniqueId());
    }
    public PlayerProfile getProfile(UUID uuid)
    {
        if(uuid == null)
        {
            return null;
        }

        return playersProfile.get(uuid);
    }

    public PlayerProfile getProfile(CommandSender sender)
    {
        if(!(sender instanceof Player player))
        {
            return null;
        }

        return playersProfile.get(player.getUniqueId());
    }

}
