package org.example.commands.commandRestriction;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.commands.commandRestriction.source.CommandRestrictionManager;
import org.example.utils.CheckPermission;

public class CommandEnableCmd implements CommandExecutor
{
    final private CheckPermission checkPermission;
    final private CommandRestrictionManager commandRestrictionManager;

    public CommandEnableCmd(CheckPermission checkPermission, CommandRestrictionManager commandRestrictionManager)
    {
        this.commandRestrictionManager = commandRestrictionManager;
        this.checkPermission = checkPermission;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!checkPermission.checkIsAdmin(sender))
        {
            sender.sendMessage("No permission!");
            return true;
        }


        if(args.length == 0)
        {
            sender.sendMessage("Use /enable <command_name>");
            sender.sendMessage("or use /enable <command_name> <player>");
            return true;
        }

        String blockedCommand = args[0];


        if(blockedCommand.isBlank())
        {
            sender.sendMessage("Command name can not be empty.");
            return true;
        }

        if(args.length == 1)
        {
            if(!commandRestrictionManager.isCommandRestricted(blockedCommand))
            {
                sender.sendMessage("Command is not restricted.");
                return true;
            }

            commandRestrictionManager.removeCommand(blockedCommand);

            sender.sendMessage("Command " + blockedCommand + " enabled for everyone.");

            return true;
        }


        if(args.length == 2)
        {
            Player target = Bukkit.getPlayer(args[1]);

            if(target == null)
            {
                sender.sendMessage("Player not found!");
                return true;
            }

            if(!commandRestrictionManager.isCommandDisabledForPlayer(blockedCommand, target))
            {
                sender.sendMessage("Command " + blockedCommand + " is not disabled for " + target.getName());
                return true;
            }

            commandRestrictionManager.removePlayer(blockedCommand, target);

            sender.sendMessage("Command " + blockedCommand + " enabled for " + target.getName());

            return true;
        }

        if(args.length > 2)
        {
            sender.sendMessage("Too many arguments.");
            sender.sendMessage("Use /enable <command_name>");
            sender.sendMessage("or use /enable <command_name> <player>");
            return true;
        }




        return true;
    }
}
