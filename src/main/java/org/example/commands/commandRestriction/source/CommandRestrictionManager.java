package org.example.commands.commandRestriction.source;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.example.Main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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
                plugin.getLogger().severe("Could not create command-restrictions.yml");

                exception.printStackTrace();
            }
        }

        this.config = YamlConfiguration.loadConfiguration(file);

        loadData();
    }



    public void blockPlayer(String command, Player player)
    {
        if(command == null || command.isBlank() || player == null)
        {
            return;
        }

        command = command.toLowerCase();

        Set<UUID> blockedPlayers = restrictions.get(command);


        if(blockedPlayers != null && blockedPlayers.isEmpty())
        {
            return;
        }

        if(blockedPlayers == null)
        {
            blockedPlayers = new HashSet<>();
            restrictions.put(command, blockedPlayers);
        }

        blockedPlayers.add(player.getUniqueId());

        saveCommandPlayers(command, blockedPlayers);
    }

    public void blockCommandForEveryone(String command)
    {
        if(command == null || command.isBlank())
        {
            return;
        }

        command = command.toLowerCase();


        restrictions.put(command, new HashSet<>());

        config.set("commands." + command + ".blocked-players", Collections.emptyList());

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
            saveFile();
            return;
        }

        saveCommandPlayers(command, blockedPlayers);
    }

    public void enableCommand(String command)
    {
        if(command == null || command.isBlank())
        {
            return;
        }

        command = command.toLowerCase();

        restrictions.remove(command);
        config.set("commands." + command, null);

        saveFile();
    }

    public boolean isDisabled(String command, Player player)
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
            return true;
        }

        return blockedPlayers.contains(player.getUniqueId());
    }

    public void loadData()
    {
        restrictions.clear();

        ConfigurationSection commandsSection = config.getConfigurationSection("commands");

        if(commandsSection == null)
        {
            return;
        }

        Set<String> allCommands = commandsSection.getKeys(false);

        for(String command : allCommands)
        {
            Set<UUID> blockedPlayers = new HashSet<>();

            List<String> savedUuids = config.getStringList("commands." + command + ".blocked-players");

            for(String savedUuid : savedUuids)
            {
                try
                {
                    blockedPlayers.add(UUID.fromString(savedUuid));
                }
                catch(IllegalArgumentException exception)
                {
                    plugin.getLogger().warning("Invalid UUID for command " + command + ": " + savedUuid);
                }
            }

            restrictions.put(command.toLowerCase(), blockedPlayers);
        }
    }

    private void saveCommandPlayers(String command, Set<UUID> blockedPlayers)
    {
        List<String> uuidList = new ArrayList<>();

        for(UUID uuid : blockedPlayers)
        {
            uuidList.add(uuid.toString());
        }

        config.set("commands." + command + ".blocked-players", uuidList);

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