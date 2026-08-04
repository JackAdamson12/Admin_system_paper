package org.example.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.commands.commandRestriction.source.CommandRestrictionManager;
import org.example.utils.CheckPermission;
import org.example.utils.TeleportUtils;

public class CommandTpAll implements CommandExecutor
{
    private final CommandRestrictionManager commandRestrictionManager;

    private final CheckPermission checkPermission;
    private final TeleportUtils teleportUtils;

    public CommandTpAll (CheckPermission checkPermission, TeleportUtils teleportUtils, CommandRestrictionManager commandRestrictionManager)
    {
        this.commandRestrictionManager = commandRestrictionManager;
        this.teleportUtils = teleportUtils;
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

        if (!(sender instanceof Player))
        {
            return true;
        }

        Player player = (Player) sender;

        if(!checkPermission.checkIsAdmin(sender))
        {
            player.sendMessage("No permission!");
            return true;
        }
        if(args.length == 1 && args[0].equalsIgnoreCase("confirm"))
        {
            teleportUtils.allTeleportToMe(player);
            player.sendMessage("All players teleported!");
        }
        else
        {
            player.sendMessage("Use /tpall confirm");
            return true;
        }


        return true;
    }
}