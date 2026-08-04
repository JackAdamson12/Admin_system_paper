package org.example.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.commands.commandRestriction.source.CommandRestrictionManager;
import org.example.utils.CheckPermission;

public class CommandMelt implements CommandExecutor
{
    private final CommandRestrictionManager commandRestrictionManager;

    private final CheckPermission checkPermission;
    private final CommandFreeze commandFreeze;

    public CommandMelt(CheckPermission checkPermission, CommandFreeze commandFreeze, CommandRestrictionManager commandRestrictionManager)
    {
        this.commandRestrictionManager = commandRestrictionManager;
        this.checkPermission = checkPermission;
        this.commandFreeze = commandFreeze;
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

        if(!(sender instanceof Player))
        {
            return true;
        }

        Player player = (Player) sender;

        if(!checkPermission.checkIsAdmin(sender))
        {
            player.sendMessage("No permission!");
            return true;
        }

        if(args.length == 0)
        {
            player.sendMessage("Use /melt <player/all>");
            return true;
        }

        if(args[0].equalsIgnoreCase("all"))
        {
            commandFreeze.getArr().clear();

            player.sendMessage("All players unfrozen!");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if(target == null)
        {
            player.sendMessage("Player not found!");
            return true;
        }

        if(commandFreeze.getArr().contains(target.getUniqueId()))
        {
            commandFreeze.getArr().remove(target.getUniqueId());

            target.sendMessage("You are unfrozen!");
            player.sendMessage("You unfroze " + target.getName());

            return true;
        }

        player.sendMessage(target.getName() + " is not frozen!");

        return true;
    }
}
