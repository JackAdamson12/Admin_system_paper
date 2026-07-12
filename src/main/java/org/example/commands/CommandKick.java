package org.example.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.utils.CheckPermission;

import java.util.Arrays;

public class CommandKick implements CommandExecutor
{
    private CheckPermission checkPermission;

    public CommandKick(CheckPermission checkPermission)
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
            player.sendMessage("No permissions.");
            return true;
        }

        if(args.length == 0)
        {
            player.sendMessage("Use /kick <player> <reason>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if(target == null)
        {
            player.sendMessage("Player not found.");
            return true;
        }

        String reason = "Kicked by admin.";

        if(args.length >= 2)
        {
            reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        }

        target.kickPlayer(reason);

        player.sendMessage("You kicked " + target.getName() + ". Reason: " + reason);

        return true;
    }
}
