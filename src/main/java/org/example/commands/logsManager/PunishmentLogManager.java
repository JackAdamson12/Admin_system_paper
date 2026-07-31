package org.example.commands.logsManager;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.example.Main;
import org.example.commands.logsManager.punishmentLogData.PunishmentData;
import org.example.commands.logsManager.punishmentLogData.PunishmentType;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PunishmentLogManager
{
    private final File file;
    private final FileConfiguration config;
    private final Main plugin;
    private final Map<UUID, List<PunishmentData>> punishmentLog = new HashMap<>();

    public PunishmentLogManager(Main plugin)
    {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "punishmentLog.yml");

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
                plugin.getLogger().severe("Error with punishmentLog.yml");
                exception.printStackTrace();
            }
        }

        this.config = YamlConfiguration.loadConfiguration(file);
        loadLog();
    }

    public synchronized void saveLog(OfflinePlayer target, String reason, CommandSender moderator, PunishmentType punishment, Instant createdAt, Instant expiresAt)
    {
        String targetName = target.getName();

        if(targetName == null)
        {
            targetName = "Unknown";
        }

        PunishmentData punishmentData = new PunishmentData(target.getUniqueId(), targetName, reason, moderator.getName(), punishment, createdAt, expiresAt);

        punishmentLog.computeIfAbsent(target.getUniqueId(), uuid -> new ArrayList<>()).add(punishmentData);

        long logId = createdAt.toEpochMilli();
        String start = "Player." + target.getUniqueId() + ".history." + logId;

        while(config.contains(start))
        {
            logId++;
            start = "Player." + target.getUniqueId() + ".history." + logId;
        }

        config.set(start + ".punishment", punishment.name());
        config.set(start + ".reason", reason);
        config.set(start + ".Nick name", targetName);
        config.set(start + ".Moderator", moderator.getName());
        config.set(start + ".Data from", createdAt.toString());

        if(expiresAt != null)
        {
            config.set(start + ".finish punishment to", expiresAt.toString());
        }
        else
        {
            config.set(start + ".finish punishment to", null);
        }

        saveFile();
    }

    public synchronized void loadLog()
    {
        punishmentLog.clear();

        ConfigurationSection playerSection = config.getConfigurationSection("Player");

        if(playerSection == null)
        {
            return;
        }

        for(String uuidString : playerSection.getKeys(false))
        {
            UUID uuid;

            try
            {
                uuid = UUID.fromString(uuidString);
            }
            catch(IllegalArgumentException exception)
            {
                plugin.getLogger().warning("Invalid UUID in punishmentLog.yml: " + uuidString);
                continue;
            }

            ConfigurationSection historySection = config.getConfigurationSection("Player." + uuidString + ".history");

            if(historySection == null)
            {
                continue;
            }

            for(String logId : historySection.getKeys(false))
            {
                String start = "Player." + uuidString + ".history." + logId;

                String punishmentString = config.getString(start + ".punishment");
                String nickName = config.getString(start + ".Nick name");
                String reason = config.getString(start + ".reason");
                String moderator = config.getString(start + ".Moderator");
                String createdString = config.getString(start + ".Data from");
                String expiresString = config.getString(start + ".finish punishment to");

                if(punishmentString == null || createdString == null)
                {
                    plugin.getLogger().warning("Invalid punishment log entry: " + start);
                    continue;
                }

                try
                {
                    PunishmentType punishment = PunishmentType.valueOf(punishmentString);
                    Instant createdAt = Instant.parse(createdString);
                    Instant expiresAt = expiresString == null ? null : Instant.parse(expiresString);

                    PunishmentData punishmentData = new PunishmentData(uuid, nickName, reason, moderator, punishment, createdAt, expiresAt);

                    punishmentLog.computeIfAbsent(uuid, key -> new ArrayList<>()).add(punishmentData);
                }
                catch(IllegalArgumentException exception)
                {
                    plugin.getLogger().warning("Could not load punishment log entry: " + start);
                }
            }
        }
    }

    public synchronized List<PunishmentData> getPunishmentLog(UUID uuid)
    {
        List<PunishmentData> logs = punishmentLog.get(uuid);

        if(logs == null)
        {
            return new ArrayList<>();
        }

        return new ArrayList<>(logs);
    }

    private void saveFile()
    {
        try
        {
            config.save(file);
        }
        catch(IOException exception)
        {
            plugin.getLogger().severe("Could not save punishmentLog.yml");
            exception.printStackTrace();
        }
    }
}