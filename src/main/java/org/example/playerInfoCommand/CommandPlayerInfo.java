package org.example.playerInfoCommand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.minePermissions.CheckPermission;
import org.example.playTimeManager.PlayTimeManager;
import org.example.playTimeManager.sourse.TimeRecorder;
import org.example.playerInfoCommand.sourse.PlayerInfoManager;
import org.example.playerProfile.PlayerProfile;
import org.example.playerProfile.PlayerProfileManager;
import org.example.playerProfile.PlayerReputation;
import org.example.playerProfile.playerReputationManager.PlayerReputationManager;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class CommandPlayerInfo implements CommandExecutor
{

    private final CheckPermission checkPermission;
    private final PlayerInfoManager playerInfoManager;
    private final PlayerProfileManager playerProfileManager;
    private final PlayTimeManager playTimeManager;
    private final PlayerReputationManager playerReputationManager;

    public CommandPlayerInfo(CheckPermission checkPermission,PlayerInfoManager playerInfoManager,PlayerProfileManager playerProfileManager,PlayTimeManager playTimeManager,PlayerReputationManager playerReputationManager)
    {
        this.checkPermission = checkPermission;
        this.playerInfoManager = playerInfoManager;
        this.playerProfileManager = playerProfileManager;
        this.playTimeManager = playTimeManager;
        this.playerReputationManager = playerReputationManager;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {

        if(sender instanceof Player)
        {
            if (!checkPermission.checkPermission(sender, command.getName())) {
                sender.sendMessage("No permissions");
                return true;
            }
        }

        if(args.length == 0)
        {
            sender.sendMessage("Usage: /info <player>");
            return true;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
        PlayerProfile target = playerProfileManager.getProfile(player.getUniqueId());

       // if(target.getNickName() == null|| target.getUuid() == null)
        if(target == null)
        {
            sender.sendMessage("Player is not found");
            return true;
        }
        sender.sendMessage("Player: " + target.getNickName());
        sender.sendMessage("UUID: " + target.getUuid().toString());
        sender.sendMessage("Role: " + playerInfoManager.cheakRole(target));
        if(player.isOnline())
        {
            sender.sendMessage(ChatColor.GREEN + "ONLINE");
        }
        else
        {
            sender.sendMessage(ChatColor.RED + "OFFLINE");
        }

        long firstPlayed = player.getFirstPlayed();

        LocalDateTime firstJoin = LocalDateTime.ofInstant(Instant.ofEpochMilli(firstPlayed), ZoneId.systemDefault());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        sender.sendMessage("First join: " + firstJoin.format(formatter));

        TimeRecorder playTimeR = playTimeManager.getPlayTime(target.getPlayTime());
        sender.sendMessage("Total playtime: " + playTimeR.toHoursString() + " hours " + playTimeR.toMinutesString() + " minutes " + playTimeR.toSecondsString() + " seconds.");

        PlayerReputation reputation = playerReputationManager.getPlayerReputation(target);

        sender.sendMessage("Reputation: " + reputation.name());




        return true;
    }
}
