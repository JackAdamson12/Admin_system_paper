package org.example.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.commands.commandRestriction.source.CommandRestrictionManager;
import org.example.minePermissions.CheckPermission;

import java.util.HashSet;
import java.util.UUID;

public class CommandGodMode implements CommandExecutor
{
    private final CommandRestrictionManager commandRestrictionManager;

    public HashSet<UUID> godList = new HashSet<>();

    private final CheckPermission checkPermission;

    public CommandGodMode(CheckPermission checkPermission, CommandRestrictionManager commandRestrictionManager)
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

        if(!checkPermission.checkPermission(sender,command.getName()))
        {
            player.sendMessage("No permission!");
            return true;
        }

        if(args.length == 0)
        {
            if(godList.contains(player.getUniqueId()))
            {
                godList.remove(player.getUniqueId());

                player.sendMessage("God mode disabled!");

                return true;
            }

            godList.add(player.getUniqueId());

            player.sendMessage("God mode enabled!");

            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if(target == null)
        {
            player.sendMessage("Player not found!");

            return true;
        }

        if(godList.contains(target.getUniqueId()))
        {
            godList.remove(target.getUniqueId());

            player.sendMessage("God mode disabled for " + target.getName());
        }
        else
        {
            godList.add(target.getUniqueId());

            player.sendMessage("God mode enabled for " + target.getName());
        }

        return true;
    }
}