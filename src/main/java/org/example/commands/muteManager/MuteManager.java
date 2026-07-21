package org.example.commands.muteManager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.example.Main;
import org.example.commands.muteManager.muteData.MuteData;

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

    public void mute(UUID uuid, String reason, String moderator)
    {
            MuteData muteData = new MuteData(uuid, reason, moderator, null);
            mutes.put(uuid, muteData);

            config.set("mutes." + uuid + ".reason", reason);
            config.set("mutes." + uuid + ".moderator", moderator);
            config.set("mutes." + uuid + ".expiresAt", null);

            saveData();
    }

    public void tempMute(UUID uuid, String reason, String moderator, Duration duration)
    {
        Instant expiresAt = Instant.now().plus(duration);
        MuteData muteData = new MuteData(uuid, reason, moderator, expiresAt);

        mutes.put(uuid, muteData);

        config.set("mutes." + uuid + ".reason", reason);
        config.set("mutes." + uuid + ".moderator", moderator);
        config.set("mutes." + uuid + ".expiresAt", expiresAt.toString());
        saveData();
    }

    public void unmute(UUID uuid)
    {
        mutes.remove(uuid);
        config.set("mutes." + uuid, null);
        saveData();
    }

    public void muteInfo(Player admin, Player target)
    {
        MuteData muteData = mutes.get(target.getUniqueId());

        if (muteData == null)
        {
            admin.sendMessage("Player " + target.getName() + " is not muted.");
            return;
        }

        admin.sendMessage("Player: " + target.getName() + " is muted");
        if(muteData.expiresAt() == null)
        {
            admin.sendMessage("Mute time: Forever");
        }
        admin.sendMessage("Mute time: " + muteData.expiresAt());
        admin.sendMessage("Reason: " + muteData.reason());
        admin.sendMessage("Moderator: " + muteData.moderator());

    }

    private void saveData()
    {
        try
        {
            config.save(file);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
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
            UUID uuid = UUID.fromString(uuidString);

            String path = "mutes." + uuidString;

            String reason = config.getString(path + ".reason");
            String moderator = config.getString(path + ".moderator");

            String expires = config.getString(path + ".expiresAt");

            Instant expiresAt = expires == null ? null : Instant.parse(expires);

            MuteData muteData = new MuteData(uuid, reason, moderator, expiresAt);

            mutes.put(uuid, muteData);
        }
    }

    public boolean isMuted(UUID uuid)
    {
        MuteData muteData = mutes.get(uuid);

        if (muteData == null)
        {
            return false;
        }

        if (muteData.expiresAt() != null && Instant.now().isAfter(muteData.expiresAt()))
        {
            unmute(uuid);
            return false;
        }

        return true;
    }
}
