package org.example.commands.commandRestriction;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.commands.commandRestriction.source.CommandRestrictionManager;
import org.example.minePermissions.CheckPermission;

public class CommandDisableCmd implements CommandExecutor
{
    private final CheckPermission checkPermission;
    private final CommandRestrictionManager commandRestrictionManager;

    public CommandDisableCmd(CheckPermission checkPermission, CommandRestrictionManager commandRestrictionManager)
    {
        this.checkPermission = checkPermission;
        this.commandRestrictionManager = commandRestrictionManager;
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
            sender.sendMessage("Use /disable <command_name>");
            sender.sendMessage("or use /disable <command_name> <player>");
            return true;
        }

        String blockedCommand = args[0];

        if(blockedCommand.isBlank())
        {
            sender.sendMessage("Command name can not be empty.");
            return true;
        }

        if(blockedCommand.equalsIgnoreCase("disable") || blockedCommand.equalsIgnoreCase("enable"))
        {
            sender.sendMessage("You can not block this command");
            return true;
        }

        // Блокировка команды для всего сервера
        if(args.length == 1)
        {
            commandRestrictionManager.saveData(blockedCommand);
            sender.sendMessage("Command " + blockedCommand + " disabled for everyone.");
            return true;
        }

        // Блокировка команды для конкретного игрока
        if(args.length == 2)
        {
            Player target = Bukkit.getPlayer(args[1]);

            if(target == null)
            {
                sender.sendMessage("Player not found!");
                return true;
            }

            commandRestrictionManager.saveData(blockedCommand, target);

            sender.sendMessage("Command " + blockedCommand + " disabled for " + target.getName());

            return true;
        }

        if(args.length > 2)
        {
            sender.sendMessage("Too many arguments.");
            sender.sendMessage("Use /disable <command_name>");
            sender.sendMessage("or use /disable <command_name> <player>");
            return true;
        }

        return true;
    }
}
