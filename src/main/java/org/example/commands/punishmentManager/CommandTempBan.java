package org.example.commands.punishmentManager;

import io.papermc.paper.ban.BanListType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.ban.ProfileBanList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.commands.logsManager.PunishmentLogManager;
import org.example.commands.logsManager.punishmentLogData.PunishmentType;
import org.example.utils.CheckPermission;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

public class CommandTempBan implements CommandExecutor
{
    private final CheckPermission checkPermission;
    private final ProfileBanList banlist;
    private final PunishmentLogManager punishmentLogManager;

    public CommandTempBan(CheckPermission checkPermission,PunishmentLogManager punishmentLogManager)
    {
        this.punishmentLogManager = punishmentLogManager;
        this.checkPermission = checkPermission;
        this.banlist = Bukkit.getBanList(BanListType.PROFILE);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {


        if(!checkPermission.checkIsAdmin(sender))
        {
            sender.sendMessage("No permission!");
            return true;
        }

        if(args.length < 3)
        {
            sender.sendMessage("Use /tempban <player> <time> <reason>");
            sender.sendMessage("m = minutes");
            sender.sendMessage("h = hours");
            sender.sendMessage("d = days");
            sender.sendMessage("w = week");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        if(!target.hasPlayedBefore() && !target.isOnline())
        {
            sender.sendMessage("Player never played before");
            return true;
        }

        if(target.isBanned())
        {
            sender.sendMessage("Player " + target.getName() + " is already banned.\n" + "UUID: " + target.getUniqueId());
            return true;
        }

        if(sender instanceof Player player && target.getUniqueId().equals(player.getUniqueId()))
        {
            sender.sendMessage("You cannot ban yourself.");
            return true;
        }
        Duration duration = Time(args[1]);

        if(duration == null)
        {
            sender.sendMessage("Invalid time. Examples: 10m, 2h, 7d");
            return true;
        }

        String reason = String.join(" ", Arrays.copyOfRange(args, 2, args.length));

        banlist.addBan(target.getPlayerProfile(), reason, duration, sender.getName());

        Player onlineTarget = target.getPlayer();

        Instant createdAt = Instant.now();
        Instant expiresAt = createdAt.plus(duration);

        if(onlineTarget != null)
        {
            onlineTarget.kickPlayer("You have been temporarily banned.\n" + "Time: " + args[1] + "\n" + "Reason: " + reason);
        }

        punishmentLogManager.saveLog(target,reason,sender, PunishmentType.TEMP_BAN, createdAt, expiresAt);



        sender.sendMessage("Player " + target.getName() + " is banned.\n" + "Time: " + args[1] + "\n" + "Reason: " + reason);

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
