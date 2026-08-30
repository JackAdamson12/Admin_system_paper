package org.example.commands.punishmentManager.muteManager;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.example.Main;
import org.example.commands.punishmentManager.muteManager.muteData.MuteData;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MuteManager
{

    private final File file;
    private final FileConfiguration config;
    private final Map<UUID, MuteData> mutes = new HashMap<>();
    private final Main plugin;

    public MuteManager(Main plugin)
    {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "mute.yml");

        if (!plugin.getDataFolder().exists())
        {
            plugin.getDataFolder().mkdirs();

        }

        if (!file.exists())
        {
            try
            {
                file.createNewFile();
            }
            catch (IOException exception)
            {
                plugin.getLogger().severe("Ошибка с mute.yml");
                exception.printStackTrace();
            }
        }

        this.config = YamlConfiguration.loadConfiguration(file);
        loadData();
    }

    public synchronized void mute(UUID uuid, String reason, String moderator)
    {
        MuteData muteData = new MuteData(uuid, reason, moderator, null);
        mutes.put(uuid, muteData);

        config.set("mutes." + uuid + ".reason", reason);
        config.set("mutes." + uuid + ".moderator", moderator);
        config.set("mutes." + uuid + ".expiresAt", null);

        saveData();
    }

    public synchronized void tempMute(UUID uuid, String reason, String moderator, Duration duration)
    {
        Instant expiresAt = Instant.now().plus(duration);
        MuteData muteData = new MuteData(uuid, reason, moderator, expiresAt);

        mutes.put(uuid, muteData);

        config.set("mutes." + uuid + ".reason", reason);
        config.set("mutes." + uuid + ".moderator", moderator);
        config.set("mutes." + uuid + ".expiresAt", expiresAt.toString());
        saveData();
    }

    public synchronized void unmute(UUID uuid)
    {
        mutes.remove(uuid);
        config.set("mutes." + uuid, null);
        saveData();
    }

    public synchronized void muteInfo(CommandSender sender, OfflinePlayer target)
    {
        MuteData muteData = mutes.get(target.getUniqueId());
        String targetName = target.getName();

        if(targetName == null)
        {
            targetName = target.getUniqueId().toString();
        }

        if(muteData == null)
        {
            sender.sendMessage("Player " + targetName + " is not muted.");
            return;
        }

        sender.sendMessage("Player: " + targetName + " is muted");

        if(muteData.expiresAt() == null)
        {
            sender.sendMessage("Mute time: Forever");
        }
        else
        {
            sender.sendMessage("Mute expires at: " + muteData.expiresAt());
        }

        sender.sendMessage("Reason: " + muteData.reason());
        sender.sendMessage("Moderator: " + muteData.moderator());
    }

    private void saveData()
    {
        try
        {
            config.save(file);
        }
        catch (IOException e)
        {
            plugin.getLogger().severe("Could not save mute.yml");
            e.printStackTrace();
        }

    }

    private void loadData()
    {
        if (!config.isConfigurationSection("mutes"))
        {
            return;
        }

        for (String uuidString : config.getConfigurationSection("mutes").getKeys(false))
        {
            UUID uuid;

            try
            {
                uuid = UUID.fromString(uuidString);
            }
            catch (IllegalArgumentException exception)
            {
                plugin.getLogger().warning("Invalid UUID in mute.yml: " + uuidString);
                continue;
            }

            String path = "mutes." + uuidString;

            String reason = config.getString(path + ".reason");
            String moderator = config.getString(path + ".moderator");
            String expires = config.getString(path + ".expiresAt");

            Instant expiresAt;

            try
            {
                expiresAt = expires == null ? null : Instant.parse(expires);
            }
            catch (Exception exception)
            {
                plugin.getLogger().warning("Invalid mute expiration for UUID: " + uuidString);
                continue;
            }

            MuteData muteData = new MuteData(uuid, reason, moderator, expiresAt);

            mutes.put(uuid, muteData);
        }
    }

    public synchronized boolean isMuted(UUID uuid)
    {
        MuteData muteData = mutes.get(uuid);

        if (muteData == null)
        {
            return false;
        }

        if (muteData.expiresAt() != null && Instant.now().isAfter(muteData.expiresAt()))
        {
            mutes.remove(uuid);

            if(Bukkit.isPrimaryThread())
            {
                config.set("mutes." + uuid, null);
                saveData();
            }
            else
            {
                plugin.getServer().getScheduler().runTask(plugin, () -> clearExpiredMute(uuid));
            }

            return false;
        }

        return true;
    }

    private synchronized void clearExpiredMute(UUID uuid)
    {
        if(mutes.containsKey(uuid))
        {
            return;
        }

        config.set("mutes." + uuid, null);
        saveData();
    }
}
