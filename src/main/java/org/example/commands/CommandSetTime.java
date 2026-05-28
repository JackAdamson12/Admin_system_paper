package org.example.commands;

import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.utils.CheckPermission;

public class CommandSetTime implements CommandExecutor
{
    private final CheckPermission checkPermission;

    public CommandSetTime(CheckPermission checkPermission)
    {
        this.checkPermission = checkPermission;
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

        World world = player.getWorld();
        if(args.length == 0)
        {
            player.sendMessage("Use /settime + (day,night,evening,morning)");
            return true;

        }

        switch (args[0].toLowerCase())
        {
            case "day":
                world.setTime(1000);
                player.sendMessage("Time set to day");
                break;

            case "night":
                world.setTime(13000);
                player.sendMessage("Time set to night");
                break;

            case "morning":
                world.setTime(0);
                player.sendMessage("Time set to morning");
                break;

            case "evening":
                world.setTime(12000);
                player.sendMessage("Time set to evening");
                break;

            default:
                try {
                    long time = Long.parseLong(args[0]);
                    world.setTime(time);
                    player.sendMessage("Time set to " + time);
                } catch (NumberFormatException e) {
                    player.sendMessage("Invalid time value");
                }
                break;

        }

        return true;
    }
}
