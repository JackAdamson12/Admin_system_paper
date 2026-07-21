package org.example.commands.punishmentManager;

import io.papermc.paper.ban.BanListType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.ban.ProfileBanList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.utils.CheckPermission;

import java.time.Duration;
import java.util.Arrays;

public class CommandTempBan implements CommandExecutor
{
    CheckPermission checkPermission;
    ProfileBanList banlist;

    public CommandTempBan(CheckPermission checkPermission)
    {
        this.checkPermission = checkPermission;
        this.banlist = Bukkit.getBanList(BanListType.PROFILE);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!(sender instanceof Player))
        {
            return true;
        }

        Player player = (Player) sender;

        if(!checkPermission.checkIsAdmin(player))
        {
            player.sendMessage("No permission!");
            return true;
        }

        if(args.length < 3)
        {
            player.sendMessage("Use /tempban <player> <time> <reason>");
            player.sendMessage("m = minutes");
            player.sendMessage("h = hours");
            player.sendMessage("d = days");
            player.sendMessage("w = week");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        if(!target.hasPlayedBefore() && !target.isOnline())
        {
            player.sendMessage("Player never played before");
            return true;
        }

        if(target.isBanned())
        {
            player.sendMessage("Player " + target.getName() + " is already banned.\n" + "UUID: " + target.getUniqueId());
            return true;
        }

        if(target.getUniqueId().equals(player.getUniqueId()))
        {
            player.sendMessage("You cannot ban yourself.");
            return true;
        }

        Duration duration = Time(args[1]);

        if(duration == null)
        {
            player.sendMessage("Invalid time. Examples: 10m, 2h, 7d");
            return true;
        }

        String reason = String.join(" ", Arrays.copyOfRange(args, 2, args.length));

        banlist.addBan(target.getPlayerProfile(), reason, duration, player.getName());

        Player onlineTarget = target.getPlayer();

        if(onlineTarget != null)
        {
            onlineTarget.kickPlayer("You have been temporarily banned.\n" + "Time: " + args[1] + "\n" + "Reason: " + reason);
        }



        player.sendMessage("Player " + target.getName() + " is banned.\n" + "Time: " + args[1] + "\n" + "Reason: " + reason);

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
