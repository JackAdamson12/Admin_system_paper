package org.example.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.commands.commandRestriction.source.CommandRestrictionManager;
import org.example.utils.CheckPermission;

import java.util.HashSet;
import java.util.UUID;

public class CommandFreeze implements CommandExecutor
{
    private final CommandRestrictionManager commandRestrictionManager;

    private final CheckPermission checkPermission;

    private final HashSet<UUID> freezePlayers = new HashSet<>();

    public CommandFreeze(CheckPermission checkPermission, CommandRestrictionManager commandRestrictionManager)
    {
        this.commandRestrictionManager = commandRestrictionManager;
        this.checkPermission = checkPermission;
    }

    public HashSet<UUID> getArr()
    {
        return freezePlayers;
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
            player.sendMessage("Use /freeze <player/all>");
            return true;
        }

        if(args[0].equalsIgnoreCase("all"))
        {
            freezePlayers.clear();

            for(Player onlinePlayer : Bukkit.getOnlinePlayers())
            {
                if(onlinePlayer != player)
                {
                    freezePlayers.add(onlinePlayer.getUniqueId());
                }
            }

            player.sendMessage("All players frozen!");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if(target == null)
        {
            player.sendMessage("Player not found!");
            return true;
        }

        if(freezePlayers.contains(target.getUniqueId()))
        {
            player.sendMessage(target.getName() + " is already frozen!");
            return true;
        }

        freezePlayers.add(target.getUniqueId());

        target.sendMessage("You are frozen by admin " + player.getName());
        player.sendMessage("You froze " + target.getName());

        return true;
    }
}