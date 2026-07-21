package org.example.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.commands.muteManager.MuteManager;
import org.example.utils.CheckPermission;

import java.time.Duration;

public class CommandTempMute implements CommandExecutor
{
    private CheckPermission checkPermission;
    private MuteManager muteManager;

    public CommandTempMute(CheckPermission checkPermission, MuteManager muteManager)
    {
        this.checkPermission = checkPermission;
        this.muteManager = muteManager;
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
            player.sendMessage("No permissions!");
            return true;
        }

        if(args.length < 3)
        {
            player.sendMessage("Use /tmute <player> <time> <reason>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if(target == null)
        {
            player.sendMessage("Player is not found");
            return true;
        }

        Duration duration = Time(args[1]);
        if(duration == null)
        {
            player.sendMessage("Write correct time");
            player.sendMessage("m = minutes, h = hours, d = days, w = weeks");
            return true;
        }

        String reason = String.join(" ", java.util.Arrays.copyOfRange(args, 2, args.length));
        if(muteManager.isMuted(target.getUniqueId()))
        {
            player.sendMessage("Player: " + target.getName() + " is already mute");
            return true;
        }
        muteManager.tempMute(target.getUniqueId(),reason,player.getName(),duration);
        player.sendMessage("Player " + target.getName() + " has been muted to " + args[1] + ". Reason: " + reason);

        target.sendMessage("You have been muted to " + args[1] + ". Reason: " + reason);

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