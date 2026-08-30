package org.example.commands.punishmentManager.muteManager;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.commands.logsManager.PunishmentLogManager;
import org.example.commands.logsManager.punishmentLogData.PunishmentType;
import org.example.commands.commandRestriction.source.CommandRestrictionManager;
import org.example.minePermissions.CheckPermission;

import java.time.Duration;
import java.time.Instant;

public class CommandTempMute implements CommandExecutor
{
    private final CommandRestrictionManager commandRestrictionManager;

    private final CheckPermission checkPermission;
    private final MuteManager muteManager;
    private final PunishmentLogManager punishmentLogManager;

    public CommandTempMute(CheckPermission checkPermission, MuteManager muteManager, PunishmentLogManager punishmentLogManager, CommandRestrictionManager commandRestrictionManager)
    {
        this.commandRestrictionManager = commandRestrictionManager;
        this.punishmentLogManager = punishmentLogManager;
        this.checkPermission = checkPermission;
        this.muteManager = muteManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        Player restrictionPlayer = null;

        if(sender instanceof Player)
        {
            restrictionPlayer = (Player) sender;
        }

        if(commandRestrictionManager.isDisabled(command.getName(), restrictionPlayer))
        {
            sender.sendMessage("This command is disabled.");
            return true;
        }

        if(!checkPermission.checkPermission(sender,command.getName()))
        {
            sender.sendMessage("No permission!");
            return true;
        }

        if(args.length < 3)
        {
            sender.sendMessage("Use /tmute <player> <time> <reason>");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        if(!target.hasPlayedBefore() && !target.isOnline())
        {
            sender.sendMessage("Player is not found");
            return true;
        }

        String targetName = target.getName();

        if(targetName == null)
        {
            targetName = args[0];
        }

        Duration parseTime = Time(args[1]);

        if(parseTime == null)
        {
            sender.sendMessage("Write correct time");
            sender.sendMessage("m = minutes, h = hours, d = days, w = weeks");
            return true;
        }

        String punishmentTime = args[1];

        if(checkPermission.cheakIsHelper(restrictionPlayer))
        {
            Duration maxDuration = Duration.ofHours(24);

            if(parseTime.compareTo(maxDuration) > 0)
            {
                parseTime = maxDuration;
                punishmentTime = "24h";
                sender.sendMessage("Helper can punish maximum for 24 hours. Time changed to 24h.");
            }
        }

        String reason = String.join(" ", java.util.Arrays.copyOfRange(args, 2, args.length));

        if(muteManager.isMuted(target.getUniqueId()))
        {
            sender.sendMessage("Player: " + targetName + " has been mute");
            return true;
        }

        muteManager.tempMute(target.getUniqueId(), reason, sender.getName(), parseTime);
        sender.sendMessage("Player " + targetName + " has been muted to " + punishmentTime + ". Reason: " + reason);

        Player onlineTarget = target.getPlayer();

        if(onlineTarget != null)
        {
            onlineTarget.sendMessage("You have been muted to " + punishmentTime + ". Reason: " + reason);
        }

        Instant createdAt = Instant.now();
        Instant expiresAt = createdAt.plus(parseTime);
        punishmentLogManager.saveLog(target, reason, sender, PunishmentType.TEMP_MUTE, createdAt, expiresAt);

        return true;
    }

    private Duration Time(String time)
    {
        String[] arr = time.split("(?<=\\d)(?=\\D)");

        if(arr.length != 2)
        {
            return null;
        }

        int intTimes;

        try
        {
            intTimes = Integer.parseInt(arr[0]);
        }
        catch(NumberFormatException exception)
        {
            return null;
        }

        String chrTimes = arr[1];

        if(intTimes <= 0)
        {
            return null;
        }

        if(chrTimes.equalsIgnoreCase("m"))
        {
            return Duration.ofMinutes(intTimes);
        }

        if(chrTimes.equalsIgnoreCase("h"))
        {
            return Duration.ofHours(intTimes);
        }

        if(chrTimes.equalsIgnoreCase("d"))
        {
            return Duration.ofDays(intTimes);
        }

        if(chrTimes.equalsIgnoreCase("w"))
        {
            return Duration.ofDays(intTimes * 7L);
        }

        return null;
    }
}
