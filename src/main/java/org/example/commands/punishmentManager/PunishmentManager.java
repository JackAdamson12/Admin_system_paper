package org.example.commands.punishmentManager;

import io.papermc.paper.ban.BanListType;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ban.ProfileBanList;
import org.bukkit.entity.Player;
import org.example.commands.punishmentManager.muteManager.MuteManager;

import java.util.UUID;

public class PunishmentManager
{

    private final MuteManager muteManager;

    public PunishmentManager(MuteManager muteManager)
    {
        this.muteManager = muteManager;

    }

    public boolean isBanned(UUID targetUuid)
    {
        return Bukkit.getOfflinePlayer(targetUuid).isBanned();
    }

    public boolean isMuted(UUID targetUUID)
    {
        return muteManager.isMuted(targetUUID);
    }

}
