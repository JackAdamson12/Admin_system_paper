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
    private PlayerReputation playerReputation;
    private int trustLevel;
    private long playTime;
    private int lastReputationRewardHours;

    public PlayerProfile(Player player)
    {
        this.uuid = player.getUniqueId();
        this.nickName = player.getName();
        this.staffRole = StaffRole.PLAYER;
        this.playerStaffStatus = PlayerStaffStatus.PLAYER;
        this.playerReputation = PlayerReputation.AVERAGE;
        this.trustLevel = 50;
        this.playTime = 0;
        this.lastReputationRewardHours = 0;
    }
    public PlayerProfile(UUID uuid, String nickName, StaffRole staffRole)
    {
        this.uuid = uuid;
        this.nickName = nickName;
        this.staffRole = staffRole;
        this.playerStaffStatus = staffRole == StaffRole.PLAYER ? PlayerStaffStatus.PLAYER : PlayerStaffStatus.OPEN;
        this.playerReputation = PlayerReputation.AVERAGE;
        this.trustLevel = 50;
        this.playTime = 0;
        this.lastReputationRewardHours = 0;
    }
    public PlayerProfile(UUID uuid, String nickName, StaffRole staffRole,PlayerStaffStatus staffStatus)
    {
        this.uuid = uuid;
        this.nickName = nickName;
        this.staffRole = staffRole;
        this.playerStaffStatus = staffStatus;
        this.playerReputation = PlayerReputation.AVERAGE;
        this.trustLevel = 50;
        this.playTime = 0;
        this.lastReputationRewardHours = 0;
    }

    public PlayerProfile(UUID uuid, String nickName, StaffRole staffRole,PlayerStaffStatus staffStatus,int trustLevel,long playTime)
    {
        this.uuid = uuid;
        this.nickName = nickName;
        this.staffRole = staffRole;
        this.playerStaffStatus = staffStatus;
        this.playerReputation = PlayerReputation.AVERAGE;
        this.trustLevel = trustLevel;
        this.playTime = playTime;
        this.lastReputationRewardHours = 0;
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

    public void reputationLevelUp(int points)
    {
        trustLevel += points;

        if(trustLevel >= 100)
        {
            trustLevel = 100;
        }

    }

    public void addPlayingTime(long time)
    {
        playTime += time;
    }

    public int getLastReputationRewardHours()
    {
        return lastReputationRewardHours;
    }

    public void setLastReputationRewardHours(int hours)
    {
        this.lastReputationRewardHours = hours;
    }

    public long getPlayTime()
    {
        return playTime;
    }

    public void setPlayTime(long time)
    {
        this.playTime = time;
    }

    public int getTrustLevel()
    {
        return trustLevel;
    }
    public void setTrustLevel(int trustLevel)
    {
        if(trustLevel < 0)
        {
            trustLevel = 0;
        }

        if(trustLevel > 100)
        {
            trustLevel = 100;
        }

        this.trustLevel = trustLevel;
    }

    public PlayerReputation getPlayerReputation()
    {
        return playerReputation;
    }
    public void setPlayerReputation(PlayerReputation playerReputation)
    {
        this.playerReputation = playerReputation;
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
