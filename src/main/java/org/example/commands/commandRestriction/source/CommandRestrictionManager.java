package org.example.commands.commandRestriction.source;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.example.Main;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CommandRestrictionManager
{
    private final Main plugin;
    private final File file;
    private final FileConfiguration config;
    private final Map<String, Set<UUID>> restrictions = new HashMap<>();

    public CommandRestrictionManager(Main plugin)
    {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "command-restrictions.yml");

        if(!plugin.getDataFolder().exists())
        {
            plugin.getDataFolder().mkdirs();
        }

        if(!file.exists())
        {
            try
            {
                file.createNewFile();
            }
            catch(IOException exception)
            {
                plugin.getLogger().severe("Error with command-restrictions.yml");
                exception.printStackTrace();
            }
        }

        this.config = YamlConfiguration.loadConfiguration(file);

        loadData();
    }


    public void saveData(String command, Player blockedPlayer)
    {
        if(command == null || command.isBlank())
        {
            return;
        }

        if(blockedPlayer == null)
        {
            return;
        }

        command = command.toLowerCase();

        Set<UUID> blockedPlayers;

        if(restrictions.containsKey(command))
        {
            blockedPlayers = restrictions.get(command);


            if(blockedPlayers.isEmpty())
            {
                return;
            }
        }
        else
        {
            blockedPlayers = new HashSet<>();
            blockedPlayers.add(blockedPlayer.getUniqueId());
            restrictions.put(command, blockedPlayers);
        }


        String path = "commands." + command + ".blocked_players";

        List<String> uuidList = new ArrayList<>();

        for(UUID uuid : blockedPlayers)
        {
            uuidList.add(uuid.toString());
        }

        config.set(path, uuidList);

        saveFile();
    }

    public void saveData(String command)
    {
        if(command == null || command.isBlank())
        {
            return;
        }

        command = command.toLowerCase();


        restrictions.put(command, new HashSet<>());

        config.set("commands." + command + ".blocked_players", Collections.emptyList());

        saveFile();
    }


    public void loadData()
    {
        restrictions.clear();

        ConfigurationSection commandSection = config.getConfigurationSection("commands");

        if(commandSection == null)
        {
            return;
        }

        Set<String> allCommands = commandSection.getKeys(false);

        for(String command : allCommands)
        {
            Set<UUID> uuids = new HashSet<>();

            List<String> savedUuids = config.getStringList("commands." + command + ".blocked_players");

            for(String strUuid : savedUuids)
            {
                try
                {
                    uuids.add(UUID.fromString(strUuid));
                }
                catch(IllegalArgumentException exception)
                {
                    plugin.getLogger().warning("Invalid UUID for command " + command + ": " + strUuid);
                }
            }

            restrictions.put(command.toLowerCase(), uuids);
        }
    }


    public boolean isDisabled(String command, Player player)
    {
        if(command == null || command.isBlank())
        {
            return false;
        }

        command = command.toLowerCase();

        Set<UUID> blockedPlayers = restrictions.get(command);


        if(blockedPlayers == null)
        {
            return false;
        }


        if(blockedPlayers.isEmpty())
        {
            return true;
        }

        if(player == null)
        {
            return false;
        }

        return blockedPlayers.contains(player.getUniqueId());
    }

    public boolean isCommandRestricted(String command)
    {
        if(command == null || command.isBlank())
        {
            return false;
        }

        command = command.toLowerCase();

        return restrictions.containsKey(command);
    }

    public boolean isCommandDisabledForPlayer(String command, Player player)
    {
        if(command == null || command.isBlank() || player == null)
        {
            return false;
        }

        command = command.toLowerCase();

        Set<UUID> blockedPlayers = restrictions.get(command);

        if(blockedPlayers == null)
        {
            return false;
        }


        if(blockedPlayers.isEmpty())
        {
            return false;
        }

        return blockedPlayers.contains(player.getUniqueId());
    }

    public void addPlayer(String command, Player player)
    {
        if(command == null || command.isBlank() || player == null)
        {
            return;
        }

        command = command.toLowerCase();


        if(restrictions.containsKey(command) && restrictions.get(command).isEmpty())
        {
            return;
        }

        if(!restrictions.containsKey(command))
        {
            Set<UUID> addingPlayers = new HashSet<>();
            addingPlayers.add(player.getUniqueId());

            restrictions.put(command, addingPlayers);
        }
        else
        {
            restrictions.get(command)
                    .add(player.getUniqueId());
        }

        Set<UUID> blockedPlayers =
                restrictions.get(command);

        List<String> uuidList = new ArrayList<>();

        for(UUID uuid : blockedPlayers)
        {
            uuidList.add(uuid.toString());
        }

        config.set("commands." + command + ".blocked_players", uuidList);

        saveFile();
    }

    public void removeCommand(String command)
    {
        if(command == null || command.isBlank())
        {
            return;
        }

        command = command.toLowerCase();

        if(!restrictions.containsKey(command))
        {
            return;
        }

        restrictions.remove(command);

        config.set("commands." + command, null);

        saveFile();
    }

    public void removePlayer(String command, Player player)
    {
        if(command == null || command.isBlank() || player == null)
        {
            return;
        }

        command = command.toLowerCase();

        Set<UUID> blockedPlayers = restrictions.get(command);

        if(blockedPlayers == null)
        {
            return;
        }

        if(blockedPlayers.isEmpty())
        {
            return;
        }

        boolean removed = blockedPlayers.remove(player.getUniqueId());

        if(!removed)
        {
            return;
        }

        String path = "commands." + command;


        if(blockedPlayers.isEmpty())
        {
            restrictions.remove(command);
            config.set(path, null);
        }
        else
        {
            List<String> uuidList = new ArrayList<>();

            for(UUID uuid : blockedPlayers)
            {
                uuidList.add(uuid.toString());
            }

            config.set(path + ".blocked_players", uuidList);
        }

        saveFile();
    }

    private void saveFile()
    {
        try
        {
            config.save(file);
        }
        catch(IOException exception)
        {
            plugin.getLogger().severe("Could not save command-restrictions.yml");
            exception.printStackTrace();
        }
    }
}