package org.example.playerProfile;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.example.luckPerms.role.listRols.StaffRole;

import java.util.UUID;

public class PlayerProfile
{
    private UUID uuid;
    private String nickName;
    private StaffRole staffRole;
    private int valueReports;
    private PlayerStaffStatus playerStaffStatus;

    public PlayerProfile(Player player)
    {
        this.uuid = player.getUniqueId();
        this.nickName = player.getName();
        this.staffRole = StaffRole.PLAYER;
        this.playerStaffStatus = PlayerStaffStatus.PLAYER;
    }
    public PlayerProfile(UUID uuid, String nickName, StaffRole staffRole)
    {
        this.uuid = uuid;
        this.nickName = nickName;
        this.staffRole = staffRole;
        this.playerStaffStatus = staffRole == StaffRole.PLAYER ? PlayerStaffStatus.PLAYER : PlayerStaffStatus.OPEN;
    }
    public PlayerProfile(UUID uuid, String nickName, StaffRole staffRole,PlayerStaffStatus staffStatus)
    {
        this.uuid = uuid;
        this.nickName = nickName;
        this.staffRole = staffRole;
        this.playerStaffStatus = staffStatus;
    }

    public void levelUp()
    {
        int currentLevel = staffRole.getLevel();

        if(currentLevel >= StaffRole.HEAD_ADMIN.getLevel())
        {
            return;
        }

        int newLevel = currentLevel + 1;

        staffRole = StaffRole.getRoleByLevel(newLevel);
    }

    public void levelDown()
    {
        int currentLevel = staffRole.getLevel();

        if(currentLevel <= StaffRole.PLAYER.getLevel())
        {
            return;
        }

        int newLevel = currentLevel - 1;

        staffRole = StaffRole.getRoleByLevel(newLevel);
    }



    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public void setStaffRole(StaffRole staffRole)
    {
        this.staffRole = staffRole;
    }

    public void setPlayerStaffStatus(PlayerStaffStatus playerStaffStatus)
    {
        this.playerStaffStatus = playerStaffStatus;
    }
    public PlayerStaffStatus getPlayerStaffStatus()
    {
        return playerStaffStatus;
    }

    public UUID getUuid()
    {
        return uuid;
    }

    public int getLevel()
    {
        return staffRole.getLevel();
    }

    public StaffRole getStaffRole()
    {
        return staffRole;
    }

    public String getNickName()
    {
        return nickName;
    }
}
