package org.example.playerProfile.playerReputationManager;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.example.Main;
import org.example.commands.punishmentManager.muteManager.MuteManager;
import org.example.playTimeManager.PlayTimeManager;
import org.example.playTimeManager.sourse.TimeRecorder;
import org.example.playerProfile.PlayerProfile;
import org.example.playerProfile.PlayerProfileManager;
import org.example.playerProfile.PlayerReputation;
import org.example.reports.sourse.ReportCase;
import org.example.reports.sourse.ReportManager;
import org.example.reports.sourse.data.ReportResult;

public class PlayerReputationManager
{
    private final PlayerProfileManager playerProfileManager;
    private final ReportManager reportManager;
    private final PlayTimeManager playTimeManager;
    private final MuteManager muteManager;
    private final Main plugin;

    public PlayerReputationManager(PlayerProfileManager playerProfileManager,ReportManager reportManager,PlayTimeManager playTimeManager,MuteManager muteManager,Main plugin)
    {
        this.playerProfileManager = playerProfileManager;
        this.reportManager = reportManager;
        this.playTimeManager = playTimeManager;
        this.muteManager = muteManager;
        this.plugin = plugin;
    }


    public int scoreReputation(Player player)
    {
        if(player == null)
        {
            return 0;
        }

        PlayerProfile target = playerProfileManager.getProfile(player);

        return scoreReputation(target);
    }

    public int scoreReputation(PlayerProfile target)
    {
        if(target == null)
        {
            return 0;
        }

        ReportCase reportCase = reportManager.getReportCase(target.getUuid());

        if(reportCase == null)
        {
            return target.getTrustLevel();
        }

        if(reportCase.getReportResult() != ReportResult.CONFIRMED)
        {
            return target.getTrustLevel();
        }

        int trustLevel = target.getTrustLevel();

        OfflinePlayer player = Bukkit.getOfflinePlayer(target.getUuid());

        if(player.isBanned())
        {
            target.setTrustLevel(trustLevel - 10);
        }
        else if(muteManager.isMuted(target.getUuid()))
        {
            target.setTrustLevel(trustLevel - 5);
        }
        else
        {
            target.setTrustLevel(trustLevel - 5);
        }

        playerProfileManager.saveProfile(target);

        return target.getTrustLevel();
    }

    public void setTrustLevelFromCommands(OfflinePlayer player, int score)
    {
        if(player == null)
        {
            return;
        }

        PlayerProfile target = playerProfileManager.getProfile(player.getUniqueId());

        if(target == null)
        {
            return;
        }

        int trustLevel = target.getTrustLevel() + score;
        target.setTrustLevel(trustLevel);

        playerProfileManager.saveProfile(target);
    }

    public PlayerReputation getPlayerReputation(PlayerProfile target) {
        if(target == null)
        {
            return PlayerReputation.ERROR;
        }

        int trustLevel = target.getTrustLevel();

        if (trustLevel >= 90 && trustLevel <= 100)
        {
            return PlayerReputation.EXCELLENT;
        }
        if (trustLevel >= 75)
        {
            return PlayerReputation.GOOD;
        }
        if (trustLevel >= 50)
        {
            return PlayerReputation.AVERAGE;
        }
        if (trustLevel >= 25)
        {
            return PlayerReputation.BELOW_AVERAGE;
        }
        if (trustLevel >= 0)
        {
            return PlayerReputation.BAD;
        }

        return PlayerReputation.ERROR;

    }

    public void addReputationForTime(Player player)
    {
        PlayerProfile target = playerProfileManager.getProfile(player);

        if(target == null)
        {
            return;
        }

        TimeRecorder currentTime = playTimeManager.getPlayTime(target.getPlayTime());
        int hoursPlay = currentTime.hours();
        int rewardHours = (hoursPlay / 5) * 5;

        if(rewardHours >= 5 && rewardHours > target.getLastReputationRewardHours())
        {
            target.reputationLevelUp(5);
            target.setLastReputationRewardHours(rewardHours);
            playerProfileManager.saveProfile(target);
        }
    }

    public void startReputationTimer()
    {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {

            for(Player player : Bukkit.getOnlinePlayers())
            {
                addReputationForTime(player);
            }

        }, 20L * 60L * 60L, 20L * 60L * 60L);
    }

}
