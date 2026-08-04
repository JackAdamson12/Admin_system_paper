package org.example.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.commands.commandRestriction.source.CommandRestrictionManager;
import org.example.utils.CheckPermission;

public class CommandEcsee implements CommandExecutor
{
    private final CommandRestrictionManager commandRestrictionManager;

    CheckPermission checkPermission;

    public CommandEcsee(CheckPermission checkPermission, CommandRestrictionManager commandRestrictionManager)
    {
        this.commandRestrictionManager = commandRestrictionManager;
        this.checkPermission = checkPermission;
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
            player.sendMessage("Use /ecsee + player");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);

        if(target == null)
        {
            player.sendMessage("Player not found");
            return true;
        }

        player.openInventory(target.getEnderChest());

        return true;
    }
}